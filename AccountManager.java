import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
/** This class is used to manage all the accounts in this program. */
public class AccountManager extends Manager implements Serializable, UserObserver {

  ArrayList<User> usersInSystem = new ArrayList<>();
  ArrayList<String> userNamesInUse = new ArrayList<>();
  ArrayList<String> passwordsInUse = new ArrayList<>();
  TransitSystemManager transitSystemManager;
  ArrayList<Card> unassociatedCardsInSystem = new ArrayList<>();

  /**
   * The initializer of this class.
   *
   * @param clock
   */
  public AccountManager(Clock clock) {
    transitSystemManager = new TransitSystemManager(clock);
    readBackupFromSer("SystemBackUp.ser");
    transitSystemManager.setClock(clock);
  }

  /**
   * This method is used to read all users from a file/
   *
   * @param fileName
   */
  private void readFileCreateUsers(String fileName) {
    // This will read lines in file.
    String line;

    try {
      // FileReader reads text files defined above.
      FileReader fileReader = new FileReader(fileName);
      // Wrapping FileReader in BufferedReader.
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      // Handling data in Users.txt
      while ((line = bufferedReader.readLine()) != null) {
        // Divide the line into lineParts, with regard to the comma ","
        String[] lineParts = line.split("\\|");

        String userType = lineParts[0].trim();
        String email = lineParts[1].trim();
        String userName = lineParts[2].trim();
        String password = lineParts[3].trim();
        String[] extra;
        if (lineParts.length > 4) {
          extra = Arrays.copyOfRange(lineParts, 4, lineParts.length);
        } else {
          extra = null;
        }
        addUser(
            UserFactory.getUser(transitSystemManager, userType, email, userName, password, extra));
      }
      // Close files.
      bufferedReader.close();

    } catch (FileNotFoundException e) {
      System.out.println("File does not exist.");
    } catch (IOException e) {
      System.out.println("Error reading file account manager 1");
    }
  }

  /**
   * This method is used to read the back up file for the users' information in case there is a
   * power outrage occurs.
   *
   * @param fileName
   */
  // Deserialization
  private void readBackupFromSer(String fileName) {
    try {

      // Reading the object from a file

      FileInputStream fileIn = new FileInputStream(fileName);
      InputStream buffer = new BufferedInputStream(fileIn);
      ObjectInputStream input = new ObjectInputStream(buffer);

      // Method for deserialization of object
      Object[] deserialized = (Object[]) input.readObject();
      transitSystemManager = (TransitSystemManager) deserialized[0];
      userNamesInUse = (ArrayList<String>) deserialized[1];
      passwordsInUse = (ArrayList<String>) deserialized[2];
      usersInSystem = (ArrayList<User>) deserialized[3];
      unassociatedCardsInSystem = (ArrayList<Card>) deserialized[4];

      input.close();
      fileIn.close();

      for (User anUsersInSystem : usersInSystem) {
        anUsersInSystem.addObserver(this);
      }
    } catch (ClassNotFoundException ex) {
      System.out.println("ClassNotFoundException" + " is caught");
    } catch (FileNotFoundException e) {
      readFileCreateUsers("Users.txt");
    } catch (IOException ex) {
      System.out.println("Error in reading from serialized file!");
    }
  }

  /**
   * This method is used to store users' information in a back up file in case there is a power
   * outrage occurs.
   */
  // Serialize
  void saveBackupToSer() {

    try {
      FileOutputStream fileOut = new FileOutputStream("SystemBackUp.ser");
      ObjectOutputStream output = new ObjectOutputStream(fileOut);

      Object[] SystemBackUp = new Object[5];
      SystemBackUp[0] = (transitSystemManager);
      SystemBackUp[1] = (userNamesInUse);
      SystemBackUp[2] = (passwordsInUse);
      SystemBackUp[3] = (usersInSystem);
      SystemBackUp[4] = (unassociatedCardsInSystem);
      output.writeObject(SystemBackUp);

      output.close();
      fileOut.close();
    } catch (IOException e) {
      System.out.println("Error reading file account manager 2");
    }
  }

  String addCardToSystem() {
    Random randNum = new Random();
    Integer randNewId = randNum.nextInt(9000000) + 1000000;
    Card newCard = new Card(randNewId.toString());
    while (getAllCards().contains(newCard) | unassociatedCardsInSystem.contains(newCard)) {
      randNewId = randNum.nextInt(9000000) + 1000000;
      newCard = new Card(randNewId.toString());
    }
    unassociatedCardsInSystem.add(newCard);
    updateManagerObservers();
    saveBackupToSer();
    return newCard.getId();
  }

  /**
   * This method is used to add cards to the system.
   *
   * @return
   */
  private ArrayList<Card> getAllCards() {
    ArrayList<Card> allCardsList = new ArrayList<>();
    for (User anUsersInSystem : usersInSystem) {
      if (anUsersInSystem instanceof RiderUser) {
        allCardsList.addAll(((RiderUser) anUsersInSystem).getCards());
      }
    }
    return allCardsList;
  }

  /**
   * This method is used to get all the cards in teh program that has a owner(which basically means
   * all the cards.
   *
   * @return
   */
  public void addUser(User user) {
    if (!usersInSystem.contains(user)) {
      usersInSystem.add(user);
      userNamesInUse.add(user.getUsername());
      passwordsInUse.add(user.getPassword());
      user.setAccountManager(this);
      user.addObserver(this);
    }
    saveBackupToSer();
  }

  /**
   * This method is used to add user into the program.
   *
   * @param username
   * @param password
   * @return
   */
  public User getUser(String username, String password) {
    for (int i = 0; i < userNamesInUse.size(); i++) {
      if (userNamesInUse.get(i).equals(username)) {
        if (passwordsInUse.get(i).equals(password)) {
          return usersInSystem.get(i);
        } else {
          return null;
        }
      }
    }
    return null;
  }

  /**
   * This method is used to remove cards from the program.
   *
   * @param card
   */
  void removeCard(Card card) {
    unassociatedCardsInSystem.remove(card);
    updateManagerObservers();
    saveBackupToSer();
  }

  ArrayList<RiderUser> getRiderUsers() {
    ArrayList<RiderUser> arrayToReturn = new ArrayList<>();
    for (User anUsersInSystem : usersInSystem) {
      if (anUsersInSystem instanceof RiderUser) {
        arrayToReturn.add((RiderUser) anUsersInSystem);
      }
    }
    return arrayToReturn;
  }

  /**
   * This method is used to change the name of the user.
   *
   * @param user
   * @param newName
   * @return
   */
  boolean changeUserName(User user, String newName) {
    if (!userNamesInUse.contains(newName)) {
      for (int i = 0; i < usersInSystem.size(); i++) {
        if (usersInSystem.get(i).equals(user)) {
          user.setUsername(newName);
          userNamesInUse.set(i, newName);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void updateUserObserver() {
    saveBackupToSer();
  }
}

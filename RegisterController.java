import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class is used to construct the controller for the register.
 */
public class RegisterController extends GUIController {
  TransitSystemManager transitSystemManager;
  AccountManager accountManager;
  Clock clock;
  RegisterView registerView;
  AdminUser newAdminUser;
  RiderUser newRiderUser;
  ObservableList<Card> cardsAdded = FXCollections.observableArrayList();

  /**
   * This method is the initializer for the class RegisterController.
   * @param transitSystemManager
   * @param accountManager
   * @param clock
   */
  RegisterController(
      TransitSystemManager transitSystemManager, AccountManager accountManager, Clock clock) {
    this.transitSystemManager = transitSystemManager;
    this.accountManager = accountManager;
    this.clock = clock;
  }

  /**
   * This method is used to set register view.
   *
   * @param registerView
   */
  public void setRegisterView(RegisterView registerView) {
    this.registerView = registerView;
  }

  /**
   * This method is used to  update cards added.
   */
  void updateCardsAdded() {
    cardsAdded.setAll(newRiderUser.getCards());
    StringBuilder cardsText = new StringBuilder();
    cardsText.append("Cards associated with this user:").append(System.lineSeparator());
    for (Card aCardsAdded : cardsAdded) {
      cardsText.append(aCardsAdded.getId()).append(System.lineSeparator());
    }
    registerView.cardsAddedText.setText(cardsText.toString());
  }

  /**
   * This method is used to submit handle.
   *
   * @param primaryStage
   * @param adminBox
   * @param userBox
   * @param userInput
   * @param passInput
   * @param emailInput
   */
  void submitHandle(
      Stage primaryStage,
      CheckBox adminBox,
      CheckBox userBox,
      TextField userInput,
      TextField passInput,
      TextField emailInput) {
    if (testUser(userInput) && testPassword(passInput) && testEmail(emailInput)) {
      if (adminBox.isSelected() && (userBox.isSelected())) {
        generateAlert("Error", "A user cannot be both Admin and Rider");
      } else if (adminBox.isSelected() && !(userBox.isSelected())) {
        newAdminUser =
            new AdminUser(userInput.getText(), passInput.getText(), emailInput.getText());
        ConstructorView constructorView =
            new ConstructorView(new ConstructorController(accountManager, newAdminUser, clock));
        constructorView.start(primaryStage);
      } else if (!(adminBox.isSelected()) && userBox.isSelected()) {
        newRiderUser =
            new RiderUser(userInput.getText(), passInput.getText(), emailInput.getText());
        updateCardsAdded();
        primaryStage.setScene(registerView.userScene);
      } else if (!(adminBox.isSelected()) && !(userBox.isSelected())) {
        generateAlert("Error", "Please pick whether you are registering an Admin or a Rider");
      }
    }
  }

  /**
   * This method is used to test email.
   *
   * @param email
   * @return
   */
  boolean testEmail(TextField email) {
    if (email.getText().matches("\\w+@\\w+.\\w+")) {
      return true;
    } else {
      generateAlert("Error", "Please make sure you enter a valid email!");
      return false;
    }
  }

  /**
   * This method is used to rest password.
   *
   * @param password
   * @return
   */
  boolean testPassword(TextField password) {
    if (password.getText().length() > 5) {
      return true;
    } else {
      generateAlert(
          "Error", "Please make sure you pick a password that is of length 5 or greater!");
      return false;
    }
  }

  /**
   * This method is used to test user.
   *
   * @param userName
   * @return
   */
  boolean testUser(TextField userName) {
    if (!userNameInSystem(userName.getText()) && userName.getText().length() > 3) {
      return true;
    } else if (userNameInSystem(userName.getText())) {
      generateAlert("Error", "This username is already in the system!");
      return false;
    } else {
      generateAlert("Error", "Please choose a username that is at least four characters long!");
      return false;
    }
  }

  /**
   * This method is used to check availability of user name.
   *
   * @param userInput
   */
  void checkAvailabilityOfUsername(TextField userInput) {
    String name = userInput.getText();
    if (!userNameInSystem(name)) {
      generateAlert("Notice", "This username is available!");
    } else {
      generateAlert("Notice", "This username is unavailable!");
    }
  }

  /**
   * This method is used to finalize rider user.
   *
   * @param primaryStage
   */
  void finalizeRiderUser(Stage primaryStage) {
    if (generateConfirmation("Are you sure you have finished?")) {
      addUser(newRiderUser);
      returnToLoginScene(primaryStage);
    }
  }

  /**
   * This method is used to return to login scene.
   *
   * @param primaryStage
   */
  void returnToLoginScene(Stage primaryStage) {
    LoginController loginController = new LoginController(accountManager, clock);
    LoginView loginView = new LoginView(loginController);
    loginView.start(primaryStage);
  }

  /**
   * This method is used to return the user name system.
   *
   * @param userName
   * @return
   */
  public boolean userNameInSystem(String userName) {
    return (accountManager.userNamesInUse.contains(userName));
  }

  /**
   * This method is used to add user.
   *
   * @param user
   */
  public void addUser(User user) {
    accountManager.addUser(user);
    this.accountManager.saveBackupToSer();
  }

  /**
   * This method is used to add card.
   * @param cardId
   * @return
   */
  public boolean addCard(String cardId) {
    if (cardId.matches("\\d\\d\\d\\d\\d\\d\\d")) {
      Card newCard = new Card(cardId);
      if (accountManager.unassociatedCardsInSystem.contains(newCard)) {
        newRiderUser.getCards().add(newCard);
        accountManager.removeCard(newCard);
        updateCardsAdded();
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}

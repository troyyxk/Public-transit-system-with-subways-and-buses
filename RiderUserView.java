import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is class RiderUserView.
 */
public class RiderUserView extends Application {
  ComboBox<Card> dropDownOfCards = new ComboBox<>();
  Text currentCardBalance = new Text();
  Text balanceText = new Text();
  Text title;
  Text noCards = new Text();
  Text pastThreeTrips = new Text();
  RiderUserController controller;
  Button addBalanceButton = new Button();
  Button logOut = new Button();
  private Stage window;
  private Scene inputScene;

  /**
   * This method is the initializer for the class RiderUserView.
   *
   * @param riderUser
   * @param clock
   */
  public RiderUserView(RiderUser riderUser, Clock clock) {
    controller = new RiderUserController(riderUser, clock);
    controller.setRiderUserView(this);
  }

  /**
   * This is the main method.
   *
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * This method is the start method to create the JavaFX view for rider view.
   * 
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    window = primaryStage;
    window.setTitle("Rider User Controller");

    // The scene of the controller
    // This scene use GridPlane as the foundation
    GridPane methodGrid = new GridPane();
    methodGrid.setVgap(15);
    methodGrid.setHgap(15);

    // The first line title at row 0
    title = new Text("This is the account page of: " + controller.getUserName());
    GridPane.setConstraints(title, 0, 0);

    // rename user
    Button renameButton = new Button("Rename User");
    GridPane.setConstraints(renameButton, 0, 1);
    renameButton.setOnAction(
        e ->
            controller.generateTextDialogue(
                "Change Username",
                "Please enter the desired username:",
                "Username changed",
                "Username was not changed. Please try a different username!",
                new processChangeUsernameDialogue()));

    dropDownOfCards = new ComboBox<>(controller.riderUserCards);
    controller.updateCards();
    GridPane.setConstraints(dropDownOfCards, 0, 2);
    dropDownOfCards.setOnAction(e -> controller.selectCard());

    // change to student
    Button changeToStudent = new Button("Set to Student Account");
    GridPane.setConstraints(changeToStudent, 1, 1);
    changeToStudent.setOnAction(
        e ->
            controller.generateTextDialogue(
                "Enter Student Email",
                "Please Enter Your Student Email",
                "Account has been set to a student account",
                "This is not a valid student email",
                new processStudentDiscountDialogue()));

    // get card balance

    GridPane.setConstraints(balanceText, 0, 3);
    GridPane.setConstraints(currentCardBalance, 1, 3);

    // add money to card
    GridPane.setConstraints(addBalanceButton, 2, 3);
    addBalanceButton.setOnAction(e -> controller.addBalance());

    // add card
    Button addCard = new Button("Add Card");
    GridPane.setConstraints(addCard, 2, 4);
    addCard.setOnAction(
        e ->
            controller.generateTextDialogue(
                "Add Card",
                "Please enter the new card;s ID:",
                "Card has been added!",
                "Card was not added. Please verify the ID you entered!",
                new processAddCardDialogue()));

    // remove card
    Button removeCard = new Button("Remove Card");
    GridPane.setConstraints(removeCard, 2, 5);
    removeCard.setOnAction(
        e ->
            controller.generateTextDialogue(
                "Remove Card",
                "Please enter the ID of the card you want to remove!:",
                "Card has been removed!",
                "Card was not removed. Please verify the ID that you entered!",
                new processRemoveCardDialogue()));

    // past three trips
    GridPane.setConstraints(pastThreeTrips, 0, 4);

    // no cards
    noCards.setText("This user has no cards!");
    GridPane.setConstraints(noCards, 0, 5);

    // logout
    logOut.setText("Log Out");
    logOut.setOnAction(e -> controller.logOut(primaryStage));
    GridPane.setConstraints(logOut, 2, 6);

    methodGrid
        .getChildren()
        .addAll(
            title,
            renameButton,
            changeToStudent,
            dropDownOfCards,
            balanceText,
            currentCardBalance,
            addBalanceButton,
            noCards,
            addCard,
            removeCard,
            pastThreeTrips,
            logOut);

    inputScene = new Scene(methodGrid, 800, 500);
    window.setScene(inputScene);
  }
}

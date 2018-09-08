import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// AdminUser username has to have some predefined indication showing that this user is an adminStaff
// so that
// regular user could not pretend to register as an adminStaff

/**
 * This class is used to construct the register view.
 */
public class RegisterView extends Application {
  ScrollPane paneOfCards = new ScrollPane();
  Text cardsAddedText = new Text();
  Scene registerScene, userScene;
  Button submit;
  RegisterController registerController;

  /**
   * This method is the initializer for the class RegisterView.
   *
   * @param registerController
   */
  public RegisterView(RegisterController registerController) {
    this.registerController = registerController;
    registerController.setRegisterView(this);
  }

  /**
   * This method is the start method to create the JavaFX view for register view.
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Register System");

    GridPane gridLogin = new GridPane();
    gridLogin.setPadding(new Insets(10, 10, 10, 10));
    gridLogin.setVgap(10);
    gridLogin.setHgap(10);

    Label userLabel = new Label("UserName: ");
    GridPane.setConstraints(userLabel, 0, 0);
    TextField userInput = new TextField();
    userInput.setPromptText("Enter username");
    GridPane.setConstraints(userInput, 1, 0);

    Label passLabel = new Label("Password: ");
    GridPane.setConstraints(passLabel, 0, 1);
    PasswordField passInput = new PasswordField();
    passInput.setPromptText("Enter password");
    GridPane.setConstraints(passInput, 1, 1);

    Label emailLabel = new Label("Email: ");
    GridPane.setConstraints(emailLabel, 0, 2);
    TextField emailInput = new TextField();
    emailInput.setPromptText("Enter email");
    GridPane.setConstraints(emailInput, 1, 2);

    CheckBox adminBox = new CheckBox("Admin");

    GridPane.setConstraints(adminBox, 0, 3);
    CheckBox userBox = new CheckBox("Rider");
    //        handleCheckBox(adminBox, userBox);
    GridPane.setConstraints(userBox, 0, 4);

    submit = new Button("Submit");
    GridPane.setConstraints(submit, 2, 5);
    submit.setOnAction(
        e ->
            registerController.submitHandle(
                primaryStage, adminBox, userBox, userInput, passInput, emailInput));

    Button cancel = new Button("Cancel");
    GridPane.setConstraints(cancel, 0, 5);
    cancel.setOnAction(event -> registerController.returnToLoginScene(primaryStage));

    Button check = new Button("Check Username");
    GridPane.setConstraints(check, 2, 0);
    check.setOnAction(e -> registerController.checkAvailabilityOfUsername(userInput));

    gridLogin
        .getChildren()
        .addAll(
            userLabel,
            userInput,
            check,
            passLabel,
            passInput,
            emailLabel,
            emailInput,
            adminBox,
            userBox,
            cancel,
            submit);
    registerScene = new Scene(gridLogin, 500, 500);

    // Scene 2

    //
    //

    GridPane gridUser = new GridPane();
    gridUser.setPadding(new Insets(10, 10, 10, 10));
    gridUser.setVgap(15);
    gridUser.setHgap(15);

    Button riderCancel = new Button("Cancel");
    GridPane.setConstraints(riderCancel, 0, 2);
    riderCancel.setOnAction(event -> registerController.returnToLoginScene(primaryStage));

    Button addCard = new Button("Add Card");
    GridPane.setConstraints(addCard, 0, 0);
    addCard.setOnAction(
        e ->
            registerController.generateTextDialogue(
                "Add Card",
                "Please input the ID of the card you want to add",
                "Card was successfully linked to the account",
                "Error in linking the card to the account",
                new processAddCardRegisterDialogue()));

    Button finishUser = new Button("Finish");
    GridPane.setConstraints(finishUser, 0, 1);
    finishUser.setOnAction(e -> registerController.finalizeRiderUser(primaryStage));

    paneOfCards.setContent(cardsAddedText);
    GridPane.setConstraints(paneOfCards, 1, 0);

    gridUser.getChildren().addAll(addCard, paneOfCards, finishUser, riderCancel);
    userScene = new Scene(gridUser, 500, 500);

    primaryStage.setScene(registerScene);
    primaryStage.show();
  }
}

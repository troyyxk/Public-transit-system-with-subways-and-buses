import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginView extends Application {
  LoginController loginController;
  Stage window;
  Button login, register;
  Scene loginScene;
  GridPane grid;
  Text noUserFound = new Text();

  public LoginView() {
    this.loginController = new LoginController();
    Stage secondStage = new Stage();
    SimulationController simulationController =
        new SimulationController(
            loginController.accountManager,
            loginController.accountManager.transitSystemManager,
            loginController.clock);
    SimulationView simulationView = new SimulationView(simulationController);
    simulationView.start(secondStage);
    secondStage.show();
  }

  public LoginView(LoginController loginController) {
    this.loginController = loginController;
  }

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * The layout of this GUI will be illustrated once the program is running
   * @param primaryStage
   */
  public void start(Stage primaryStage) {
    window = primaryStage;
    window.setTitle("Login & Register");
    grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(10);
    grid.setHgap(10);

    Label nameLabel = new Label("User Name: ");
    GridPane.setConstraints(nameLabel, 0, 0);
    TextField userInput = new TextField("Username");
    GridPane.setConstraints(userInput, 1, 0);

    Label passLabel = new Label("Password: ");
    GridPane.setConstraints(passLabel, 0, 1);
    PasswordField passInput = new PasswordField();
    passInput.setPromptText("Password");
    GridPane.setConstraints(passInput, 1, 1);

    login = new Button("Login");
    GridPane.setConstraints(login, 1, 2);
    login.setOnAction(e -> validate(primaryStage, userInput, passInput));

    register = new Button("Register New Rider/Admin User");
    GridPane.setConstraints(register, 1, 3);
    register.setOnAction(e -> setUpRegistration(primaryStage));

    Button exit = new Button("Exit");
    GridPane.setConstraints(exit, 1, 5);
    exit.setOnAction(e -> System.exit(0));

    grid.getChildren().addAll(nameLabel, userInput, passLabel, passInput, login, register, exit);
    loginScene = new Scene(grid, 500, 500);
    window.setScene(loginScene);
    window.show();

    noUserFound.setText("Incorrect Username/Password");
    noUserFound.setStroke(Color.RED);
    GridPane.setConstraints(noUserFound, 1, 5);
    grid.getChildren().add(noUserFound);
    noUserFound.setVisible(false);
  }

  /**
   * Set the primary stage to the login scene
   * @param primaryStage
   */
  private void setUpRegistration(Stage primaryStage) {
    RegisterController registerController =
        new RegisterController(
            new TransitSystemManager(loginController.clock),
            loginController.accountManager,
            loginController.clock);
    RegisterView registerView = new RegisterView(registerController);
    registerView.start(primaryStage);
  }

  /**
   * if the username and the password is validate, then login scene is set to the primary stage
   *
   * @param primaryStage the main stage
   * @param userInput username
   * @param passInput password
   */
  private void validate(Stage primaryStage, TextField userInput, TextField passInput) {
    noUserFound.setVisible(false);
    String userName = userInput.getText();
    String passWord = passInput.getText();
    User user = loginController.login(userName, passWord);
    if (user instanceof AdminUser) {
      AdminView adminView = new AdminView((AdminUser) user, loginController.clock);
      adminView.start(primaryStage);
    } else if (user instanceof RiderUser) {
      RiderUserView riderUserView = new RiderUserView((RiderUser) user, loginController.clock);
      riderUserView.start(primaryStage);
    } else {
      noUserFound.setVisible(true);
    }
  }
}

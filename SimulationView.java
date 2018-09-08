import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SimulationView extends Application {

  Text timeText = new Text();
  SimulationController simulationController;
  ComboBox<TransitSystem> dropDownOfTransitSystems;
  ComboBox<RiderUser> dropDownOfRiderUsers;
  ComboBox<Card> dropDownOfUsersCard;
  Text existingTripWarning = new Text();
  Text noTransitSystems = new Text("No Transit Systems Exist Yet!");
  Text noRiderUser = new Text("No Rider Users In The System");
  ComboBox<TransitLine> dropDownOfTransitLines;
  ComboBox<Terminal> dropDownOfTerminals;
  ScrollPane unassociatedCards = new ScrollPane();
  Text unassociatedCardsText = new Text();
  Button tapInButton;
  Button tapOutButton;
  Scene menuScene;
  Scene movementScene;

  public SimulationView(SimulationController simulationController) {
    this.simulationController = simulationController;
    simulationController.setSimulationView(this);
  }


  /**
   * This GUI simulates the tap in/out method, add card etc. the time of simulation could be modified
   *
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    // Main Menu Scene
    GridPane menuGrid = new GridPane();
    menuGrid.setPadding(new Insets(15, 15, 15, 15));
    menuGrid.setVgap(15);
    menuGrid.setHgap(15);

    simulationController.updateTime();
    GridPane.setConstraints(timeText, 0, 0);

    Button movementButton = new Button("Tap In/Out");
    GridPane.setConstraints(movementButton, 0, 1);
    movementButton.setOnAction(e -> simulationController.switchToMovementScene(primaryStage));

    simulationController.updateUnassociatedCards();
    unassociatedCards.setPrefViewportWidth(100);
    unassociatedCards.setPrefViewportHeight(50);
    GridPane.setConstraints(unassociatedCards, 1, 1, 2, 2);

    Button addCardButton = new Button("Add Card");
    GridPane.setConstraints(addCardButton, 0, 2);
    addCardButton.setOnAction(e -> simulationController.addCard());

    Button speedUpButton = new Button("Speed Up Simulation");
    GridPane.setConstraints(speedUpButton, 0, 3);
    speedUpButton.setOnAction(e -> simulationController.speedUpSimulation());

    Button progressDays = new Button("Fast Forward in Days");
    GridPane.setConstraints(progressDays, 1, 3);
    progressDays.setOnAction(e -> simulationController.progressDays());

    Button slowDownButton = new Button("Slow Down Simulation");
    GridPane.setConstraints(slowDownButton, 0, 4);
    slowDownButton.setOnAction(e -> simulationController.slowDownSimulation());

    Button progressHours = new Button("Fast Forward in Hours");
    GridPane.setConstraints(progressHours, 1, 4);
    progressHours.setOnAction(e -> simulationController.progressHours());

    Button defaultSpeed = new Button("Return to Default Speed");
    GridPane.setConstraints(defaultSpeed, 0, 5);
    defaultSpeed.setOnAction(e -> simulationController.returnToDefault());

    Button progressMinutes = new Button("Fast Forward in Minutes");
    GridPane.setConstraints(progressMinutes, 1, 5);
    progressMinutes.setOnAction(e -> simulationController.progressMinutes());

    Button exitButton = new Button("Exit");
    GridPane.setConstraints(exitButton, 0, 6);
    exitButton.setOnAction(e -> System.exit(0));

    menuGrid
        .getChildren()
        .addAll(
            timeText,
            movementButton,
            unassociatedCards,
            unassociatedCardsText,
            addCardButton,
            speedUpButton,
            slowDownButton,
            defaultSpeed,
            progressDays,
            progressHours,
            progressMinutes,
            exitButton);

    // Movement Scene
    GridPane movementGrid = new GridPane();
    movementGrid.setPadding(new Insets(15, 15, 15, 15));
    movementGrid.setVgap(15);
    movementGrid.setHgap(15);

    dropDownOfUsersCard = new ComboBox<>(simulationController.riderUsersCards);
    dropDownOfTransitLines = new ComboBox<>(simulationController.transitLines);
    dropDownOfTerminals = new ComboBox<>(simulationController.terminals);
    dropDownOfTransitSystems = new ComboBox<>(simulationController.transitSystems);
    dropDownOfRiderUsers = new ComboBox<>(simulationController.riderUsers);
    tapInButton = new Button("Tap In");
    tapOutButton = new Button("Tap Out");

    GridPane.setConstraints(dropDownOfUsersCard, 1, 2);
    dropDownOfUsersCard.setPromptText("Please select the desired card");
    dropDownOfUsersCard.setOnAction(
        event -> {
          simulationController.cardSelected();
        });

    GridPane.setConstraints(existingTripWarning, 0, 5, 2, 1);

    GridPane.setConstraints(dropDownOfTransitLines, 0, 2);
    dropDownOfTransitLines.setOnAction(
        event -> {
          simulationController.transitLineSelected();
        });

    GridPane.setConstraints(dropDownOfTerminals, 0, 3);
    dropDownOfTerminals.setOnAction(e -> simulationController.terminalSelected());

    dropDownOfTransitSystems.getSelectionModel().selectFirst();
    simulationController.transitSystemSelected();
    GridPane.setConstraints(dropDownOfTransitSystems, 0, 1);
    dropDownOfTransitSystems.setOnAction(event -> simulationController.transitSystemSelected());

    GridPane.setConstraints(noTransitSystems, 0, 1);
    noTransitSystems.setStroke(Color.RED);
    noTransitSystems.setVisible(false);

    dropDownOfRiderUsers.getSelectionModel().selectFirst();
    simulationController.riderUserSelected();
    GridPane.setConstraints(dropDownOfRiderUsers, 1, 1);
    dropDownOfRiderUsers.setOnAction(event -> simulationController.riderUserSelected());

    GridPane.setConstraints(noRiderUser, 1, 1);
    noRiderUser.setStroke(Color.RED);
    noRiderUser.setVisible(false);

    GridPane.setConstraints(tapInButton, 0, 4);
    tapInButton.setOnAction(e -> simulationController.entry(primaryStage));

    GridPane.setConstraints(tapOutButton, 1, 4);
    tapOutButton.setOnAction(e -> simulationController.exit(primaryStage));

    Button backButton = new Button("Back to Menu");
    GridPane.setConstraints(backButton, 0, 0);
    backButton.setOnAction(e -> simulationController.switchToMenuScene(primaryStage));

    movementGrid
        .getChildren()
        .addAll(
            noTransitSystems,
            noRiderUser,
            dropDownOfTransitSystems,
            dropDownOfRiderUsers,
            dropDownOfUsersCard,
            dropDownOfTransitLines,
            dropDownOfTerminals,
            existingTripWarning,
            tapInButton,
            tapOutButton,
            backButton);
    movementScene = new Scene(movementGrid, 800, 600);
    menuScene = new Scene(menuGrid, 800, 600);

    // Main Menu Layout

    primaryStage.setTitle("Simulation Window");
    primaryStage.setScene(menuScene);
    primaryStage.show();
  }
}

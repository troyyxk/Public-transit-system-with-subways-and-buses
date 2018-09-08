import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

class SimulationController extends GUIController implements ClockObserver, ManagerObserver {
  AccountManager accountManager;
  TransitSystemManager transitSystemManager;
  ObservableList<TransitSystem> transitSystems = FXCollections.observableArrayList();
  ObservableList<RiderUser> riderUsers = FXCollections.observableArrayList();
  ObservableList<Card> riderUsersCards = FXCollections.observableArrayList();
  ObservableList<TransitLine> transitLines = FXCollections.observableArrayList();
  ObservableList<Terminal> terminals = FXCollections.observableArrayList();
  SimulationView simulationView;
  TransitSystem transitSystemSelected;
  RiderUser riderUserSelected;
  Card cardSelected;
  TransitLine transitLineSelected;
  Terminal terminalSelected;
  Clock clock;
  String fastForwardType;

  SimulationController(
      AccountManager accountManager, TransitSystemManager transitSystemManager, Clock clock) {
    this.accountManager = accountManager;
    riderUsers.addAll(accountManager.getRiderUsers());
    this.transitSystemManager = transitSystemManager;
    transitSystems.addAll(transitSystemManager.transitSystems);
    this.clock = clock;
    accountManager.addObserver(this);
    transitSystemManager.addObserver(this);
    clock.addObserver(this);
  }

  @Override
  public void updateEverySec() {
    updateTime();
  }

  /**
   * Set the starting time to the current date
   */
  void updateTime() {
    simulationView.timeText.setText(getStrDate());
  }

  /**
   *This method updates all the transit System
   */
  void updateTransitSystems() {
    transitSystems.setAll(transitSystemManager.transitSystems);
    simulationView.dropDownOfTransitSystems.getSelectionModel().selectFirst();
    transitSystemSelected();
  }

  /**
   * This method updates all the user
   */
  void updateRiderUsers() {
    riderUsers.setAll(accountManager.getRiderUsers());
    simulationView.dropDownOfRiderUsers.getSelectionModel().selectFirst();
    riderUserSelected();
  }

  /**
   * This method set the scene to the movement scene
   *
   * @param primaryStage
   */
  void switchToMovementScene(Stage primaryStage) {
    updateTransitSystems();
    updateRiderUsers();
    primaryStage.setScene(simulationView.movementScene);
  }

  void switchToMenuScene(Stage primaryStage) {
    primaryStage.setScene(simulationView.menuScene);
  }

  /**
   * This function set the selected transit system visible
   */
  void transitSystemSelected() {
    if (transitSystems.size() != 0) {
      simulationView.noTransitSystems.setVisible(false);
      setVisible();
      transitSystemSelected = simulationView.dropDownOfTransitSystems.getValue();
      transitLines.setAll(transitSystemSelected.map.getAllLines());
      simulationView.dropDownOfTransitLines.getSelectionModel().selectFirst();
      transitLineSelected();
    } else {
      simulationView.noTransitSystems.setVisible(true);
      setInvisible();
    }
  }

  /**
   * This function set the selected transit line visible
   */
  void transitLineSelected() {
    transitLineSelected = simulationView.dropDownOfTransitLines.getValue();
    terminals.setAll(transitLineSelected.getListOfTerminals());
    simulationView.dropDownOfTerminals.getSelectionModel().selectFirst();
    terminalSelected();
  }

  void terminalSelected() {
    terminalSelected = simulationView.dropDownOfTerminals.getValue();
  }

  /**
   * This function set the riderUser selected visible
   */
  void riderUserSelected() {
    if (riderUsers.size() != 0) {
      setVisible();
      simulationView.noRiderUser.setVisible(false);
      riderUserSelected = simulationView.dropDownOfRiderUsers.getValue();
      riderUsersCards.setAll(riderUserSelected.getCards());
      simulationView.dropDownOfUsersCard.getSelectionModel().selectFirst();
      cardSelected();
    } else {
      simulationView.noRiderUser.setVisible(true);
      setInvisible();
    }
  }

  /**
   * Set everything invisible
   */
  void setInvisible() {
    simulationView.dropDownOfTransitSystems.setVisible(false);
    simulationView.dropDownOfTransitLines.setVisible(false);
    simulationView.dropDownOfTerminals.setVisible(false);
    simulationView.dropDownOfRiderUsers.setVisible(false);
    simulationView.dropDownOfUsersCard.setVisible(false);
    simulationView.tapInButton.setVisible(false);
    simulationView.tapOutButton.setVisible(false);
  }

  /**
   * Set everything visible
   */
  void setVisible() {
    simulationView.dropDownOfTransitSystems.setVisible(true);
    simulationView.dropDownOfTransitLines.setVisible(true);
    simulationView.dropDownOfTerminals.setVisible(true);
    simulationView.dropDownOfRiderUsers.setVisible(true);
    simulationView.dropDownOfUsersCard.setVisible(true);
    simulationView.tapInButton.setVisible(true);
    simulationView.tapOutButton.setVisible(true);
  }

  /**
   * Skip ahead for a given amount of days
   */
  void progressDays() {
    fastForwardType = "Day";
    generateTextDialogue(
        "Days Selection",
        "Please enter the number of days you would like to fast forward by:",
        "Successfully fast forwarded",
        "Please make you sure you entered a valid integer",
        new processFastForwardDialogue());
  }

  /**
   * Skip ahead for a given amount of hours.
   */
  void progressHours() {
    fastForwardType = "Hour";
    generateTextDialogue(
        "Hour Selection",
        "Please enter the number of hours you would like to fast forward by:",
        "Successfully fast forwarded",
        "Please make you sure you entered a valid integer",
        new processFastForwardDialogue());
  }

  /**
   * Skip ahead for a given amount of minutes
   */
  void progressMinutes() {
    fastForwardType = "Minute";
    generateTextDialogue(
        "Minute Selection",
        "Please enter the number of minutes you would like to fast forward by:",
        "Successfully fast forwarded",
        "Please make you sure you entered a valid integer",
        new processFastForwardDialogue());
  }

  void returnToDefault() {
    this.clock.defaultSpeed();
  }


  void cardSelected() {
    cardSelected = simulationView.dropDownOfUsersCard.getValue();
    updateExistingTripWarning();
  }

  /**
   * This function generates the error warning message to be displayed
   */
  void updateExistingTripWarning() {
    if (cardSelected.currentTrip != null) {
      Trip currentTrip = cardSelected.currentTrip;
      simulationView.existingTripWarning.setText(
          "Please note that card: "
              + System.lineSeparator()
              + cardSelected.getId()
              + System.lineSeparator()
              + "has already tapped in at: "
              + System.lineSeparator()
              + currentTrip.entrance
              + System.lineSeparator()
              + "which is on transit line: "
              + System.lineSeparator()
              + currentTrip.entrance.transitLine
              + System.lineSeparator()
              + "in transit system:"
              + System.lineSeparator()
              + currentTrip.transitSystem
              + System.lineSeparator()
              + "If you select a terminal that is not in this line, it will be treated such that the user forgot to tap out.");
      simulationView.existingTripWarning.setFill(Color.RED);
    } else {
      simulationView.existingTripWarning.setText("");
    }
  }

  void setSimulationView(SimulationView simulationView) {
    this.simulationView = simulationView;
  }

  /**
   * Set the message on the screen when the user taps in
   * @param primaryStage
   */
  void entry(Stage primaryStage) {
    Date date = clock.getDate();
    if (terminalSelected != null) {
      cardSelected.tapIn(transitSystemSelected, terminalSelected, date);
      generateAlert("Success", "Successfully tapped in!");
    } else {
      generateAlert("Error", "Error in tapping in!");
    }
    switchToMenuScene(primaryStage);
  }

  /**
   * Set the message on the screen when the user taps out
   * @param primaryStage
   */
  void exit(Stage primaryStage) {
    Date date = clock.getDate();
    if (terminalSelected != null) {
      cardSelected.tapOut(transitSystemSelected, terminalSelected, date);
      generateAlert("Success", "Successfully tapped out!");
    } else {
      generateAlert("Error", "Error in tapping out!");
    }
    switchToMenuScene(primaryStage);
  }


  void updateUnassociatedCards() {
    String toSet = "Unassociated Cards: " + System.lineSeparator();
    for (int i = 0; i < accountManager.unassociatedCardsInSystem.size(); i++) {
      toSet += accountManager.unassociatedCardsInSystem.get(i) + System.lineSeparator();
    }
    simulationView.unassociatedCardsText.setText(toSet);
    simulationView.unassociatedCards.setContent(simulationView.unassociatedCardsText);
  }

  /**
   * This method adds a new card to the system and update all the observers
   */
  void addCard() {
    String newId = accountManager.addCardToSystem();
    updateUnassociatedCards();
    generateAlert("Notice", "Created a new card with ID: " + newId);
  }

  void speedUpSimulation() {
    clock.speedUp();
  }

  void slowDownSimulation() {
    clock.slowDown();
  }

  String getStrDate() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return dateFormat.format(clock.getDate());
  }

    @Override
    public void updateManagerObserver() {
        updateUnassociatedCards();
        updateTransitSystems();

    }
}

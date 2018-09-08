import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This class is used to construct the controller for the constructor.
 */
public class ConstructorController extends GUIController {
  AdminUser adminUser;
  ConstructorView constructorView;
  AccountManager accountManager;
  TransitSystemManager transitSystemManager;
  TransitSystem transitSystemInConstruction;
  Map mapInConstruction;
  Clock clock;
  int width;
  int height;
  String mapName;
  String stationType;
  ArrayList<Button> currentLineButtons = new ArrayList<>();
  ArrayList<Button> backUpList = new ArrayList<>();
  ArrayList<String> terminalNamesAlreadyUsed = new ArrayList<>();
  ArrayList<String> currentLineTerminalNames = new ArrayList<>();
  Integer[] currentLinePoints;
  Button currentButton;
  String currentStyle;

  /**
   * This method is the initializer for the class ConstructorController.
   *
   * @param accountManager
   * @param adminUser
   * @param clock
   */
  public ConstructorController(AccountManager accountManager, AdminUser adminUser, Clock clock) {
    this.accountManager = accountManager;
    this.clock = clock;
    this.transitSystemManager = accountManager.transitSystemManager;
    this.adminUser = adminUser;
  }

  /**
   * This method is used to set constructor view.
   *
   * @param constructorView
   */
  public void setConstructorView(ConstructorView constructorView) {
    this.constructorView = constructorView;
  }

  /**
   * This method is used to add transit line.
   *
   * @param terminalNames
   * @param lineName
   * @param lineType
   */
  public void addTransitLine(String[] terminalNames, String lineName, String lineType) {
    assert (mapInConstruction != null);
    ArrayList<Integer> temp = new ArrayList<>();
    currentLinePoints = new Integer[currentLineButtons.size() * 2];
    for (Button currButton : currentLineButtons) {
      for (int i = 0; i < constructorView.gridOfButtons.length; i++) {
        for (int j = 0; j < constructorView.gridOfButtons[i].length; j++) {
          if (constructorView.gridOfButtons[i][j].equals(currButton)) {
            temp.add(i);
            temp.add(j);
          }
        }
      }
    }
    for (int j = 0; j < temp.size(); j++) {
      currentLinePoints[j] = temp.get(j);
    }
    mapInConstruction.coordinateOfLines.add(currentLinePoints);
    TransitLine newLine = TransitLineFactory.getTransitLine(lineType, lineName, terminalNames);
    mapInConstruction.getAllLines().add(newLine);
    currentLineTerminalNames = new ArrayList<>();
  }

  /**
   * This method is used to cancel line selection.
   */
  void cancelLineSelection() {
    if (generateConfirmation("Are you sure you want to cancel the construction of this line?")) {
      for (int i = 0; i < currentLineButtons.size(); i++) {
        for (int x = 0; x < constructorView.gridOfButtons.length; x++) {
          for (int y = 0; y < constructorView.gridOfButtons[x].length; y++) {
            if (currentLineButtons.get(i).equals(constructorView.gridOfButtons[x][y])) {
              constructorView.gridOfButtons[x][y].setStyle(backUpList.get(i).getStyle());
              constructorView.gridOfButtons[x][y].setText(backUpList.get(i).getText());
            }
          }
        }
      }
      setGridInvisible();
      for (int i = 0; i < currentLineTerminalNames.size(); i++) {
        terminalNamesAlreadyUsed.remove(currentLineTerminalNames.get(i));
      }
      currentLineTerminalNames = new ArrayList<>();
      currentLineButtons = new ArrayList<>();
      backUpList = new ArrayList<>();
      constructorView.busStationButton.setVisible(true);
      constructorView.subwayStationButton.setVisible(true);
      constructorView.cancelSelection.setVisible(false);
    }
  }

  /**
   * This method is used to process bus button press.
   */
  public void processBusButtonPress() {
    if (constructorView.gridOfButtons == null) {
      constructGrid();
    } else {
      setGridVisible();
    }
    constructorView.cancelSelection.setVisible(true);
    constructorView.subwayStationButton.setVisible(false);
    stationType = "Bus";
    updateTerminalType();
    constructorView.submit.setVisible(false);
    currentStyle = "-fx-background-color: #4682b4";
  }

  /**
   * This method is used to  process finalize button.
   *
   * @param primaryStage
   */
  void processFinalizeButton(Stage primaryStage) {
    finalizeTransitSystem();
    accountManager.addUser(adminUser);
    generateAlert(
        "Notice", "The transit system was created and the admin user was added into the system!");
    LoginController loginController = new LoginController(accountManager, clock);
    LoginView loginView = new LoginView(loginController);
    loginView.start(primaryStage);
  }

  /**
   * This method is used to return to the log in scene.
   *
   * @param primaryStage
   */
  void returnToLogin(Stage primaryStage) {
    if (generateConfirmation("Are you sure you want to cancel the registration process?")) {
      LoginController loginController = new LoginController(accountManager, clock);
      LoginView loginView = new LoginView(loginController);
      loginView.start(primaryStage);
    }
  }

  /**
   * This method is used to process subway button press.
   */
  void processSubwayButtonPress() {
    if (constructorView.gridOfButtons == null) {
      constructGrid();
    } else {
      setGridVisible();
    }
    constructorView.cancelSelection.setVisible(true);
    constructorView.busStationButton.setVisible(false);
    constructorView.submit.setVisible(false);
    stationType = "Subway";
    updateTerminalType();
    currentStyle = "-fx-background-color: #FFFFF0";
  }

  /**
   * This method is used to set grid visible.
   */
  void setGridVisible() {
    for (int i = 0; i < constructorView.gridOfButtons.length; i++) {
      for (int j = 0; j < constructorView.gridOfButtons[i].length; j++) {
        constructorView.gridOfButtons[i][j].setVisible(true);
      }
    }
  }

  /**
   * This method is used to set grid invisible.
   */
  void setGridInvisible() {
    for (int i = 0; i < constructorView.gridOfButtons.length; i++) {
      for (int j = 0; j < constructorView.gridOfButtons[i].length; j++) {
        constructorView.gridOfButtons[i][j].setVisible(false);
      }
    }
  }

  /**
   * This method is used to process grid button press.
   * @param button
   */
  public void processGridButtonPress(Button button) {
    currentButton = button;
    Button backUpButton = new Button();
    backUpButton.setStyle(currentButton.getStyle());
    backUpButton.setText(currentButton.getText());
    backUpList.add(backUpButton);
    if (!currentLineButtons.contains(currentButton)) {
      if (currentButton.getText().equals("None")) {
        generateTextDialogue(
            "Choose Terminal Name",
            "Please enter your desired terminal name:",
            "Terminal successfully created",
            "Please choose a new name!",
            new processTerminalNameDialogue());
      } else {
        currentButton.setStyle("-fx-background-color: #FFE5B4");
        currentLineTerminalNames.add(currentButton.getText());
        currentLineButtons.add(currentButton);
      }
    } else {
      generateAlert("Error", "Transit Lines cannot intersect themselves!");
    }
    if (currentLineTerminalNames.size() >= 2) {
      constructorView.endTransitLineButton.setVisible(true);
    }
  }

  /**
   * This method is used to update terminal type.
   */
  void updateTerminalType() {
    constructorView.currentTerminalTypeLabel.setText(
        "Current terminal type is " + stationType + ".");
  }

  /**
   * This method is used to construct grid.
   */
  void constructGrid() {
    constructorView.gridOfButtons = new Button[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Button button = new Button();
        currentButton = button;
        button.setText("None");
        // the id of the button
        button.setId(Integer.toString(row) + ',' + Integer.toString(col));
        constructorView.gridOfButtons[row][col] = currentButton;
        button.setOnAction(
            event -> {
              processGridButtonPress(button);
            });
        constructorView.constructionMap.add(button, col + 1, row + 1);
      }
    }
  }

  /**
   * This method is used to test user inputs.
   *
   * @param widthInput
   * @param heightInput
   * @param mapNameInput
   * @return
   */
  public boolean testUserInputs(
      TextField widthInput, TextField heightInput, TextField mapNameInput) {
    try {
      width = Integer.parseInt(widthInput.getText());
      height = Integer.parseInt(heightInput.getText());
      if (transitSystemManager.transitSystemNamesInUse.contains(mapNameInput.getText())) {
        generateAlert("Error", "Please choose a different name! This name is already in use.");
        return false;
      } else if (mapNameInput.getText().length() < 4) {
        generateAlert("Error", "Please choose a map name that contains at least four characters!");
        return false;
      } else if (width > 10 | height > 10) {
        generateAlert("Error", "The size of the grid is currently capped at 10x10");
        return false;
      }
      mapName = mapNameInput.getText();
      mapInConstruction = new Map(mapName);
      return true;
    } catch (NumberFormatException e1) {
      generateAlert(
          "Error",
          "Please verify that you entered in valid integers in the height and width field!");
      return false;
    }
  }

  /**
   * This method is used to finalize transit system.
   */
  public void finalizeTransitSystem() {
    transitSystemInConstruction = new TransitSystem(mapInConstruction, transitSystemManager.clock);
    for (int i = 0; i < mapInConstruction.getAllLines().size(); i++) {
      mapInConstruction.getAllLines().get(i).setTransitSystem(transitSystemInConstruction);
    }
    adminUser.setTransitSystem(transitSystemInConstruction);
    transitSystemManager.addTransitSystem(transitSystemInConstruction);
    transitSystemInConstruction = null;
    mapInConstruction = null;
    terminalNamesAlreadyUsed = new ArrayList<>();
  }
}

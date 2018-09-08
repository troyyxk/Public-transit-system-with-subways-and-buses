import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class is used to construct the constructor view.
 */
public class ConstructorView extends Application {
  Stage window;
  Scene inputScene, mapScene;
  Button startConstruction, submit;
  GridPane constructionMap;
  Button subwayStationButton;
  Button busStationButton;
  Button endTransitLineButton;
  Button cancelSelection;
  Button[][] gridOfButtons;
  Label currentTerminalTypeLabel = new Label();

  ConstructorController constructorController;

  /**
   * This method is the initializer for the class ConstructorView.
   *
   * @param constructorController
   */
  public ConstructorView(ConstructorController constructorController) {
    this.constructorController = constructorController;
    this.constructorController.setConstructorView(this);
  }

  /**
   * This method is the start method to create the JavaFX view for constructor view.
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {

    window = primaryStage;
    window.setTitle("Public Transportation Constructor");

    // The input scene

    GridPane inputGrid = new GridPane();
    inputGrid.setPadding(new Insets(10, 10, 10, 10));
    inputGrid.setVgap(10);
    inputGrid.setHgap(10);

    Button cancelDimensions = new Button("Cancel");
    GridPane.setConstraints(cancelDimensions, 0, 3);
    cancelDimensions.setOnAction(
        event -> {
          constructorController.returnToLogin(primaryStage);
        });

    Label width = new Label("Width: ");
    GridPane.setConstraints(width, 0, 0);
    TextField widthInput = new TextField();
    widthInput.setPromptText("Width");
    GridPane.setConstraints(widthInput, 1, 0);

    Label height = new Label("Height: ");
    GridPane.setConstraints(height, 0, 1);
    TextField heightInput = new TextField();
    heightInput.setPromptText("Height");
    GridPane.setConstraints(heightInput, 1, 1);

    Label mapNameLabel = new Label("Transit System Name:");
    GridPane.setConstraints(mapNameLabel, 0, 2);
    TextField mapNameInput = new TextField();
    mapNameInput.setPromptText("Map Name");
    GridPane.setConstraints(mapNameInput, 1, 2);

    startConstruction = new Button("Start Construction");
    GridPane.setConstraints(startConstruction, 1, 3);

    startConstruction.setOnAction(
        e -> {
          if (constructorController.testUserInputs(widthInput, heightInput, mapNameInput)) {
            constructionMap = new GridPane();
            constructionMap.setPadding(new Insets(10, 10, 10, 10));
            constructionMap.setVgap(10);
            constructionMap.setHgap(10);
            constructionMap.add(currentTerminalTypeLabel, 0, 0);

            // the button for BusStation
            busStationButton = new Button("Bus");
            busStationButton.setStyle("-fx-background-color: #4682b4");
            busStationButton.setOnAction(
                event -> {
                  constructorController.processBusButtonPress();
                });
            constructionMap.add(busStationButton, 0, 1);

            // the button for SubwayStation
            subwayStationButton = new Button("Subway");
            subwayStationButton.setStyle("-fx-background-color: #FFFFF0");
            subwayStationButton.setOnAction(
                event -> {
                  constructorController.processSubwayButtonPress();
                });
            constructionMap.add(subwayStationButton, 0, 2);

            // the button for end the current line and start a new line
            endTransitLineButton = new Button("End Transit Line");
            endTransitLineButton.setOnAction(
                event -> {
                  constructorController.generateTextDialogue(
                      "Finalize Transit Line",
                      "Please input a name for the transit line:",
                      "Transit line successfully created",
                      "",
                      new processTransitLineNameDialogue());
                });
            constructionMap.add(endTransitLineButton, 0, 3);
            endTransitLineButton.setVisible(false);

            cancelSelection = new Button("Cancel current line construction");
            constructionMap.add(cancelSelection, 0, 5);
            cancelSelection.setVisible(false);
            cancelSelection.setOnAction(event -> constructorController.cancelLineSelection());

            Button cancelGrid = new Button("Cancel");
            cancelGrid.setOnAction(event -> constructorController.returnToLogin(primaryStage));
            constructionMap.add(cancelGrid, 0, 6);

            // the button to end finalize the System
            submit = new Button("Finalize");
            submit.setOnAction(
                event -> {
                  constructorController.processFinalizeButton(primaryStage);
                });
            constructionMap.add(submit, 0, 4);
            submit.setVisible(false);

            mapScene = new Scene(constructionMap, 800, 500);
            window.setScene(mapScene);
          }
        });

    inputGrid
        .getChildren()
        .addAll(
            height,
            heightInput,
            width,
            widthInput,
            mapNameLabel,
            mapNameInput,
            startConstruction,
            cancelDimensions);

    inputScene = new Scene(inputGrid, 300, 300);
    window.setScene(inputScene);

    // Get the map started.

    window.show();
  }
}

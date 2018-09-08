import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/** This class is used to construct the admin view. */
public class AdminView extends Application {
  ScrollPane usersCurrentlyInTransitSystem;
  Text usersInTransitText = new Text();
  Text revenueText = new Text();
  Text revenueLostText = new Text();
  ScrollPane mostVisitedTerminalsWindow = new ScrollPane();
  ScrollPane leastVisitedTerminalsWindow = new ScrollPane();
  Text mostVisitedTerminals = new Text();
  Text leastVisitedTerminals = new Text();
  Text terminalsReport = new Text();
  Text noTransitSystem = new Text("No Transit System associated with this Admin User");
  Button changeBusFare;
  Button changeSubFare;
  Button constructTransitSystem = new Button("Construct a Transit System");
  Button reset;
  Button changeStudentDiscount = new Button("Set the student discount");
  Text dateThatHadMostRevenue = new Text();
  Text pickDateToGetReport = new Text();
  GridPane grid2;
  GridPane visualMap = new GridPane();
  DatePicker getReportDatePicker = new DatePicker();
  private AdminController controller;

  /**
   * This method is the initializer for the class AdminView.
   *
   * @param adminUser
   * @param clock
   */
  public AdminView(AdminUser adminUser, Clock clock) {
    this.controller = new AdminController(adminUser, clock);
    controller.setAdminView(this);
  }

  /**
   * This method is the start method to create the JavaFX view for admin view.
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Admin View");

    grid2 = new GridPane();
    grid2.setPadding(new Insets(15, 15, 15, 15));
    grid2.setVgap(15);
    grid2.setHgap(15);

    visualMap.setAlignment(Pos.CENTER);
    visualMap.setPadding(new Insets(25, 25, 25, 25));
    visualMap.setVgap(25);
    visualMap.setHgap(25);

    changeBusFare = new Button("Change Bus Fare");
    GridPane.setConstraints(changeBusFare, 0, 0);
    changeBusFare.setOnAction(
        e -> {
          controller.generateTextDialogue(
              "Change Bus Fare",
              "Please input the desired bus fare:",
              "Bus fare has been changed",
              "Error in changing bus fare. Please make sure you entered a valid value!",
              new processChangeBusFareDialogue());
        });

    changeSubFare = new Button("Change Subway Fare");
    GridPane.setConstraints(changeSubFare, 1, 0);
    changeSubFare.setOnAction(
        e -> {
          controller.generateTextDialogue(
              "Change Subway Fare",
              "Please input the desired subway fare:",
              "Subway fare has been changed",
              "Error in changing subway fare. Please make sure you entered a valid value!",
              new processChangeSubwayFareDialogue());
        });

    GridPane.setConstraints(changeStudentDiscount, 2, 0);
    changeStudentDiscount.setOnAction(
        e ->
            controller.generateTextDialogue(
                "Change Student Discount",
                "Please input the % of the fare that you want students to pay",
                "Student discount has been changed",
                "Please enter a value between 0 and 1",
                new processSetDiscountRateDialogue()));

    GridPane.setConstraints(noTransitSystem, 0, 1);

    GridPane.setConstraints(constructTransitSystem, 0, 2);
    constructTransitSystem.setOnAction(event -> controller.constructTransitSystem(primaryStage));

    reset = new Button("System Reset");
    GridPane.setConstraints(reset, 0, 1);
    reset.setOnAction(e -> controller.resetSystem());

    usersCurrentlyInTransitSystem = new ScrollPane();
    GridPane.setConstraints(usersCurrentlyInTransitSystem, 0, 2);

    GridPane.setConstraints(mostVisitedTerminalsWindow, 0, 3);
    GridPane.setConstraints(leastVisitedTerminalsWindow, 1, 3);

    GridPane.setConstraints(dateThatHadMostRevenue, 0, 5);

    GridPane.setConstraints(revenueText, 0, 6);
    GridPane.setConstraints(revenueLostText, 1, 6);

    GridPane.setConstraints(terminalsReport, 0, 7);

    pickDateToGetReport.setText("Please pick the date you want the report for!");
    GridPane.setConstraints(pickDateToGetReport, 2, 2);
    GridPane.setConstraints(getReportDatePicker, 2, 3);
    getReportDatePicker.setOnAction(
        event -> {
          controller.getReportOnDate();
        });

    Button logOut = new Button("Log Out");
    logOut.setOnAction(event -> controller.logOut(primaryStage));
    GridPane.setConstraints(logOut, 0, 8);

    controller.updateUserObserver();
    controller.constructMap();

    grid2
        .getChildren()
        .addAll(
            changeBusFare,
            changeSubFare,
            changeStudentDiscount,
            noTransitSystem,
            constructTransitSystem,
            reset,
            usersCurrentlyInTransitSystem,
            mostVisitedTerminalsWindow,
            leastVisitedTerminalsWindow,
            pickDateToGetReport,
            getReportDatePicker,
            dateThatHadMostRevenue,
            revenueText,
            revenueLostText,
            terminalsReport,
            logOut);

    VBox test = new VBox(10);
    test.getChildren().addAll(grid2, visualMap);
    Scene opsScene = new Scene(test, 1200, 600);
    primaryStage.setScene(opsScene);

    primaryStage.show();
  }
}

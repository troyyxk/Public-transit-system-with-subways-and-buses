import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;

/** This class is the controller class for the admin user. */
public class AdminController extends GUIController implements UserObserver {
  AdminUser adminUserModel;
  AdminView adminView;
  Clock clock;
  ObservableList<TransitLine> transitLines = FXCollections.observableArrayList();

  /**
   * This method is the initializer for the class AdminController.
   *
   * @param adminUserModel
   * @param clock
   */
  public AdminController(AdminUser adminUserModel, Clock clock) {
    this.adminUserModel = adminUserModel;
    this.adminUserModel.addObserver(this);
    if (adminUserModel.transitSystem != null) {
      transitLines.addAll(adminUserModel.transitSystem.map.getAllLines());
    }
    this.clock = clock;
  }

  /**
   * This method is used to set Admin view.
   *
   * @param adminView
   */
  public void setAdminView(AdminView adminView) {
    this.adminView = adminView;
  }

  /** This method is used to reset the system. */
  public void resetSystem() {
    if (generateConfirmation("Are you sure you want to reset the system?")) {
      adminUserModel.reset();
      generateAlert("Notice", "System was reset!");
    }
  }

  /** This method is used to construct the map. */
  void constructMap() {
    ArrayList<Color> colors = new ArrayList<>();
    colors.add(Color.RED);
    colors.add(Color.BLUE);
    colors.add(Color.GREEN);
    colors.add(Color.YELLOW);
    colors.add(Color.BROWN);
    colors.add(Color.OLIVE);
    Color lineColor = Color.BLACK;
    if (adminUserModel.transitSystem != null
        && adminUserModel.transitSystem.map.coordinateOfLines.size() > 0) {
      for (int i = 0; i < adminUserModel.transitSystem.map.coordinateOfLines.size(); i++) {
        if (i < colors.size()) {
          lineColor = colors.get(i);
        }
        Integer[] currVisualTransitLine = adminUserModel.transitSystem.map.coordinateOfLines.get(i);
        ArrayList<Circle> currTerminals = new ArrayList<>();
        for (int j = 0; j < currVisualTransitLine.length; j += 2) {
          Circle visualTerminal = new Circle(10 - i, lineColor);
          GridPane.setConstraints(
              visualTerminal, currVisualTransitLine[j], currVisualTransitLine[j + 1]);
          adminView.visualMap.getChildren().add(visualTerminal);
          currTerminals.add(visualTerminal);
        }
      }
    }
  }

  /** This method is used to update new changes in the admin user model. */
  public void updateUserObserver() {
    if (adminUserModel.transitSystem != null) {
      updateUsersInTransit();
      updateTerminalReport();
      updateRevenue();
      updateMostVisitedTerminals();
      updateLeastVisitedTerminals();
      updateDayWithMostRevenue();
      adminView.noTransitSystem.setVisible(false);
      adminView.constructTransitSystem.setVisible(false);
      adminView.usersCurrentlyInTransitSystem.setVisible(true);
      adminView.terminalsReport.setVisible(true);
      adminView.revenueText.setVisible(true);
      adminView.mostVisitedTerminalsWindow.setVisible(true);
      adminView.leastVisitedTerminalsWindow.setVisible(true);
      adminView.reset.setVisible(true);
      adminView.changeBusFare.setVisible(true);
      adminView.changeSubFare.setVisible(true);
      adminView.pickDateToGetReport.setVisible(true);
      adminView.getReportDatePicker.setVisible(true);
      adminView.changeStudentDiscount.setVisible(true);
    } else {
      adminView.constructTransitSystem.setVisible(true);
      adminView.noTransitSystem.setVisible(true);
      adminView.usersCurrentlyInTransitSystem.setVisible(false);
      adminView.terminalsReport.setVisible(false);
      adminView.revenueText.setVisible(false);
      adminView.mostVisitedTerminalsWindow.setVisible(false);
      adminView.leastVisitedTerminalsWindow.setVisible(false);
      adminView.reset.setVisible(false);
      adminView.pickDateToGetReport.setVisible(false);
      adminView.getReportDatePicker.setVisible(false);
      adminView.changeBusFare.setVisible(false);
      adminView.changeSubFare.setVisible(false);
      adminView.changeStudentDiscount.setVisible(false);
    }
  }

  /**
   * This method is used to construct the transit system.
   *
   * @param primaryStage
   */
  void constructTransitSystem(Stage primaryStage) {
    ConstructorView constructorView =
        new ConstructorView(
            new ConstructorController(adminUserModel.accountManager, adminUserModel, clock));
    constructorView.start(primaryStage);
  }

  /** This method is used to update users in the transit system. */
  void updateUsersInTransit() {
    adminView.usersInTransitText.setText(getAllUsersInTransit());
    adminView.usersCurrentlyInTransitSystem.setContent(adminView.usersInTransitText);
  }

  /** This method is used to update terminal report. */
  void updateTerminalReport() {
    String terminalReport = "Total Terminals Reached: ";
    terminalReport += adminUserModel.getTotalTerminals();
    terminalReport += System.lineSeparator();
    terminalReport += "Total Stations Reached: ";
    terminalReport += adminUserModel.getTotalStations();
    terminalReport += System.lineSeparator();
    terminalReport += "Total Stops Reached: ";
    terminalReport += adminUserModel.getTotalStops();
    adminView.terminalsReport.setText(terminalReport);
  }

  /** This method is used to update revenue. */
  void updateRevenue() {
    adminView.revenueText.setText(
        "Total Revenue Generated Today: "
            + NumberFormat.getCurrencyInstance()
                .format(adminUserModel.transitSystem.statisticsTracker.getTotalRevenue()));
    adminView.revenueLostText.setText(adminUserModel.getRevenueLostFromDiscount());
  }

  /**
   * This method is used to ger all users in the transit system.
   *
   * @return
   */
  public String getAllUsersInTransit() {
    return adminUserModel.getUsersInTransit();
  }

  /** This method is used to get report on data. */
  public void getReportOnDate() {
    LocalDate dateOfReport = adminView.getReportDatePicker.getValue();
    Report report =
        adminUserModel.transitSystem.statisticsTracker.reportManager.getReportOnDate(dateOfReport);
    if (report != null) {
      generateAlert("Report", report.toString());
    } else {
      generateAlert("Notice", "No report on this date was found on record!");
    }
  }

  /** This method is used to update most visited terminals. */
  public void updateMostVisitedTerminals() {
    String mostVisitedTerminal = "Most Visited Terminal(s): ";
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += adminUserModel.getMostVisitedTerminal("TERMINAL");
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += "Most Visited Station(s): ";
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += adminUserModel.getMostVisitedTerminal("STATION");
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += "Most Visited Stop(s): ";
    mostVisitedTerminal += System.lineSeparator();
    mostVisitedTerminal += adminUserModel.getMostVisitedTerminal("STOP");
    adminView.mostVisitedTerminals.setText(mostVisitedTerminal);
    adminView.mostVisitedTerminalsWindow.setContent(adminView.mostVisitedTerminals);
  }

  /** This method is used to update least visited terminals. */
  public void updateLeastVisitedTerminals() {
    String leastVisitedTerminal = "Least Visited Terminal(s): ";
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += adminUserModel.getLeastVisitedTerminal("TERMINAL");
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += "Least Visited Station(s): ";
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += adminUserModel.getLeastVisitedTerminal("STATION");
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += "Least Visited Stop(s): ";
    leastVisitedTerminal += System.lineSeparator();
    leastVisitedTerminal += adminUserModel.getLeastVisitedTerminal("STOP");
    adminView.leastVisitedTerminals.setText(leastVisitedTerminal);
    adminView.leastVisitedTerminalsWindow.setContent(adminView.leastVisitedTerminals);
  }

  /**
   * This method is used to log out.
   *
   * @param primaryStage
   */
  public void logOut(Stage primaryStage) {
    LoginController loginController = new LoginController(adminUserModel.accountManager, clock);
    LoginView loginView = new LoginView(loginController);
    loginView.start(primaryStage);
  }

  /** This method is used to update day with most revenue. */
  public void updateDayWithMostRevenue() {
    adminView.dateThatHadMostRevenue.setText(adminUserModel.getDayWithMostRevenue());
  }

  /**
   * This method is used to change bus fare.
   *
   * @param newFare
   */
  public void changeBusFare(Double newFare) {
    adminUserModel.changeBusFare(newFare);
  }

  /**
   * This method is used to change subway fare.
   *
   * @param newFare
   */
  public void changeSubwayFare(Double newFare) {
    adminUserModel.changeSubwayFare(newFare);
  }
}

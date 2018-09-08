import java.io.Serializable;

/** This class is the class for admin user. */
public class AdminUser extends User implements Serializable {
  TransitSystem transitSystem;

  /**
   * This method is the initializer for the class AdminUser.
   *
   * @param userName
   * @param password
   * @param email
   */
  public AdminUser(String userName, String password, String email) {
    super(userName, password, email);
  }

  /** This method is used to get users in the transit system. */
  public String getUsersInTransit() {
    StringBuilder listOfUsers =
        new StringBuilder("The users in transit are:" + System.lineSeparator());
    for (int i = 0; i < transitSystem.riderUsersInTransit.size(); i++) {
      listOfUsers
          .append(transitSystem.riderUsersInTransit.get(i).toString())
          .append(System.lineSeparator());
    }
    return listOfUsers.toString();
  }

  /** This method is used to change bus fare. */
  public void changeBusFare(double newFare) {
    transitSystem.setBusFare(newFare);
    updateObservers();
  }

  /**
   * This method is used to change subway fare.
   *
   * @param newFare
   */
  public void changeSubwayFare(double newFare) {
    transitSystem.setSubwayFare(newFare);
    updateObservers();
  }

  /**
   * This method is used to get transit system.
   *
   * @return
   */
  TransitSystem getTransitSystem() {
    return transitSystem;
  }

  /**
   * This method is used toset transit system.
   *
   * @param transitSystem
   */
  public void setTransitSystem(TransitSystem transitSystem) {
    this.transitSystem = transitSystem;
    transitSystem.setAdminUser(this);
  }

  /**
   * This method is used to get most visited terminal.
   *
   * @param type
   * @return
   */
  public String getMostVisitedTerminal(String type) {
    return transitSystem.statisticsTracker.getMostVisitedTerminal(type);
  }

  /**
   * This method is used to get least visited terminals.
   *
   * @param type
   * @return
   */
  public String getLeastVisitedTerminal(String type) {
    return transitSystem.statisticsTracker.getLeastVisitedTerminal(type);
  }

  /**
   * This method is used to get revenue lost from discount.
   *
   * @return
   */
  public String getRevenueLostFromDiscount() {
    return transitSystem.statisticsTracker.getRevenueSavedFromDiscount();
  }

  /**
   * This method is used to get day with most revenue.
   *
   * @return
   */
  public String getDayWithMostRevenue() {
    return transitSystem.statisticsTracker.getMostRevenueDay();
  }

  /** This method is used to reset. */
  public void reset() {
    transitSystem.statisticsTracker.reset();
    updateObservers();
  }

  /**
   * This method is used to get total terminals.
   *
   * @return
   */
  public int getTotalTerminals() {
    return transitSystem.statisticsTracker.getTotalTerminals();
  }

  /**
   * This method is used to get total stops.
   *
   * @return
   */
  public int getTotalStops() {
    return transitSystem.statisticsTracker.getTotalStops();
  }

  /**
   * This method is used to get total stations.
   *
   * @return
   */
  public int getTotalStations() {
    return transitSystem.statisticsTracker.getTotalStations();
  }

  /**
   * This method is used to change student discount.
   *
   * @param newDiscount
   */
  public void changeStudentDiscount(double newDiscount) {
    transitSystem.setStudentDiscount(newDiscount);
    updateObservers();
  }
}

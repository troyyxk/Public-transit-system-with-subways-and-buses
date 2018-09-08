import java.io.Serializable;
import java.util.ArrayList;

public class TransitSystem implements Serializable {
  double busFare = 2.00;
  double subwayFare = 0.5;
  double studentDiscount = 0.75;
  Map map;
  StatisticsTracker statisticsTracker;
  AdminUser adminUser;
  String name;
  ArrayList<RiderUser> riderUsersInTransit = new ArrayList<>();

  /**
   * The constructor constructs a transitSystem class with three attributes
   * the statisticsTracker is the class where all the operating statistics are kept track of
   *
   * @param map the map consisting of all the terminals and transit lines
   * @param clock an instance of the time controlling class
   */
  public TransitSystem(Map map, Clock clock) {
    this.map = map;
    this.statisticsTracker = new StatisticsTracker(this, clock);
    this.name = map.mapName;
  }


  void setClock(Clock clock) {
    statisticsTracker.clock = clock;
  }

  /**
   * This method update the statistics of the transit system
   */
  void update() {
    if (adminUser != null) {
      adminUser.updateObservers();
    }
  }

  void addUsersInTransit(RiderUser riderUser) {
    riderUsersInTransit.add(riderUser);
  }

  void removeUsersInTransit(RiderUser riderUser) {
    riderUsersInTransit.remove(riderUser);
  }

  public void setAdminUser(AdminUser adminUser) {
    this.adminUser = adminUser;
  }

  public void setBusFare(double newFare) {
    busFare = newFare;
  }

  public void setSubwayFare(double newFare) {
    subwayFare = newFare;
  }

  public void setStudentDiscount(double newDiscount) {
    this.studentDiscount = newDiscount;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TransitSystem && ((TransitSystem) obj).name.equals(this.name);
  }

  @Override
  public String toString() {
    return this.name;
  }
}

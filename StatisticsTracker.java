import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StatisticsTracker implements Serializable, ClockObserver {
  ReportManager reportManager;
  TransitSystem transitSystem;
  transient Clock clock;
  double[] fareEachDay = new double[31];
  double averageFareFromYesterday = 4.00;
  double totalTrips = 0;
  double revenueLostFromDiscount = 0;
  transient SimpleDateFormat dayFormat = new SimpleDateFormat("d");
  transient SimpleDateFormat monthFormat = new SimpleDateFormat("M");
  private int totalTerminals = 0;
  private int totalStops = 0;
  private int totalStations = 0;
  private double totalRevenue = 0;
  private Map map;
  private String day;
  private String month;

  /**
   * Class constructor
   *
   * @param transitSystem The transit System that is currently using
   * @param clock time controlling class
   */
  StatisticsTracker(TransitSystem transitSystem, Clock clock) {
    this.transitSystem = transitSystem;
    this.map = transitSystem.map;
    this.clock = clock;
    this.clock.addObserver(this);
    day = dayFormat.format(this.clock.getDate());
    month = monthFormat.format(this.clock.getDate());
    this.reportManager = new ReportManager(this);
  }

  /**
   * reset the whole value of the tracker
   */
  void reset() {
    addReport();
    if (!(totalTrips == 0)) {
      averageFareFromYesterday = totalRevenue / totalTrips;
    } else {
      averageFareFromYesterday = 4.00;
    }
    totalTrips = 0;
    totalTerminals = 0;
    totalRevenue = 0;
    totalStops = 0;
    totalStations = 0;
    revenueLostFromDiscount = 0;
    map.reset();
  }

  /**
   * This function updates observers
   */
  public void updateEverySec() {
    String currentDay = dayFormat.format(clock.getDate());
    if (!day.equals(currentDay)) {
      fareEachDay[Integer.parseInt(day) - 1] = totalRevenue;
      reset();
      transitSystem.update();
      day = currentDay;
    }
    String currentMonth = monthFormat.format(clock.getDate());
    if (!month.equals(currentMonth)) {
      getMostRevenueDay();
      fareEachDay = new double[31];
      month = currentMonth;
    }
  }

  /**
   *
   * @return a string saying the day with the most revenue
   */
  String getMostRevenueDay() {
    int dayWithMost = 0;
    String monthStr = new DateFormatSymbols().getMonths()[Integer.parseInt(month) - 1];
    for (int i = 0; i < fareEachDay.length; i++) {
      if (fareEachDay[dayWithMost] < fareEachDay[i]) {
        dayWithMost = i;
      }
    }
    dayWithMost += 1;
    return "In "
        + monthStr
        + " the date that generated the most revenue was "
        + monthStr
        + ", "
        + dayWithMost
        + " with total revenue "
        + NumberFormat.getCurrencyInstance().format(fareEachDay[dayWithMost - 1]);
  }

  /**
   * This method writes to the eventWriter the report at the current time
   */
  void addReport() {
    Report report =
        new Report(
            reportManager,
            clock.getDate(),
            totalTerminals,
            totalStops,
            totalStations,
            totalRevenue);
    EventsWriter.writeLog(report.toString());
  }

  /**
   * The function return the name of the terminal that is mostly visited
   *
   * @param input whether it's a terminal, a station or a stop
   * @return a terminal name that is most visited
   */
  String getMostVisitedTerminal(String input) {
    ArrayList<TransitLine> listToIterateOver = map.getDesiredLines(input);
    if (listToIterateOver.size() == 0) {
      return "No " + input + " lines exist in this system!";
    }
    Terminal mostVisited = listToIterateOver.get(0).get(0);
    for (TransitLine currentTransitLine : listToIterateOver) {
      for (int j = 0; j < currentTransitLine.getSize(); j++) {
        Terminal currentTerminal = currentTransitLine.get(j);
        if (currentTerminal.numTimesAccessed > mostVisited.numTimesAccessed) {
          mostVisited = currentTerminal;
        }
      }
    }
    ArrayList<Terminal> terminalsWithSameAmount = new ArrayList<>();
    for (TransitLine currentTransitLine : listToIterateOver) {
      for (int j = 0; j < currentTransitLine.getSize(); j++) {
        Terminal currentTerminal = currentTransitLine.get(j);
        if (currentTerminal.numTimesAccessed == mostVisited.numTimesAccessed) {
          terminalsWithSameAmount.add(currentTerminal);
        }
      }
    }
    StringBuilder toReturn = new StringBuilder();
    for (Terminal aTerminalsWithSameAmount : terminalsWithSameAmount) {
      toReturn.append(aTerminalsWithSameAmount.name).append(System.lineSeparator());
    }
    toReturn
        .append("is/are the most visited ")
        .append(input.toLowerCase())
        .append("(s)")
        .append(System.lineSeparator())
        .append("They have been visited: ")
        .append(mostVisited.numTimesAccessed)
        .append(" times since last reset.");
    return toReturn.toString();
  }


  /**
   * The function return the name of the terminal that is mostly visited
   *
   * @param input whether it's a terminal, a station or a stop
   * @return a terminal name that is most visited
   */
  String getLeastVisitedTerminal(String input) {
    ArrayList<TransitLine> listToIterateOver = map.getDesiredLines(input);
    if (listToIterateOver.size() == 0) {
      return "No " + input + " lines exist in this system!";
    }
    Terminal leastVisited = listToIterateOver.get(0).get(0);
    for (TransitLine currentTransitLine : listToIterateOver) {
      for (int j = 0; j < currentTransitLine.getSize(); j++) {
        Terminal currentTerminal = currentTransitLine.get(j);
        if (currentTerminal.numTimesAccessed < leastVisited.numTimesAccessed) {
          leastVisited = currentTerminal;
        }
      }
    }
    ArrayList<Terminal> terminalsWithSameAmount = new ArrayList<>();
    for (TransitLine currentTransitLine : listToIterateOver) {
      for (int j = 0; j < currentTransitLine.getSize(); j++) {
        Terminal currentTerminal = currentTransitLine.get(j);
        if (currentTerminal.numTimesAccessed == leastVisited.numTimesAccessed) {
          terminalsWithSameAmount.add(currentTerminal);
        }
      }
    }
    StringBuilder toReturn = new StringBuilder();
    for (Terminal aTerminalsWithSameAmount : terminalsWithSameAmount) {
      toReturn.append(aTerminalsWithSameAmount.name).append(System.lineSeparator());
    }
    toReturn
        .append("is/are the least visited ")
        .append(input.toLowerCase())
        .append("(s)")
        .append(System.lineSeparator())
        .append("They have been visited: ")
        .append(leastVisited.numTimesAccessed)
        .append(" times since last reset.");
    return toReturn.toString();
  }

  String getRevenueSavedFromDiscount() {
    return "Revenue lost from discounts: "
        + NumberFormat.getCurrencyInstance().format(revenueLostFromDiscount);
  }


  /**
   * This function adds the extra number of stations to the existing system
   *
   * @param numberOfStops the extra number of station
   */
  void addStops(int numberOfStops) {
    totalStops = totalStops + numberOfStops;
    totalTerminals = totalTerminals + numberOfStops;
  }

  /**
   * This function adds the extra number of stations to the existing system
   *
   * @param numberOfStations the extra number of station
   */
  void addStations(int numberOfStations) {
    totalStations = totalStations + numberOfStations;
    totalTerminals = totalTerminals + numberOfStations;
  }

  /**
   * It added the amount of revenue to the corresponding day
   * @param fare The amount of revenue obtained through the day
   */
  void addRevenue(double fare) {
    totalRevenue += fare;
    fareEachDay[Integer.parseInt(day) - 1] += fare;
  }

  public TransitSystem getTransitSystem() {
    return transitSystem;
  }

  public int getTotalTerminals() {
    return totalTerminals;
  }

  public int getTotalStops() {
    return totalStops;
  }

  public int getTotalStations() {
    return totalStations;
  }

  public double getTotalRevenue() {
    return totalRevenue;
  }
}

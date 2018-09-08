// import sun.java2d.pipe.SpanShapeRenderer;
//
// import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Trip implements Serializable {
  TransitSystem transitSystem;
  Card card;
  Terminal entrance;
  Terminal exit;
  double fare;
  Date startTime;
  Date exitTime;
  private ArrayList<Terminal> tripBreakdown = new ArrayList<>();
  private TransitLine transitLine;


    /**
     * This constructor construct a trip where a user taps in. The exit terminal of this instance is set
     * via the setExit method
     *
     * @param transitSystem The transitSystem that manages the transit lines
     * @param entrance The entrance terminal of this trip
     * @param card The card the user is currently using
     * @param startTime The trip start time
     */
  public Trip(TransitSystem transitSystem, Terminal entrance, Card card, Date startTime) {
    this.entrance = entrance;
    this.card = card;
    this.transitLine = entrance.getTransitLine();
    this.startTime = startTime;
    this.transitSystem = transitSystem;
  }

  // Constructor if the user tapped out only

    /**
     *This constructor construct a trip where a user taps in. The exit terminal of this instance is set
     * via the setExit method
     *
     * @param transitSystem The transitSystem that manages the transit lines
     * @param exit The exit terminal of this trip
     * @param card The card the user is currently using
     * @param exitTime The trip ends time
     */
  public Trip(TransitSystem transitSystem, Card card, Terminal exit, Date exitTime) {
    this.exit = exit;
    this.card = card;
    this.transitLine = exit.getTransitLine();
    this.exitTime = exitTime;
    this.transitSystem = transitSystem;
  }

  public ArrayList<Terminal> getTripBreakdown() {
    return tripBreakdown;
  }

  public void processTrip(double discount, TripProcessor tripProcessor, double sumOfPreviousFare) {
    tripProcessor.processTrip(discount, sumOfPreviousFare, this);
    transitSystem.statisticsTracker.totalTrips += 1;
  }

  public void addCountToEntrance() {
    entrance.numTimesAccessed += 1;
  }

  public void addCountToExit() {
    exit.numTimesAccessed += 1;
  }

  public void addRevenueLostToDiscount(double revLost) {
    transitSystem.statisticsTracker.revenueLostFromDiscount += revLost;
  }

    /**
     * This function breaks down a trip into single stops or stations
     * tripBreakdown is an ArrayList consisting of terminals
     */
  public void generateTripBreakdown() {
    int startIndex = 0;
    int endIndex = 0;
    for (int i = 0; i < transitLine.getSize(); i++) {
      if (entrance.equals(transitLine.get(i))) {
        startIndex = i;
      }
      if (exit.equals(transitLine.get(i))) {
        endIndex = i;
      }
    }
    if (endIndex > startIndex) {
      for (int i = startIndex; i < endIndex + 1; i++) {
        tripBreakdown.add(transitLine.get(i));
      }
    } else {
      for (int i = endIndex; i < startIndex + 1; i++) {
        tripBreakdown.add(transitLine.get(i));
      }
    }
  }

    /**
     * The function set the exit terminal and the time for this trip
     *
     * @param exit This is the exit terminal
     * @param exitTime This is the exit time
     */
  public void setExit(Terminal exit, Date exitTime) {
    this.exit = exit;
    this.exitTime = exitTime;
  }

  public TransitLine getTransitLine() {
    return transitLine;
  }

  public abstract void addTerminal(int numberOfTerminals);

  public abstract double calculateRawFare();

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Trip
        && ((Trip) obj).startTime == this.startTime
        && ((Trip) obj).exitTime == this.exitTime
        && ((Trip) obj).entrance.equals(this.entrance)
        && ((Trip) obj).exit.equals(this.exit);
  }

  @Override
  public String toString() {
    String exitName;
    String entranceName;
    String sTime;
    String eTime;
    if (exit != null) {
      eTime = String.format(" at time: %tc", exitTime);
      exitName = exit.getName();
    } else {
      eTime = "at time: N/A";
      exitName = "N/A";
    }
    if (entrance != null) {
      sTime = String.format(" at time: %tc", startTime);
      entranceName = entrance.getName();
    } else {
      sTime = " at time: N/A";
      entranceName = "N/A";
    }
    return "A trip starting from "
        + entranceName
        + System.lineSeparator()
        + sTime
        + System.lineSeparator()
        + " to "
        + exitName
        + System.lineSeparator()
        + eTime
        + System.lineSeparator()
        + "Paid: "
        + NumberFormat.getCurrencyInstance().format(fare);
  }
}

import java.util.Date;

/**
 * This class generates whether an entrance trip or an exit trip by having two different constructors
 * the first constructor construct an entrance trip and the second one an exit trip. The two constructors
 * differ in the order of parameter when it comes to terminal and card as shown below
 */
public class SubwayTrip extends Trip {
    /**
     *
     * @param transitSystem the overarching transit system
     * @param entrance the entrance terminal
     * @param card the card used
     * @param startTime the time when user tapped in the system
     */
  SubwayTrip(TransitSystem transitSystem, Terminal entrance, Card card, Date startTime) {
    super(transitSystem, entrance, card, startTime);
  }

    /**
     *
     * @param transitSystem the overarching transit system
     * @param card the card used
     * @param exit the exit terminal
     * @param exitTime the time when user tapped out the system
     */
  SubwayTrip(TransitSystem transitSystem, Card card, Terminal exit, Date exitTime) {
    super(transitSystem, card, exit, exitTime);
  }

    /**
     *
     * @return the subway fare
     */
  public double calculateRawFare() {
    if (this.getTripBreakdown().size() != 0) {
      return transitSystem.subwayFare * (this.getTripBreakdown().size() - 1);
    } else {
      return 6;
    }
  }

  public void addTerminal(int numberOfTerminals) {
    transitSystem.statisticsTracker.addStations(numberOfTerminals);
  }
}

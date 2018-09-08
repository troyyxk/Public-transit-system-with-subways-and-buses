import java.util.Date;

/**
 * This class is the subclass of class Trip which is for one type of Trip which is BusTrip.
 */
public class BusTrip extends Trip {

  /**
   *  The constructor for class BusTrip.
   * @param transitSystem
   * @param entrance
   * @param card
   * @param startTime
   */
  BusTrip(TransitSystem transitSystem, Terminal entrance, Card card, Date startTime) {
    super(transitSystem, entrance, card, startTime);
  }

  /**
   * The constructor for class BusTrip.
   * @param transitSystem
   * @param card
   * @param exit
   * @param exitTime
   */
  BusTrip(TransitSystem transitSystem, Card card, Terminal exit, Date exitTime) {
    super(transitSystem, card, exit, exitTime);
  }

  /**
   * Calculate the rough fare for this trip.
   *
   * @return busFare : int
   */
  public double calculateRawFare() {
    return transitSystem.busFare;
  }

  /**
   * Add a certain number of terminals to this trip.
   *
   * @param numberOfTerminals
   */
  public void addTerminal(int numberOfTerminals) {
    transitSystem.statisticsTracker.addStops(numberOfTerminals);
  }
}

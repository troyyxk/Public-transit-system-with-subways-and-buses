/**
 * This class is a subclass of class TripProcessor and it is used to process trips that consist of
 * difference lines.
 */
public class processDifferentLineOrSystemTrip implements TripProcessor {

  /** The constructor of cf class processDifferentLineOrSystemTrip. */
  @Override
  public void processTrip(double discount, double sumOfPreviousFare, Trip trip) {
    trip.addTerminal(trip.getTransitLine().getSize() / 2);
    trip.fare = discount * Math.min(6, 6 - sumOfPreviousFare);
    trip.addRevenueLostToDiscount(Math.min(6, 6 - sumOfPreviousFare) - trip.fare);
    trip.card.deductMoney(trip.fare);
    trip.transitSystem.statisticsTracker.addRevenue(trip.fare);
    EventsWriter.tappedAtDifferentLineOrSystem(trip.card);
  }
}

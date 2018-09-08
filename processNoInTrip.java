/**
 * This class is a subclass of class TripProcessor and it is used to process trips without tap in.
 */
public class processNoInTrip implements TripProcessor {

  /** The constructor of cf class processNoInTrip. */
  @Override
  public void processTrip(double discount, double sumOfPreviousFare, Trip trip) {
    int tripLength = trip.getTransitLine().getSize() / 2;
    trip.addTerminal(tripLength);
    trip.fare = discount * Math.min(trip.calculateRawFare(), 6 - sumOfPreviousFare);
    trip.addRevenueLostToDiscount(
        Math.min(trip.calculateRawFare(), 6 - sumOfPreviousFare) - trip.fare);
    trip.card.deductMoney(trip.fare);
    trip.transitSystem.statisticsTracker.addRevenue(trip.fare);
    EventsWriter.forgotToTapIn(trip.card);
  }
}

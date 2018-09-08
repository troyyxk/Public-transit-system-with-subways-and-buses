/**
 * This class is a subclass of class TripProcessor and it is used to process trips without tap out.
 */
public class processNoOutTrip implements TripProcessor {

  /**
   * The constructor of cf class processNoOutTrip.
   */
  @Override
  public void processTrip(double discount, double sumOfPreviousFare, Trip trip) {
    trip.addTerminal(trip.getTransitLine().getSize() / 2);
    trip.fare =
        discount
            * Math.min(
                trip.transitSystem.statisticsTracker.averageFareFromYesterday,
                6 - sumOfPreviousFare);
    trip.addRevenueLostToDiscount(Math.min(6, 6 - sumOfPreviousFare) - trip.fare);
    trip.card.deductMoney(trip.fare);
    trip.transitSystem.statisticsTracker.addRevenue(trip.fare);
    EventsWriter.forgotToTapOut(trip.card);
  }
}

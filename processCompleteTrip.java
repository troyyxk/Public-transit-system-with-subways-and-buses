/** This class is a subclass of class TripProcessor and it is used to process the complete trips. */
public class processCompleteTrip implements TripProcessor {

  /** The constructor of cf class processCompleteTrip. */
  @Override
  public void processTrip(double discount, double sumOfPreviousFare, Trip trip) {
    trip.generateTripBreakdown();
    int tripLength = trip.getTripBreakdown().size() - 1;
    trip.fare = discount * Math.min(trip.calculateRawFare(), 6 - sumOfPreviousFare);
    trip.addRevenueLostToDiscount(
        Math.min(trip.calculateRawFare(), 6 - sumOfPreviousFare) - trip.fare);
    trip.card.deductMoney(trip.fare);
    trip.transitSystem.statisticsTracker.addRevenue(trip.fare);
    EventsWriter.completeTripLog(trip.card);
    trip.addTerminal(tripLength);
  }
}

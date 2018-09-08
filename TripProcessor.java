public interface TripProcessor {
  void processTrip(double discount, double sumOfPreviousFare, Trip trip);
}

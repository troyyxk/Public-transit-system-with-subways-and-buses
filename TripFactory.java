import java.util.Date;

public class TripFactory {
  /**
   * the method takes in the following parameters and return a corresponding trip depends on
   * whether a user is entering or exiting the transit system
   *
   * @param transitSystem the overarching transitSystem that manages the transit lines
   * @param terminal this could be either a bus stop or a subway terminal
   * @param card the transit card associated with this user
   * @param tripType whether the user is entering or exiting the transit system
   * @param time entrance or exit time
   * @return a new trip depending whether it's an entrance trip or exit trip
   */
  public static Trip getTrip(TransitSystem transitSystem, Terminal terminal, Card card, String tripType, Date time) {
    if (tripType.equalsIgnoreCase("In")) {
      if (terminal instanceof Station) {
        return new SubwayTrip(transitSystem, terminal, card, time);
      } else if (terminal instanceof Stop) {
        return new BusTrip(transitSystem, terminal, card, time);
      } else {
        System.out.println("Error in creating trip");
        return null;
      }
    } else if (tripType.equalsIgnoreCase("Out")) {
      if (terminal instanceof Station) {
        return new SubwayTrip(transitSystem, card, terminal, time);
      } else if (terminal instanceof Stop) {
        return new BusTrip(transitSystem, card, terminal, time);
      } else {
        System.out.println("Error in creating trip");
        return null;
      }
    } else {
      System.out.println("Error in creating trip");
      return null;
    }
  }
}

public class TerminalFactory {
  /**
   * Implementation fo the factory design pattern
   *
   * @param terminalType the input could either be a subway or a bus transit line
   * @param name the name of this bus stop or subway station that is about to be created
   * @return a station with the given name is the input type is subway and stop if bus.
   */
  public static Terminal getTerminal(String terminalType, String name) {
    if (terminalType.equalsIgnoreCase("Subway")) {
      return new Station(name);
    } else if (terminalType.equalsIgnoreCase("Bus")) return new Stop(name);
    else {
      return null;
    }
  }
}

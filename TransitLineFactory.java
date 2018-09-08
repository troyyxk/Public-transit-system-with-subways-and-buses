public class TransitLineFactory {
  /**
   * Factory design pattern, the input is the transit line type and the returned object is
   * a bus line or a subway line depending on the input
   *
   * @param transitLineType this could either be a bus line or a subway line
   * @param name the transit line name
   * @param terminalNames an array of strings where each string stands for the name of the stop/terminal that
   *                      this transit line is consisted of
   * @return a subway line or a bus line
   */
  public static TransitLine getTransitLine(
      String transitLineType, String name, String[] terminalNames) {
    if (transitLineType.equalsIgnoreCase("Subway")) {
      return new SubwayLine(terminalNames, name);
    } else if (transitLineType.equalsIgnoreCase("Bus")) return new BusLine(terminalNames, name);
    else {
      return null;
    }
  }
}

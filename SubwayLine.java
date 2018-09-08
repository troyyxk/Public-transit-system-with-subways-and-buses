public class SubwayLine extends TransitLine {
  /**
   * This method construct a new subway line by calling the parent constructor
   *
   * @param terminalNames the array of the strings where each string stands for the terminal name containing in this
   *                      transit line
   * @param name the transit line name
   */
  SubwayLine(String[] terminalNames, String name) {
    super(terminalNames, name, "Subway");
  }
}

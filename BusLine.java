/**
 * This class is the subclass of class TransitLine which is for one type of TransitLine which is BusLines.
 */
public class BusLine extends TransitLine {

  /**
   * The constructor for class BusLine.
   * @param terminalNames
   * @param name
   */
  BusLine(String[] terminalNames, String name) {
    super(terminalNames, name, "Bus");
  }
}

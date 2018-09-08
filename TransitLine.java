import java.io.Serializable;
import java.util.ArrayList;

public abstract class TransitLine implements Serializable {
  TransitSystem transitSystem;
  private ArrayList<Terminal> listOfTerminals = new ArrayList<>();
  private String name;

  /**
   * The constructor construct a subway line
   *
   * @param terminalNames The arrayList of strings where each string is the name of the terminal
   * @param name The transit line name
   * @param terminalType whether it's a subway line or a bus line
   */
  TransitLine(String[] terminalNames, String name, String terminalType) {
    this.name = name;
    for (String terminalName : terminalNames) {
      Terminal currentTerminal = TerminalFactory.getTerminal(terminalType, terminalName);
      if (currentTerminal != null) {
        currentTerminal.setTransitLine(this);
      } else {
        System.out.println("Error with formatting of text file!");
        return;
      }
      listOfTerminals.add(currentTerminal);
    }
  }

  public boolean contains(Terminal someTerminal) {
    return listOfTerminals.contains(someTerminal);
  }

  public void setTransitSystem(TransitSystem transitSystem) {
    this.transitSystem = transitSystem;
  }

  public int getSize() {
    return listOfTerminals.size();
  }

  public String getTransitLineName() {
    return this.name;
  }

    /**
     *
     * @return the transit line type 
     */
  public int getLineType() {
    if (listOfTerminals.get(0) instanceof Station) {
      return 0;
    } else if (listOfTerminals.get(0) instanceof Stop) {
      return 1;
    } else {
      System.out.println("Error");
      return -1;
    }
  }

  public Terminal get(int i) {
    return listOfTerminals.get(i);
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof TransitLine
        && ((TransitLine) obj).transitSystem.name.equals(this.transitSystem.name)
        && ((TransitLine) obj).getTransitLineName().equals(this.name));
  }

  public ArrayList<Terminal> getListOfTerminals() {
    return listOfTerminals;
  }
}

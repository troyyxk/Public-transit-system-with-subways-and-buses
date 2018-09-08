import java.io.Serializable;

/** * A Terminal, super class of (Bus) Stop and (Subway) Station. */
public abstract class Terminal implements Serializable{
  int numTimesAccessed;
  TransitLine transitLine;
  String name;

  /**
   * Construct a terminal with a given name and numTimesAccessed
   *
   * @param name
   */
  public Terminal(String name) {
    this.name = name;
    numTimesAccessed = 0;
  }

  String getName() {
    return this.name;
  }

  public TransitLine getTransitLine() {
    return transitLine;
  }

  public void setTransitLine(TransitLine transitLine) {
    this.transitLine = transitLine;
  }

  @Override
  public String toString() {
    return this.name;
  }
}

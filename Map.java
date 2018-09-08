import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
  String mapName;
  ArrayList<Integer[]> coordinateOfLines = new ArrayList<>();
  private ArrayList<TransitLine> transitLines = new ArrayList<>();

  Map(String mapName) {
    this.mapName = mapName;
  }

  /**
   * depending on different input, different transit lines will be the output
   *
   * @param input whether it's a station, a stop or a terminal in general
   * @return
   */
  ArrayList<TransitLine> getDesiredLines(String input) {
    if (input.equalsIgnoreCase("STATION")) {
      return getSubwayLines();
    } else if (input.equalsIgnoreCase("STOP")) {
      return getBusLines();
    } else if (input.equalsIgnoreCase("TERMINAL")) {
      return getAllLines();
    } else {
      System.out.println("Incorrect input!");
      return null;
    }
  }

  /**
   *
   * @return an arrayList of all the bus line
   */
  ArrayList<TransitLine> getBusLines() {
    ArrayList<TransitLine> busLines = new ArrayList<>();
    for (TransitLine transitLine : transitLines) {
      if (transitLine.getLineType() == 1) {
        busLines.add(transitLine);
      }
    }
    return busLines;
  }

  /**
   *
   * @return an arrayList of all the subway line
   */
  ArrayList<TransitLine> getSubwayLines() {
    ArrayList<TransitLine> subwayLines = new ArrayList<>();
    for (TransitLine transitLine : transitLines) {
      if (transitLine.getLineType() == 0) {
        subwayLines.add(transitLine);
      }
    }
    return subwayLines;
  }

  ArrayList<TransitLine> getAllLines() {
    return transitLines;
  }

  private String getMapName() {
    return mapName;
  }

  /**
   * Reset every terminal visited time to 0
    */
  public void reset() {
    for (TransitLine currentTransitLine : transitLines) {
      for (int j = 0; j < currentTransitLine.getSize(); j++) {
        currentTransitLine.get(j).numTimesAccessed = 0;
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Map && ((Map) obj).getMapName().equals(this.mapName);
  }
}

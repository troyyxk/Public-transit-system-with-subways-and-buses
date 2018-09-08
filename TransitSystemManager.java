import java.io.*;
import java.util.ArrayList;

public class TransitSystemManager extends Manager implements Serializable {
  private static final transient String mapSetupName = "Terminals.txt";
  ArrayList<TransitSystem> transitSystems = new ArrayList<>();
  ArrayList<String> transitSystemNamesInUse = new ArrayList<>();
  transient Clock clock;

  /**
   * This constructor takes a clock instance and pass it to the TransitSystemManager clock
   * attribute. then create the transit map through reading a txt file
   *
   * @param clock
   */
  TransitSystemManager(Clock clock) {
    this.clock = clock;
    readFileCreateMap(mapSetupName);
  }

  public void setClock(Clock clock) {
    this.clock = clock;
    for (int i = 0; i < transitSystems.size(); i++) {
      transitSystems.get(i).setClock(clock);
    }
  }

  /**
   * This method create the map by adding transit lines which are constructed by reading terminal
   * names in the file
   *
   * @param fileName The name of the file consisting of all the transit lines and terminals
   */
  private void readFileCreateMap(String fileName) {
    TransitLine currentLine;
    String line;
    Map currentMap = null;

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
      line = bufferedReader.readLine();
      while (line != null) {
        if (line.contains("Transit Map")) {
          if (currentMap != null) {
            TransitSystem transitSystem = new TransitSystem(currentMap, clock);
            for (int i = 0; i < currentMap.getAllLines().size(); i++) {
              currentMap.getAllLines().get(i).setTransitSystem(transitSystem);
            }
            this.addTransitSystem(transitSystem);
          }
          String[] lineType = line.trim().split("\\|");
          currentMap = new Map(lineType[1].trim());
        }
        String[] lineType = line.trim().split("\\s+");
        StringBuilder lineName = new StringBuilder();
        for (int i = 0; i < lineType.length; i++) {
          if (i < lineType.length - 1) {
            lineName.append(lineType[i]).append(" ");
          } else lineName.append(lineType[i]);
        }
        if (lineType[0].equalsIgnoreCase("Subway") || lineType[0].equalsIgnoreCase("Bus")) {
          line = bufferedReader.readLine();
          String[] terminalNames = line.trim().split("\\s+\\|\\s+");
          currentLine =
              TransitLineFactory.getTransitLine(lineType[0], lineName.toString(), terminalNames);
          try {
            assert currentMap != null;
            currentMap.getAllLines().add(currentLine);
          } catch (NullPointerException e) {
            System.out.println("Incorrect Formatting in Terminals.txt");
          }
        }
        line = bufferedReader.readLine();
      }
      if (currentMap != null) {
        TransitSystem transitSystem = new TransitSystem(currentMap, clock);
        for (int i = 0; i < currentMap.getAllLines().size(); i++) {
          currentMap.getAllLines().get(i).setTransitSystem(transitSystem);
        }
        this.addTransitSystem(transitSystem);
      }
      bufferedReader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Unable to open file");
    } catch (IOException ex) {
      System.out.println("Error reading file transitsystemmanager 1");
    }
  }

  /**
   * This function add one transit system
   *
   * @param transitSystem the new transit system
   */
  public void addTransitSystem(TransitSystem transitSystem) {
    transitSystems.add(transitSystem);
    transitSystemNamesInUse.add(transitSystem.name);
    updateManagerObservers();
  }

  /**
   * This method takes in the transit system name and return the object matching this name
   *
   * @param name the name of this transit system
   * @return the transit system object in the transit system arrayList
   */
  public TransitSystem getTransitSystem(String name) {
    for (TransitSystem transitSystem : transitSystems) {
      if (transitSystem.name.equals(name)) {
        return transitSystem;
      }
    }
    System.out.println("Transit System not found!");
    return null;
  }
}

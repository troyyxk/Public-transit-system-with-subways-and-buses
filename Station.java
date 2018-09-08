/** * A (bus) Station, extends Terminal. */
public class Station extends Terminal {
  /**
   * This constructor calls the parent constructor and pass in a name of the terminal to be constructed
   *
   * @param name the name of this terminal
   */
  Station(String name) {
    super(name);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Station
        && this.name.equals(((Terminal) obj).getName())
        && ((Terminal) obj).transitLine == this.transitLine;
  }
}

/** * A (bus) Station, extends Terminal. */
public class Stop extends Terminal {
  /**
   * This constructor calls the parent constructor and pass in a name of the terminal to be constructed
   *
   * @param name the name of this terminal
   */
  Stop(String name) {
    super(name);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Stop
        && this.name.equals(((Terminal) obj).getName())
        && ((Terminal) obj).transitLine == this.transitLine;
  }
}

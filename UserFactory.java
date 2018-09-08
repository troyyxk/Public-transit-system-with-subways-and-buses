public class UserFactory {
  /**
   * This class creates a new user with a given type of an adminUser or a riderUser
   *
   * @param transitSystemManager
   * @param userType whether this is an adminUser or a riderUser
   * @param email the email that was linked to this new user
   * @param userName the username of this new user
   * @param password the password of this new user
   * @param extra
   * @return
   */
  public static User getUser(
      TransitSystemManager transitSystemManager,
      String userType,
      String email,
      String userName,
      String password,
      String[] extra) {
    if (userType.equalsIgnoreCase("AdminUser")) {
      AdminUser newAdminUser = new AdminUser(userName, password, email);
      if (extra != null) {
        newAdminUser.setTransitSystem(transitSystemManager.getTransitSystem(extra[0].trim()));
      }
      return newAdminUser;
    } else if (userType.equalsIgnoreCase("RiderUser")) {
      RiderUser newRiderUser = new RiderUser(userName, password, email);
      if (extra != null) {
        for (String anExtra : extra) {
          Card card = new Card(anExtra.trim());
          newRiderUser.addCard(card);
        }
      }
      return newRiderUser;
    } else {
      System.out.println("Error with the formatting of Users.txt");
      return null;
    }
  }
}

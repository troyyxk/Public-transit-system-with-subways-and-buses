public class LoginController {
  AccountManager accountManager;

  Clock clock;

  /**
   * The constructor function if no account manager is passed
   */
  public LoginController() {
    clock = new Clock();
    this.accountManager = new AccountManager(clock);
  }

  /**
   * The constructor function if account manager is passed
   */
  public LoginController(AccountManager accountManager, Clock clock) {
    this.accountManager = accountManager;
    this.clock = clock;
  }

  /**
   *This method returns the very user who just logged in 
   *
   * @param username The userName of this user
   * @param password The passWord of this user
   * @return a user object
   */
  public User login(String username, String password) {
    return accountManager.getUser(username, password);
  }
}

import java.io.Serializable;
import java.util.ArrayList;

public abstract class User implements Serializable {
  AccountManager accountManager;
  private String userName;
  private String password;
  private String email;
  private transient ArrayList<UserObserver> observers = new ArrayList<>();

  public User(String userName, String password, String email) {
    /** Construct a new user with the given parameters */
    this.userName = userName;
    this.password = password;
    this.email = email;
  }

  /** The following methods are setter and getters for this class */
  String getPassword() {
    return password;
  }

  String getUsername() {
    return userName;
  }

  void setUsername(String newUserName) {
    this.userName = newUserName;
  }

  String getEmail() {
    return this.email;
  }

  void setAccountManager(AccountManager accountManager) {
    this.accountManager = accountManager;
  }

  void addObserver(UserObserver userObserver) {
    if (observers == null) {
      observers = new ArrayList<>();
    }
    observers.add(userObserver);
  }

  void updateObservers() {
    for (UserObserver observer : observers) {
      observer.updateUserObserver();
    }
  }
}

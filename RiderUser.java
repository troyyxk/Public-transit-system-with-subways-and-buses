import java.io.Serializable;
import java.util.ArrayList;

/** Things needs to be done entry log generation */
public class RiderUser extends User implements Serializable {
  private ArrayList<Card> cards = new ArrayList<>();

  /**
   * The constructor method
   *
   * @param userName <-
   * @param password <-
   * @param email <-
   */
  RiderUser(String userName, String password, String email) {
    super(userName, password, email);
  }

  /**
   * This class adds a card to the user cards
   *
   * @param card a card object
   */
  void addCard(Card card) {
    if (!cards.contains(card)) {
      cards.add(card);
    }
    if (!(this.equals(card.getCardHolder()))) {
      card.associateUser(this);
    }
    updateObservers();
  }

  /**
   * This function changes the username to the newly given string
   * @param newUserName
   * @return
   */
  boolean changeName(String newUserName) {
    if (accountManager.changeUserName(this, newUserName)) {
      updateObservers();
      return true;
    } else {
      return false;
    }
  }

  /**
   * If this email address has .edu in it then it's set to a student acount
   * @param email The user email
   * @return true if the user is a student
   */
  boolean setToStudent(String email) {
    if (email.matches("\\w+@\\w+.edu")) {
      for (Card card : cards) {
        card.applyDiscount();
      }
      return true;
    } else {
      return false;
    }
  }

  ArrayList<Card> getCards() {
    return this.cards;
  }

  /**
   * This method finds a card object with matching card id and return it
   *
   * @param id the card it
   * @return the card object
   */
  Card findCard(String id) {
    for (Card card : this.cards) {
      if (card.getId().equals(id)) {
        return card;
      }
    }
    return null;
  }

  void cardBalanceChanged() {
    updateObservers();
  }

  void pastThreeChanged() {
    updateObservers();
  }

  void removeCard(Card obj) {
    this.cards.remove(obj);
    updateObservers();
  }

  @Override
  public String toString() {
    return getUsername();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof RiderUser && ((RiderUser) obj).getEmail().equals(getEmail()));
  }
}

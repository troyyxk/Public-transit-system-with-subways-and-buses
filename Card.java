import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is used to construct cards.
 */
public class Card implements Serializable {
  Trip currentTrip;
  private Trip prevTrip;
  private String id;
  private RiderUser cardHolder = null;
  private double balance;
  private Trip[] pastThree = new Trip[3];
  private ArrayList<Trip> consecutiveTrips = new ArrayList<>();
  private double discount;

  /**
   * This method is the initializer for the class Card with input id only.
   *
   * @param id
   */
  Card(String id) {
    this.id = id;
    this.balance = 19;
    this.discount = 1;
  }


  /**
   * This method is the initializer for the class Card with both id and balance.
   *
   * @param id
   */
  Card(String id, double balance) {
    this.id = id;
    this.balance = balance;
    this.discount = 1;
  }

  /**
   * This method is used to associate user to this card.
   * @param riderUser
   */
  void associateUser(RiderUser riderUser) {
    this.cardHolder = riderUser;
    if (!(this.cardHolder.getCards().contains(this))) {
      this.cardHolder.addCard(this);
    }
  }

  /**
   * This method is used to get id.
   *
   * @return
   */
  String getId() {
    return this.id;
  }

  /**
   * This method is used to get card holder.
   *
   * @return
   */
  RiderUser getCardHolder() {
    return this.cardHolder;
  }

  /**
   * This method is used to get balance.
   *
   * @return
   */
  double getBalance() {
    return this.balance;
  }

  /**
   * This method is used to apply discount.
   */
  void applyDiscount() {
    this.discount = 0;
  }

  /**
   * This method is used to  add money.
   *
   * @param amount
   */
  void addMoney(double amount) {
    if (amount == 10 || amount == 20 || amount == 50) {
      this.balance += amount;
      cardHolder.cardBalanceChanged();
      EventsWriter.addMoney(this, amount);
      System.out.println(
          "$" + amount + " has been added to Card [" + id + "]" + System.lineSeparator());
    } else {
      System.out.println("Card can only load values of $10, $20 or $50" + System.lineSeparator());
    }
  }

  /**
   * This method is used to deduct money.
   *
   * @param amount
   */
  void deductMoney(double amount) {
    this.balance -= amount;
    cardHolder.cardBalanceChanged();
  }

  /**
   * This method is used to add trip.
   *
   * @param trip
   */
  private void addTrip(Trip trip) {
    if (pastThree[0] == null) {
      pastThree[0] = trip;
    } else if (pastThree[1] == null) {
      pastThree[1] = trip;
    } else if (pastThree[2] == null) {
      pastThree[2] = trip;
    } else {
      pastThree[2] = pastThree[1];
      pastThree[1] = pastThree[0];
      pastThree[0] = trip;
    }
  }

  /**
   * This method is used to get recent trips.
   *
   * @return
   */
  Trip[] getRecentTrips() {
    return pastThree;
  }

  /**
   * This method is used to ta[ in.
   *
   * @param transitSystem
   * @param entrance
   * @param startTime
   */
  void tapIn(TransitSystem transitSystem, Terminal entrance, Date startTime) {
    if (balance >= 0) {
      // RiderUser forgot to tap out in the previous trip
      if (currentTrip != null) {
        currentTrip.transitSystem.riderUsersInTransit.remove(cardHolder);
        currentTrip.processTrip(
            Math.max(discount, transitSystem.studentDiscount),
            new processNoOutTrip(),
            this.calculateSumOfPreviousFare());
        addTrip(currentTrip);
        cardHolder.pastThreeChanged();
        prevTrip = currentTrip;
      }
      // RiderUser did not forget to tap out in the previous trip
      currentTrip = TripFactory.getTrip(transitSystem, entrance, this, "In", startTime);
      assert currentTrip != null;
      currentTrip.addCountToEntrance();
      // Trip is consecutive
      if (prevTrip != null) {
        assert currentTrip != null;
        long TWO_HOURS = 7200000;
        if (prevTrip.exit != null
            && currentTrip.transitSystem.equals(prevTrip.transitSystem)
            && currentTrip.entrance.getName().equals(prevTrip.exit.getName())
            && currentTrip.startTime.getTime() - prevTrip.startTime.getTime() < TWO_HOURS) {
          consecutiveTrips.add(currentTrip);
        }
      } else {
        consecutiveTrips = new ArrayList<>();
        consecutiveTrips.add(currentTrip);
      }
      EventsWriter.tapInLog(this);
      transitSystem.addUsersInTransit(cardHolder);
      transitSystem.update();
      cardHolder.accountManager.saveBackupToSer();
    } else {
      System.out.println("You have insufficient funds to ride on the transit line!");
    }
  }

  /**
   * This method is used to tap out.
   *
   * @param transitSystem
   * @param exit
   * @param exitTime
   */
  void tapOut(TransitSystem transitSystem, Terminal exit, Date exitTime) {
    // RiderUser did not forget to tap-in
    if (currentTrip != null) {
      currentTrip.transitSystem.removeUsersInTransit(cardHolder);
      // RiderUser tapped out in a different TransitLine or TransitSystem
      if (!currentTrip.entrance.transitLine.equals(exit.transitLine)) {
        currentTrip.exit = exit;
        currentTrip.addCountToExit();
        currentTrip.processTrip(
            Math.max(discount, transitSystem.studentDiscount),
            new processDifferentLineOrSystemTrip(),
            this.calculateSumOfPreviousFare());
        currentTrip.exit = null;
        addTrip(currentTrip);
        prevTrip = currentTrip;
        currentTrip = TripFactory.getTrip(transitSystem, exit, this, "Out", exitTime);
        assert currentTrip != null;
        currentTrip.addCountToExit();
        currentTrip.processTrip(
            Math.max(discount, transitSystem.studentDiscount),
            new processNoInTrip(),
            this.calculateSumOfPreviousFare());
      } else {
        currentTrip.setExit(exit, exitTime);
        currentTrip.addCountToExit();
        currentTrip.processTrip(
            Math.max(discount, transitSystem.studentDiscount),
            new processCompleteTrip(),
            this.calculateSumOfPreviousFare());
      }
    }
    // RiderUser did forgot to tap-in
    else {
      currentTrip = TripFactory.getTrip(transitSystem, exit, this, "Out", exitTime);
      assert currentTrip != null;
      currentTrip.addCountToExit();
      currentTrip.processTrip(
          Math.max(discount, transitSystem.studentDiscount),
          new processNoInTrip(),
          this.calculateSumOfPreviousFare());
    }

    addTrip(currentTrip);
    prevTrip = currentTrip;
    currentTrip = null;
    cardHolder.pastThreeChanged();
    cardHolder.accountManager.saveBackupToSer();
    transitSystem.update();
  }

  /**
   * This method is used to calculate sum of previous fare.
   *
   * @return
   */
  private double calculateSumOfPreviousFare() {
    double sumOfPreviousFare = 0;
    for (Trip consecutiveTrip : consecutiveTrips) {
      sumOfPreviousFare += consecutiveTrip.fare;
    }
    return sumOfPreviousFare;
  }

  /**
   * This method is used to help pint the instance of this class.
   *
   * @return
   */
  @Override
  public String toString() {
    return ("Card [" + this.id + "]");
  }

  /**
   * This method is used to determine if another object is equals to an instance of this class.
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Card && ((Card) obj).getId().equals(this.getId());
  }
}

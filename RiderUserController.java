import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RiderUserController extends GUIController implements UserObserver {
  Clock clock;
  ObservableList<Card> riderUserCards = FXCollections.observableArrayList();
  private RiderUser riderUserModel;
  private RiderUserView riderUserView;
  private Card cardSelected;

  public RiderUserController(RiderUser riderUserModel, Clock clock) {
    this.riderUserModel = riderUserModel;
    this.riderUserModel.addObserver(this);
    this.clock = clock;
  }

  /**
   * Set the current card to the selected card
   * @param cardSelected the selected card
   */
  public void setCardSelected(Card cardSelected) {
    this.cardSelected = cardSelected;
  }

  public void setRiderUserView(RiderUserView riderUserView) {
    this.riderUserView = riderUserView;
  }

  public String getUserName() {
    return riderUserModel.getUsername();
  }

  /**
   * Display the past three recent trips
   */
  public void getCardRecentTrips() {
    Trip[] arrayOfRecentTrips = cardSelected.getRecentTrips();
    StringBuilder pastThreeTripsText = new StringBuilder();
    for (int i = 0; i < arrayOfRecentTrips.length; i++) {
      if (arrayOfRecentTrips[i] != null) {
        int j = i + 1;
        pastThreeTripsText
            .append(j)
            .append(". ")
            .append(arrayOfRecentTrips[i].toString())
            .append(System.lineSeparator());
      }
    }
    riderUserView.pastThreeTrips.setText(pastThreeTripsText.toString());
  }

  void updatePastThree() {
    getCardRecentTrips();
  }

  void updateCardBalance() {
    riderUserView.currentCardBalance.setText(
        NumberFormat.getCurrencyInstance().format(cardSelected.getBalance()));
  }

  /**
   * Ask the user to load their card
   */
  public void addBalance() {
    List<String> choices = new ArrayList<>();
    choices.add("10");
    choices.add("20");
    choices.add("50");
    ChoiceDialog<String> addBalanceDialog = new ChoiceDialog<>("10", choices);
    addBalanceDialog.setTitle("Add Balance");
    addBalanceDialog.setContentText("Please enter the balance you want to add");
    Optional<String> result = addBalanceDialog.showAndWait();
    result.ifPresent(
        s -> cardSelected.addMoney(Double.parseDouble(addBalanceDialog.getSelectedItem())));
  }

  /**
   * Update the card with all the new values
   */
  public void updateCards() {
    riderUserCards.setAll(riderUserModel.getCards());
    if (riderUserCards.size() != 0) {
      riderUserView.noCards.setVisible(false);
      riderUserView.dropDownOfCards.getSelectionModel().selectFirst();
      setCardSelected(riderUserView.dropDownOfCards.getValue());
      riderUserView.balanceText.setText(
          "The balance of Card " + riderUserView.dropDownOfCards.getValue().getId() + " is:");
      riderUserView.currentCardBalance.setText(
          NumberFormat.getCurrencyInstance()
              .format((Double) riderUserView.dropDownOfCards.getValue().getBalance()));
      riderUserView.addBalanceButton.setText(
          "Add Balance to " + riderUserView.dropDownOfCards.getValue().getId());
      getCardRecentTrips();
      riderUserView.dropDownOfCards.setVisible(true);
      riderUserView.balanceText.setVisible(true);
      riderUserView.currentCardBalance.setVisible(true);
      riderUserView.addBalanceButton.setVisible(true);
    } else {
      riderUserView.noCards.setVisible(true);
      riderUserView.dropDownOfCards.setVisible(false);
      riderUserView.balanceText.setVisible(false);
      riderUserView.currentCardBalance.setVisible(false);
      riderUserView.addBalanceButton.setVisible(false);
    }
  }

  boolean setToStudent(String email) {
    return riderUserModel.setToStudent(email);
  }

  public boolean addCard(String cardId) {
    Card newCard = new Card(cardId);
    if (riderUserModel.accountManager.unassociatedCardsInSystem.contains(newCard)) {
      riderUserModel.addCard(newCard);
      riderUserModel.accountManager.removeCard(newCard);
      return true;
    } else {
      return false;
    }
  }

  public void logOut(Stage primaryStage) {
    LoginController loginController = new LoginController(riderUserModel.accountManager, clock);
    LoginView loginView = new LoginView(loginController);
    loginView.start(primaryStage);
  }

  public boolean removeCard(String cardId) {
    Card targetCard = this.riderUserModel.findCard(cardId);
    if (targetCard != null && riderUserModel.getCards().contains(targetCard)) {
      riderUserModel.removeCard(targetCard);
      return true;
    }
    return false;
  }

  public boolean changeUserName(String userName) {
    return this.riderUserModel.changeName(userName);
  }

  public void notifyUserNameChanged() {
    riderUserView.title.setText("This is the account page of: " + getUserName());
  }

  void selectCard() {
    if (riderUserCards.size() > 0) {
      Card card = riderUserView.dropDownOfCards.getValue();
      riderUserView.addBalanceButton.setText("Add Balance to " + card.getId());
      riderUserView.currentCardBalance.setText(
          NumberFormat.getCurrencyInstance().format(card.getBalance()));
      riderUserView.balanceText.setText("The balance of Card " + card.getId() + " is:");
      cardSelected = card;
    } else {
      riderUserView.noCards.setVisible(true);
    }
  }

  public void updateUserObserver() {
    updateCards();
    notifyUserNameChanged();
    updateCardBalance();
    updatePastThree();
  }
}

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public abstract class GUIController {
  /**
   * This function generates the text dialogue
   *
   * @param title The title to be displayed
   * @param contentText The context message to be displayed
   * @param successMessage The success message to be displayed
   * @param failureMessage The failure message to be displayed
   * @param textDialogueProcessor processor to handle the text
   */
  void generateTextDialogue(
      String title,
      String contentText,
      String successMessage,
      String failureMessage,
      TextDialogueProcessor textDialogueProcessor) {
    TextInputDialog newNameDialog = new TextInputDialog();
    newNameDialog.setTitle(title);
    newNameDialog.setContentText(contentText);
    Optional<String> result = newNameDialog.showAndWait();
    result.ifPresent(
        s -> {
          textDialogueProcessor.processTextDialogue(this, result, successMessage, failureMessage);
        });
  }

  /**
   *
   * @param contentText The content to be displayed in the confirmation box
   * @return true if the user confirmed
   */
  boolean generateConfirmation(String contentText) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm");
    alert.setHeaderText("Notice");
    alert.setContentText(contentText);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * This function generates the alert message for the user
   *
   * @param titleText The title to be displayed
   * @param contentText The message to be displayed
   */
  void generateAlert(String titleText, String contentText) {
    Alert sameSystemName = new Alert(Alert.AlertType.INFORMATION);
    sameSystemName.setTitle(titleText);
    sameSystemName.setHeaderText(null);
    sameSystemName.setContentText(contentText);
    sameSystemName.showAndWait();
  }
}

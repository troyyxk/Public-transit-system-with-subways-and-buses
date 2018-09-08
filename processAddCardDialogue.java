import java.util.Optional;

/**
 * This class is used to process add card dialogue.
 */
public class processAddCardDialogue implements TextDialogueProcessor<RiderUserController> {

  /**
   * This method is used to process text dialogue.
   *
   * @param riderUserController
   * @param result
   * @param successMessage
   * @param failureMessage
   */
  @Override
  public void processTextDialogue(
      RiderUserController riderUserController,
      Optional<String> result,
      String successMessage,
      String failureMessage) {
    if (riderUserController.addCard(result.get())) {
      riderUserController.generateAlert("Notice", successMessage);
    } else {
      riderUserController.generateAlert("Error", failureMessage);
    }
  }
}

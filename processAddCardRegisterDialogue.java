import java.util.Optional;

/**
 * This class is used to process add card register dialogue.
 */
public class processAddCardRegisterDialogue implements TextDialogueProcessor<RegisterController> {

  /**
   * This method is used to process text dialogue.
   *
   * @param controller
   * @param result
   * @param successMessage
   * @param failureMessage
   */
  @Override
  public void processTextDialogue(
      RegisterController controller,
      Optional<String> result,
      String successMessage,
      String failureMessage) {
    if (controller.addCard(result.get())) {
      controller.generateAlert("Notice", successMessage);
    } else {
      controller.generateAlert("Error", failureMessage);
    }
  }
}

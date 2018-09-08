import java.util.Optional;

/**
 * This class is used to process change bus fare dialogue.
 */
public class processChangeBusFareDialogue implements TextDialogueProcessor<AdminController> {

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
      AdminController controller,
      Optional<String> result,
      String successMessage,
      String failureMessage) {
    try {
      controller.changeBusFare(Double.parseDouble(result.get()));
      controller.generateAlert("Notice", successMessage);
    } catch (NumberFormatException f) {
      controller.generateAlert("Error", failureMessage);
    }
  }
}

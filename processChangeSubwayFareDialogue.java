import java.util.Optional;

/**
 * This class is used to process change subway fare dialogue.
 */
public class processChangeSubwayFareDialogue implements TextDialogueProcessor<AdminController> {

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
      controller.changeSubwayFare(Double.parseDouble(result.get()));
      controller.generateAlert("Notice", successMessage);
    } catch (NumberFormatException f) {
      controller.generateAlert("Error", failureMessage);
    }
  }
}

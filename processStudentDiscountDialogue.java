import java.util.Optional;

/**
 * This class is used to process student discount dialogue.
 */
public class processStudentDiscountDialogue implements TextDialogueProcessor<RiderUserController> {

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
      RiderUserController controller,
      Optional<String> result,
      String successMessage,
      String failureMessage) {
    if (controller.setToStudent(result.get())) {
      controller.generateAlert("Notice", successMessage);
    } else {
      controller.generateAlert("Error", failureMessage);
    }
  }
}

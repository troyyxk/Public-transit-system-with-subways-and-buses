import java.util.Optional;

/**
 * This class is used to process set discount rate dialogue.
 */
public class processSetDiscountRateDialogue implements TextDialogueProcessor<AdminController> {

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
      double discount = Double.parseDouble(result.get());
      if (discount >= 0 && discount <= 1) {
        controller.adminUserModel.changeStudentDiscount(discount);
      } else {
        controller.generateAlert("Error", failureMessage);
      }
    } catch (NumberFormatException e) {
      controller.generateAlert("Error", "Please make sure you enter a valid number!");
    }
  }
}

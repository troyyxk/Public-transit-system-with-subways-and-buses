import java.util.Optional;

/**
 * This class is used to process fast forward dialogue.
 */
public class processFastForwardDialogue implements TextDialogueProcessor<SimulationController> {

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
      SimulationController controller,
      Optional<String> result,
      String successMessage,
      String failureMessage) {
    try {
      int fastForwardBy = Integer.parseInt(result.get());
      if (controller.fastForwardType.equalsIgnoreCase("Day")){
          controller.clock.progressDays(fastForwardBy);
      }
      else if (controller.fastForwardType.equalsIgnoreCase("Hour")){
          controller.clock.progressHours(fastForwardBy);
      }
      else if (controller.fastForwardType.equalsIgnoreCase("Minute")){
          controller.clock.progressMinutes(fastForwardBy);
      }
    } catch (NumberFormatException e) {
      controller.generateAlert("Error", failureMessage);
    }
  }
}

import java.util.Optional;

/**
 * This class is used to process terminal name dialogues.
 */
public class processTerminalNameDialogue implements TextDialogueProcessor<ConstructorController> {

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
      ConstructorController controller,
      Optional<String> result,
      String successMessage,
      String failureMessage) {
    if (controller.terminalNamesAlreadyUsed.contains(result.get())) {
      controller.generateAlert("Error", failureMessage);
    } else if (result.get().length() == 0) {
      controller.generateAlert("Error", "Terminal Names must contain at least one character!");
    } else {
      controller.currentButton.setText(result.get());
      controller.currentButton.setStyle(controller.currentStyle);
      controller.currentLineTerminalNames.add(result.get());
      controller.terminalNamesAlreadyUsed.add(result.get());
      controller.currentLineButtons.add(controller.currentButton);
    }
  }
}

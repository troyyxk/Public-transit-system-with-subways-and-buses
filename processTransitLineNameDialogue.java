import java.util.ArrayList;
import java.util.Optional;

/**
 * This class is used to process transit line name dialogue.
 */
public class processTransitLineNameDialogue
    implements TextDialogueProcessor<ConstructorController> {

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
    String[] terminalNames = new String[controller.currentLineTerminalNames.size()];
    for (int i = 0; i < controller.currentLineTerminalNames.size(); i++) {
      terminalNames[i] = controller.currentLineTerminalNames.get(i);
    }
    controller.addTransitLine(terminalNames, result.get(), controller.stationType);
    controller.constructorView.busStationButton.setVisible(true);
    controller.constructorView.subwayStationButton.setVisible(true);
    controller.constructorView.endTransitLineButton.setVisible(false);
    controller.constructorView.cancelSelection.setVisible(false);
    controller.setGridInvisible();
    controller.constructorView.submit.setVisible(true);
    controller.currentLineButtons = new ArrayList<>();
    controller.backUpList = new ArrayList<>();
    controller.currentLineTerminalNames = new ArrayList<>();
    controller.generateAlert("Notice", successMessage);
  }
}

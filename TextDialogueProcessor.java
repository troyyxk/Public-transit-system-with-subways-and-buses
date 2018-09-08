import java.util.Optional;

public interface TextDialogueProcessor<T extends GUIController> {
  void processTextDialogue(
      T controller, Optional<String> result, String successMessage, String failureMessage);
}

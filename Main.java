import javafx.application.Application;

/**
 * This is the main class for the program.
 */
public class Main {

    /**
     * This is the constructor for the main class.
     *
     * @param args
     */
    public static void main(String[] args) {
        EventsWriter.resetLog();
        Application.launch(LoginView.class, args);
    }
}

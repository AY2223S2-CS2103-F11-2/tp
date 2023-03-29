package seedu.address.ui;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.main.CommandBox;
import seedu.address.ui.main.ResultDisplay;
import seedu.address.ui.main.StatusBarFooter;
import seedu.address.ui.timetable.TimetableDetailPanel;

/**
 * Controller for a timetable page
 */
public class TimetableWindow extends UiPart<Stage> {

    private static final String FXML = "TimetableWindow.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());
    private Stage primaryStage;
    private Logic logic;
    private LocalDate focusDate;
    private int focusDayOfWeek;
    private ResultDisplay resultDisplay;

    @FXML
    private Scene scene;

    @FXML
    private VBox contentContainer;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane timetablePlaceholder;

    /**
     * Creates a {@code TimeTableWindow} with the given {@code Stage} and {@code Logic}.
     */
    public TimetableWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(true);
        contentContainer.prefHeightProperty().bind(scene.heightProperty());
        contentContainer.prefWidthProperty().bind(scene.widthProperty());

        this.logic = logic;
        focusDate = LocalDate.now();
        focusDayOfWeek = focusDate.getDayOfWeek().getValue();


        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
    }

    /**
     * Shows the timetable window.
     */
    public void show() {
        logger.fine("Showing timetable of week of " + focusDate.toString());
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the timetable window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the timetable window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the timetable window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Fills up all the placeholders of this window.
     */
    public void fillInnerParts() {

        updateTimetable();

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Updates timetable with the updated job list
     */
    void updateTimetable() {
        //Get year and month of week
        focusDate = logic.getFocusDate();

        timetablePlaceholder.getChildren().clear();
        TimetableDetailPanel timetableDetail = new TimetableDetailPanel(focusDate, logic, primaryStage);
        timetablePlaceholder.getChildren().add(timetableDetail.getRoot());
    }


    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        primaryStage.hide();
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#executeTimetableCommand(String)
     */
    private CommandResult executeCommand(String commandText)
            throws CommandException, ParseException, FileNotFoundException {
        try {
            CommandResult commandResult = logic.executeTimetableCommand(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            focusDate = logic.getFocusDate();
            updateTimetable();


            if (commandResult.isExit()) {
                handleExit();
            }


            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        } catch (FileNotFoundException e) {
            logger.info("File not found: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

}

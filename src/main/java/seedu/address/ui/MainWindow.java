package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.jobs.DeleteDeliveryJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.jobs.DeliveryJobDetailPane;
import seedu.address.ui.jobs.DeliveryJobListPanel;
import seedu.address.ui.main.CommandBox;
import seedu.address.ui.main.ResultDisplay;
import seedu.address.ui.main.StatusBarFooter;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    // private PersonListPanel personListPanel;
    private DeliveryJobListPanel deliveryJobListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private TimetableWindow timetableWindow;
    private ReminderListWindow reminderListWindow;
    private StatisticsWindow statsWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane deliveryJobDetailPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private MenuItem timetableMenuItem;
    @FXML
    private MenuItem reminderListMenuItem;
    @FXML
    private MenuItem statsItem;

    // @FXML
    // private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane deliveryJobListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
        timetableWindow = new TimetableWindow(new Stage(), logic);
        reminderListWindow = new ReminderListWindow(new Stage(), logic);
        statsWindow = new StatisticsWindow(new Stage(), logic);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        // personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        // personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        deliveryJobListPanel = new DeliveryJobListPanel(logic.getFilteredDeliveryJobList(), (idx, job) -> {
            deliveryJobDetailPlaceholder.getChildren().clear();
            if (idx >= 0) {
                DeliveryJobDetailPane detailPane = new DeliveryJobDetailPane(job, idx);
                detailPane.fillInnerParts(logic.getAddressBook());
                deliveryJobDetailPlaceholder.getChildren().add(detailPane.getRoot());
            } else {
                Label emptyLabel = new Label("No job found, create one now!");
                emptyLabel.setAlignment(Pos.CENTER);
                deliveryJobDetailPlaceholder.getChildren().add(emptyLabel);
            }
        }, job -> {
            try {
                executeCommand(DeleteDeliveryJobCommand.COMMAND_WORD + " " + job.getJobId());
            } catch (ParseException e) {
                logger.warning(e.getMessage());
            } catch (CommandException e) {
                logger.warning(e.getMessage());
            }
        });

        deliveryJobListPanelPlaceholder.getChildren().add(deliveryJobListPanel.getRoot());
        deliveryJobListPanel.selectItem(0);

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
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

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    /**
     * Opens Timetable window.
     */
    @FXML
    private void handleTimetable() {
        if (!timetableWindow.isShowing()) {
            timetableWindow.show();
            timetableWindow.fillInnerParts();
        } else {
            timetableWindow.focus();
        }
    }

    /**
     * Opens Reminder List window.
     */
    @FXML
    private void handleReminderList() {
        if (!reminderListWindow.isShowing()) {
            reminderListWindow.show();
            reminderListWindow.fillInnerParts();
        } else {
            reminderListWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        timetableWindow.hide();
        statsWindow.hide();
        primaryStage.hide();
    }

    /**
     * Opens Statistics window.
     */
    @FXML
    private void handleStats() {
        if (!statsWindow.isShowing()) {
            statsWindow.show();
            statsWindow.fillInnerParts();
        } else {
            statsWindow.focus();
        }
    }

    // public PersonListPanel getPersonListPanel() {
    //     return personListPanel;
    // }

    public DeliveryJobListPanel getDeliveryJobListPanel() {
        return deliveryJobListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isShowTimetable()) {
                handleTimetable();
            }

            if (commandResult.isShowStatistics()) {
                handleStats();
            }

            if (commandResult.isShowReminderList()) {
                handleReminderList();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}

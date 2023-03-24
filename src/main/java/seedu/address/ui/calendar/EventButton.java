package seedu.address.ui.calendar;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import seedu.address.model.calendar.CalendarEvent;

/**
 * A UI component that represents a button that contains a {@code CalendarEvent}.
 * When clicked, a {@code CalendarPopup} displaying the event's details will appear.
 */
public class EventButton extends CalendarButton {
    private static final String FXML = "EventButton.fxml";
    private static final String EVENT_BUTTON_STYLE = "-fx-font-size: 8pt; -fx-border-radius: 5; -fx-min-width: 100;";
    private static final double ORIGIN = 0.0;
    private static final int HALF_CONTENT_WIDTH = 150;
    private Stage primaryStage;
    private CalendarEvent calendarEvent;
    @FXML
    private CalendarPopup calendarPopup;
    @FXML
    private Button eventButton;

    /**
     * Constructs an {@code EventButton} with the given {@code CalendarEvent} and {@code Stage}.
     * @param calendarEvent The {@code CalendarEvent} to be displayed on the button.
     * @param primaryStage The primary {@code Stage} of the application.
     */
    public EventButton(CalendarEvent calendarEvent, Stage primaryStage) {
        super(FXML);
        this.calendarEvent = calendarEvent;
        this.primaryStage = primaryStage;
        this.calendarPopup = new CalendarPopup(calendarEvent, eventButton);
        initialiseEventButton();
    }

    /**
     * Initializes the UI components of the {@code EventButton}.
     */
    private void initialiseEventButton() {
        eventButton.setText(calendarEvent.getTimeFormat() + " " + calendarEvent.getName());
        eventButton.focusedProperty().addListener(this::handleFocusedEvent);
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            if (calendarPopup.getRoot().isShowing()) {
                calendarPopup.getRoot().hide();
            }
        };
        primaryStage.heightProperty().addListener(stageSizeListener);
        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.xProperty().addListener(stageSizeListener);
        primaryStage.yProperty().addListener(stageSizeListener);
    }

    /**
     * Handles the event where the {@code EventButton} is clicked.
     * Displays the {@code CalendarPopup} for the {@code CalendarEvent}.
     * @param event The {@code ActionEvent} that triggered the method call.
     */
    @FXML @Override
    protected void handleOnAction(ActionEvent event) {
        if (!calendarPopup.getRoot().isShowing()) {
            displayPopup();
        }
    }

    /**
     * Handles the event where the {@code EventButton} is focused.
     * Displays the {@code CalendarPopup} for the {@code CalendarEvent} and changes the border of the button.
     * @param observable The {@code Observable} object that triggered the method call.
     */
    @FXML @Override
    protected void handleFocusedEvent(Observable observable) {
        if (!calendarPopup.getRoot().isShowing() && eventButton.isFocused()) {
            eventButton.setStyle(EVENT_BUTTON_STYLE + ORANGE_BORDER);
            displayPopup();
        }
        if (!eventButton.isFocused()) {
            eventButton.setStyle(EVENT_BUTTON_STYLE + GREY_BORDER);
            calendarPopup.getRoot().hide();
        }
    }

    /**
     * Displays the {@code CalendarPopup} for the {@code CalendarEvent}.
     * Places the popup on the {@code EventButton} and aligns it to the center of the screen.
     */
    private void displayPopup() {
        Point2D p = eventButton.localToScene(ORIGIN, ORIGIN);
        calendarPopup.getRoot().show(eventButton,
                eventButton.getScene().getWindow().getWidth() / 2 - HALF_CONTENT_WIDTH
                        + eventButton.getScene().getWindow().getX(), p.getY()
                        + eventButton.getScene().getY() + eventButton.getScene().getWindow().getY());
    }
}

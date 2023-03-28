package seedu.address.model.session;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


import seedu.address.model.calendar.CalendarEvent;
import seedu.address.model.person.Name;

/**
 * Represents a Session in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Session implements Comparable<Session> {

    public static final String MESSAGE_CONSTRAINTS = "Start date time should be before end date time.";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private String command;
    private final String startDateTime;
    private final String endDateTime;
    private final SessionName name;
    private final int id;
    private Location location;
    private HashMap<String, Boolean> attendanceMap;

    /**
     * Every field must be present and not null.
     * @param startDateTime
     * @param endDateTime
     */
    public Session(String startDateTime, String endDateTime, SessionName name, Location location) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.name = name;
        this.location = location;
        this.id = name.hashCode();
        this.attendanceMap = new HashMap<>();
        if (!isValidDateTimeFormat(this.startDateTime) || !isValidDateTimeFormat(this.endDateTime)) {
            throw new IllegalArgumentException("Date Time should be in the format dd-MM-yyyy HH:mm");
        }
        if (!this.isValidSession()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    public Session(String startDateTime, String endDateTime, SessionName name, Location location, int id) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.name = name;
        this.location = location;
        this.id = id;
        this.attendanceMap = new HashMap<>();
        if (!isValidDateTimeFormat(this.startDateTime) || !isValidDateTimeFormat(this.endDateTime)) {
            throw new IllegalArgumentException("Date Time should be in the format dd-MM-yyyy HH:mm");
        }
        if (!this.isValidSession()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    public Session(String startDateTime, String endDateTime, SessionName name, Location location, int id, List<NameBooleanPair> NameBooleanPairs) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.name = name;
        this.location = location;
        this.id = id;

        attendanceMap = new HashMap<>();

        for (NameBooleanPair pair : NameBooleanPairs) {
            attendanceMap.put(pair.name, pair.isPresent);
        }

        this.attendanceMap = attendanceMap;

        if (!isValidDateTimeFormat(this.startDateTime) || !isValidDateTimeFormat(this.endDateTime)) {
            throw new IllegalArgumentException("Date Time should be in the format dd-MM-yyyy HH:mm");
        }
        if (!this.isValidSession()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * adds person to a session
     * @param name
     */
    public void addPersonToSession(String name) {
        attendanceMap.put(name, false);
    }

    /**
     * removes person from a session
     * @param name
     */
    public void removePersonFromSession(String name) {
        attendanceMap.remove(name);
    }

    /**
     * sets student as present
     * @param name
     */
    public void markStudentPresent(String name) {
        attendanceMap.put(name, true);
    }

    /**
     * sets student as absent
     * @param name
     */
    public void markStudentAbsent(String name) {
        attendanceMap.put(name, false);
    }

    /**
     * checks if session contains student
     * @param name
     */
    public boolean contains(String name) {
        return attendanceMap.containsKey(name);
    }

//    /**
//     * Represents a session that a person can have.
//     * A session consists of a start date time and an end date time.
//     * @param sessionString
//     * @throws IllegalValueException
//     */
//    public Session(String sessionString) throws IllegalValueException {
//        String[] parts = sessionString.split(" to ");
//        if (parts.length != 2) {
//            throw new IllegalValueException(Session.MESSAGE_CONSTRAINTS);
//        }
//
//        String startDateTime = parts[0];
//        String endDateTime = parts[1];
//
//        if (!Session.isValidDateTimeFormat(startDateTime) || !Session.isValidDateTimeFormat(endDateTime)) {
//            throw new IllegalValueException(Session.MESSAGE_CONSTRAINTS);
//        }
//
//        this.startDateTime = startDateTime;
//        this.endDateTime = endDateTime;
//    }

    /**
     * Returns true if the given string is a valid date format.
     * The date format is "dd-MM-yyyy".
     */
    public static boolean isValidDateFormat(String str) {
        try {
            LocalDate.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the given string is a valid time format, i.e. "HH:mm".
     */
    public static boolean isValidTimeFormat(String test) {
        if (test == null || test.isEmpty()) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(test, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getName() {
        return name.toString();
    }

    public String getLocation() {
        return location.toString();
    }

    /**
     * Returns true if a given string is a valid date time format.
     */
    public static boolean isValidDateTimeFormat(String test) {
        try {
            LocalDateTime.parse(test, DATE_TIME_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if both sessions have the same identity and attributes.
     * This defines a stronger notion of equality between two sessions.
     */
    public boolean isSameSession(Session otherSession) {
        if (otherSession == this) {
            return true;
        }

        return otherSession != null
                && otherSession.getStartDateTime().equals(getStartDateTime())
                && otherSession.getEndDateTime().equals(getEndDateTime())
                && otherSession.getName().equals(getName())
                && otherSession.getLocation().equals(getLocation());
    }

    /**
     * Returns true if both sessions have the same identity and attributes.
     * This defines a weaker notion of equality between two sessions.
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Session)) {
            return false;
        }

        Session otherSession = (Session) other;
        return otherSession.getStartDateTime().equals(getStartDateTime())
                && otherSession.getEndDateTime().equals(getEndDateTime())
                && otherSession.getName().equals(getName())
                && otherSession.getLocation().equals(getLocation());
    }

    public boolean isValidSession() {
        return LocalDateTime.parse(startDateTime, DateTimeFormatter
                        .ofPattern("dd-MM-yyyy HH:mm"))
                .isBefore(LocalDateTime.parse(endDateTime, DateTimeFormatter
                        .ofPattern("dd-MM-yyyy HH:mm")))
                && Name.isValidName(getName()) && Location.isValidLocation(getLocation());
    }

    /**
     * Returns the duration of the session.
     * @return duration of the session.
     */
    public Duration getSessionDuration() {
        return Duration.between(LocalDateTime.parse(startDateTime,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                LocalDateTime.parse(endDateTime,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(startDateTime, endDateTime, name, location);
    }

    public String getAttendees() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : attendanceMap.entrySet()) {
            sb.append(entry.getKey()).append(": ");
            if (entry.getValue()) {
                sb.append("1, ");
            } else {
                sb.append("0, ");
            }
        }

        if (attendanceMap.size() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String toStringOutput = sb.toString();
        return toStringOutput;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Session name: ")
                .append(getName())
                .append("\n Start: ")
                .append(LocalDateTime.parse(getStartDateTime(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).format(DATE_TIME_FORMATTER))
                .append("\n End: ")
                .append(LocalDateTime.parse(getEndDateTime(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).format(DATE_TIME_FORMATTER))
                .append("\n Location: ")
                .append(getLocation())
                .append("\n Attendees: ")
                .append(getAttendees());
        return builder.toString();
    }

    public String getCommand() {
        return String.format("%s: from %s to %s | at %s",
                getName(), startDateTime, endDateTime, getLocation());
    }

    /**
     * Returns a command string representation of this {@code Session} object.
     * The command string is in the format of "start date time to end date time".
     * @return a command string representation of this {@code Session} object.
     */
    public String toCommandString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(": ")
                .append(LocalDateTime.parse(getStartDateTime(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).format(DATE_TIME_FORMATTER))
                .append(" to ")
                .append(LocalDateTime.parse(getEndDateTime(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).format(DATE_TIME_FORMATTER))
                .append(" at ")
                .append(getLocation());
        return builder.toString();
    }

    public String getDate() {
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public int getDay() {
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return dateTime.getDayOfMonth();
    }

    public int getMonth() {
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return dateTime.getMonthValue();
    }

    public int getYear() {
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return dateTime.getYear();
    }

    public String getTimeFormat() {
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public int compareTo(Session other) {
        LocalDateTime thisStartDateTime = LocalDateTime.parse(startDateTime, DATE_TIME_FORMATTER);
        LocalDateTime otherStartDateTime = LocalDateTime.parse(other.startDateTime, DATE_TIME_FORMATTER);

        return thisStartDateTime.compareTo(otherStartDateTime);
    }

    public String getId() {
        return String.valueOf(id);
    }

    public ArrayList<NameBooleanPair> getMap() {
        ArrayList<NameBooleanPair> map = new ArrayList<>();
        for (Map.Entry<String, Boolean> set :
                attendanceMap.entrySet()) {
            NameBooleanPair toAdd =
                    new NameBooleanPair(
                            set.getKey(),
                            set.getValue());
            map.add(toAdd);
        }
        return map;
    }

    public HashMap<String, Boolean> getHashMap() {
        return attendanceMap;
    }

    public List<CalendarEvent> getCalendarEvents() {
        return Collections.singletonList(new CalendarEvent(this));
    }
}

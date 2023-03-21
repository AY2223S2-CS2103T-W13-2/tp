package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.awt.desktop.SystemEventListener;
import java.util.*;
import java.util.stream.Collectors;

import seedu.address.model.calendar.CalendarEvent;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final PayRate payRate;

    // Data fields
    private final Address address;
    private final Session session;
    private final Set<Tag> tags = new HashSet<>();


    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Address address, PayRate payRate, Session session, Set<Tag> tags) {
        requireAllNonNull(name, phone, payRate, address, session, tags);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.payRate = payRate;
        this.session = session;
        this.tags.addAll(tags);
    }


    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public PayRate getPayRate() {
        return payRate;
    }

    public Address getAddress() {
        return address;
    }

    public Session getSession() {
        return session;
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getPayRate().equals(getPayRate())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getSession().equals(getSession())
                && otherPerson.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, payRate, session, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Address: ")
                .append(getAddress())
                .append("; Pay Rate: ")
                .append(getPayRate())
                .append("; Session: ")
                .append(getSession());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }


    public List<CalendarEvent> getCalendarEvents() {
        return Collections.singletonList(new CalendarEvent(this));
    }
}

package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionName;
import seedu.address.model.session.UniqueSessionList;
import seedu.address.model.session.exceptions.SessionNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final UniqueSessionList sessions;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();
        sessions = new UniqueSessionList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Alternate AddressBook constructor
     */
    public AddressBook(AddressBook toBeCloned) {
        this();
        setPersons(toBeCloned.getPersonList());
        setTags(toBeCloned.getTagList());
        setSessions(toBeCloned.getSessionList());
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    public void setTags(List<Tag> tags) {
        this.tags.setTags(tags);
    }

    public void setSessions(List<Session> sessions) {
        this.sessions.setSessions(sessions);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setTags(newData.getTagList());
        setSessions(newData.getSessionList());
    }

    /**
     * Alternate resetData implementation.
     */
    public void resetData(AddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setTags(newData.getTagList());
        setSessions(newData.getSessionList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a tag with the same identity as {@code tag} exists in the address book.
     */
    public boolean hasTag(Tag tag) {
        requireNonNull(tag);
        return tags.contains(tag);
    }

    /**
     * Returns true if a session with the same identity as {@code session} exists in the address book.
     */
    public boolean hasSession(Session session) {
        requireNonNull(session);
        return sessions.contains(session);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Adds a tag to the address book.
     * The tag must not already exist in the address book.
     */
    public void addTag(Tag t) {
        tags.add(t);
    }

    /**
     * Adds a session to the address book.
     * The session must not already exist in the address book.
     */
    public void addSession(Session s) {
        sessions.add(s);
    }

    /**
     * Adds all tags to the address book.
     * The tag must not already exist in the address book.
     */
    public void addAllTags(Set<Tag> tags) {
        for (Tag tag: tags) {
            addTag(tag);
        }
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    public void setSession(Session target, Session editedSession) {
        requireNonNull(editedSession);

        sessions.setSession(target, editedSession);
    }

    public void addPersonToSession(Person person, Session session) {
        requireAllNonNull(person, session);
        Session newSession = session;
        String name = person.getName().toString();
        if (!sessions.contains(session)) {
            throw new SessionNotFoundException();
        }

        newSession.addPersonToSession(name);
        sessions.setSession(session, newSession);
    }

    public void removePersonFromSession(Person person, Session session) {
        requireAllNonNull(person, session);
        String name = person.getName().toString();
        if (!sessions.contains(session)) {
            throw new SessionNotFoundException();
        }
        if (!session.contains(name)) {
            throw new PersonNotFoundException();
        }

        Session newSession = session;
        newSession.addPersonToSession(name);
        sessions.setSession(session, newSession);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Session> getSessionList() {
        return sessions.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons)
                && tags.equals(((AddressBook) other).tags))
                && sessions.equals(((AddressBook) other).sessions);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    public void sort(int targetField) {
        persons.sort(targetField);
    }

    public Session getSessionFromName(SessionName name) throws PersonNotFoundException {
        for(Session session: sessions) {
            if (name.sessionName.equals(session.getName().toString())) {
                return session;
            }
        }
        throw new SessionNotFoundException();
    }
}

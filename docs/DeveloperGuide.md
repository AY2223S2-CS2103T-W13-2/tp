---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* Undo/redo feature implementation concept from original [_AddressBook 3 Developer Guide_](https://nus-cs2103-ay2223s2.github.io/tp/DeveloperGuide.html).
* Calendar feature implementation adapted from this [_JavaFX guide_](http://www.java2s.com/ref/java/javafx-gridpane-layout-calendar.html).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2223S2-CS2103T-W13-2/tp/tree/master/docs/diagrams) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the Coach issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.)

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `personListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes Coach commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `athlete` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the Coach command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add an athlete).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the 'destroy' marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a coach command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a Coach command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the Coach command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the AddressBook data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the Coach’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/ModelClassDiagram3.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2223S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both AddressBook data and Coach preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.AddressBook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Sort feature

#### Implementation

The proposed sort mechanism is facilitated by `UniquePersonList`.

Step 1. The Coach launches the application for the first time. The AddressBook is initialised.

Step 2. The Coach keys in some athletes and their details with the `add` method.

Step 3. After adding his athletes, the Coach wants to view his athletes in alphabetical order, so he wants to sort them by name in ascending order.

Step 4. The Coach then decides to execute the command `sort 1 1`.

#### Design considerations:

Restricting attribute and order to an integer value allows for the input to be easily anticipated and controlled.

* **Alternative 1 (current choice):** Allow coaches to specify what and how to sort their list.
    * Pros: More flexible and customisable to the needs of the coach.
    * Cons: More troublesome as coach needs to check the User Guide to learn what integers to use.

* **Alternative 2:** Coach types `sort` and it sorts based on a pre-determined attribute and order.
    * Pros: Easy for coach to use.
    * Cons: Not flexible nor customisable.

### Undo/redo feature

#### Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current AddressBook state in its history.
* `VersionedAddressBook#undo()` — Restores the previous AddressBook state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone AddressBook state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The Coach launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial AddressBook state, and the `currentStatePointer` pointing to that single AddressBook state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The Coach executes `delete 5` command to delete the 5th athlete in the AddressBook. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the AddressBook after the `delete 5` command executes to be saved in the `AddressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted AddressBook state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The Coach executes `add n/David …​` to add a new athlete. The `add` command also calls `Model#commitAddressBook()`, causing another modified AddressBook state to be saved into the `AddressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the AddressBook state will not be saved into the `AddressBookStateList`.

</div>

Step 4. The Coach now decides that adding the athlete was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous AddressBook state, and restores the AddressBook to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `VersionedAddressBook#canUndo()` to check if this is the case. If so, it will return an error to the Coach rather
than attempting to perform the undo operation.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the 'destroy' marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the AddressBook to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `AddressBookStateList.size() - 1`, pointing to the latest AddressBook state, then there are no undone AddressBook states to restore. The `redo` command uses `VersionedAddressBook#canRedo()` to check if this is the case. If so, it will return an error to the Coach rather than attempting to perform the redo.

</div>

Step 5. The Coach then decides to execute the command `list`. Commands that do not modify the AddressBook, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `AddressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The Coach executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `AddressBookStateList`, all AddressBook states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a Coach executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire AddressBook.
  * Pros: Easy to implement.
  * Cons: May eventually have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the `Person` being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

### Grouping feature

#### Introduction:
The grouping feature enables the addition and deletion of groups in the AddressBook. It provides two commands — `group` and `groupmod` that can be used to add, delete or modify the contents of groups in the `AddressBook`.

#### Architecture:
The grouping feature is supported by two classes — the `Group` class and the `UniqueGroupList` class. The `Group` class consists of two attributes a `UniquePersonList` (similar to that present in the `Addressbook`) and a `Tag`,
While  the `UniqueGroupList` is essentially a list of unique groups. The class diagram below  illustrates the relationship between the two classes.

![GroupListClassDiagram](images/GroupListclassdiagram.png)

To allow coaches to take full advantage of the grouping functionality, two commands are required.
### GroupCommand
This command is used to add or delete groups from the AddressBook.<br>`group m/MODIFICATION g/GROUPNAME` where:<br>
1. `m/`: flag to indicate the type of modification.
    1. `add`: adds an athlete to an existing group.
    2. `remove`: deletes an athlete from an existing group.
2. `g/`: flag to indicate the group name.<br>

##### Adding a group
To add a new `Group` with the `Tag` “Hall”, the coach can execute the following command:

`group m/add g/Hall`

When the coach gives the above input, a `Tag` object of name Hall is created,
this tag is tied to a `Group` class which will be added to `UniqueGroupList` in the existing `AddressBook`.
The object diagram shown depicts such an instance.

![GroupObject](images/GroupObject.png)

##### Deleting a group
To delete an existing group with the name “Hall”, the coach can execute the following command:
`group m/delete g/Hall`

This will trigger method `deleteGroup()` in `ModelManager` that will call the method `deleteGroup()` in the `AddressBook`.
During this method, the group cannot merely be deleted. It must first iterate through every athlete on the list and remove the "Hall"
`Tag` for every athlete. Only after removing all "Hall" tags in the AddressBook can we delete the specified group.

### GroupModifyCommand
After enabling the creation and deletion of groups, the next step is to allow the addition and removal of
athletes to / from groups. The `groupmod` command allows for such a function.

`groupmod INDEX m/MODIFICATION g/GROUPNAME`

1. `index`: index of the athlete.
2. `m/`: flag to indicate the type of modification.
   1. `add`: adds an athlete to an existing group.
   2. `remove`: deletes an athlete from an existing group.
3. `g/`: flag to indicate the group name.

#### Adding an athlete to a group:
1. To add an athlete named “John” (at index 1) to an existing group named “Hall”, the coach can execute:
`groupmod 1 m/add g/Hall`.

In such a scenario, two key things occur.
1. The athlete "John" will be added to the `Group` with a `Tag` "Hall".
2. The `Tag` "Hall" will be added to the list of tags in John's attribute.

##### Removing an athlete from a group
To remove an athlete named “John” from an existing group named “Hall”, the coach can execute the following command:
`groupmod 1 m/delete g/Hall`

#### Sequence Of Events:
1. When the command is called, the parser finds the athlete at index 1 and creates a` Group` of name "Hall"
2. A `GroupModifyCommand` is created.
3. Once the `execute()` method is called on the `GroupModifyCommand` object, `removePersonFromGroup(John)` is called in the `Model`.
4. This calls the similarly named method in the `AddressBook` which will check whether the "Hall" group exists.
5. If it does, "John" will be removed from the "Hall" group.
6. John's "Hall" group tag will also be removed.

### Conclusion:
The grouping feature provides a way to add, delete and modify the contents of groups in the AddressBook. It is built on top of the `UniqueGroupList` data structure and provides two commands - `group` and `groupmod` - to perform the desired operations.
Although the overall design of the `GroupList` may seem like an inefficient use of space, since we store duplicate copies of an athlete, an athlete belongs to every existing `Group`. It makes implementation simpler, allowing more operations to be implemented in the future.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target Coach profile**: Sports Coaches

* has a need to manage a significant number of athletes.
* prefer desktop apps over other types.
* can type fast.
* prefers typing rather than mouse interaction.
* is reasonably comfortable using CLI (Command Line Interface) apps.
* requires to organise their coaching schedule on a frequent basis.

**Value proposition**:
* manage contacts faster than a typical mouse/GUI-driven app.
* features are geared towards managing athletes.
* makes scheduling of training sessions more convenient than a regular calendar app.


### Coach stories

Priorities:<br>**High (must have)** - `* * *`<br>Medium (nice to have) - `* *`<br>Low (unlikely to have) - `*`

| Priority | As a …​                           | I want to …​                              | So that I can…​                                                           |
|----------|-----------------------------------|-------------------------------------------|---------------------------------------------------------------------------|
| `* * *`  | new coach                         | see usage instructions                    | refer to instructions when I forget how to use the app                    |
| `* * *`  | coach                             | add a new athlete                         | record their profile                                                      |
| `* * *`  | coach                             | delete an athlete                         | remove athletes that I no longer train                                    |
| `* * *`  | coach                             | edit an athlete's details                 | refer to accurate information regarding that athlete                      |
| `* *`    | coach                             | find an athlete by name                   | locate details of an athlete without having to go through the entire list |
| `* *`    | coach                             | hide an athlete's private contact details | minimize chance of someone else seeing them by accident                   |
| `* *`    | coach                             | organise athletes by groups               | facilitate better contact management                                      |
| `*`      | coach with many athletes          | sort athletes by name                     | locate an athlete easily                                                  |
| `*`      | coach with many training sessions | view my upcoming schedule as a calendar   | better plan my week.                                                      |

### Use cases

#### Use case: Ask for help

**MSS**

1. Coach requests for help.
2. SportSync displays a link to the SportSync User Guide.

    Use case ends.

#### Use case: Add an athlete

**MSS**

1. Coach requests to add a new athlete in the list.
2. SportSync adds the athlete.

   Use case ends.

**Extensions**

* 1a. Not enough details of the athlete were given.

    * 1a1. SportSync shows an error message.

      Use case resumes at step 1.

#### Use case: Delete an athlete

**MSS**

1. Coach requests to delete a specific athlete in the list.
2. SportSync deletes the athlete.

    Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. SportSync shows an error message.

      Use case resumes at step 1.

* 2a. The list is empty.

    * 2a1. SportSync shows an error message.

      Use case resumes at step 1.

   Use case ends.

#### Use case: Edit an athlete

**MSS**

1. Coach requests to edit a specific athlete's attribute in the list.
2. SportSync edits the specified athlete's attribute.

    Use case ends.

**Extensions**

* 1a. The given index/attribute(s) are invalid.

    * 1a1. SportSync shows an error message.

      Use case resumes at step 1.

#### Use case: Find an athlete

**MSS**

1. Coach requests to find a specific athlete by part of their name, in the list.
2. SportSync displays the search results.

    Use case ends.

**Extensions**

* 1a. The given name / part of the name does not exist within the list.

    * 1a1. SportSync shows an empty list.

      Use case resumes at step 1.

#### Use case: Clear the list

**MSS**

1.  Coach requests to clear all athletes from the list.
2.  SportSync clears all athletes from the list.

    Use case ends.

#### Use case: Sort the list by name in alphabetical order

**MSS**

1. Coach requests to sort all athletes in the list by name in alphabetical order.
2. SportSync sorts all athletes in the list by name in alphabetical order.

    Use case ends.

#### Use case: Sort the list by pay rate in increasing / decreasing order

**MSS**

1.  Coach requests to sort all athletes in the list by pay rate in increasing / decreasing order.
2.  SportSync sorts all athletes in the list by pay rate in increasing / decreasing order.

    Use case ends.

#### Use case: Create new group

**MSS**

1. Coach requests to create a new group with specified group name.
2. SportSync creates a new group with the specified group name.

    Use case ends.

**Extensions**

* 1a. The group already exists.

    * 1a1. SportSync shows an error message.

      Use case resumes at step 1.

#### Use case: Add athlete to group

**MSS**

1. Coach requests to add an athlete to a specified group.
2. SportSync adds the athlete to the specified group.

   Use case ends.

**Extensions**

* 1a. The athlete already belongs to the group, the athlete does not exist, or the group does not exist.

    * 1a1. SportSync shows an error message.

      Use case resumes at step 1.

#### Use case: Delete athlete from group

**MSS**

1. Coach requests to delete an athlete from a specified group.
2. SportSync deletes the athlete from the specified group.

   Use case ends.

**Extensions**

* 1a. The athlete does not exist, or the group does not exist.

    * 1a1. SportSync shows an error message.

      Use case resumes at step 1.

#### Use case: Display all groups

**MSS**

1. Coach requests to display all group names.
2. SportSync displays all group names.

   Use case ends.

#### Use case: Display all athletes belonging to group(s)

**MSS**

1. Coach requests to display all athletes belonging to specified group(s) by name.
2. SportSync displays all athletes by name, belonging to one or more of the specified group(s).

   Use case ends.

#### Use case: Undo a command

**MSS**

1. Coach requests to undo a mistakenly entered command.
2. SportSync undoes the mistakenly entered command.

   Use case ends.

#### Use case: Redo a command

**MSS**

1. Coach requests to redo a mistakenly undone command.
2. SportSync redoes the mistakenly undone command.

   Use case ends.

#### Use case: View calendar events

**MSS**

1. Coach requests to view the calendar events.
2. SportSync displays the calendar events on the interface.
3. Coach interacts with the calendar events as required. 

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 athletes without a noticeable sluggishness in performance for typical usage.
3.  A Coach with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X.
* **Private contact detail**: A contact detail that is not meant to be shared with others.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting an athlete

1. Deleting an athlete while all athletes are being shown

   1. Prerequisites: List all athletes using the `list` command. Multiple athletes in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No athlete is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_



package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalDevelopers.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.delete.DeleteDeveloperCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.developer.Developer;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteDeveloperCommand}.
 */
public class DeleteDeveloperCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Developer developerToDelete = model.getFilteredDeveloperList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteDeveloperCommand deleteDeveloperCommand = new DeleteDeveloperCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteDeveloperCommand.MESSAGE_DELETE_DEVELOPER_SUCCESS,
                Messages.format(developerToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteDeveloper(developerToDelete);

        assertCommandSuccess(deleteDeveloperCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredDeveloperList().size() + 1);
        DeleteDeveloperCommand deleteDeveloperCommand = new DeleteDeveloperCommand(outOfBoundIndex);

        assertCommandFailure(deleteDeveloperCommand, model, Messages.MESSAGE_INVALID_DEVELOPER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Developer developerToDelete = model.getFilteredDeveloperList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteDeveloperCommand deleteDeveloperCommand = new DeleteDeveloperCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteDeveloperCommand.MESSAGE_DELETE_DEVELOPER_SUCCESS,
                Messages.format(developerToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteDeveloper(developerToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteDeveloperCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getDeveloperList().size());

        DeleteDeveloperCommand deleteDeveloperCommand = new DeleteDeveloperCommand(outOfBoundIndex);

        assertCommandFailure(deleteDeveloperCommand, model, Messages.MESSAGE_INVALID_DEVELOPER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteDeveloperCommand deleteFirstCommand = new DeleteDeveloperCommand(INDEX_FIRST_PERSON);
        DeleteDeveloperCommand deleteSecondCommand = new DeleteDeveloperCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteDeveloperCommand deleteFirstCommandCopy = new DeleteDeveloperCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different developer -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteDeveloperCommand deleteDeveloperCommand = new DeleteDeveloperCommand(targetIndex);
        String expected = DeleteDeveloperCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteDeveloperCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredDeveloperList(p -> false);

        assertTrue(model.getFilteredDeveloperList().isEmpty());
    }
}

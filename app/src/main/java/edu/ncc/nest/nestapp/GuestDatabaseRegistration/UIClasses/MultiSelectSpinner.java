package edu.ncc.nest.nestapp.GuestDatabaseRegistration.UIClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.ncc.nest.nestapp.R;

/**
 * <b>Title: </b> MultiSelectSpinner
 * <b>File: </b> MultiSelectSpinner.java
 * <b>Description:</b>
 * The MultiSelectSpinner extends the Spinner class and implements OnMultiChoiceClickListener
 * to create a pop-up display that allows a user to make multiple selections on a spinner object.
 * When instantiated, the MultiSelectSpinner can be built from arrays or lists. The index and value
 * of selected items can be retrieved and returned as Strings, ints (where applicable), arrays,
 * or lists. Selections work as checkboxes, and can be selected and deselected at will.
 *
 * @author Created by Aneh Thakur on 5/7/2015.
 * @author edited by Matt Brevetti 12/4/2021
 * <b>Sourced from:</b> https://trinitytuts.com/tips/multiselect-spinner-item-in-android/
 */

// TODO add a method to clear all items. Can attach a link/button on the popup that allows it
// TODO add a method to select all items. Can attach link/button on the popup
// TODO add a way to handle placeholders. Either deactivate first item in List permanently or inject Text before list
// TODO currently, finalize selection by clicking off of the dialog. Is there a better way? Button?
    // back button currently works to finalize. Glad that's not a bug lol.

public class MultiSelectSpinner extends androidx.appcompat.widget.AppCompatSpinner implements
        OnMultiChoiceClickListener {
    String[] _items = null;
    boolean[] mSelection = null;

    ArrayAdapter<String> simple_adapter;

    /**
     * constructor extending the spinner class. Sets up a basic ArrayAdapter of Strings
     * with the context passed and applies a simple_spinner_item layout.
     * @param context the current context
     */
    public MultiSelectSpinner(Context context) {
        super(context);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    // TODO can we do cool things with that attrs parameter?
    /**
     * constructor extending the spinner class. Sets up a basic ArrayAdapter of Strings
     * with the context and attributes passed and applies a simple_spinner_item layout.
     * @param context the current context
     * @param attrs attributes to pass to the object
     */
    public MultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    /**
     * Handles the user clicks to activate selections. When clicked, the
     * item at that index is marked as selected
     * @param dialog the DialogInterface to check
     * @param which the index of the item selected
     * @param isChecked marks it as selected if true, else marks unselected
     */
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;

            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    /**
     * Displays the list of multiselect items to the user in an AlertDialog.
     * Called when the user clicks on the multiselect input.
     * @return <i>true</i> when method has completed (?)
     */
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_all_that_apply);
        builder.setMultiChoiceItems(_items, mSelection, this);
        builder.show();
        return true;
    }

    // should i return true here to make sure it completes?
    /**
     * Closes the dialog box
     */
    public boolean closeDialog() {
        return false;
    }


    /**
     * Applies a SpinnerAdapter to the Spinner. Throws an exception if the adapter
     * can not be set.
     * @param adapter the SpinnerAdapter to apply
     * @throws RuntimeException if the adapter is not supported
     */
    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    /**
     * Inserts items in the multi select from an array of strings.
     * This is used to build the multiselect in the .java file
     * @param items
     */
    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

    /**
     * Inserts items in the multiselect from a List of strings.
     * @param items
     */
    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

    /**
     * marks one of the selections as 'selected' or not
     * @param selection
     */
    public void setSelection(String[] selection) {
        for (String cell : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(cell)) {
                    mSelection[j] = true;
                }
            }
        }
    }

    /**
     * set multiple selections based on a list of strings
     * passed.
     * @param selection
     */
    public void setSelection(List<String> selection) {
        // clear the selection
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        // search for matches from the list of strings and mark those items as selected
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    /**
     * finds the item at the index passed and marks it
     * as selected
     * @param index
     */
    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    /**
     * sets multiple selections at once by passing an array
     * of indices. Can use the getSelectedIndices method to
     * get that array?
     * @param selectedIndices
     */
    public void setSelection(int[] selectedIndices) {
        // clears so we can set (?)
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        // loop through and mark selections based on the indicies based
        for (int index : selectedIndices) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    /**
     * gets the selections in the multi-select and returns them
     * as a List.
     * @return a list of the selected items as strings
     */
    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<String>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    /**
     * gets and returns list of the indices of the selected items
     * @return an integer List of the indices of the selected items
     */
    public List<Integer> getSelectedIndices() {
        List<Integer> selection = new LinkedList<Integer>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    /**
     * builds a string of selected items. This is called internally by other methods
     * and is not intended for public use. See getSelectedItemsAsStrings method
     * @return a string of the selected items separated by commas
     */
    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    /**
     * builds a string of selected items. Can be used to display
     * visually to the user
     * @return a string of the selected items separated by commas
     */
    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }
}
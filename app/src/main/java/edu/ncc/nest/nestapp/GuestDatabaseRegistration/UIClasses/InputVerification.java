package edu.ncc.nest.nestapp.GuestDatabaseRegistration.UIClasses;

import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class InputVerification {
    private String str;
    private int num;

    public boolean validTextEdit(String value) {
        if(value.length() > 0) {
            return true;
        }
        return false;
    }

    public boolean validTextEdit(EditText editText) {
        if (editText.getText().length() > 0) {
            return true;
        }
        return false;
    }

    public boolean validTextEdits(String[] values) {
        for (int i = 0; i < values.length; i++) {
            if(values[i].length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean validTextEditStrings(List<String> values) {
        String[] arrVals = values.toArray(new String[values.size()]);
        for (int i = 0; i < arrVals.length; i++) {
            if(arrVals[i].length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean validTextEdits(List<EditText> values) {
        EditText[] arrVals = values.toArray(new EditText[values.size()]);
        for (int i = 0; i < arrVals.length; i++) {
            if(arrVals[i].getText().length() <= 0 ) {
                return false;
            }
        }
        return true;
    }

    public boolean validLimitedTextEdit(String value, int numChars) {
        if(value.length() == numChars) {
            return true;
        }
        return false;
    }

    public boolean validLimitedTextEdit(EditText editText, int numChars) {
        if (editText.getText().length() == numChars) {
            return true;
        }
        return false;
    }

    public boolean validSingleSelect(Spinner spinner) {
        if (spinner.getSelectedItemPosition() > 0) {
            return true;
        }
        return false;
    }

    public boolean validSingleSelectText(String spinnerValue) {
        if (spinnerValue.equals("Select One")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validMultiSelect(MultiSelectSpinner multiSelectSpinner) {
        List<String> values = multiSelectSpinner.getSelectedStrings();
        if (values.size() > 0) {
            return true;
        }
        return false;
    }

}

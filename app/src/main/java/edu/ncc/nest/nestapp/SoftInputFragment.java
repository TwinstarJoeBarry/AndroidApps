package edu.ncc.nest.nestapp;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class SoftInputFragment extends Fragment {

    @Override
    public void onDestroyView() {

        // NOTE: Makes sure that the keyboard is hidden when this view is destroyed

        FragmentActivity fragmentActivity = requireActivity();

        View currentFocus = fragmentActivity.getCurrentFocus();

        if (currentFocus != null)

            ((InputMethodManager) fragmentActivity.getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(currentFocus.getApplicationWindowToken(), 0);

        super.onDestroyView();

    }

}

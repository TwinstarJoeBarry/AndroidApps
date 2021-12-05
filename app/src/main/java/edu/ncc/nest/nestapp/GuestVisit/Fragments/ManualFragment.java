package edu.ncc.nest.nestapp.GuestVisit.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

public class ManualFragment extends Fragment {


    String barcode, field1, field2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_visit_manual_entry, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) { //Recovering after a screen rotation
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            ((EditText) view.findViewById(R.id.guest_visit_barcode_entry)).setText(savedInstanceState.getString("barE"));
            ((EditText) view.findViewById(R.id.guest_visit_msie_pt1)).setText(savedInstanceState.getString("msiE1"));
            ((EditText) view.findViewById(R.id.guest_visit_msie_pt2)).setText(savedInstanceState.getString("msiE2"));

        }

        ( view.findViewById(R.id.manual_submit_btn)).setOnClickListener(view1 -> {

            // Create an instance of the database helper
            GuestRegistrySource db = new GuestRegistrySource(requireContext());

            //Saving the information in the EditText views
            barcode = ((EditText) view1.findViewById(R.id.guest_visit_barcode_entry)).getText().toString().trim();

            //Packaging the values in a neat little bundle
            Bundle entryResults = new Bundle();
            entryResults.putString("barE", barcode);

            //If barcode is entered other information is irrelevant
            if (barcode != null) {

                final String GUEST_NAME = db.isRegistered(barcode);

                if (GUEST_NAME != null)


                    // If the guest is registered, include the guest's name in the result
                    entryResults.putString("GUEST_NAME", GUEST_NAME);

            }

            //Allowing the bundle to be accessed from other fragments
            //Using Scan_confirmed to stop conflicts
            getParentFragmentManager().setFragmentResult("SCAN_CONFIRMED", entryResults);


            NavHostFragment.findNavController(ManualFragment.this)
                    .navigate(R.id.action_GV_ManualFragment_to_ConfirmationFragment);


        });
    }
}

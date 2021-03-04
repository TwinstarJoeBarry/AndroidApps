package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.ncc.nest.nestapp.R;

public class SummaryPageFragment extends Fragment  {


        protected static final String TAG = "TESTING";

        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {
            Log.d(TAG, "In SummaryPageFragment onCreateView()");

            // Inflate the layout for this fragment - This is where the navigation begins
            return inflater.inflate(R.layout.fragment_guest_database_registration_summary_page, container, false);
        }

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Navigates to the Guest Registration Entry Form for person information
            view.findViewById(R.id.entry_form_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Uncomment this code when the layouts for the registration form page is available &
                    // complete this line with the appropriate nav action -> navigate( R.id.action_secondFragment_to_summaryFragmnet)
//                    NavHostFragment.findNavController(edu.ncc.nest.nestapp.FragmentsGuestRegistration.StartPageFragment.this)
//                            .navigate(R.id.action_FragmentGuestRegistrationStartPage_to_FirstFragmentGuestRegistration);
                }
            });


        }




    }





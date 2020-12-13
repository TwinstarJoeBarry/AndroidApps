package edu.ncc.nest.nestapp.FragmentsGuestRegistration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.ncc.nest.nestapp.R;

public class GuestFormRegistrationStartPageFragment extends Fragment {

    protected static final String TAG = "TESTING";

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Log.d(TAG, "In GuestFormRegistrationStartPageFragment onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_form_registration_start_page, container, false);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.entry_form_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(GuestFormRegistrationStartPageFragment.this)
//                        .navigate();
            }
        });

        view.findViewById(R.id.barcode_scanner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(GuestFormRegistrationStartPageFragment.this)
//                        .navigate();
            }
        });
    }




}

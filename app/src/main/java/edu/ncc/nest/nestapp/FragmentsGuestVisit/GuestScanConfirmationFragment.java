package edu.ncc.nest.nestapp.FragmentsGuestVisit;

/**
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;

public class GuestScanConfirmationFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GuestScanConfirmationFragment.class.getSimpleName();

    private String guestName = null;

    private String guestId = null;

    /************ LifeCycle Methods Start ************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {

        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_guest_scan_confirmation_flipper, container, false );
    }

    public void onViewCreated( @NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        if (savedInstanceState != null) {

            // Get guest name
            guestName = savedInstanceState.getString("GUEST_NAME");
            // Get guest id
            guestId = savedInstanceState.getString("GUEST_ID");

            selectDisplayedView(view.findViewById(R.id.confirmation_view_flipper));

        } else

            getParentFragmentManager().setFragmentResultListener("CONFIRM_SCAN",
                this, (requestKey, result) -> {

                    guestName = result.getString("GUEST_NAME");

                    guestId = result.getString("BARCODE");

                    selectDisplayedView(view.findViewById(R.id.confirmation_view_flipper));

                });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Save guest name
        outState.putString("GUEST_NAME", guestName);
        // Save guest id
        outState.putString("GUEST_ID", guestId);

        super.onSaveInstanceState(outState);

    }

    /************ Implementation Methods Start ************/

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.rescan_code_button || id == R.id.confirmation_0_rescan_btn) {

            // Navigate back to the scanner to rescan the barcode
            NavHostFragment.findNavController(GuestScanConfirmationFragment.this )
                    .navigate( R.id.action_GuestScanConfirmationFragment_to_GuestScanFragment );

        } else if (id == R.id.name_confirmed) {

            // Create a bundle
            Bundle guestInfo = new Bundle();

            // Put the Guest's info into the bundle
            guestInfo.putString("GUEST_NAME", guestName);
            guestInfo.putString("GUEST_ID", guestId);

            // Set the FragmentManager
            getParentFragmentManager().setFragmentResult("GUEST_CONFIRMED", guestInfo);

            // Navigate to the questionnaire
            NavHostFragment.findNavController(GuestScanConfirmationFragment.this)
                    .navigate(R.id.action_GuestScanConfirmationFragment_to_GuestQuestionnaireFragment);

        } else if (id == R.id.confirmation_0_register_btn) {

            // Navigate to the questionnaire
            NavHostFragment.findNavController(GuestScanConfirmationFragment.this)
                    .navigate(R.id.action_GuestScanConfirmationFragment_to_RegistrationFragment);

            // TODO Navigate to the registration fragment (Does not exist yet)

            //Toast.makeText(getContext(), "The registration fragment doesn't exist yet.", Toast.LENGTH_SHORT).show();

        }

    }

    /************ Custom Methods Start ************/

    /**
     * selectDisplayedView --
     * selects the view depending on if the guest is registered or not
     */
    private void selectDisplayedView(@NonNull ViewFlipper viewFlipper) {

        if (guestName == null) {

            // Get the registration layout from the ViewFlipper and initialize it
            onCreateRegistrationView(viewFlipper.getChildAt(0));

            viewFlipper.setDisplayedChild(0);

        } else {

            // Get the confirmation layout from the ViewFlipper and initialize it
            onCreateConfirmationView(viewFlipper.getChildAt(1));

            viewFlipper.setDisplayedChild(1);

        }

    }

    /**
     * onCreateConfirmationView --
     * sets the onClickListener for the buttons in the fragment_guest_scan_confirmation layout
     * Fills in the text views that display the guest's name and id
     */
    public void onCreateConfirmationView(@NonNull View view) {

        // Set the onclick listener for the buttons
        view.findViewById( R.id.rescan_code_button ).setOnClickListener(this);
        view.findViewById( R.id.name_confirmed ).setOnClickListener(this);

        // Display the guest's name and id
        ((TextView) view.findViewById(R.id.guest_name_lable)).setText(guestName);
        ((TextView) view.findViewById(R.id.guest_id_lable)).setText(guestId);

    }

    /**
     * onCreateRegistrationView --
     * sets the onClickListener for the buttons in the fragment_guest_scan_confirmation_reg layout
     * Fills in the text view that displays the barcode
     */
    public void onCreateRegistrationView(@NonNull View view) {

        // Set the onclick listener for the buttons
        view.findViewById( R.id.confirmation_0_rescan_btn ).setOnClickListener(this);
        view.findViewById( R.id.confirmation_0_register_btn ).setOnClickListener(this);

        // Display the barcode
        ((TextView) view.findViewById(R.id.confirmation_0_guest_id)).setText(guestId);

    }
}

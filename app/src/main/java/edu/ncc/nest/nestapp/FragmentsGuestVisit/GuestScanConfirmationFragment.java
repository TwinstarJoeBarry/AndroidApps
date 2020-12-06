package edu.ncc.nest.nestapp.FragmentsGuestVisit;

/**
 *
 * Copyright (C) 2019 The LibreFoodPantry Developers.
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.GuestFormHelper;
import edu.ncc.nest.nestapp.R;

public class GuestScanConfirmationFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GuestScanFragment.class.getSimpleName();

    private String guestName;

    private String guestId;

    /************ LifeCycle Methods Start ************/

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {

        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_guest_scan_confirmation_flipper, container, false );
    }

    public void onViewCreated( @NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        ViewFlipper viewFlipper = (ViewFlipper) view.findViewById(R.id.confirmation_view_flipper);

        if (savedInstanceState != null) {
            // Get guest name
            guestName = savedInstanceState.getString("NAME");
            // Get guest id
            guestId = savedInstanceState.getString("ID");

            if ( guestName != null) {

                onCreateConfirmationView(view, guestName, guestId);

                viewFlipper.setDisplayedChild(1);
            } else {

                onCreateRegistrationView(view, guestId);

                viewFlipper.setDisplayedChild(0);
            }
        } else

            getParentFragmentManager().setFragmentResultListener("CONFIRM_SCAN",
                this, (requestKey, result) -> {

                    guestName = result.getString("GUEST_NAME");

                    guestId = result.getString("BARCODE");

                    if ( guestName != null) {

                        onCreateConfirmationView(view, guestName, guestId);

                        viewFlipper.setDisplayedChild(1);
                    } else {

                        onCreateRegistrationView(view, guestId);

                        viewFlipper.setDisplayedChild(0);
                    }

                });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Save guest name
        outState.putString("NAME", guestName);
        // Save guest id
        outState.putString("ID", guestId);

        super.onSaveInstanceState(outState);
    }

    /************ Implementation Methods Start ************/

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if ( id == R.id.rescan_code_button )
            // Navigate back to the scanner to rescan the barcode
            NavHostFragment.findNavController(GuestScanConfirmationFragment.this )
                    .navigate( R.id.action_GuestScanConfirmationFragment_to_GuestScanFragment );

        else if ( id == R.id.name_confirmed ) {

            // Create a bundle
            Bundle guestInfo = new Bundle();

            // Put the Guest's info into the bundle
            guestInfo.putString("GUEST_NAME", guestName);
            guestInfo.putString("BARCODE", guestId);

            // Set the FragmentManager
            getParentFragmentManager().setFragmentResult("CONFIRM_SCAN", guestInfo);

            // Navigate to the questionnaire
            NavHostFragment.findNavController(GuestScanConfirmationFragment.this)
                    .navigate(R.id.action_GuestScanConfirmationFragment_to_GuestQuestionnaireFragment);
        }
    }

    /************ Custom Methods Start ************/

    public void onCreateConfirmationView(View view, String name, String id) {

        // Set the onclick listener for the buttons
        view.findViewById( R.id.rescan_code_button ).setOnClickListener(this);
        view.findViewById( R.id.name_confirmed ).setOnClickListener(this);

        // Display the guest's name and id
        ((TextView) view.findViewById(R.id.guest_name_lable)).setText(name);
        ((TextView) view.findViewById(R.id.guest_id_lable)).setText(id);
    }

    public void onCreateRegistrationView(View view, String id) {

        // Set the onclick listener for the buttons
        view.findViewById( R.id.confirmation_0_rescan_btn ).setOnClickListener(this);
        view.findViewById( R.id.confirmation_0_register_btn ).setOnClickListener(this);

        // Display the barcode
        ((TextView) view.findViewById(R.id.confirmation_0_guest_id)).setText(id);
    }
}

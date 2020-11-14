package edu.ncc.nest.nestapp.FragmentsUpc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ncc.nest.nestapp.R;

public class EnterUpcFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_upc, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_enter_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                NavHostFragment.findNavController(EnterUpcFragment.this)
                        .navigate(R.id.action_StartFragment_to_ScanFragment);
                 */
            }
        });
    }
}
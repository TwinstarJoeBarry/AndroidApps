package edu.ncc.nest.nestapp.GuestVisit.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.R;

public class SelectionFragment extends Fragment implements View.OnClickListener{

    Button manual, barcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {

        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_guest_visit_splitter, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        barcode = view.findViewById(R.id.barcode_selection_btn);
        barcode.setOnClickListener(this);
        manual = view.findViewById(R.id.manual_selection_btn);
        manual.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case (R.id.barcode_selection_btn):
                    NavHostFragment.findNavController(SelectionFragment.this)
                            .navigate(R.id.action_GV_SelectionFragment_to_ScannerFragment);
                    break;
            case (R.id.manual_selection_btn):
                NavHostFragment.findNavController(SelectionFragment.this)
                        .navigate(R.id.action_GV_SelectionFragment_to_ManualFragment);
                break;
        }

    }




}

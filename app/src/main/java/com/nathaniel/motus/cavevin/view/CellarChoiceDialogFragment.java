package com.nathaniel.motus.cavevin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.controller.MainActivity;
import com.nathaniel.motus.cavevin.model.Cellar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CellarChoiceDialogFragment extends DialogFragment {

    private Spinner mCellarChoiceSpinner;
    private onCellarChoiceFragmentClickListener mCallBack;
    private String mDialogTitle;

    public CellarChoiceDialogFragment() {
        // Required empty public constructor
    }

    public interface onCellarChoiceFragmentClickListener {
        public void onCellarChoiceFragmentClick(int position);
    }

    public static CellarChoiceDialogFragment newInstance(String dialogTitle){

        CellarChoiceDialogFragment dialogFragment=new CellarChoiceDialogFragment();
        dialogFragment.mDialogTitle=dialogTitle;

        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_cellar_choice_dialog,null);
        mCellarChoiceSpinner =view.findViewById(R.id.fragment_cellar_choice_dialog_spinner);

        initializeCellarChoiceSpinner();

        adb.setView(view);
        adb.setTitle(mDialogTitle);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position= mCellarChoiceSpinner.getSelectedItemPosition();
                mCallBack.onCellarChoiceFragmentClick(position);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog!=null) dialog.dismiss();
            }
        });
        return adb.create();
    }

    private void initializeCellarChoiceSpinner(){

        ArrayAdapter<String> cellarNameListAdapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item);

        int i=0;
        while (i< Cellar.getCellarPool().size()){
            cellarNameListAdapter.add(Cellar.getCellarPool().get(i).getCellarName());
            i++;
        }

        cellarNameListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCellarChoiceSpinner.setAdapter(cellarNameListAdapter);
        mCellarChoiceSpinner.setSelection(MainActivity.getCurrentCellarIndex());

        createCallBackToParentActivity();
    }

    private void createCallBackToParentActivity(){
        mCallBack=(onCellarChoiceFragmentClickListener)getActivity();
    }
}

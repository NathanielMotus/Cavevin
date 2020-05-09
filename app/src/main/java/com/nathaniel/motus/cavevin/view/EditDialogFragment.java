package com.nathaniel.motus.cavevin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.controller.CellarInputUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDialogFragment extends DialogFragment {
    //abstract class that shows a dialog with an EditText

    private EditText mInputTextEdit;
    private TextView mInviteText;
    private onEditDialogClickListener mCallBack;
    private String mDialogTitle;
    private String mInvite;
    private String mPreFilledInput;

    public interface onEditDialogClickListener{
//        Interface needed for callback

        public void onEditDialogClick(View v, String inputText);
    }

    public EditDialogFragment() {
        // Required empty public constructor
    }

    public static EditDialogFragment newInstance(String dialogTitle, String invite, String preFilledInput){
//        Create a new instance

        EditDialogFragment edf=new EditDialogFragment();
        edf.mDialogTitle=dialogTitle;
        edf.mInvite=invite;
        edf.mPreFilledInput=preFilledInput;

        return edf;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater=getActivity().getLayoutInflater();

        final View view=inflater.inflate(R.layout.fragment_edit_dialog,null);
        mInputTextEdit=view.findViewById(R.id.fragment_edit_dialog_edit);
        mInviteText=view.findViewById(R.id.fragment_edit_dialog_text);

        mInputTextEdit.setText(mPreFilledInput);
        mInviteText.setText(mInvite);

        adb.setView(view);
        adb.setTitle(mDialogTitle);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createCallBackToParentActivity();
                String inputText= CellarInputUtils.replaceForbiddenCharacters(view.getContext(),mInputTextEdit.getText().toString());
                mCallBack.onEditDialogClick(view,inputText);
            }
        });
        adb.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });

        return adb.create();
    }

    private void createCallBackToParentActivity(){

        mCallBack=(onEditDialogClickListener)getActivity();
    }

}

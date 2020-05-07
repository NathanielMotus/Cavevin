package com.nathaniel.motus.cavevin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nathaniel.motus.cavevin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomBarFragment extends Fragment implements View.OnClickListener {

    //Buttons declaration
    ImageView mAllWinesButton,mRedButton,mWhiteButton,mPinkButton;

    //Shared preferences tags
    private static final String CURRENT_CELLAR_INDEX="Current cellar index";
    private static final String CURRENT_TYPE_FILTER="Current type filter";
    private static final String CURRENT_SORT_OPTION ="Current sort options";

    //Callback
    onBottomBarClickedListener mCallBack;


    public BottomBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result=inflater.inflate(R.layout.fragment_bottom_bar, container, false);

//        Inflate buttons
        mAllWinesButton=result.findViewById(R.id.bottombar_all_wines_button);
        mRedButton=result.findViewById(R.id.bottombar_red_button);
        mWhiteButton=result.findViewById(R.id.bottombar_white_button);
        mPinkButton=result.findViewById(R.id.bottombar_pink_button);

//        Activate the last chosen
        String currentTypeFilter= getActivity().getPreferences(Context.MODE_PRIVATE).getString(CURRENT_TYPE_FILTER,"all");
        switch (currentTypeFilter){
            case "red":
                mRedButton.setBackgroundColor(result.getContext().getResources().getColor(R.color.colorAccent));
                break;
            case "white":
                mWhiteButton.setBackgroundColor(result.getContext().getResources().getColor(R.color.colorAccent));
                break;
            case "pink":
                mPinkButton.setBackgroundColor(result.getContext().getResources().getColor(R.color.colorAccent));
                break;
            default:
                mAllWinesButton.setBackgroundColor(result.getContext().getResources().getColor(R.color.colorAccent));
        }

//        Put on clicklistener
        mAllWinesButton.setOnClickListener(this);
        mRedButton.setOnClickListener(this);
        mWhiteButton.setOnClickListener(this);
        mPinkButton.setOnClickListener(this);

//        create callback
        createCallBackToParentActivity();

        return result;
    }

    @Override
    public void onClick(View v) {
        String buttonTag=v.getTag().toString();
        //change the colors
        mAllWinesButton.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorPrimary));
        mRedButton.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorPrimary));
        mWhiteButton.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorPrimary));
        mPinkButton.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorPrimary));
        v.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorAccent));

        mCallBack.onBottomBarItemClicked(v,buttonTag);
    }

//    **********************************************************************************************
//    Callback subs
//    **********************************************************************************************

    public interface onBottomBarClickedListener{
        public void onBottomBarItemClicked(View v, String buttonTag);
    }

    private void createCallBackToParentActivity(){
        try {
            mCallBack=(onBottomBarClickedListener)getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(e.toString()+" must implement onItemClickedListener");
        }
    }

}

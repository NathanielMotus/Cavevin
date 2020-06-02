package com.nathaniel.motus.cavevin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.controller.MainActivity;
import com.nathaniel.motus.cavevin.model.Cellar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CellarFragment extends Fragment {

//    **********************************************************************************************
//    Valeurs de debug
//    **********************************************************************************************
    private static String TAG="CellarFragment";

    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    //Remember recyclerview state
    private static final String KEY_RECYCLER_STATE="key_recycler_view";
    private static Bundle mSavedRecyclerState;


    public CellarFragment() {
        // Required empty public constructor
    }

//    **********************************************************************************************
//    Fragment events
//    **********************************************************************************************

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result=inflater.inflate(R.layout.fragment_cellar, container, false);

        //Instantiate recyclerview
        mRecyclerView= result.findViewById(R.id.fragment_cellar_recycler_view);

        return result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //update the recyclerview
        //only possible in onActivityCreated as it uses getActivity
        if (Cellar.getCellarPool().size()!=0)
            updateCellarRecyclerView(Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).typeFiltered(MainActivity.getCurrentTypeFilter()));
    }

    @Override
    public void onResume() {
        super.onResume();

        //update the recyclerview when coming back from cellar edit
        if(Cellar.getCellarPool().size()!=0)
            updateCellarRecyclerView(Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).typeFiltered(MainActivity.getCurrentTypeFilter()));

        //Get back recyclerview state
        if (mSavedRecyclerState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerState.getParcelable(KEY_RECYCLER_STATE));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //Save recyclerview state
        mSavedRecyclerState=new Bundle();
        mSavedRecyclerState.putParcelable(KEY_RECYCLER_STATE,mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

//    **********************************************************************************************
//    Working subs
//    **********************************************************************************************

    public void updateCellarRecyclerView(Cellar cellar){
        //update cellar RecyclerView

        mAdapter = new MyRecyclerViewAdapter(cellar, this.getActivity().getApplicationContext(), this.getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }


}

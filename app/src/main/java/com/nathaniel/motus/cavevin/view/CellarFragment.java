package com.nathaniel.motus.cavevin.view;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.controller.MainActivity;
import com.nathaniel.motus.cavevin.model.Cellar;
import com.nathaniel.motus.cavevin.model.CellarStorageUtils;
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter;

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


    public CellarFragment() {
        // Required empty public constructor
    }


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
    }

    public void updateCellarRecyclerView(Cellar cellar){
        //update cellar RecyclerView

        mAdapter = new MyRecyclerViewAdapter(cellar, this.getActivity().getApplicationContext(), this.getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }


}

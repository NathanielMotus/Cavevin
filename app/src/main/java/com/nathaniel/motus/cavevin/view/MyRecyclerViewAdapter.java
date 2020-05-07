package com.nathaniel.motus.cavevin.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.controller.MainActivity;
import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.Cellar;

import static com.nathaniel.motus.cavevin.R.*;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private Cellar mCellar;
    private Context mContext;
    private Activity mActivity;
    private onItemClickedListener mCallback;

    public MyRecyclerViewAdapter(Cellar cellar, Context context, Activity activity) {
        mCellar = cellar;
        mContext=context;
        mActivity=activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(layout.recyclerview_cellar_row,parent,false);
        MyViewHolder mViewHolder=new MyViewHolder(itemView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

//                Get the current cellar position before callback (position is filtered cellar position)
        final int actualPosition=Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).getCellList().indexOf(mCellar.getCellList().get(position));

        final Cell cell=mCellar.getCellList().get(position);
        Bottle bottle=cell.getBottle();
        String color=bottle.getType();
        switch (color){
            case "0":
                holder.type.setBackgroundColor(mContext.getResources().getColor(R.color.redWine));
                break;
            case "1":
                holder.type.setBackgroundColor(mContext.getResources().getColor(R.color.whiteWine));
                break;
            case "2":
                holder.type.setBackgroundColor(mContext.getResources().getColor(R.color.pinkWine));
                break;
            default:
                holder.type.setBackgroundColor(mContext.getResources().getColor(R.color.redWine));
        }
        holder.appellation.setText(bottle.getAppellation());
        holder.domain.setText(bottle.getDomain());
        holder.cuvee.setText(bottle.getCuvee());
        holder.vintage.setText(bottle.getVintage());
        holder.bottleName.setText(bottle.getBottleName());
        holder.capacity.setText(Float.toString(bottle.getCapacity())+" L");
        holder.quantity.setText(Integer.toString(cell.getStock()));
        holder.origin.setText(cell.getOrigin());
        holder.bottleComment.setText(bottle.getBottleComment());
        holder.cellarComment.setText(cell.getCellComment());

        createCallBackToParentActivity();
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClicked(v,actualPosition);
            }
        });

        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cell cell=Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).getCellList().get(actualPosition);
                cell.setStock(cell.getStock()+1);
                holder.quantity.setText(Integer.toString(cell.getStock()));
            }
        });

        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cell cell=Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).getCellList().get(actualPosition);
                if (cell.getStock()>0) cell.setStock(cell.getStock()-1);
                holder.quantity.setText(Integer.toString(cell.getStock()));
            }
        });

        holder.buttonCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.originTitle.getVisibility()==View.VISIBLE) {
                    holder.originTitle.setVisibility(View.GONE);
                    holder.origin.setVisibility(View.GONE);
                    holder.bottleCommentTitle.setVisibility(View.GONE);
                    holder.bottleComment.setVisibility(View.GONE);
                    holder.cellarCommentTitle.setVisibility(View.GONE);
                    holder.cellarComment.setVisibility(View.GONE);
                    holder.buttonEdit.setVisibility(View.GONE);
                    holder.buttonCollapse.setImageResource(drawable.baseline_keyboard_arrow_down_white_18dp);
                }else{
                    holder.originTitle.setVisibility(View.VISIBLE);
                    holder.origin.setVisibility(View.VISIBLE);
                    holder.bottleCommentTitle.setVisibility(View.VISIBLE);
                    holder.bottleComment.setVisibility(View.VISIBLE);
                    holder.cellarCommentTitle.setVisibility(View.VISIBLE);
                    holder.cellarComment.setVisibility(View.VISIBLE);
                    holder.buttonEdit.setVisibility(View.VISIBLE);
                    holder.buttonCollapse.setImageResource(drawable.baseline_keyboard_arrow_up_white_18dp);

                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mCellar.getCellList().size();
    }

//    **********************************************************************************************
//    Callback subs
//    **********************************************************************************************

    public interface onItemClickedListener{
        public void onItemClicked(View view,int position);
    }

    private void createCallBackToParentActivity(){
        try{
            mCallback=(onItemClickedListener)mActivity;
        }catch (ClassCastException e){
            throw new ClassCastException(e.toString()+" must implement onItemClickedListener");
        }
    }
}
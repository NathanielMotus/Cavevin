package com.nathaniel.motus.cavevin.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nathaniel.motus.cavevin.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView appellation,domain,cuvee,type,vintage,bottleName,capacity,quantity;
    public TextView originTitle,origin,bottleCommentTitle,bottleComment,cellarCommentTitle,cellarComment;
    public Button buttonPlus,buttonMinus;
    public ImageView buttonCollapse,buttonEdit;

    public MyViewHolder(View view) {
        super(view);
        appellation=view.findViewById(R.id.recycler_cellar_row_appellation_text);
        domain=view.findViewById(R.id.recycler_cellar_row_domain_text);
        cuvee= view.findViewById(R.id.recycler_cellar_row_cuvee_text);
        type=view.findViewById(R.id.recycler_cellar_row_type_text);
        vintage= view.findViewById(R.id.recycler_cellar_row_vintage_text);
        bottleName=view.findViewById(R.id.recycler_cellar_row_bottle_name_text);
        capacity=view.findViewById(R.id.recycler_cellar_row_capacity_text);
        quantity=view.findViewById(R.id.recycler_cellar_row_quantity_text);
        buttonPlus=view.findViewById(R.id.recycler_cellar_row_plus_button);
        buttonMinus=view.findViewById(R.id.recycler_cellar_row_minus_button);
        origin=view.findViewById(R.id.recycler_cellar_row_origin_text);
        originTitle=view.findViewById(R.id.recycler_cellar_row_origin_title_text);
        bottleCommentTitle=view.findViewById(R.id.recycler_cellar_row_bottle_comment_title_text);
        bottleComment=view.findViewById(R.id.recycler_cellar_row_bottle_comment_text);
        cellarCommentTitle=view.findViewById(R.id.recycler_cellar_row_cellar_comment_title_text);
        cellarComment=view.findViewById(R.id.recycler_cellar_row_cellar_comment_text);
        buttonCollapse=view.findViewById(R.id.recycler_cellar_row_collapse_image);
        buttonEdit=view.findViewById(R.id.recycler_cellar_row_edit_image);
    }
}

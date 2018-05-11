package com.example.rasmus.p2app.frontend.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.frontend.ui.activities.RecipeClickedActivity;
import com.example.rasmus.p2app.frontend.models.SingleItemModel;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        SingleItemModel singleItem = itemsList.get(i);
        Recipe recipe = InRAM.recipesInRAM.get(singleItem.getRecipeID());

        if(recipe != null){
            holder.tvTitle.setText(recipe.getTitle());
            holder.setImage(recipe.getPictureLink());
            holder.setRecipeID(recipe.getID());
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle;
        protected ImageView itemImage;
        private int recipeID = 0;
        private View view;


        public SingleItemRowHolder(View view) {
            super(view);
            this.view = view;
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, RecipeClickedActivity.class);
                    intent.putExtra("delete", false);
                    intent.putExtra("id", recipeID);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });

        }

        public void setRecipeID(int recipeID) {
            this.recipeID = recipeID;
        }

        public void setImage(String URL){
            new DownloadImageTask(itemImage).execute(URL);
        }

    }

}
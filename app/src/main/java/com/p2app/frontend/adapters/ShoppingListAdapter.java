package com.p2app.frontend.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2app.R;
import com.p2app.frontend.models.ShoppingListItemModel;

import java.util.List;

public class ShoppingListAdapter extends BaseAdapter {
    Activity activity;
    List<ShoppingListItemModel> items;
    LayoutInflater inflater;


    public ShoppingListAdapter(Activity activity) {
        this.activity = activity;
    }

    public ShoppingListAdapter(Activity activity, List<ShoppingListItemModel> items) {
        this.activity = activity;
        this.items = items;

        inflater = activity.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null) {

            view = inflater.inflate(R.layout.list_view_item_shopping_list, viewGroup, false);

            holder = new ViewHolder();

            holder.tvItemName = (TextView) view.findViewById(R.id.tv_item_name);
            holder.ivCheckbox = (ImageView) view.findViewById(R.id.iv_check_box);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        ShoppingListItemModel model = items.get(i);

        holder.tvItemName.setText(model.getItemName());

        if (model.isSelected())
            holder.ivCheckbox.setBackgroundResource(R.drawable.ic_checked);

        else
            holder.ivCheckbox.setBackgroundResource(R.drawable.ic_unchecked);

        return view;
    }

    public void updateRecords(List<ShoppingListItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tvItemName;
        ImageView ivCheckbox;
    }
}

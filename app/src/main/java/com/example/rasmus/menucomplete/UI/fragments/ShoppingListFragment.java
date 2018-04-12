/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.example.rasmus.menucomplete.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rasmus.menucomplete.R;
import com.example.rasmus.menucomplete.adapters.ShoppingListAdapter;
import com.example.rasmus.menucomplete.models.ShoppingListItemModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);



        ListView listView = (ListView) view.findViewById(R.id.listview_shopping_list);

        final List<ShoppingListItemModel> items = new ArrayList<>();
        items.add(new ShoppingListItemModel(false, "milk"));
        items.add(new ShoppingListItemModel(false, "sausage"));
        items.add(new ShoppingListItemModel(false, "chips"));

        final ShoppingListAdapter adapter = new ShoppingListAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                ShoppingListItemModel model = items.get(i);

                if(model.isSelected())
                    model.setSelected(false);

                else
                    model.setSelected(true);

                items.set(i, model);

                adapter.updateRecords(items);
            }
        });


        return view;
    }
}

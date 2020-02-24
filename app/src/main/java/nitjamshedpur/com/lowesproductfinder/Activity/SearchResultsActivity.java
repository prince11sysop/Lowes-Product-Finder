package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


//        holder.addToList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ItemModal itemModal=data.get(position);
//
//                SharedPreferences sharedPreferences = AppConstants.mSearchProductActivity
//                        .getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//
//                ArrayList<ListItem> myList = new ArrayList<>();
//
//
//                //fetch older item list
//                Gson gson = new Gson();
//                String response = sharedPreferences.getString("ItemList", "");
//
//                if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
//                }.getType()) != null)
//                    myList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
//                    }.getType());
//                else
//                    myList = new ArrayList<>();
//
//
//                //change item list
//                myList.add(new ListItem(
//                        itemModal.getCategory(),
//                        itemModal.getSubCategory(),
//                        itemModal.getPrice(),
//                        itemModal.getFloor(),
//                        itemModal.getShelf(),
//                        itemModal.getDescription(),
//                        itemModal.getName(),
//                        1, false
//                ));
//                Collections.reverse(myList);
//
//                Gson gson2 = new Gson();
//                String json = gson2.toJson(myList);
//
//                editor = sharedPreferences.edit();
//                editor.remove("ItemList").commit();
//                editor.putString("ItemList", json);
//                editor.commit();
//
//                AppConstants.mSearchProductActivity.finish();
//
//                if (AppConstants.isCreateShoppingListActivityOpen) {
//                    //AppConstants.mCreateShoppingListActivity.adapter.notifyDataSetChanged();
//
//                    AppConstants.mCreateShoppingListActivity.recyclerView
//                            .setAdapter(new MyShoppingListAdapter(
//                                    context, myList
//                            ));
//                }
//
//                else{
//                    context.startActivity(new Intent(context, CreateShoppingListActivity.class));
//                }
//            }
//        });
//
//        holder.infoItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppConstants.openAddItemDialog(context, data.get(position), 1);
//            }
//        });


    }
}

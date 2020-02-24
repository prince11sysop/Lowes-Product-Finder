package nitjamshedpur.com.lowesproductfinder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

public class SearchProductItemAdapter extends RecyclerView.Adapter<SearchProductItemAdapter.Viewholder> {

    private Context context;
    private ArrayList<ItemModal> data;

    public SearchProductItemAdapter(Context context, ArrayList<ItemModal> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_product_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        holder.name.setText(data.get(position).getName());
        holder.subCat.setText(data.get(position).getSubCategory());
        holder.price.setText("Rs." + data.get(position).getPrice());
        holder.floor.setText("Floor-" + data.get(position).getFloor()+
                ", Shelf-"+data.get(position).getShelf());

        holder.addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemModal itemModal=data.get(position);

                SharedPreferences sharedPreferences = AppConstants.mSearchProductActivity
                        .getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                ArrayList<ListItem> myList = new ArrayList<>();


                //fetch older item list
                Gson gson = new Gson();
                String response = sharedPreferences.getString("ItemList", "");

                if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
                }.getType()) != null)
                    myList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
                    }.getType());
                else
                    myList = new ArrayList<>();


                //change item list
                myList.add(new ListItem(
                        itemModal.getCategory(),
                        itemModal.getSubCategory(),
                        itemModal.getPrice(),
                        itemModal.getFloor(),
                        itemModal.getShelf(),
                        itemModal.getDescription(),
                        itemModal.getName(),
                        1, false
                ));
                Collections.reverse(myList);

                Gson gson2 = new Gson();
                String json = gson2.toJson(myList);

                editor = sharedPreferences.edit();
                editor.remove("ItemList").commit();
                editor.putString("ItemList", json);
                editor.commit();

                AppConstants.mSearchProductActivity.finish();

                if (AppConstants.isCreateShoppingListActivityOpen) {
                    //AppConstants.mCreateShoppingListActivity.adapter.notifyDataSetChanged();

                    AppConstants.mCreateShoppingListActivity.recyclerView
                            .setAdapter(new MyShoppingListAdapter(
                                    context, myList
                            ));
                }

                else{
                    context.startActivity(new Intent(context, CreateShoppingListActivity.class));
                }
            }
        });

        holder.infoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.openAddItemDialog(context, data.get(position), 1);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.openAddItemDialog(context, data.get(position), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView name, subCat, price, floor;
        ImageView addToList, infoItem;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_item_name);
            subCat = itemView.findViewById(R.id.search_item_subcat);
            price = itemView.findViewById(R.id.search_item_price);
            floor = itemView.findViewById(R.id.search_product_floor);
            addToList=itemView.findViewById(R.id.add_to_list_sp);
            infoItem=itemView.findViewById(R.id.info_sp);
        }
    }
}

package nitjamshedpur.com.lowesproductfinder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.SearchResultsActivity;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

public class SearchProductItemAdapter extends RecyclerView.Adapter<SearchProductItemAdapter.Viewholder> {

    private Context context;
    private ArrayList<ItemModal> data;
    private String keyWord;
    public HashMap<String, String> subCatMap = new HashMap<>(), catMap = new HashMap<>(), nameMap = new HashMap<>();

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

        String type = "", key = "";

        ItemModal currentItem = data.get(position);
        keyWord = AppConstants.searchKeyWord;
        String keyWords[] = keyWord.split(" ");
        Log.e("onBindViewHolder: ", keyWord + " level " + currentItem.getName() + " " + position);
        for (int i = 0; i < keyWords.length; i++) {
            if (currentItem.getName().toLowerCase().contains(keyWords[i].toLowerCase())) {
                if (nameMap.containsKey(currentItem.getName())) {
                    holder.itemView.setVisibility(View.GONE);
                    Log.e("onBindViewHolder: ", keyWords[i] + " level 2 " + currentItem.getName() + " " + position);

                    return;
                } else {
                    nameMap.put(currentItem.getName(), "true");
                    holder.name.setText(currentItem.getName());
                    holder.inText.setText("in " + currentItem.getSubCategory());
                    type = "name";
                    key = currentItem.getName();
                }
                break;
            } else if (currentItem.getDescription().toLowerCase().contains(keyWords[i].toLowerCase())) {
                if (nameMap.containsKey(currentItem.getName())) {
                    holder.itemView.setVisibility(View.GONE);
                    Log.e("onBindViewHolder: ", keyWords[i] + " level 5 " + currentItem.getName() + " " + position);

                    return;
                } else {
                    nameMap.put(currentItem.getName(), "true");
                    holder.name.setText(currentItem.getName());
                    holder.inText.setText("in " + currentItem.getSubCategory());
                    type = "name";
                    key = currentItem.getName();
                }
                break;
            }

            if (currentItem.getSubCategory().toLowerCase().contains(keyWords[i].toLowerCase())) {
                if (subCatMap.containsKey(currentItem.getSubCategory())) {
                    holder.itemView.setVisibility(View.GONE);
                    Log.e("onBindViewHolder: ", keyWords[i] + " level 3 " + currentItem.getName() + " " + position);

                    return;
                } else {
                    subCatMap.put(currentItem.getSubCategory(), "true");
                    holder.name.setText(currentItem.getSubCategory());
                    holder.inText.setText("in " + currentItem.getCategory());
                    type = "subcat";
                    key = currentItem.getSubCategory();
                }
                break;
            }

            if (currentItem.getCategory().toLowerCase().contains(keyWords[i].toLowerCase())) {
                if (catMap.containsKey(currentItem.getCategory())) {
                    holder.itemView.setVisibility(View.GONE);
                    Log.e("onBindViewHolder: ", keyWords[i] + " level 4 " + currentItem.getName() + " " + position);

                    return;
                } else {
                    catMap.put(currentItem.getCategory(), "true");
                    holder.name.setText(currentItem.getCategory());
                    holder.inText.setVisibility(View.GONE);
                    type = "cat";
                    key = currentItem.getCategory();
                }
                break;
            }
        }


        final String finalType = type;
        final String finalKey = key;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchResultsActivity.class);
                intent.putExtra("type", finalType);
                intent.putExtra("key", finalKey);
                context.startActivity(intent);
                AppConstants.mSearchProductActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView name, inText;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_item_name_sp);
            inText = itemView.findViewById(R.id.search_item_in_sp);
        }
    }
}

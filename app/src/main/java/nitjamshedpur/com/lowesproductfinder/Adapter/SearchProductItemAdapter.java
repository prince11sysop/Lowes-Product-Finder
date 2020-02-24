package nitjamshedpur.com.lowesproductfinder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
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
        holder.cat.setText(data.get(position).getCategory());
        holder.price.setText("Rs." + data.get(position).getPrice());
        holder.floor.setText("Floor-" + data.get(position).getFloor());

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
        TextView name, subCat, cat, price, floor;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_item_name);
            subCat = itemView.findViewById(R.id.search_item_subcat);
            cat = itemView.findViewById(R.id.search_item_category);
            price = itemView.findViewById(R.id.search_item_price);
            floor = itemView.findViewById(R.id.search_product_floor);
        }
    }
}

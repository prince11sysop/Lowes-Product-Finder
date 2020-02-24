package nitjamshedpur.com.lowesproductfinder.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;

import static nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity.adapter;
import static nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity.itemList;
import static nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity.recyclerView;

public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListAdapter.MyShoppingListViewHolder> {

    private Context myContext;
    private List<ListItem> myItemList;

    String key = "Key";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    Gson gson;
    String response;



    public MyShoppingListAdapter(Context myContext, ArrayList<ListItem> itemList){
        this.myContext = myContext;
        this.myItemList = itemList;

        //Adding list in local storage
        shref = myContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        gson = new Gson();
        response=shref.getString(key , "");
    }

    @NonNull
    @Override
    public MyShoppingListAdapter.MyShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        View view = layoutInflater.inflate(R.layout.card_format, null);

        return new MyShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyShoppingListViewHolder myShoppingListViewHolder, final int position) {

        final ListItem listItem = myItemList.get(position);

        myShoppingListViewHolder.mItemName.setText(listItem.getName());
        myShoppingListViewHolder.mItemCount.setText(listItem.getItemCount()+"");
        myShoppingListViewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemList.remove(position);
                Gson gson = new Gson();
                String json = gson.toJson(itemList);

                editor = shref.edit();
                editor.remove(key).commit();
                editor.putString(key, json);
                editor.commit();
                //adapter.notifyDataSetChanged();
                MyShoppingListAdapter adapter = new MyShoppingListAdapter(myContext, itemList);
                recyclerView.setAdapter(adapter);
            }
        });

        if(listItem.isStatus()){
            myShoppingListViewHolder.checkBox.setChecked(true);
        }

        myShoppingListViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ListItem listItem1=itemList.get(position);
                    itemList.remove(position);
                    listItem1.setStatus(true);
                    itemList.add(listItem1);

                }else{
                    ListItem listItem1=itemList.get(position);
                    itemList.remove(position);

                    listItem1.setStatus(false);
                    itemList.add(0,listItem1);
                }

                Gson gson = new Gson();
                String json = gson.toJson(itemList);

                editor = shref.edit();
                editor.remove(key).commit();
                editor.putString(key, json);
                editor.commit();
                MyShoppingListAdapter adapter = new MyShoppingListAdapter(myContext, itemList);
                recyclerView.setAdapter(adapter);

            }
        });
    }


    @Override
    public int getItemCount() {
        return myItemList.size();
    }


    //    public static class MyReportsViewHolder extends RecyclerView.ViewHolder {
    public class MyShoppingListViewHolder extends RecyclerView.ViewHolder {

        TextView mItemName, mItemCount;
        ImageView deleteItem;
        CheckBox checkBox;


        public MyShoppingListViewHolder(@NonNull final View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.itemName);
            mItemCount= itemView.findViewById(R.id.count_text_cf);
            deleteItem=itemView.findViewById(R.id.deleteBtn);
            checkBox=itemView.findViewById(R.id.checkBox);

            itemView.setTag(getAdapterPosition());

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

//                    myContext.startActivity(new Intent(myContext,PotholeDetails.class));
//                    startActivity(new Intent(this,PotholeDetails.class));
                }
            });
        }

    }

}


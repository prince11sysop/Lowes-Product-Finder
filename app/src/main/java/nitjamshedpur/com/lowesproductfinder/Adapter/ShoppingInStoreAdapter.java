package nitjamshedpur.com.lowesproductfinder.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;

public class ShoppingInStoreAdapter extends RecyclerView.Adapter<ShoppingInStoreAdapter.MyShoppingListViewHolder> {

    private Context myContext;
    private List<ListItem> myItemList;

    String key = "Key";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    Gson gson;
    String response;
    public static  RelativeLayout viewBackground,viewBackground1;



    public ShoppingInStoreAdapter(Context myContext, ArrayList<ListItem> itemList){
        this.myContext = myContext;
        this.myItemList = itemList;

        //Adding list in local storage
        shref = myContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        gson = new Gson();
        response=shref.getString(key , "");
    }

    @NonNull
    @Override
    public ShoppingInStoreAdapter.MyShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            LayoutInflater layoutInflater = LayoutInflater.from(myContext);
            View view = layoutInflater.inflate(R.layout.shopping_card_format, null);
//            view.findViewById(R.id.view_background).setVisibility(View.GONE);

            return new MyShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyShoppingListViewHolder myShoppingListViewHolder, final int position) {

        final ListItem listItem = myItemList.get(position);

        myShoppingListViewHolder.mItemName.setText(listItem.getName());
        myShoppingListViewHolder.mItemCount.setText(listItem.getItemCount()+"");
        myShoppingListViewHolder.mItemLocation.setText(listItem.getFloor()+"-"+listItem.getShelf());


        if(listItem.isStatus()){
            myShoppingListViewHolder.checkBox.setChecked(true);
        }

        myShoppingListViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ListItem listItem1=myItemList.get(position);
                    myItemList.remove(position);
//                    listItem1.setStatus(true);
//                    myItemList.add(listItem1);

                }else{
                    ListItem listItem1=myItemList.get(position);
                    myItemList.remove(position);

//                    listItem1.setStatus(false);
//                    myItemList.add(0,listItem1);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return myItemList.size();
    }


    public void removeItem(int position) {
        myItemList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ListItem item, int position) {
        myItemList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    public class MyShoppingListViewHolder extends RecyclerView.ViewHolder {

        TextView mItemName,mItemLocation,mItemCount;
        CheckBox checkBox;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;


        public MyShoppingListViewHolder(@NonNull final View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.itemName);
            mItemCount= itemView.findViewById(R.id.itemCount);
            mItemLocation=itemView.findViewById(R.id.itemLocation);
            checkBox=itemView.findViewById(R.id.checkBox);
            viewBackground=itemView.findViewById(R.id.view_background);
            viewForeground=itemView.findViewById(R.id.view_foreground);
//            viewBackground1=itemView.findViewById(R.id.view_background_1);

        }

    }

}


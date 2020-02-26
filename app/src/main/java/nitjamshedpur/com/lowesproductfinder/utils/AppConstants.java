package nitjamshedpur.com.lowesproductfinder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.SearchProductActivity;
import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;

public class AppConstants {
    public static ArrayList<ItemModal> mItemList = new ArrayList<>();
    public static SearchProductActivity mSearchProductActivity;
    public static CreateShoppingListActivity mCreateShoppingListActivity;
    public static boolean isCreateShoppingListActivityOpen = false;
    public static String searchKeyWord = "";
    public static String listFromScan = "";

    public static void openAddItemDialog(final Context context, final ItemModal itemModal, final int type) {

        Activity callingActivity;
        //type 1 is for search activity

        //type 2 is for my shopping list activity

        Rect displayRectangle = new Rect();
        ViewGroup viewGroup;

        if (type == 1) {
            Window window = AppConstants.mSearchProductActivity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            viewGroup = AppConstants.mSearchProductActivity.findViewById(android.R.id.content);
        } else {
            Window window = AppConstants.mCreateShoppingListActivity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            viewGroup = AppConstants.mCreateShoppingListActivity.findViewById(android.R.id.content);
        }

        View dialogView = LayoutInflater.from(context).inflate(R.layout.item_details_dialog, viewGroup, false);

        TextView upper_text = dialogView.findViewById(R.id.upper_text);

        TextView itemName, itemCat, itemSubCat, itemPrice, itemFloor, itemDesc;
        itemCat = dialogView.findViewById(R.id.details_item_category);
        itemSubCat = dialogView.findViewById(R.id.details_item_subcat);
        itemPrice = dialogView.findViewById(R.id.details_item_price);
        itemFloor = dialogView.findViewById(R.id.details_item_floor);
        itemDesc = dialogView.findViewById(R.id.details_item_desc);
        itemName = dialogView.findViewById(R.id.details_item_name);

        itemName.setText(itemModal.getName());
        itemCat.setText(itemModal.getCategory());
        itemSubCat.setText(itemModal.getSubCategory());
        itemPrice.setText("Rs." + itemModal.getPrice());
        itemFloor.setText("Floor-" + itemModal.getFloor());
        itemDesc.setText(itemModal.getDescription());

        TextView addButton = dialogView.findViewById(R.id.add_item_button);
        if (type == 2) {
            addButton.setText("Got It");
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialogOtp;
        alertDialogOtp = builder.create();
        alertDialogOtp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogOtp.setCancelable(true);
        alertDialogOtp.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do-things

                //if type ==2
                if (type == 2) {
                    alertDialogOtp.dismiss();
                    return;
                }

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

                alertDialogOtp.dismiss();

                mSearchProductActivity.finish();

                if (AppConstants.isCreateShoppingListActivityOpen) {
                    //AppConstants.mCreateShoppingListActivity.adapter.notifyDataSetChanged();

                    AppConstants.mCreateShoppingListActivity.recyclerView
                            .setAdapter(new MyShoppingListAdapter(
                                    context, myList
                            ));
                } else {
                    context.startActivity(new Intent(context, CreateShoppingListActivity.class));
                }

            }
        });
    }
}

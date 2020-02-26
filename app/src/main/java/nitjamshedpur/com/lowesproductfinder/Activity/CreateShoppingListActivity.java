package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateShoppingListActivity extends Activity {

    FloatingActionButton fab;
    public static RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    public static ArrayList<ListItem> itemList = new ArrayList<>();

    String key = "ItemList";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    public static MyShoppingListAdapter adapter;
    private boolean firstTimeFlag = true;

    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
//        if (!firstTimeFlag) {
//            adapter.notifyDataSetChanged();
//        }
        AppConstants.mCreateShoppingListActivity = CreateShoppingListActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConstants.isCreateShoppingListActivityOpen = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);

        init();

        //Adding list in local storage
        fetchItemList();

        receiveClicks();
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        layoutManager = new GridLayoutManager(CreateShoppingListActivity.this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MyShoppingListAdapter(CreateShoppingListActivity.this, itemList);
        recyclerView.setAdapter(adapter);
        firstTimeFlag = false;
        AppConstants.mCreateShoppingListActivity = CreateShoppingListActivity.this;
        AppConstants.isCreateShoppingListActivityOpen = true;
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Fetching shopping list...");
        progressDialog.setCancelable(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void fetchItemList() {
        shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        if (!AppConstants.listFromScan.equals("")) {
            progressDialog.show();

            String response = AppConstants.listFromScan;
            String oneLine[] = response.split("\n");
            for (String pd : oneLine) {
                String keywords[] = pd.split(" ");
                for (String oneWord : keywords) {
                    boolean flag = false;
                    for (ItemModal im : AppConstants.mItemList) {
                        if (im.getName().toLowerCase().contains(oneWord.toLowerCase())) {
                            itemList.add(new ListItem(im.getCategory(),
                                    im.getSubCategory(), im.getPrice(), im.getFloor(),
                                    im.getShelf(), im.getDescription(),
                                    im.getName(), 1, false));
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }
            }

            // add scanned list to shared preferences
            String json = gson.toJson(itemList);

            editor = shref.edit();
            editor.remove("ItemList").commit();
            editor.putString("ItemList", json);
            editor.commit();

            AppConstants.listFromScan = "";
            progressDialog.dismiss();
            return;
        }


        String response = shref.getString(key, "");

        if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
        }.getType()) != null)
            itemList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
            }.getType());
    }

    private void receiveClicks() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreateShoppingListActivity.this, SearchProductActivity.class));
            }
        });
    }
}

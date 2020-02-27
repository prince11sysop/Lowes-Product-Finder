package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private ImageView backButton, clearListButton, overFlowMenuButton;

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
//        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        itemList = AppConstants.sortItemList(itemList);
        adapter = new MyShoppingListAdapter(CreateShoppingListActivity.this, itemList);
        recyclerView.setAdapter(adapter);
        firstTimeFlag = false;
        AppConstants.mCreateShoppingListActivity = CreateShoppingListActivity.this;
        AppConstants.isCreateShoppingListActivityOpen = true;
    }

    private void init() {
        overFlowMenuButton=findViewById(R.id.overflow_menu_crl);
        backButton = findViewById(R.id.back_button_crl);
        clearListButton = findViewById(R.id.clear_my_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching shopping list...");
        progressDialog.setCancelable(false);

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

        overFlowMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Start Shopping", "Remind Me"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateShoppingListActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Start Shopping")) {
                            startActivity(new Intent(CreateShoppingListActivity.this, StoreMapActivity.class));
                        }
                        else {
                            Toast.makeText(CreateShoppingListActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppConstants.mItemList.size() == 0) {
                    if (AppConstants.isNetworkAvailable(CreateShoppingListActivity.this)) {
                        AppConstants.fetchGoodsItemList(CreateShoppingListActivity.this);
                    } else {
                        Toast.makeText(CreateShoppingListActivity.this, "Please make sure you have a secure internet connection.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                startActivity(new Intent(CreateShoppingListActivity.this, SearchProductActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String response = shref.getString(key, "");

                if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
                }.getType()) != null)
                    itemList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
                    }.getType());

                if (itemList.size() == 0) {
                    Toast.makeText(CreateShoppingListActivity.this, "Item List is Empty...", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog alertDialog = new AlertDialog.Builder(CreateShoppingListActivity.this).create();
                alertDialog.setTitle("Clear Shopping List");
                alertDialog.setMessage("Are you sure you want to clear your list?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CLEAR",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                                editor = shref.edit();
                                editor.remove("ItemList").commit();
                                itemList.clear();
                                adapter = new MyShoppingListAdapter(CreateShoppingListActivity.this, itemList);
                                recyclerView.setAdapter(adapter);

                            }
                        });
                alertDialog.show();
            }
        });
    }
}

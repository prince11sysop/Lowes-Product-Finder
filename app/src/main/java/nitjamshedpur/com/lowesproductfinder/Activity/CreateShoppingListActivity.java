package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Model.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;

import android.app.Activity;
import android.content.Context;
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
import java.util.List;

public class CreateShoppingListActivity extends Activity {

    FloatingActionButton fab;
    public static RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    public static ArrayList<ListItem> itemList;

    EditText itemText;
    Button addItemBtn;

    String key = "Key";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab=(FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        //Adding list in local storage
        shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String response=shref.getString(key , "");

        if(gson.fromJson(response,new TypeToken<List<ListItem>>(){}.getType())!=null)
            itemList = gson.fromJson(response,new TypeToken<List<ListItem>>(){}.getType());
        else
            itemList = new ArrayList<>();


        layoutManager = new GridLayoutManager(CreateShoppingListActivity.this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        MyShoppingListAdapter adapter = new MyShoppingListAdapter(CreateShoppingListActivity.this, itemList);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.activity_add_item, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(CreateShoppingListActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(view1);
                dialog.show();


                addItemBtn = (Button) view1.findViewById(R.id.addItemBtn);
                itemText = (EditText) view1.findViewById(R.id.addItemText);

                addItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    itemList.add(new ListItem(itemText.getText().toString(),3,false,"Shelf-2"));

                        Gson gson = new Gson();
                        String json = gson.toJson(itemList);

                        editor = shref.edit();
                        editor.remove(key).commit();
                        editor.putString(key, json);
                        editor.commit();

                    MyShoppingListAdapter adapter = new MyShoppingListAdapter(CreateShoppingListActivity.this, itemList);
                    recyclerView.setAdapter(adapter);
                    dialog.dismiss();
                    }
                });


            }
        });



    }
}

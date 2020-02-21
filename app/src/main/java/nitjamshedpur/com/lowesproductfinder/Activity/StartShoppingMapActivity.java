package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity.recyclerView;

public class StartShoppingMapActivity extends AppCompatActivity {

    Button showShoppingList;
    String key = "Key";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    SharedPreferences.Editor editor;

    public static RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    public static ArrayList<ListItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shopping_map);

        showShoppingList = (Button) findViewById(R.id.showShoppingList);

        showShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.activity_show_shopping_list, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(StartShoppingMapActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(view1);
                dialog.show();

                recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view);
                shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String response = shref.getString(key, "");

                if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
                }.getType()) != null)
                    itemList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
                    }.getType());
                else
                    itemList = new ArrayList<>();


                layoutManager = new GridLayoutManager(StartShoppingMapActivity.this, 1);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                MyShoppingListAdapter adapter = new MyShoppingListAdapter(StartShoppingMapActivity.this, itemList);
                recyclerView.setAdapter(adapter);


            }
        });
    }
}

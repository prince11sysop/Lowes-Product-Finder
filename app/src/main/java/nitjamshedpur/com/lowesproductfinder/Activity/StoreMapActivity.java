package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nitjamshedpur.com.lowesproductfinder.Adapter.ShoppingInStoreAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.RecyclerItemTouchHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class StoreMapActivity extends FragmentActivity implements OnMapReadyCallback,
                                                RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private GoogleMap mMap;
    Button showShoppingList;

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    ArrayList<ListItem> itemList;
    ShoppingInStoreAdapter adapter;
    ArrayList<ListItem> pendingItem;
    ArrayList<ListItem> completedItem;
    Button pending,completed;

    String key = "ItemList";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);
        showShoppingList=(Button)findViewById(R.id.showShoppingList);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Adding list in local storage
        shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String response = shref.getString(key, "");

        //getting data from local storage
        if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
        }.getType()) != null)
            itemList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
            }.getType());
        else
            itemList = new ArrayList<>();

        showShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemList.size() != 0) {
                    View view1 = getLayoutInflater().inflate(R.layout.activity_show_shopping_list, null);
                    final BottomSheetDialog dialog = new BottomSheetDialog(StoreMapActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(view1);
                    dialog.show();

                    recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view);
                    pending = (Button) view1.findViewById(R.id.pending);
                    completed = (Button) view1.findViewById(R.id.completed);

                    pendingItem = new ArrayList<>();
                    for (int i = 0; i < itemList.size(); i++)
                        if (itemList.get(i).isStatus() == false)
                            pendingItem.add(itemList.get(i));

                    layoutManager = new GridLayoutManager(StoreMapActivity.this, 1);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation()));

                    pending.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pending.setAlpha((float) 1.0);
                            completed.setAlpha((float) 0.6);

                            pendingItem = new ArrayList<>();
                            for (int i = 0; i < itemList.size(); i++)
                                if (itemList.get(i).isStatus() == false)
                                    pendingItem.add(itemList.get(i));

                            adapter = new ShoppingInStoreAdapter(StoreMapActivity.this, pendingItem);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                    completed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            completed.setAlpha((float) 1.0);
                            pending.setAlpha((float) 0.6);

                            completedItem = new ArrayList<>();
                            for (int i = 0; i < itemList.size(); i++)
                                if (itemList.get(i).isStatus() == true)
                                    completedItem.add(itemList.get(i));

                            adapter = new ShoppingInStoreAdapter(StoreMapActivity.this, completedItem);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                    adapter = new ShoppingInStoreAdapter(StoreMapActivity.this, pendingItem);
                    recyclerView.setAdapter(adapter);
                    swipeFun();
                } else {
                    Toast.makeText(StoreMapActivity.this, "Shopping List Empty!", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    public void swipeFun(){
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
                RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 19.6f; //This goes up to 21

        LatLng sydney = new LatLng(28.567892, 77.323089);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Jamshedpur"));

        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.LEFT) {
            if (viewHolder instanceof ShoppingInStoreAdapter.MyShoppingListViewHolder) {


                // get the removed item name to display it in snack bar
                String name = itemList.get(viewHolder.getAdapterPosition()).getName();

                // backup of removed item for undo purpose
                final ListItem deletedItem = itemList.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();

                // remove the item from recycler view
                adapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(this, name + " removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    adapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
                Toast.makeText(this, name + "removed!", Toast.LENGTH_SHORT).show();
            }
        } else if (direction == ItemTouchHelper.RIGHT) {
//            Gson gson = new Gson();
//            SharedPreferences.Editor editor;
//            SharedPreferences shref=getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

            itemList.get(viewHolder.getAdapterPosition()).setStatus(true);
//            String json = gson.toJson(itemList);
//
//            editor = shref.edit();
//            editor.remove(key).commit();
//            editor.putString(key, json);
//            editor.commit();
            adapter.removeItem(viewHolder.getAdapterPosition());
        }
    }
}

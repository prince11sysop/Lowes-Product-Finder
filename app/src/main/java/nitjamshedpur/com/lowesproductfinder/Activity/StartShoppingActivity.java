package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Adapter.ShoppingInStoreAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;
import nitjamshedpur.com.lowesproductfinder.utils.RecyclerItemTouchHelper;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class StartShoppingActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    ArrayList<ListItem> itemList;
    ShoppingInStoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shopping);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        itemList = new ArrayList<>();
        itemList.add(new ListItem("Abc", "abc", "abc", "abc", "abc", "abc", "abc", 3, true));
        itemList.add(new ListItem("Abc", "abc", "abc", "abc", "abc", "abc", "abc1", 3, true));
        itemList.add(new ListItem("Abc", "abc", "abc", "abc", "abc", "abc", "abc2", 3, true));

        layoutManager = new GridLayoutManager(StartShoppingActivity.this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation()));

        //adapter = new ShoppingInStoreAdapter(StoreMapActivity.this,StartShoppingActivity.this, itemList);
        recyclerView.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
                RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


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
            Toast.makeText(this, "Right to left swipe", Toast.LENGTH_SHORT).show();
        }
    }
}

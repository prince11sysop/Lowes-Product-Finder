package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Adapter.SearchProductItemAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchProductActivity extends Activity {

    private ImageView searchBack, searchMic;
    private EditText enterSearchText;
    private RecyclerView search_items_list;
    private SearchProductItemAdapter mAdapter;
    private ArrayList<ItemModal> currentItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        init();
        receiveClicks();
    }

    private void receiveClicks() {
        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchProductActivity.this, "Ruk bhai thoda...", Toast.LENGTH_SHORT).show();
            }
        });

        enterSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItemList();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterItemList() {

        Log.e("filterItemList: ","filter clicked");
        currentItemList.clear();

        ArrayList<ItemModal> fullList = new ArrayList<>();
        fullList = AppConstants.mItemList;
        String searchText = enterSearchText.getText().toString();
        if (searchText.equals("")){
            mAdapter.notifyDataSetChanged();
            return;
        }

        for (int i = 0; i < fullList.size(); i++) {
            if (fullList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())||
                    fullList.get(i).getCategory().toLowerCase().contains(searchText.toLowerCase())||
                    fullList.get(i).getSubCategory().toLowerCase().contains(searchText.toLowerCase())||
                    fullList.get(i).getDescription().toLowerCase().contains(searchText.toLowerCase())
            ){
                currentItemList.add(fullList.get(i));
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void init() {
        currentItemList = new ArrayList<>();

        searchBack = findViewById(R.id.search_product_back);
        searchMic = findViewById(R.id.search_item_mic);
        enterSearchText = findViewById(R.id.edit_search_text);
        search_items_list = findViewById(R.id.search_items_list);

        mAdapter = new SearchProductItemAdapter(this, currentItemList);
        search_items_list.setLayoutManager(new LinearLayoutManager(this));
        search_items_list.setAdapter(mAdapter);
    }
}

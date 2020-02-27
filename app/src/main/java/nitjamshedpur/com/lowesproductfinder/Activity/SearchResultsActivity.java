package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nitjamshedpur.com.lowesproductfinder.Adapter.MyShoppingListAdapter;
import nitjamshedpur.com.lowesproductfinder.Adapter.SearchResultAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.Modal.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

public class SearchResultsActivity extends Activity {

    private ImageView clearButton;
    private RelativeLayout openSearchActivity;
    private RecyclerView searchResultsList;
    private TextView searchText;
    private SearchResultAdapter mAdapter;
    private ArrayList<ItemModal> currentList;
    private String type, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //remember to implement appconstant keyword clear on click for each item here
        init();
        receiveClicks();
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConstants.mSearchResultsActivity = SearchResultsActivity.this;
    }

    private void setUpRecyclerView() {
        filterDataList();
        mAdapter = new SearchResultAdapter(this, currentList, SearchResultsActivity.this);
        searchResultsList.setLayoutManager(new LinearLayoutManager(this));
        searchResultsList.setAdapter(mAdapter);
    }

    private void filterDataList() {

        if (type.equals("name")) {
            Log.e("filterDataList: ", "name");
            for (ItemModal im : AppConstants.mItemList) {
                if (im.getName().equalsIgnoreCase(key)) {
                    currentList.add(im);
                }
            }
        } else if (type.equals("subcat")) {
            Log.e("filterDataList: ", "subCat");
            for (ItemModal im : AppConstants.mItemList) {
                if (im.getSubCategory().equalsIgnoreCase(key)) {
                    currentList.add(im);
                }
            }
        } else if (type.equals("cat")) {
            Log.e("filterDataList: ", "cat");
            for (ItemModal im : AppConstants.mItemList) {
                if (im.getCategory().equalsIgnoreCase(key)) {
                    currentList.add(im);
                }
            }
        }

    }

    private void receiveClicks() {
        openSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultsActivity.this, SearchProductActivity.class);
                startActivity(intent);
                finish();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultsActivity.this, SearchProductActivity.class);
                AppConstants.searchKeyWord = "";
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        currentList = new ArrayList<>();
        clearButton = findViewById(R.id.clear_button_sr);
        openSearchActivity = findViewById(R.id.open_search_sr);
        searchText = findViewById(R.id.search_keyword_sr);
        searchText.setText(AppConstants.searchKeyWord);
        searchResultsList = findViewById(R.id.search_results_list);

        type = getIntent().getStringExtra("type");
        key = getIntent().getStringExtra("key");

    }
}

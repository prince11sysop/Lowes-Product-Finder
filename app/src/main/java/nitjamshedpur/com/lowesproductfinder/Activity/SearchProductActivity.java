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
        ArrayList<ItemModal> fullList= new ArrayList<>();
        //fullList=AppConstants.mItemList;
    }

    private void init() {
        currentItemList = new ArrayList<>();

        searchBack = findViewById(R.id.search_product_back);
        searchMic = findViewById(R.id.search_item_mic);
        enterSearchText = findViewById(R.id.edit_search_text);
        search_items_list = findViewById(R.id.search_items_list);

        mAdapter = new SearchProductItemAdapter(this,currentItemList);
        search_items_list.setLayoutManager(new LinearLayoutManager(this));
        search_items_list.setAdapter(mAdapter);
    }
}

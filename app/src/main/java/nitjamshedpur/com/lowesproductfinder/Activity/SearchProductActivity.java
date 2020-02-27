package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nitjamshedpur.com.lowesproductfinder.Adapter.SearchProductItemAdapter;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SearchProductActivity extends Activity {

    private final int REQ_CODE_SPEECH_INPUT = 2;
    private ImageView searchBack, searchMic;
    private EditText enterSearchText;
    private RecyclerView search_items_list;
    private SearchProductItemAdapter mAdapter;
    private ArrayList<ItemModal> currentItemList;
    private boolean mIsKeyBoardOpen = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsKeyBoardOpen)
            closeKeyboard();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsKeyBoardOpen)
            closeKeyboard();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppConstants.searchKeyWord = "";
        Log.e("onBackPressed: ", "heyy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        Log.e("onCreate: ", AppConstants.searchKeyWord);
        init();
        receiveClicks();
        enterSearchText.setText(AppConstants.searchKeyWord);
    }

    private void receiveClicks() {
        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.searchKeyWord = "";
                finish();
            }
        });

        searchMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SearchProductActivity.this, "Ruk bhai thoda...", Toast.LENGTH_SHORT).show();
                promptSpeechInput();
            }
        });

        enterSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppConstants.searchKeyWord = enterSearchText.getText().toString().trim();
                filterItemList();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        enterSearchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    showKeyboard();
                else
                    closeKeyboard();
            }
        });

        enterSearchText.requestFocus();
    }

    private void filterItemList() {

        Log.e("filterItemList: ", "filter clicked");
        currentItemList.clear();

        ArrayList<ItemModal> fullList = new ArrayList<>();
        fullList = AppConstants.mItemList;
        String searchText = enterSearchText.getText().toString().trim();
        if (searchText.equals("")) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        String temp[] = searchText.split(" ");

        for (int i = 0; i < fullList.size(); i++) {
            for (int j = 0; j < temp.length; j++) {
                if (fullList.get(i).getName().toLowerCase().contains(temp[j].toLowerCase()) ||
                        fullList.get(i).getCategory().toLowerCase().contains(temp[j].toLowerCase()) ||
                        fullList.get(i).getSubCategory().toLowerCase().contains(temp[j].toLowerCase()) ||
                        fullList.get(i).getDescription().toLowerCase().contains(temp[j].toLowerCase())
                ) {
                    currentItemList.add(fullList.get(i));
                    break;
                }
            }
        }

        mAdapter.notifyDataSetChanged();
        mAdapter.nameMap.clear();
        mAdapter.subCatMap.clear();
        mAdapter.catMap.clear();
    }

    private void init() {
        AppConstants.mSearchProductActivity = SearchProductActivity.this;

        currentItemList = new ArrayList<>();

        searchBack = findViewById(R.id.search_product_back);
        searchMic = findViewById(R.id.search_item_mic);
        enterSearchText = findViewById(R.id.edit_search_text);
        search_items_list = findViewById(R.id.search_items_list);

        mAdapter = new SearchProductItemAdapter(this, currentItemList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        search_items_list.setLayoutManager(llm);
        search_items_list.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));
        search_items_list.setAdapter(mAdapter);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Item's Name or Type");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    enterSearchText.setText(result.get(0));
                }
                break;
        }
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        mIsKeyBoardOpen = true;
    }

    public void closeKeyboard() {
        if (!mIsKeyBoardOpen) return;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        mIsKeyBoardOpen = false;
    }

}
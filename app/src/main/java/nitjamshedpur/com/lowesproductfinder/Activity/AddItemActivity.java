package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import nitjamshedpur.com.lowesproductfinder.Model.ListItem;
import nitjamshedpur.com.lowesproductfinder.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    EditText itemText;
    Button itemAddBtn;
    String itemName;
    int count=7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

//        itemAddBtn=(Button)findViewById(R.id.addItemBtn);
//        itemText=(EditText)findViewById(R.id.addItemText);
//
//        itemName=itemText.getText().toString();
//
//
//        itemAddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateShoppingListActivity.itemList.add(new ListItem(itemName,count));
//            }
//        });


    }


}

package nitjamshedpur.com.lowesproductfinder.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.MapCoordinateActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.NavigationActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.SearchProductActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.StartShoppingMapActivity;
import nitjamshedpur.com.lowesproductfinder.R;

public class HomeFragment extends Fragment {

    EditText searchBox;
    Button shoppingList;
    Button navigateBtn;
    Button checkPoints;
    Button startShopping;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        searchBox=(EditText)root.findViewById(R.id.searchBox);
        shoppingList=(Button)root.findViewById(R.id.shoppingList);
        navigateBtn=(Button)root.findViewById(R.id.navigateBtn);
        checkPoints=(Button)root.findViewById(R.id.checkPoints);
        startShopping=(Button)root.findViewById(R.id.startShopping);

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchProductActivity.class));
            }
        });

        shoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateShoppingListActivity.class));
            }
        });

        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NavigationActivity.class));
            }
        });

        checkPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MapCoordinateActivity.class));
            }
        });

        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StartShoppingMapActivity.class));
            }
        });

        return root;
    }
}
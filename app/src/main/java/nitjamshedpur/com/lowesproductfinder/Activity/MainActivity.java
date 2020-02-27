package nitjamshedpur.com.lowesproductfinder.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private SharedPreferences mSharedPreferences;
    private DatabaseReference dref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

//        R.id.nav_tools, R.id.nav_share, R.id.nav_send
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (mSharedPreferences.getBoolean("firstTime", true) == true) {
//            fetchItemList();
//        }
        if(AppConstants.mItemList.size()==0){
            fetchGoodsItemList();
        }
    }

    private void fetchGoodsItemList() {
        progressDialog.show();
        //final ArrayList<String> itemArray=new ArrayList<>();

        AppConstants.mItemList.clear();

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

//                    String itemString=ds.child("category").getValue().toString()+","+
//                    ds.child("subCategory").getValue().toString()+","+
//                            ds.child("price").getValue().toString()+","+
//                            ds.child("floor").getValue().toString()+","+
//                            ds.child("shelf").getValue().toString()+","+
//                            ds.child("description").getValue().toString()+","+
//                            ds.child("name").getValue().toString();
//
//                    itemArray.add(itemString);

                    ItemModal itemModal = new ItemModal(
                            ds.child("category").getValue().toString(),
                            ds.child("subCategory").getValue().toString(),
                            ds.child("price").getValue().toString(),
                            ds.child("floor").getValue().toString(),
                            ds.child("shelf").getValue().toString(),
                            ds.child("description").getValue().toString(),
                            ds.child("name").getValue().toString()
                    );

                    Log.e("onDataChange: ",ds.child("name").getValue().toString());

                    AppConstants.mItemList.add(itemModal);
                }
                Log.e("onDataChange: ",AppConstants.mItemList.toString());
                progressDialog.dismiss();
//                SharedPreferences.Editor editor=mSharedPreferences.edit();
//                editor.putBoolean("firstTime",false);
//                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void init() {
        dref = FirebaseDatabase.getInstance().getReference("shopName").child("items");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Initialising App Data...");
        progressDialog.setCancelable(false);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

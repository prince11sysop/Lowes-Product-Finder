package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.cardview.widget.CardView;
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
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;
import nitjamshedpur.com.lowesproductfinder.utils.RecyclerItemTouchHelper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoreMapActivity extends FragmentActivity implements OnMapReadyCallback,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, TextToSpeech.OnInitListener {

    private GoogleMap mMap;
    Button showShoppingList;

    public RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    public ArrayList<ListItem> itemList;
    public ShoppingInStoreAdapter adapter;
    ArrayList<ListItem> pendingItem;
    ArrayList<ListItem> completedItem;
    Button pending, completed;

    String key = "ItemList";
    private static final String SHARED_PREF = "SharedPref";
    SharedPreferences shref;
    Button direction;
    TextToSpeech tts;
    private String directionTextString = "";
    private TextView directionTextView;
    private ImageView volumeButton;
    private Boolean isVolumeUp = true;
    private CardView voice_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        init();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        shref = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String response = shref.getString(key, "");

        receiveClicks();

        //getting data from local storage
        if (gson.fromJson(response, new TypeToken<List<ListItem>>() {
        }.getType()) != null)
            itemList = gson.fromJson(response, new TypeToken<List<ListItem>>() {
            }.getType());
        else
            itemList = new ArrayList<>();
    }

    private void receiveClicks() {
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoreMapActivity.this, "Opening Google Maps", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=22.772938,86.1444722&daddr=28.567892,77.323089"));
                startActivity(intent);
            }
        });

        volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVolumeUp) {
                    isVolumeUp = false;
                    volumeButton.setImageResource(R.drawable.ic_volume_off_black_24dp);
                    //tts.setPitch(0.0f);
                } else {
                    isVolumeUp = true;
                    volumeButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    //tts.setPitch(1.0f);
                }
            }
        });

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

                            adapter = new ShoppingInStoreAdapter(StoreMapActivity.this, StoreMapActivity.this, pendingItem);
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

                            adapter = new ShoppingInStoreAdapter(StoreMapActivity.this, StoreMapActivity.this, completedItem);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                    adapter = new ShoppingInStoreAdapter(StoreMapActivity.this, StoreMapActivity.this, pendingItem);
                    recyclerView.setAdapter(adapter);
                    swipeFun();
                } else {
                    Toast.makeText(StoreMapActivity.this, "Shopping List Empty!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void init() {
        tts = new TextToSpeech(this, this);
        showShoppingList = (Button) findViewById(R.id.showShoppingList);
        direction = findViewById(R.id.direction);
        directionTextView = findViewById(R.id.sm_direction_text);
        volumeButton = findViewById(R.id.sm_volume_button);
        voice_card = findViewById(R.id.sm_first_layer);
    }

    public void setVoiceDirectionsAndText() {
        if (itemList.size() == 0) {
            directionTextString = "Your shopping list is empty";
            directionTextView.setText(directionTextString);
            speakOut();
            return;
        }
        if (itemList.size() > 0) {
            ArrayList<ListItem> tempList = new ArrayList<>();
            for (ListItem li : itemList) {
                if (!li.isStatus()) {
                    tempList.add(li);
                }
            }
            if (tempList.size() == 0 && itemList.size() > 0) {
                directionTextString = "You have completed your shopping";
                directionTextView.setText("Shopping completed");
                speakOut();
                return;
            }
            tempList = AppConstants.sortItemList(tempList);
            ListItem tempItem = tempList.get(0);

            String noSuffix = "th";
            int i;
            if (tempItem.getFloor().equalsIgnoreCase("gf") ||
                    tempItem.getFloor().equalsIgnoreCase("ugf")) {
                i = 0;
                noSuffix = "";
                tempItem.setFloor("Ground");
            } else {
                try {
                    i = Integer.parseInt(tempItem.getFloor());
                } catch (Exception e) {
                    i = 0;
                }
                if (i == 1) noSuffix = "st";
                else if (i == 2) noSuffix = "nd";
                else if (i == 3) noSuffix = "rd";
                else noSuffix = "th";
            }

            directionTextString = "Move to shelf " + tempItem.getShelf() + " at " + tempItem.getFloor() + noSuffix + " floor";
            directionTextView.setText(directionTextString);
            speakOut();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                setVoiceDirectionsAndText();
                //button.setEnabled(true);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakOut() {
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                final String keyword = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Started" + keyword, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        String text = directionTextString;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "Dummy String");
    }

    public void swipeFun() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
                RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 19.6f; //This goes up to 21
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);

        LatLng sydney = new LatLng(28.567892, 77.323089);
        mMap.addMarker(new MarkerOptions().position(sydney).title("NIT Jamshedpur")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

        LatLng shelf1 = new LatLng(28.568010, 77.322767);
        mMap.addMarker(new MarkerOptions().position(shelf1).title("Shelf: 1")).showInfoWindow();

        LatLng shelf2 = new LatLng(28.567813, 77.322782);
        mMap.addMarker(new MarkerOptions().position(shelf2).title("Shelf: 2")).showInfoWindow();

        LatLng shelf3 = new LatLng(28.567573, 77.323008);
        mMap.addMarker(new MarkerOptions().position(shelf3).title("Shelf: 3")).showInfoWindow();

        LatLng shelf4 = new LatLng(28.568155, 77.322871);
        mMap.addMarker(new MarkerOptions().position(shelf4).title("Shelf: 4")).showInfoWindow();

        LatLng shelf5 = new LatLng(28.568174, 77.323079);
        mMap.addMarker(new MarkerOptions().position(shelf5).title("Shelf: 5")).showInfoWindow();

        LatLng shelf6 = new LatLng(28.568050, 77.323194);
        mMap.addMarker(new MarkerOptions().position(shelf6).title("Shelf: 6")).showInfoWindow();

        LatLng shelf7 = new LatLng(28.567674, 77.323212);
        mMap.addMarker(new MarkerOptions().position(shelf7).title("Shelf: 7")).showInfoWindow();

        LatLng shelf8 = new LatLng(28.568017, 77.322567);
        mMap.addMarker(new MarkerOptions().position(shelf8).title("Shelf: 8")).showInfoWindow();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.RIGHT) {
            if (viewHolder instanceof ShoppingInStoreAdapter.MyShoppingListViewHolder) {

                adapter.removeItem(viewHolder.getAdapterPosition());
            }

        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}

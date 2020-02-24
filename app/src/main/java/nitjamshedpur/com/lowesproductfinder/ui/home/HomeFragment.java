package nitjamshedpur.com.lowesproductfinder.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.MLTextRecognition;
import nitjamshedpur.com.lowesproductfinder.Activity.MapCoordinateActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.NavigationActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.SearchProductActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.StartShoppingMapActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.WebViewActivity;
import nitjamshedpur.com.lowesproductfinder.Carousel.SliderAdapter;
import nitjamshedpur.com.lowesproductfinder.R;

public class HomeFragment extends Fragment {

    Button shoppingList;
    Button navigateBtn;
    Button checkPoints;
    Button startShopping;

    ViewPager viewPager;
    TabLayout indicator;
    List<Integer> sliderImages;
    List<String> sliderText;
    String sliderText1="";
    String sliderText2="";
    String sliderText4="";
    String sliderText5="";

    LinearLayout mItemFinder,mShoppingList, mPriceChecker;
    LinearLayout mCaptureShoppingList;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    Task<Uri> result;
    LinearLayout appliances,bath,lighting,tools,flooring,outdoor;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        searchBox=(EditText)root.findViewById(R.id.searchBox);
        shoppingList=(Button)root.findViewById(R.id.shoppingList);
        navigateBtn=(Button)root.findViewById(R.id.navigateBtn);
        checkPoints=(Button)root.findViewById(R.id.checkPoints);
        startShopping=(Button)root.findViewById(R.id.startShopping);

        mItemFinder=(LinearLayout)root.findViewById(R.id.itemFinder);
        mShoppingList=(LinearLayout)root.findViewById(R.id.myShoppingList);
        mPriceChecker=(LinearLayout)root.findViewById(R.id.checkPrice);
        mCaptureShoppingList=(LinearLayout)root.findViewById(R.id.captureShoppingList);
        appliances=(LinearLayout)root.findViewById(R.id.appliances);
        bath=(LinearLayout)root.findViewById(R.id.bath);
        lighting=(LinearLayout)root.findViewById(R.id.lighting);
        tools=(LinearLayout)root.findViewById(R.id.tools);
        flooring=(LinearLayout)root.findViewById(R.id.flooring);
        outdoor=(LinearLayout)root.findViewById(R.id.outdoor);

        //carousel
        viewPager=(ViewPager)root.findViewById(R.id.viewPager);
        indicator=(TabLayout)root.findViewById(R.id.indicator);
        setCarouselViewPager(); //to implement carousel using viewpager


        mItemFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchProductActivity.class));
            }
        });

        mShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateShoppingListActivity.class));
            }
        });

        mPriceChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MLTextRecognition.class));
            }
        });

        mCaptureShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Hey", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (getFromPref(getActivity().getApplicationContext(), ALLOW_KEY)) {
                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                            showAlert();

                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    openCamera();
                }

            }
        });

        appliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.lowes.com/c/Appliances?int_cmp=Home%3AA2%3AMajorAppliances%3AOther%3APC_Appliances";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title","Appliances");
                startActivity(intent);
            }
        });

        bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.lowes.com/l/bath-event.html?int_cmp=Home%3AA2%3AFashionFixtures%3AOther%3APC_Bath";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title","Bath");
                startActivity(intent);
            }
        });

        lighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.lowes.com/c/Lighting-ceiling-fans?int_cmp=Home%3AA2%3ALighting%3APct_Off%3APC_Lighting";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title","Lighting");
                startActivity(intent);
            }
        });

        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.lowes.com/c/Tools?int_cmp=Home%3AA2%3AToolsHardware%3AOther%3APC_Tools";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title","Tools");
                startActivity(intent);
            }
        });

        flooring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.lowes.com/c/Flooring?int_cmp=Home%3AA2%3AFlooring%3AOther%3APC_Flooring";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title","Flooring");
                startActivity(intent);
            }
        });

        outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.lowes.com/l/Outdoor-tools-equipment-Outdoors?int_cmp=Home%3AA2%3AOutdoors%3AOther%3APC_OPE";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title","Outdoor");
                startActivity(intent);
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

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity() , "Cannot report Without Camera Permissions" , Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
//                        openCamera();
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        openCamera();
                        startInstalledAppDetailsActivity(getActivity());
                    }
                });
        alertDialog.show();
    }

    public static Boolean getFromPref(Context context, String key){
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }


    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null)
            return;

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,MY_PERMISSIONS_REQUEST_CAMERA);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;

        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {

            if(resultCode == Activity.RESULT_OK){
                bitmap = (Bitmap) data.getExtras().get("data");
//                img.setImageBitmap(bitmap);
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Uploading image...");
                progressDialog.setTitle("Please Wait");
                progressDialog.show();
                progressDialog.setCancelable(false);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] images = baos.toByteArray();

                final String filename = System.currentTimeMillis()+" ";

            } else {
                Toast.makeText(getContext() , "Photo not Taken." , Toast.LENGTH_LONG).show();
            }
        }
    }









    //carousel/slider implementation function
    public  void setCarouselViewPager(){

        sliderText = new ArrayList<>();
        sliderText.add(sliderText1);
        sliderText.add(sliderText2);
        sliderText.add(sliderText4);
        sliderText.add(sliderText5);

        sliderImages=new ArrayList<Integer>();
        sliderImages.add(R.drawable.image_5);
        sliderImages.add(R.drawable.image_2);
        sliderImages.add(R.drawable.image_3);
        sliderImages.add(R.drawable.image_4);

        viewPager.setAdapter(new SliderAdapter(getActivity(), sliderImages, sliderText));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 5000, 6000);
    }

    //carousel image auto-slider
    public class SliderTimer extends TimerTask {
        @Override
        public void run() {

            if(getActivity()!=null) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < sliderImages.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }else
                return;;
        }
    }


}
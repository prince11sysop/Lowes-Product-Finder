package nitjamshedpur.com.lowesproductfinder.ui.home;

import android.Manifest;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import nitjamshedpur.com.lowesproductfinder.Activity.CreateShoppingListActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.MLTextRecognition;
import nitjamshedpur.com.lowesproductfinder.Activity.SearchProductActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.StartShoppingActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.StartShoppingMapActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.StoreMapActivity;
import nitjamshedpur.com.lowesproductfinder.Activity.WebViewActivity;
import nitjamshedpur.com.lowesproductfinder.Carousel.SliderAdapter;
import nitjamshedpur.com.lowesproductfinder.R;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    TabLayout indicator;
    List<Integer> sliderImages;
    List<String> sliderText;
    String sliderText1 = "";
    String sliderText2 = "";
    String sliderText4 = "";
    String sliderText5 = "";

    LinearLayout mItemFinder,mShoppingList, mPriceChecker;
    LinearLayout mCaptureShoppingList;


    Task<Uri> result;
    LinearLayout appliances,bath,lighting,tools,flooring,outdoor;
    Button navigateBtn;

    Button captureImageBtn;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int PICK_IMAGE_REQUEST=71;
    Bitmap imageBitmap;
    private Uri filePath;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


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
        navigateBtn=(Button)root.findViewById(R.id.navigateBtn);

        //carousel
        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        indicator = (TabLayout) root.findViewById(R.id.indicator);


        setCarouselViewPager(); //to implement carousel using viewpager

        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=22.772938,86.1444722&daddr=28.567892,77.323089"));
                startActivity(intent);

            }
        });

        mItemFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppConstants.mItemList.size() == 0) {
                    if (AppConstants.isNetworkAvailable(getActivity())) {
                        AppConstants.fetchGoodsItemList(getActivity());
                    } else {
                        Toast.makeText(getActivity(), "Please make sure you have a secure internet connection.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
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
                startActivity(new Intent(getContext(), StoreMapActivity.class));
            }
        });

        mCaptureShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (getFromPref(getContext().getApplicationContext(), ALLOW_KEY)) {
                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    selectImage();
                }


            }
        });

        appliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lowes.com/c/Appliances?int_cmp=Home%3AA2%3AMajorAppliances%3AOther%3APC_Appliances";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Appliances");
                startActivity(intent);
            }
        });

        bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lowes.com/l/bath-event.html?int_cmp=Home%3AA2%3AFashionFixtures%3AOther%3APC_Bath";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Bath");
                startActivity(intent);
            }
        });

        lighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lowes.com/c/Lighting-ceiling-fans?int_cmp=Home%3AA2%3ALighting%3APct_Off%3APC_Lighting";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Lighting");
                startActivity(intent);
            }
        });

        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lowes.com/c/Tools?int_cmp=Home%3AA2%3AToolsHardware%3AOther%3APC_Tools";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Tools");
                startActivity(intent);
            }
        });

        flooring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lowes.com/c/Flooring?int_cmp=Home%3AA2%3AFlooring%3AOther%3APC_Flooring";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Flooring");
                startActivity(intent);
            }
        });

        outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lowes.com/l/Outdoor-tools-equipment-Outdoors?int_cmp=Home%3AA2%3AOutdoors%3AOther%3APC_OPE";
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Outdoor");
                startActivity(intent);
            }
        });

        return root;
    }

    private  void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload shopping List");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                } else if (options[item].equals("Choose from Gallery")){

                    Intent intent =new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            detectTextFromImage();

        } else if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null&& data.getData()!=null ){
            filePath=data.getData();
            try {
                imageBitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                detectTextFromImage();

            }catch(Exception e){

            }
        }
    }

    public static Boolean getFromPref(Context context, String key){
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the device Camera to scan the Shopping list.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext() , "Cannot proceed Without Camera Permissions" , Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},
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



    private void detectTextFromImage() {

        FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer= FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error: ",e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.TextBlock> blockList=firebaseVisionText.getTextBlocks();
        if(blockList.size()==0){
            Toast.makeText(getActivity(), "No Text Found!", Toast.LENGTH_SHORT).show();
        }else{
//            for(FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks() ){
//
//                String text=block.getText();
//                textView.setText(text);
//            }

            String detectedText="";
            for (int i = 0; i < blockList.size(); i++) {
                List<FirebaseVisionText.Line> lines = blockList.get(i).getLines();
                for (int j = 0; j < lines.size(); j++) {
                    List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                    detectedText+="\n";
                    for (int k = 0; k < elements.size(); k++) {
                        detectedText +=elements.get(k).getText()+" ";
                    }
                }
            }
            Toast.makeText(getActivity(), detectedText, Toast.LENGTH_SHORT).show();

        }
    }






    //carousel/slider implementation function
    public void setCarouselViewPager() {

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

            if (getActivity() != null) {

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

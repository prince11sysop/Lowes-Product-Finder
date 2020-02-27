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
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    TabLayout indicator;
    List<Integer> sliderImages;
    List<String> sliderText;
    String sliderText1 = "";
    String sliderText2 = "";
    String sliderText4 = "";
    String sliderText5 = "";

    LinearLayout mItemFinder, mShoppingList, mPriceChecker;
    RelativeLayout mCaptureShoppingList;

    LinearLayout appliances, bath, lighting, tools, flooring, outdoor;
    Button navigateBtn;
    LottieAnimationView animationView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        mItemFinder = (LinearLayout) root.findViewById(R.id.itemFinder);
        mShoppingList = (LinearLayout) root.findViewById(R.id.myShoppingList);
        mPriceChecker = (LinearLayout) root.findViewById(R.id.checkPrice);
        mCaptureShoppingList = root.findViewById(R.id.captureShoppingList);
        appliances = (LinearLayout) root.findViewById(R.id.appliances);
        bath = (LinearLayout) root.findViewById(R.id.bath);
        lighting = (LinearLayout) root.findViewById(R.id.lighting);
        tools = (LinearLayout) root.findViewById(R.id.tools);
        flooring = (LinearLayout) root.findViewById(R.id.flooring);
        outdoor = (LinearLayout) root.findViewById(R.id.outdoor);
        navigateBtn = (Button) root.findViewById(R.id.navigateBtn);

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
                    }
                    return;
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

                startActivity(new Intent(getContext(),MLTextRecognition.class));
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

    //carousel/slider implementation function
    public void setCarouselViewPager() {

        sliderText = new ArrayList<>();
        sliderText.add(sliderText1);
        sliderText.add(sliderText2);
        sliderText.add(sliderText4);
        sliderText.add(sliderText5);

        sliderImages = new ArrayList<Integer>();
        sliderImages.add(R.drawable.image_1);
        sliderImages.add(R.drawable.img2);
        sliderImages.add(R.drawable.image_4);
        sliderImages.add(R.drawable.img8);


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
            } else
                return;
            ;
        }
    }


}

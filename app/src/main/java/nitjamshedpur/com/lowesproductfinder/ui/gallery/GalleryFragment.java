package nitjamshedpur.com.lowesproductfinder.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import nitjamshedpur.com.lowesproductfinder.R;

public class GalleryFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button direction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        direction=root.findViewById(R.id.direction);
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Opening Google Maps", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=22.772938,86.1444722&daddr=28.567892,77.323089"));
                startActivity(intent);
            }
        });

        return root;
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
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

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

}
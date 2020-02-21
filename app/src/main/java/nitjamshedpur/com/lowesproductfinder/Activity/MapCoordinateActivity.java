package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import nitjamshedpur.com.lowesproductfinder.Modal.Coordinate;
import nitjamshedpur.com.lowesproductfinder.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapCoordinateActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    List<Coordinate> list;
    int height,width;
    Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_coordinate);

        find=(Button)findViewById(R.id.find);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Coordinates");

        list=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Coordinate coordinate=dataSnapshot.getValue(Coordinate.class);
                    list.add(coordinate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x=(list.get(0).getScaleX())*width;
                float y=(list.get(0).getScaleY())*height;

                Toast.makeText(MapCoordinateActivity.this, "X: "+x+" Y: "+y, Toast.LENGTH_SHORT).show();
            }
        });


    }
}

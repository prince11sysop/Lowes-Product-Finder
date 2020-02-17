package nitjamshedpur.com.lowesproductfinder.Activity;

import nitjamshedpur.com.lowesproductfinder.Modal.Coordinate;
import nitjamshedpur.com.lowesproductfinder.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NavigationActivity extends Activity {

    int height,width;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Coordinates");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);

        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        float scaleX=(float)x/width;
        float scaleY=(float)y/height;

        final String filename = System.currentTimeMillis() + " ";
        Coordinate coordinate=new Coordinate("Shelf1",scaleX,scaleY,x,y);
        databaseReference.push().setValue(coordinate);

        Toast.makeText(this, "X: "+scaleX+" Y: "+scaleY, Toast.LENGTH_SHORT).show();
        return false;
    }
}

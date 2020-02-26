package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import nitjamshedpur.com.lowesproductfinder.R;
import nitjamshedpur.com.lowesproductfinder.utils.AppConstants;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class MLTextRecognition extends Activity {

    Button detectTextBtn;
    ImageView imageView,captureImageBtn;
    TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    Bitmap imageBitmap;
    private Uri filePath;
    private boolean ifImageAdded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mltext_recognition);

        captureImageBtn = findViewById(R.id.capture_image);
        detectTextBtn = (Button) findViewById(R.id.detect_text_image);
        imageView = (ImageView) findViewById(R.id.image_view);
        textView = (TextView) findViewById(R.id.text_display);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
                textView.setText("");
            }
        });

        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifImageAdded){
                    Toast.makeText(MLTextRecognition.this, "Add an Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                detectTextFromImage();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] options = {"Take a Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add your Shopping List!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take a Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            ifImageAdded=true;

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(imageBitmap);
                ifImageAdded=true;
            } catch (Exception e) {

            }
        }

        if(ifImageAdded){
            detectTextBtn.setEnabled(true);
            detectTextBtn.setAlpha(0.9f);
        }
    }


    private void detectTextFromImage() {

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MLTextRecognition.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error: ", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if (blockList.size() == 0) {
            Toast.makeText(this, "No Text Found!", Toast.LENGTH_SHORT).show();
        } else {
//            for(FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks() ){
//
//                String text=block.getText();
//                textView.setText(text);
//            }

            String s = "";
            for (int i = 0; i < blockList.size(); i++) {
                List<FirebaseVisionText.Line> lines = blockList.get(i).getLines();
                for (int j = 0; j < lines.size(); j++) {
                    List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                    s += "\n";
                    for (int k = 0; k < elements.size(); k++) {
                        s += elements.get(k).getText() + " ";
                    }
                }
            }
            textView.setText(s);
            Log.e("displayTextFromImage: ",s);
            AppConstants.listFromScan = s;
            startActivity(new Intent(this, CreateShoppingListActivity.class));
            finish();
        }
    }

}

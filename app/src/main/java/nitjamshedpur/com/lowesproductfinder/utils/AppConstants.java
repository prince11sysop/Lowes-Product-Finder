package nitjamshedpur.com.lowesproductfinder.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import nitjamshedpur.com.lowesproductfinder.Activity.SearchProductActivity;
import nitjamshedpur.com.lowesproductfinder.Modal.ItemModal;
import nitjamshedpur.com.lowesproductfinder.R;

public class AppConstants {
    public static ArrayList<ItemModal> mItemList = new ArrayList<>();
    public static SearchProductActivity mSearchProductActivity;

    public static void openAddItemDialog(Context context) {
        Rect displayRectangle = new Rect();
        Window window = AppConstants.mSearchProductActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = AppConstants.mSearchProductActivity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.item_details_dialog, viewGroup, false);

        TextView upper_text = dialogView.findViewById(R.id.upper_text);

        TextView sendOtp = dialogView.findViewById(R.id.send_otp);

        //final EditText enterOtp = dialogView.findViewById(R.id.enter_phone_no_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialogOtp;
        alertDialogOtp = builder.create();
        alertDialogOtp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogOtp.setCancelable(true);
        alertDialogOtp.show();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do it

                alertDialogOtp.hide();
            }
        });
    }
}

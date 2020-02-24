package nitjamshedpur.com.lowesproductfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import nitjamshedpur.com.lowesproductfinder.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends Activity {

    WebView webView;
    ProgressDialog progressDialog;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView=(WebView)findViewById(R.id.webView);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);

        webView.setWebViewClient(new MyWebViewClient());

        Intent i = getIntent();
        String url= i.getStringExtra("url");
        String title=i.getStringExtra("title");
        toolbarTitle.setText(title);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog = new ProgressDialog(WebViewActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }
}

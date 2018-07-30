package net.jahanco.jahandaru.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.R;

public class WebActivity extends AppCompatActivity {
    private WebView mbrowser;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mbrowser= (WebView) findViewById(R.id.browser);
        CustomTextView page_title= (CustomTextView) findViewById(R.id.page_title);
        String id=getIntent().getExtras().getString("id");
        AppCompatImageView back= (AppCompatImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mbrowser.getSettings().setJavaScriptEnabled(true);
        mbrowser.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");


        switch (id){
            case "2":mbrowser.loadUrl("file:///android_asset/html/moaseer.html");
                page_title.setText("مواد موثر");

                break;
            case "3":mbrowser.loadUrl("file:///android_asset/html/ravesh_jamavari.html");
                page_title.setText("روش های جمع آوری");
                break;
            case "4":mbrowser.loadUrl("file:///android_asset/html/noskhe.html");
                page_title.setText("نسخه درمانی گیاهی");

                break;
            case "5":mbrowser.loadUrl("file:///android_asset/html/ravesh_estefadeh.html");
                page_title.setText("روش استفاده");

                break;
            case "6":mbrowser.loadUrl("file:///android_asset/html/teb.html");
                page_title.setText("انواع طب");

                break;
            case "7":mbrowser.loadUrl("file:///android_asset/html/andam.html");
                page_title.setText("اندام های قابل مصرف");

                break;
            case "8":mbrowser.loadUrl("file:///android_asset/html/ghodad.html");
                page_title.setText("غدد و اندامهای بدن");

                break;
            case "9":mbrowser.loadUrl("file:///android_asset/html/darbare_ketab.html");
                page_title.setText("درباره کتاب");

                break;
            case "10":mbrowser.loadUrl("file:///android_asset/html/darbare_sherkat.html");
                page_title.setText("درباره شرکت");

                break;
        }



    }
    public class WebViewJavaScriptInterface{

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(){
            Toast.makeText(context, "hi",Toast.LENGTH_SHORT).show();
        }
    }
}

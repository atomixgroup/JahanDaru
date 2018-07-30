package net.jahanco.jahandaru.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Constants;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Helpers.WebService;
import net.jahanco.jahandaru.Models.SpecialSick;
import net.jahanco.jahandaru.R;

import java.util.HashMap;

public class SpecialItemActivity extends AppCompatActivity {
    CustomTextView title,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_item_activity);
        final ScaleRatingBar ratingBar = (ScaleRatingBar) findViewById(R.id.rate);
        final TextView rate_result= (TextView) findViewById(R.id.rate_result);
        final String id=getIntent().getExtras().getString("id");

        ratingBar.setClearRatingEnabled(true);


        App.webService.postRequest("id", id, Constants.netWorkUrl + "getAverageRate", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                ratingBar.setOnRatingChangeListener(null);
                ratingBar.setRating(Float.parseFloat(message));
                rate_result.setText(String.format("%.1f", Float.parseFloat(message)));
                ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                        HashMap<String,String> params=new HashMap<String, String>();
                        params.put("rate",v+"");
                        params.put("IMEI",App.IMEI+"");
                        params.put("s_id",id);
                        WebService webService=new WebService();
                        webService.postRequest(params, Constants.netWorkUrl + "setRate", new WebService.OnPostReceived() {
                            @Override
                            public void onReceived(String message) {
                                rate_result.setText(String.format("%.1f", Float.parseFloat(message)));
                            }

                            @Override
                            public void onReceivedError(String message) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onReceivedError(String message) {

            }
        });
        title= (CustomTextView) findViewById(R.id.title);
        description= (CustomTextView) findViewById(R.id.description);



        SpecialSick specialSick= App.dbHelper.getSpecialItem(Integer.parseInt(id));
        title.setText(specialSick.getName());
        if(App.prefManager.getPreference("premium").equals("true")){
            description.setText(specialSick.getBody());

        }




    }
}

package net.jahanco.jahandaru.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import net.jahanco.jahandaru.Adapters.SicknessAdapter;
import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomButton;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Helpers.HelperDownloader;
import net.jahanco.jahandaru.Models.Sick;
import net.jahanco.jahandaru.R;

import java.util.ArrayList;

public class SicknessActivity extends AppCompatActivity {
    private RecyclerView sicknessList;
    private RelativeLayout wait_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sickness);
        wait_layout = (RelativeLayout) findViewById(R.id.wait_layout);
        if (Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF81D4FA"));
        }
        AppCompatImageView back= (AppCompatImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<Sick> sicks= App.dbHelper.getSicknessItems();
        if(sicks!=null && (!sicks.isEmpty())) {
            sicknessList= (RecyclerView) findViewById(R.id.sicknessList);
            SicknessAdapter adapter=new SicknessAdapter(this,sicks);
            sicknessList.setLayoutManager(new LinearLayoutManager(this));
            sicknessList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else{
            final LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.error_dialog, null);
            final AlertDialog dialog= new AlertDialog.Builder(SicknessActivity.this).create();
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            CustomButton btn_success= (CustomButton) dialogView.findViewById(R.id.btn_success);
            CustomTextView title= (CustomTextView) dialogView.findViewById(R.id.dialog_title);
            CustomTextView body= (CustomTextView) dialogView.findViewById(R.id.dialog_body);
            title.setText("مشکل در دریافت اطلاعات");
            body.setText("مشکل در دریافت اطلاعات به وجود آمده است بعد از اطمینان از وصل بودن اینترنت خود بر روی تایید لمس کنید تا اطلاعات دوباره از سرور گرفته شود.");
            btn_success.setText("پس از اتصال اینترنت تایید کنید");
            btn_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(App.isNetworkAvailable()) {
                        wait_layout.setVisibility(View.VISIBLE);
                        dialog.dismiss();

                        App.getDataFromServer(new HelperDownloader.OnDownloadCompleteListener() {
                            @Override
                            public void onDownloadComplete() {
                                wait_layout.setVisibility(View.GONE);
                                App.prefManager.savePreference("initialized", "true");
                                finish();
                            }
                        });
                    }
                    else{

                    }

                }
            });
            dialog.show();
        }




    }
}

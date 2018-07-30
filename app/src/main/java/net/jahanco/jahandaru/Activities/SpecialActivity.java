package net.jahanco.jahandaru.Activities;

import android.content.Intent;
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

import net.jahanco.jahandaru.Adapters.SpecialSicknessAdapter;
import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomButton;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Helpers.HelperDownloader;
import net.jahanco.jahandaru.Models.SpecialSick;
import net.jahanco.jahandaru.R;

import java.util.ArrayList;

public class SpecialActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private CustomButton upgrade;
    private final int reqCode = 1231;
    private RelativeLayout wait_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        SpecialSicknessAdapter adapter;
        wait_layout = (RelativeLayout) findViewById(R.id.wait_layout);
        RecyclerView special_list= (RecyclerView) findViewById(R.id.special_list);
        AppCompatImageView back= (AppCompatImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF81D4FA"));
        }
        ArrayList<SpecialSick> specialSicks=App.dbHelper.getSpecialItems();
        if(specialSicks!=null && (!specialSicks.isEmpty())) {
            adapter = new SpecialSicknessAdapter(this, specialSicks, new SpecialSicknessAdapter.OnOpenDialogSet() {
                @Override
                public void openDialogSet() {
                    final LayoutInflater factory = LayoutInflater.from(SpecialActivity.this);
                    final View dialogView = factory.inflate(R.layout.upgrade_dialog, null);
                    dialog = new AlertDialog.Builder(SpecialActivity.this).create();
                    upgrade = (CustomButton) dialogView.findViewById(R.id.btnUpgrade);
                    upgrade.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SpecialActivity.this, PurchaseActivity.class);
                            startActivityForResult(intent, reqCode);
                            dialog.dismiss();
                        }
                    });
                    dialog.setView(dialogView);
                    dialog.show();

                }
            });
            special_list.setLayoutManager(new LinearLayoutManager(this));
            special_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else{
            final LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.error_dialog, null);
            final AlertDialog dialog= new AlertDialog.Builder(SpecialActivity.this).create();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == reqCode && resultCode == RESULT_OK) {
            try {
                String result = data.getExtras().getString("result");
                if (result.equals("true")) {
                    //TODO update ui
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

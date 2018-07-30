package net.jahanco.jahandaru.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.jahanco.jahandaru.Adapters.MedicalItemsAdapter;
import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomButton;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Helpers.HelperDownloader;
import net.jahanco.jahandaru.Models.ItemEntity;
import net.jahanco.jahandaru.R;

import java.util.ArrayList;

public class MedicinalPlantsActivity extends AppCompatActivity {
    private RecyclerView medical_item_list;
    private MedicalItemsAdapter adapter;
    private LinearLayout upgrade_premium;
    private AppCompatImageView back;
    private final int reqCode=1231;
    private RelativeLayout wait_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinal_plants);
        if (Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF81D4FA"));
        }
        setupViews();
    }

    private void setupViews() {
        back= (AppCompatImageView) findViewById(R.id.back);
        wait_layout = (RelativeLayout) findViewById(R.id.wait_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        medical_item_list= (RecyclerView) findViewById(R.id.medical_item_list);
        ArrayList<ItemEntity> itemEntities;
        String type=getIntent().getExtras().getString("type");
        if(type.equals("search")){
            int sick_id=Integer.parseInt(getIntent().getExtras().getString("id"));
            itemEntities=App.dbHelper.getItemsWithSickness(sick_id);
        }
        else{
            itemEntities=App.dbHelper.getItems();
        }
        if(itemEntities!=null && (!itemEntities.isEmpty())) {
            adapter = new MedicalItemsAdapter(MedicinalPlantsActivity.this, itemEntities);
            medical_item_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            medical_item_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            final LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.error_dialog, null);
            final AlertDialog dialog= new AlertDialog.Builder(MedicinalPlantsActivity.this).create();
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
        upgrade_premium= (LinearLayout) findViewById(R.id.upgrade_premium);
        upgrade_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MedicinalPlantsActivity.this,PurchaseActivity.class);
                startActivityForResult(intent,reqCode);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==reqCode && resultCode==RESULT_OK){
            try{
                String result=data.getExtras().getString("result");
                if(result.equals("true")) {
                    upgrade_premium.setVisibility(View.INVISIBLE);

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(App.prefManager.getPreference("premium").equals("true")){
            upgrade_premium.setVisibility(View.GONE);
        }
        else{
            upgrade_premium.setVisibility(View.VISIBLE);

        }
    }
}

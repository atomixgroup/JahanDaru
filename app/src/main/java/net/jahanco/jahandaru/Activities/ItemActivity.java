package net.jahanco.jahandaru.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Models.ItemEntity;
import net.jahanco.jahandaru.R;

import java.io.File;

public class ItemActivity extends AppCompatActivity {
    CustomTextView name,head_name,en_name,scientific_name,family,nature,specifications,ingredients,properties,contraindications,organs,habitat,construction;
    ImageView image;
    LinearLayout ravesh,upgrade_pro,upgrade_pro2,share;

    AppCompatImageView back;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsing_toolbar;
    private final int reqCode=1231;
    private ItemEntity itemEntity;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        id=getIntent().getExtras().getString("id");
        setupViews();
        updateUI();


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsing_toolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsing_toolbar)) {
                    head_name.setVisibility(View.VISIBLE);
                } else {
                    head_name.setVisibility(View.INVISIBLE);
                }
            }
        });

        upgrade_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemActivity.this,PurchaseActivity.class);
                startActivityForResult(intent,reqCode);
            }
        });
        upgrade_pro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemActivity.this,PurchaseActivity.class);
                startActivityForResult(intent,reqCode);
            }
        });

    }

    private void setupViews() {
        if (Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF81D4FA"));
        }
        appBarLayout= (AppBarLayout) findViewById(R.id.appBar);
        collapsing_toolbar= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        name= (CustomTextView) findViewById(R.id.name);
        head_name= (CustomTextView) findViewById(R.id.head_name);
        en_name= (CustomTextView) findViewById(R.id.en_name);
        scientific_name= (CustomTextView) findViewById(R.id.scientific_name);
        family= (CustomTextView) findViewById(R.id.family);
        nature= (CustomTextView) findViewById(R.id.nature);
        specifications= (CustomTextView) findViewById(R.id.specifications);
        ingredients= (CustomTextView) findViewById(R.id.ingredients);
        properties= (CustomTextView) findViewById(R.id.properties);
        contraindications= (CustomTextView) findViewById(R.id.contraindications);
        organs= (CustomTextView) findViewById(R.id.organs);
        habitat= (CustomTextView) findViewById(R.id.habitat);
        construction= (CustomTextView) findViewById(R.id.construction);
        image= (ImageView) findViewById(R.id.image);
        ravesh= (LinearLayout) findViewById(R.id.ravesh);
        upgrade_pro= (LinearLayout) findViewById(R.id.upgrade_premium_item);
        upgrade_pro2= (LinearLayout) findViewById(R.id.upgrade_premium_item2);
        back= (AppCompatImageView) findViewById(R.id.back);
        share= (LinearLayout) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAddress();
            }
        });

    }
    private String getTrimedText(String text){
        if(text==null || text.equals("")||text.equals("NULL")||text.equals("null")){
            return "";
        }
        text= text.replace("  ","");
        text= text.replace("هه","ه ه");
        text= text.replace("\r\n","");
        text= text.replace("\n","");
       return text.replace("\r","");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==reqCode && resultCode==RESULT_OK){
            try{
                String result=data.getExtras().getString("result");
                if(result.equals("true")) {
                    updateUI();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {
        if(App.prefManager.getPreference("premium").equals("-1")){
            upgrade_pro.setVisibility(View.VISIBLE);
            upgrade_pro2.setVisibility(View.VISIBLE);
        }
        else {
            upgrade_pro.setVisibility(View.GONE);
            upgrade_pro2.setVisibility(View.GONE);

        }
        itemEntity= App.dbHelper.getItem(Integer.parseInt(id));
        name.setText(getTrimedText(itemEntity.getName()).replace(":",":\n"));
        head_name.setText(getTrimedText(itemEntity.getName()));
        en_name.setText(getTrimedText(itemEntity.getEn_name()));
        scientific_name.setText(getTrimedText(itemEntity.getScientific_name()));
        family.setText(getTrimedText(itemEntity.getFamily()));
        nature.setText(getTrimedText(itemEntity.getNature()));
        specifications.setText(getTrimedText(itemEntity.getSpecifications()));
        ingredients.setText(getTrimedText(itemEntity.getIngredients()));
        properties.setText(getTrimedText(itemEntity.getProperties()));
        contraindications.setText(getTrimedText(itemEntity.getContraindications()));
        organs.setText(getTrimedText(itemEntity.getOrgans()));
        habitat.setText(getTrimedText(itemEntity.getHabitat()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String imageUri = App.EXTERNAL_STORAGE_URI + "images/" + itemEntity.getId() ;
        Uri uri = Uri.fromFile(new File(imageUri));
        Picasso.with(this).load(uri)
                .placeholder(R.drawable.placeholder)
                .into(image);
        if(itemEntity.getConstruction()==null || itemEntity.getConstruction().equals("")||itemEntity.getConstruction().equals("NULL")||itemEntity.getConstruction().equals("null")){
            ravesh.setVisibility(View.GONE);
        }
        else{
            construction.setText(getTrimedText(itemEntity.getConstruction()));
            ravesh.setVisibility(View.VISIBLE);
        }
    }
    private void shareAddress(){
        String shareBody = "http://www.japp.pro/apps/JahanDaru";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "دانلود اپلیکیشن جهان دارو از لینک زیر امکانپذیر می باشد:");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"ارسال به دوستان جهت دانلود"));
    }
}

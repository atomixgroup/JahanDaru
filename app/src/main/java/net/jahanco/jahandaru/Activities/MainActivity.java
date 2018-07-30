package net.jahanco.jahandaru.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Constants;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Helpers.HelperDownloader;
import net.jahanco.jahandaru.Helpers.SendMailTask;
import net.jahanco.jahandaru.Helpers.WebService;
import net.jahanco.jahandaru.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private WebService.OnPostReceived onPostReceived;

    private AppCompatImageView item_image1, item_image2, item_image3, item_image4, item_image5, item_image6, item_image7, item_image8,item_image9;
    private Intent intent;
    private final int reqCode = 1231;
    private LinearLayout upgrade_premium;
    private RelativeLayout wait_layout;

    private EditText name,address,postal_code,number,edtCount;
    private Button dismiss,send;
    DrawerLayout drawer;
    private AlertDialog dialog;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean check=checkPermissions();
        setupViews();

        if(check){
            Log.i("permission", "onCreate: true");
            if(!App.prefManager.isSetPereference()) {
                if(!App.isNetworkAvailable()){
                    showNetworkAvaibleDialog();
                }else{
                    wait_layout.setVisibility(View.VISIBLE);
                    App.getDataFromServer(new HelperDownloader.OnDownloadCompleteListener() {
                        @Override
                        public void onDownloadComplete() {
                            wait_layout.setVisibility(View.GONE);
                            App.prefManager.savePreference("initialized","true");
                        }
                    });
                }


            }
        }
        else{
            Log.i("permission", "onCreate: false");
        }


    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        item_image1 = (AppCompatImageView) findViewById(R.id.item_image1);
        item_image1.setOnClickListener(this);
        item_image2 = (AppCompatImageView) findViewById(R.id.item_image2);
        item_image2.setOnClickListener(this);
        item_image3 = (AppCompatImageView) findViewById(R.id.item_image3);
        item_image3.setOnClickListener(this);
        item_image4 = (AppCompatImageView) findViewById(R.id.item_image4);
        item_image4.setOnClickListener(this);
        item_image5 = (AppCompatImageView) findViewById(R.id.item_image5);
        item_image5.setOnClickListener(this);
        item_image6 = (AppCompatImageView) findViewById(R.id.item_image6);
        item_image6.setOnClickListener(this);
        item_image7 = (AppCompatImageView) findViewById(R.id.item_image7);
        item_image7.setOnClickListener(this);
        item_image8 = (AppCompatImageView) findViewById(R.id.item_image8);
        item_image8.setOnClickListener(this);
        item_image9= (AppCompatImageView) findViewById(R.id.item_image9);
        item_image9.setOnClickListener(this);
        upgrade_premium = (LinearLayout) findViewById(R.id.upgrade_premium);
        upgrade_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivityForResult(intent, reqCode);
            }
        });
        wait_layout = (RelativeLayout) findViewById(R.id.wait_layout);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        TextView designedBy= (TextView) headerView.findViewById(R.id.designed_by);
        designedBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://jahanco.net/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
//        final View menuLayout = factory.inflate(R.layout.app_bar_main, null);
        CustomTextView contact_us= (CustomTextView) headerView.findViewById(R.id.contact_us);
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+989358146997"));
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        CustomTextView about_book= (CustomTextView) headerView.findViewById(R.id.about_book);
        about_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,WebActivity.class);
                intent.putExtra("id","9");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        CustomTextView about_designer= (CustomTextView) headerView.findViewById(R.id.about_designer);
        about_designer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        CustomTextView book_shop= (CustomTextView) headerView.findViewById(R.id.book_shop);
        book_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        CustomTextView social_media= (CustomTextView) headerView.findViewById(R.id.social_media);
        social_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                    telegramIntent.setData(Uri.parse("http://telegram.me/ejahanco"));
                    startActivity(telegramIntent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"تلگرام بروی گوشی شما نصب نمیباشد از طریق شماره تماس با ما در ارتباط باشید.",Toast.LENGTH_LONG).show();
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        CustomTextView menu_exit= (CustomTextView) headerView.findViewById(R.id.menu_exit);
        menu_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.cart_dialog, null);
        dialog= new AlertDialog.Builder(MainActivity.this).create();
        dialog.setView(dialogView);

        name= (EditText) dialogView.findViewById(R.id.edtName);
        address= (EditText) dialogView.findViewById(R.id.edtAddress);
        postal_code= (EditText) dialogView.findViewById(R.id.postal_code);
        number= (EditText) dialogView.findViewById(R.id.edtNum);
        edtCount= (EditText) dialogView.findViewById(R.id.edtCount);
        dismiss= (Button) dialogView.findViewById(R.id.btnDisMiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        send= (Button) dialogView.findViewById(R.id.btnSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String fromEmail = "jahanco.net@gmail.com";
                String fromPassword = "gmail1366";
                String toEmails = "jahanco.net@gmail.com";
                List<String> toEmailList = new ArrayList<String>();
                toEmailList.add(toEmails);
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = "Email from jahandaru app";
                String emailBody = "Name:" + name.getText() + "\n" + "Phone:" + number.getText() + "\n" + "Address:" + address.getText()+ "\n" + "Postal Code:" + postal_code.getText()+ "\n" + "Count:" + edtCount.getText();
                new SendMailTask(MainActivity.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
            }
        });

    }


    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull final String[] permissions, @NonNull int[] grantResults) {
        boolean flag=false;
        for (int grant:grantResults) {
            if (grant!=0){
                flag=true;
            }
        }
        if (!flag) {
            if(!App.prefManager.isSetPereference()){
                wait_layout.setVisibility(View.VISIBLE);
                if(!App.isNetworkAvailable()){
                    showNetworkAvaibleDialog();
                }
                else{
                    App.getDataFromServer(new HelperDownloader.OnDownloadCompleteListener() {
                        @Override
                        public void onDownloadComplete() {
                            wait_layout.setVisibility(View.GONE);
                            App.prefManager.savePreference("initialized","true");
                        }
                    });
                }

            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                App.IMEI = telephonyManager.getDeviceId();
                final HashMap<String,String> params=new HashMap<>();
                if(App.prefManager.getPreference("token").equals("-1")) return;
                params.put("token",App.prefManager.getPreference("token"));
                params.put("IMEI",App.IMEI+"");
                onPostReceived=new WebService.OnPostReceived() {
                    @Override
                    public void onReceived(String message) {
                        if(message.equals("1:error")){
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            App.webService.postRequest(params, Constants.netWorkUrl + "setToken", onPostReceived);
                        }
                    }

                    @Override
                    public void onReceivedError(String message) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        App.webService.postRequest(params, Constants.netWorkUrl + "setToken", onPostReceived);
                    }
                };
                App.webService.postRequest(params, Constants.netWorkUrl + "setToken", onPostReceived);
            } catch (Exception e) {

            }

        }
        else{
            finish();
        }

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_image1:
                intent = new Intent(MainActivity.this, MedicinalPlantsActivity.class);
                intent.putExtra("type","normal");
                startActivity(intent);
                break;
            case R.id.item_image2:
                intent = new Intent(MainActivity.this, SicknessActivity.class);
                intent.putExtra("id", "2");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image3:
                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("id", "3");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image4:
                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("id", "4");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image5:
                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("id", "5");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image6:
                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("id", "6");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image7:
                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("id", "7");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image8:
                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("id", "8");
                intent.putExtra("type","normal");

                startActivity(intent);
                break;
            case R.id.item_image9:
                intent = new Intent(MainActivity.this, SpecialActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.prefManager.getPreference("premium").equals("true")) {
            upgrade_premium.setVisibility(View.GONE);
        } else {
            upgrade_premium.setVisibility(View.VISIBLE);

        }

    }
    private void showNetworkAvaibleDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        alertDialogBuilder.setTitle("توجه");
        alertDialogBuilder
                .setMessage("برای گرفتن اطلاعات بروز نیاز به اتصال اینترنت می باشد لطفا اینترنت خود را وصل فرمایید .")
                .setCancelable(false)
                .setPositiveButton("بعد از اتصال تایید کنید ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if(App.isNetworkAvailable()){
                            wait_layout.setVisibility(View.VISIBLE);
                            App.getDataFromServer(new HelperDownloader.OnDownloadCompleteListener() {
                                @Override
                                public void onDownloadComplete() {
                                    wait_layout.setVisibility(View.GONE);
                                    App.prefManager.savePreference("initialized","true");
                                }
                            });
                        }
                        else{
                            alertDialog.dismiss();
                            showNetworkAvaibleDialog();
                        }
                    }
                });

        // create alert dialog
         alertDialog= alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}

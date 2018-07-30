package net.jahanco.jahandaru.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomButton;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (App.prefManager.getPreference("terms").equals("-1")) {
            final LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.error_dialog, null);
            final AlertDialog dialog = new AlertDialog.Builder(SplashScreenActivity.this).create();
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            CustomButton btn_success = (CustomButton) dialogView.findViewById(R.id.btn_success);
            CustomTextView title = (CustomTextView) dialogView.findViewById(R.id.dialog_title);
            CustomTextView body = (CustomTextView) dialogView.findViewById(R.id.dialog_body);
            title.setText("توافق نامه");
            body.setText("از مصرف خودسرانه ی هرگونه داروی گیاهی یا شیمیایی بدون تجویز پزشک پرهیز نمایید مطالب این نرم افزار صرفا جهت اطلاع رسانی میباشد و استفاده از آنها با هماهنگی پزشک بلامانع می باشد.");
            btn_success.setText("متوجه شدم");
            btn_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.prefManager.savePreference("terms", "true");
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            });
            dialog.show();


        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }


    }
}

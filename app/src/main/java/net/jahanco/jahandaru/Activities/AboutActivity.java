package net.jahanco.jahandaru.Activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.jahanco.jahandaru.Helpers.SendMailTask;
import net.jahanco.jahandaru.R;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {
    private AlertDialog emailDialog;
    private EditText edtName,edtEmail,edtMessage;
    private Button btnSend,btnDisMiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AppCompatImageView back= (AppCompatImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView instagram= (ImageView) findViewById(R.id.instagram);
        ImageView telegram= (ImageView) findViewById(R.id.telegram);
        ImageView web= (ImageView) findViewById(R.id.website);
        ImageView email= (ImageView) findViewById(R.id.email);

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://japp.pro/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.show();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/ejahanco");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/ejahanco")));
                }
            }

        });
        final LayoutInflater factory = LayoutInflater.from(this);
        View emailDialogView = factory.inflate(R.layout.email_dialog_layout, null);
        emailDialog= new AlertDialog.Builder(AboutActivity.this).create();
        emailDialog.setView(emailDialogView);
        edtName= (EditText) emailDialogView.findViewById(R.id.edtName);
        edtEmail = (EditText) emailDialogView.findViewById(R.id.edtEmail);
        edtMessage = (EditText) emailDialogView.findViewById(R.id.edtMessage);
        btnSend= (Button) emailDialogView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.dismiss();
                String fromEmail = "rainman.jalili@gmail.com";
                String fromPassword = "09144414719";
                String toEmails = "jahanco.net@gmail.com";
                List<String> toEmailList = new ArrayList<String>();
                toEmailList.add(toEmails);
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = "Email from pooya group app";
                String emailBody = "Name:" + edtName.getText() + "\n" + "Email:" + edtEmail.getText() + "\n" + "Message:" + edtMessage.getText();
                new SendMailTask(AboutActivity.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
            }
        });

        btnDisMiss = (Button) emailDialogView.findViewById(R.id.btnDisMiss);
        btnDisMiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.dismiss();
            }
        });
        emailDialog.setCancelable(false);
        emailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                    telegramIntent.setData(Uri.parse("https://t.me/ejahanco"));
                    startActivity(telegramIntent);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this,"تلگرام بروی گوشی شما نصب نمیباشد از طریق شماره تماس با ما در ارتباط باشید.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

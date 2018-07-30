package net.jahanco.jahandaru.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Constants;
import net.jahanco.jahandaru.Helpers.WebService;
import net.jahanco.jahandaru.R;
import net.jahanco.jahandaru.util.IabHelper;
import net.jahanco.jahandaru.util.IabResult;
import net.jahanco.jahandaru.util.Inventory;
import net.jahanco.jahandaru.util.Purchase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity implements IabHelper.OnIabSetupFinishedListener ,IabHelper.QueryInventoryFinishedListener{
    private String rsa="MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDc/tS1yDD+tpW+NskGRPVzmJTAD/I4Fo4ff800/YcstU4LaxWJtOVvOg3tQyKur/H4AgxYn6lQyCckWzm3AQSqCKBLoc9AbTSHOqXpt6UzwEufjZaD9aHRxz/5h/SzLujZzj0pvo69Yu6ATtsmHmUfaGx5sgyH6QhLjBkSifpWMvMs0PYZ0c85O6vSRp5vFcsjPEZzVJQKV9oNkbQ1dwF5cjvGoQV3fv2UZ5PjYc8CAwEAAQ==";
    private static final String APP_PRIMIUM_ACCOUNT="premium_account";
    private final int reqCode=1876;
    private IabHelper iabHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        checkAccount();
    }


    private void checkAccount() {
        iabHelper=new IabHelper(this,rsa);
        iabHelper.startSetup(this);
    }


    @Override
    public void onIabSetupFinished(IabResult result) {
        if(result.isSuccess()){
            List<String> products=new ArrayList<>();
            products.add(APP_PRIMIUM_ACCOUNT);
            iabHelper.queryInventoryAsync(true,products,this);
        }
        else{
            Toast.makeText(PurchaseActivity.this,"برای ارتقا نیاز به کافه بازار می باشد",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        if(result.isSuccess()){
            Purchase purchase=inv.getPurchase(APP_PRIMIUM_ACCOUNT);
            if(purchase!=null){
                changeStateToPrimium(purchase.getToken());
            }
            else{
                purchasePremiumAccount();
            }
        }
        else{
            purchasePremiumAccount();
            Log.i("iab", "onQueryInventoryFinished: error");
            App.prefManager.savePreference("premium","false");

        }
    }

    private void changeStateToPrimium(String token) {

        HashMap<String,String> params=new HashMap<>();
        params.put("token",token);
        params.put("email",token);
        params.put("IMEI",App.IMEI+"");
        final Intent intent=getIntent();
        App.webService.postRequest(params, Constants.netWorkUrl+"plants", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                intent.putExtra("result", "true");
//                App.prefManager.savePreference("premium_account","true");

                try {
                    JSONArray array=new JSONArray(message);
                    App.dbHelper.batch_update_Item(App.dbHelper.jsonArrayToItem(array.getString(0)));
                    App.prefManager.savePreference("premium","true");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onReceivedError(String message) {
                intent.putExtra("result", "false");
                Log.i("webservice", "onReceivedError: " + message);
                App.prefManager.savePreference("premium","false");

                setResult(RESULT_OK, intent);
                finish();
            }
        }
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( iabHelper!=null){
            iabHelper.dispose();
            iabHelper=null;
        }
    }


    private void purchasePremiumAccount(){
        iabHelper.launchPurchaseFlow(this, APP_PRIMIUM_ACCOUNT, reqCode, new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                if(result.isSuccess()){
                    if(info!=null){
                        changeStateToPrimium(info.getToken());
                    }
                }
                else if(result.isFailure()){
                    Toast.makeText(PurchaseActivity.this,"مشکل در پرداخت به وجود آمده است",Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==reqCode){
            iabHelper.handleActivityResult(requestCode,resultCode,data);
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


}

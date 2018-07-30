package net.jahanco.jahandaru;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.jahanco.jahandaru.Helpers.WebService;

import java.util.HashMap;

/**
 * Created by Mr R00t on 6/10/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private WebService.OnPostReceived onPostReceived;
    private void sendRegistrationToServer(final String token) {
        String oldToken=App.prefManager.getPreference("token");
        if(oldToken.equals(token)){
            return;
        }
        // TODO: Implement this method to send token to your app server.
        final HashMap<String,String> params=new HashMap<>();
        params.put("token",token);
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
                else if(message.equals("0:OK")){
                    App.prefManager.savePreference("token",token);
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

    }
}

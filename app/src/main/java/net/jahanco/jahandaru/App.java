package net.jahanco.jahandaru;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import net.jahanco.jahandaru.Helpers.DbHelper;
import net.jahanco.jahandaru.Helpers.EHelper;
import net.jahanco.jahandaru.Helpers.HelperDownloader;
import net.jahanco.jahandaru.Helpers.SharedPrefManager;
import net.jahanco.jahandaru.Helpers.WebService;
import net.jahanco.jahandaru.Models.ItemEntity;
import net.jahanco.jahandaru.Models.Sick;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.NoSuchPaddingException;


public class App
        extends Application {
    public static Handler handler = new Handler();

    private static final String TAG = "App";
    public static final String EXTERNAL_STORAGE_URI = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JahanDaru/";
    public static WebService webService = new WebService();
    public static AssetManager assetManager;
    public static Context context;
    public static DbHelper dbHelper;
    private static Typeface irsans;
    private static Typeface ojan;
    private static Typeface yekan;
    public static SharedPrefManager prefManager;

    public static String IMEI = "0";
    private static boolean flag = false;
    private static int counter = 1;

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        SQLiteDatabase.loadLibs(context);
        dbHelper = new DbHelper(context);
        assetManager = getAssets();
        prefManager = new SharedPrefManager("jd");
        ArrayList<ItemEntity> localArrayList = dbHelper.getItems();
        makeDirectories();
        if ((!prefManager.isSetPereference()) || (localArrayList == null)) {
            if (localArrayList == null) {
//        localArrayList = new ArrayList<>();
//        String localObject1 = getFileContent("title");
//        String localObject2 = getFileContent("body");
//        String[] titles = ((String)localObject1).split(",,,");
//        String[] bodies = ((String)localObject2).split(",,,");
//        for (int i=0;i<titles.length;i++){
//
//        }
//        dbHelper.batch_insert_message(localArrayList);


//        prefManager.savePreference("initialize", "true");
            }

        }

        yekan = Typeface.createFromAsset(assetManager, "fonts/yekan.ttf");
        ojan = Typeface.createFromAsset(assetManager, "fonts/ojan.ttf");
        irsans = Typeface.createFromAsset(assetManager, "fonts/irsans.ttf");
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = telephonyManager.getDeviceId();
        } catch (Exception e) {
            IMEI = "0";
        }
    }

    public static String excerpt(String paramString, int paramInt) {
        String[] arrayOfString = paramString.split("[\\s\\xA0]+", paramInt + 1);
        paramString = "";
        int i = 0;
        while (i < paramInt) {
            paramString = paramString + arrayOfString[i] + " ";
            i += 1;
        }
        return paramString;
    }

    public static String getFileContent(String paramString) {

        if (paramString.equals("title")) {
            try {
                return EHelper.decrypt(assetManager.open("fonts/roya.ttf"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return EHelper.decrypt(assetManager.open("fonts/tahoma.ttf"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Typeface getFontWithName(String paramString) {
        if (paramString.equals("ojan")) {
            return ojan;
        } else if (paramString.equals("yekan")) {
            return yekan;
        } else if (paramString.equals("irsans")) {
            return irsans;
        }
        return null;
    }

    public static BottomOffsetDecoration getRecyclerViewDecorationWithBottomOffset(int paramInt) {
        return new BottomOffsetDecoration(paramInt);
    }

    public static void getDataFromServer(final HelperDownloader.OnDownloadCompleteListener onDownloadCompleteListener) {
        webService.postRequest("IMEI", App.IMEI + "", Constants.netWorkUrl + "getPlants", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                ArrayList<ItemEntity> itemEntities = null;
                try {
                    JSONArray array=new JSONArray(message);
                    itemEntities = dbHelper.jsonArrayToItem(array.getString(0));
                    dbHelper.batch_insert_special_sickness_item(dbHelper.jsonArrayToSpecialSicknessItem(array.getString(1)));
                    dbHelper.batch_insert_Item(itemEntities);
                    final HelperDownloader helperDownloader = new HelperDownloader();
                    helperDownloader.outPutFileName = "images";
                    helperDownloader.outPutPath = App.EXTERNAL_STORAGE_URI + "/images/";
                    helperDownloader.downloadUrl = Constants.netWorkUrl + "getImages?id=1";
                    helperDownloader.onDownloadComplete = new HelperDownloader.OnDownloadCompleteListener() {
                        @Override
                        public void onDownloadComplete() {
                            helperDownloader.unpackZip(App.EXTERNAL_STORAGE_URI + "/images/", "images");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onDownloadCompleteListener.onDownloadComplete();
                                }
                            });
                            counter++;
                            downloadImages();

                        }
                    };
                    helperDownloader.execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onReceivedError(String message) {
                Log.e(TAG, "onReceivedError: " + message);
                onDownloadCompleteListener.onDownloadComplete();
            }
        });
        webService.postRequest("IMEI", App.IMEI + "", Constants.netWorkUrl + "getSickness", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                ArrayList<Sick> sicks = null;
                try {
                    sicks = dbHelper.jsonArrayToSicknessItem(message);
                    dbHelper.batch_insert_sickness_item(sicks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReceivedError(String message) {
                Log.e(TAG, "onReceivedError: " + message);
            }
        });

    }


    public static class BottomOffsetDecoration
            extends RecyclerView.ItemDecoration {
        private int mBottomOffset;

        public BottomOffsetDecoration(int paramInt) {
            this.mBottomOffset = paramInt;
        }

        public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
            super.getItemOffsets(paramRect, paramView, paramRecyclerView, paramState);
            int i = paramState.getItemCount();
            int j = paramRecyclerView.getChildPosition(paramView);
            if ((i > 0) && (j == i - 1)) {
                paramRect.set(0, 0, 0, this.mBottomOffset);
                return;
            }
            paramRect.set(0, 0, 0, 0);
        }
    }

    public static void makeDirectories() {
        new File(EXTERNAL_STORAGE_URI + "/images/catch").mkdirs();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static native int[] imageBlur();

    public static native int[] imageResize();

    public static native int[] imageScale();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("image_process");
    }
    public static void downloadImages(){
        final HelperDownloader helperDownloader = new HelperDownloader();
        helperDownloader.outPutFileName = "images";
        helperDownloader.outPutPath = App.EXTERNAL_STORAGE_URI + "/images/";
        helperDownloader.downloadUrl = Constants.netWorkUrl + "getImages?id=" + counter;
        helperDownloader.onDownloadComplete = new HelperDownloader.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete() {
                helperDownloader.unpackZip(App.EXTERNAL_STORAGE_URI + "/images/", "images");
                if (counter < 7) {
                    counter++;
                    downloadImages();
                }

            }
        };
        helperDownloader.execute();

    }

}


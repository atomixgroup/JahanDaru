package net.jahanco.jahandaru.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import net.jahanco.jahandaru.App;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class HelperDownloader extends AsyncTask<String, String, String> {

    public String downloadUrl = null;
    public String outPutPath=null;
    public String outPutFileName=null;
    public OnDownloadCompleteListener onDownloadComplete;

    @Override
    protected String doInBackground(String... params) {
        App.makeDirectories();
        try {

            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if(outPutFileName==null){
                int pos = downloadUrl.lastIndexOf("/") + 1;
                outPutFileName = downloadUrl.substring(pos);
            }

            File outputFileCache;
            if(outPutPath==null){
                outputFileCache= new File(App.EXTERNAL_STORAGE_URI+"/catch/",outPutFileName);
            }
            else{
                outputFileCache = new File(outPutPath + "/catch/",outPutFileName);
            }

            if (outputFileCache.exists()) {
                connection.setAllowUserInteraction(true);
                connection.setRequestProperty("Range", "bytes=" + outputFileCache.length() + "-");
            }

            connection.setConnectTimeout(14000);
            connection.setReadTimeout(20000);
            connection.connect();
            Long downloadedSize = new Long(0);
            int code=connection.getResponseCode();
            if (code / 100 != 2 && code!=416)
                throw new Exception("Invalid response code!");
            else if(code==416){
                throw new Exception("File is complete!");
            }
            else {
                String connectionField = connection.getHeaderField("content-range");

                if (connectionField != null) {
                    String[] connectionRanges = connectionField.substring("bytes=".length()).split("-");
                    downloadedSize = Long.valueOf(connectionRanges[0]);
                }

                if (connectionField == null && outputFileCache.exists())
                    outputFileCache.delete();

                long fileLength = connection.getContentLength() + downloadedSize;
                InputStream input = new BufferedInputStream(connection.getInputStream());
                RandomAccessFile output = new RandomAccessFile(outputFileCache, "rw");
                output.seek(downloadedSize);

                byte data[] = new byte[1024];
                int count = 0;


                while ((count = input.read(data, 0, 1024)) != -1) {
                    downloadedSize += count;
                    output.write(data, 0, count);
                    if(isCancelled()){
                        output.close();
                        input.close();
                        return outputFileCache.getAbsolutePath();
                    }
                }
                output.close();
                input.close();
                moveFile(outputFileCache.getParent(),outputFileCache.getName(), outPutPath);

            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return outPutFileName;
    }



    @Override
    protected void onPostExecute(String s) {
        if(onDownloadComplete!=null){
            onDownloadComplete.onDownloadComplete();
        }else {
            Log.i("DownloaderListener","Post Listener is not found");
        }
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath+"/" +inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath+"/"+ inputFile).delete();
        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void deleteFile(String filePath) {
        try {
            // delete the original file
            new File(filePath).delete();
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }


    public interface OnDownloadCompleteListener{
        public void onDownloadComplete();
    }
    public boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while((ze = zis.getNextEntry()) != null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String filename = ze.getName();
                FileOutputStream fout = new FileOutputStream(path + filename);

                // reading and writing
                while((count = zis.read(buffer)) != -1)
                {
                    baos.write(buffer, 0, count);
                    byte[] bytes = baos.toByteArray();
                    fout.write(bytes);
                    baos.reset();
                }

                fout.close();
                zis.closeEntry();
                File from = new File(path,filename);
                File to = new File(path,filename.replace(".jpg",""));
                from.renameTo(to);
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        deleteFile(path+zipname);
        return true;
    }
}

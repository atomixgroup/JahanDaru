package net.jahanco.jahandaru.Helpers;

import java.util.ArrayList;

/**
 * Created by Mr R00t on 5/29/2017.
 */

public class DownloadQueue {
    private ArrayList<String[]> urls=new ArrayList<>();
    private int i=0;
    public void put(String url,String outPutFileName){
        String[] file=new String[2];
        file[0]=url;
        file[1]=outPutFileName;
        urls.add(file);
    }
    public String[] pop(){
        if(urls.size()>i) {
            String[] temp = urls.get(i);
            i++;
            return temp;
        }
        else{
            return null;
        }
    }
    public int getSize(){
        return urls.size();
    }
}

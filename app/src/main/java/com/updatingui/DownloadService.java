package com.updatingui;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Sunil on 7/28/2017.
 */

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String url=intent.getStringExtra("URL");
        ResultReceiver receiver=(ResultReceiver) intent.getParcelableExtra("receiver");
        try{
            URL finalurl=new URL(url);
            URLConnection urlConnection=finalurl.openConnection();
            urlConnection.connect();

            int filelength = urlConnection.getContentLength();

            InputStream input=new BufferedInputStream(urlConnection.getInputStream());
            OutputStream outputStream=new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PatientMylab");

            byte data[]=new byte[1024];
            int total=0;
            int count;
            while((count = input.read(data)) != -1){
                total+=count;
                //publishing result
                Bundle bundle=new Bundle();
                bundle.putInt("progress",(total * 100 / filelength));
                receiver.send(UPDATE_PROGRESS,bundle);
                outputStream.write(data,0,count);
            }
            outputStream.flush();
            outputStream.close();
            input.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        Bundle bundle=new Bundle();
        bundle.putInt("progress",100);
        receiver.send(UPDATE_PROGRESS,bundle);
    }
}

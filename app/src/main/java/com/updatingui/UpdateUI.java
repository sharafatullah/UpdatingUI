package com.updatingui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class UpdateUI extends AppCompatActivity {
    ProgressDialog progressDialog;
    final String URL="https://anydesk.com/download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ui);

        progressDialog=new ProgressDialog(UpdateUI.this);

        (findViewById(R.id.btn_download)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                Intent intent=new Intent(UpdateUI.this,DownloadService.class);
                intent.putExtra("URL",URL);
                intent.putExtra("receiver",new DownloadReceiver(new Handler()));
                startService(intent);
            }
        });
    }

    public class DownloadReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == DownloadService.UPDATE_PROGRESS){
                int progress=resultData.getInt("progress");
                Log.e("update",String.valueOf(progress));
                progressDialog.setProgress(progress);
                if(progress == 100){
                    progressDialog.dismiss();
                }
            }
        }
    }
}

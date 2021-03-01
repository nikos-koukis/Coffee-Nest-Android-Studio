package com.example.coffeenest.Listener;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.example.coffeenest.R;
import com.example.coffeenest.Utility.Connection;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //!Connection is from file Utility - Class Connection
        //When Internet is not connected
        if (!Connection.isConnectedToInternet(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.internet_connection_dialog, null);
            builder.setView(layout_dialog);

            //Hook Elements From xml File - internet_connection.xml
            Button buttonRetry = layout_dialog.findViewById(R.id.btnRetry);

            //Show Dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            //Button Retry Listener
            //When Retry Button pressed, then dialog dismiss
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });

        }

    }
}

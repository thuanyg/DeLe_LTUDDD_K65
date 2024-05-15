package com.android.todos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            String message = !isAirplaneModeOn ? "Airplane mode vừa Bật" : "Airplane mode vừa Tắt";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}

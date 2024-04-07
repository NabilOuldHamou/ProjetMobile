package fr.devmobile.projetmobile.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class TimeTickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TimeTickReceiver", "Time has changed");
        Toast.makeText(context, "Time has changed", Toast.LENGTH_SHORT).show();
    }
}

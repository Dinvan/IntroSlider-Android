package org.snowcorp.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.notifications.clear();
        Log.e("onReceive","onReceive");
    }

}

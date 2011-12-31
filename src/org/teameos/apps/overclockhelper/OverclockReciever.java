package org.teameos.apps.overclockhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OverclockReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean execute = false;
        try {
            Process p = Runtime.getRuntime().exec("getprop eos.overclocking.failed");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String input;
            while ((input = reader.readLine()) != null) {
                if (input.contains("1"))
                    execute = true;
            }
        } catch (IOException e) {
            return;
        }

        if (execute) {

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            long when = System.currentTimeMillis();

            Notification notification = new Notification(R.drawable.ic_dialog_alert,
                    "Overclocking Not Applied", when);
            CharSequence contentTitle = "Eos Overclocking";
            CharSequence contentText = "Values have not been applied due to recent reboot.";
            // Intent notificationIntent = new Intent(this,
            // OverclockReciever.class);

            Intent intent1 = new Intent("android.settings.eos.performance_settings");
            intent1.addCategory(Intent.CATEGORY_DEFAULT);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
            mNotificationManager.notify(0, notification);

            Log.i("eos", "there should be a notification now");
        }
    }

}

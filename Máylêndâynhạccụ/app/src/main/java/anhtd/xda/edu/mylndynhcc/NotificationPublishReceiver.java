package anhtd.xda.edu.mylndynhcc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class NotificationPublishReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_TITLE = "Stay in tune!";
    public static final String NOTIFICATION_TEXT = "Lâu rồi bạn chưa lên dây cho đàn phải không?.";
    public static final int REQUEST_CODE = 0;
    public static final int ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(NOTIFICATION_TITLE);
        builder.setContentText(NOTIFICATION_TEXT);
        builder.setSmallIcon(R.mipmap.icon_app);
        builder.setAutoCancel(true);
        Intent i = new Intent(context, TunerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, i, 0);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(ID, builder.build());
    }

}

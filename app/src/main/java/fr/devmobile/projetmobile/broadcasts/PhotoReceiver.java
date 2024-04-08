package fr.devmobile.projetmobile.broadcasts;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.LoginActivity;
import fr.devmobile.projetmobile.activities.MainActivity;


public class PhotoReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "CameraButtonChannel";

    private final String TITLE = "NOUVELLE PHOTO";

    private final String TEXT = "Publiez la nouvelle photo ?";

    @Override
    public void onReceive(Context context, Intent intent) {
        makeNotification(context);
        Log.i("PhotoReceiver", "Camera button pressed");
    }

    private void makeNotification(Context context) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(TITLE)
                .setContentText(TEXT)
                .setColor(context.getResources().getColor(R.color.md_theme_dark_background))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("NewPhotoTakedNotification", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, TITLE, importance);
            channel.setDescription(TEXT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }

}

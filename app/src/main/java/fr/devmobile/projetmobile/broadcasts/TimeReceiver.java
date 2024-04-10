package fr.devmobile.projetmobile.broadcasts;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.MainActivity;

public class TimeReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "TimeTickChannel";

    private final String TITLE = "Publiez une photo ?";

    private final String TEXT = "Il est temps de poster une nouvelle photo !";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TimeReceiver", "TimeReceiver triggered");
        Calendar currentTime = Calendar.getInstance();

        // Vérifier si l'heure actuelle est égale à l'heure suivante
        if (currentTime.get(Calendar.MINUTE) == 0 && currentTime.get(Calendar.SECOND) == 0) {
            Log.i("TimeReceiver", "It's time to take a photo");
            makeNotification(context);
        }
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

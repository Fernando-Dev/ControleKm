package com.example.fernando.controlekm.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.example.fernando.controlekm.CadastrarKm;
import com.example.fernando.controlekm.R;
import com.example.fernando.controlekm.Utilitario;

public class AlarmReceiverTrocaOleo extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
            CadastrarKm.enableBootReceiver(context);
            Intent notificationIntent = new Intent(context, Utilitario.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Utilitario.class);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(CadastrarKm.ALARM_TYPE,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Notification notification = builder.setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Você precisa fazer a troca do óleo da sua moto!")
                    .setTicker(context.getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.new_ic_controlekm)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Você precisa fazer a troca do óleo da sua moto!"))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent).build();
            notification.defaults |= notification.DEFAULT_SOUND;
            notification.defaults |= notification.DEFAULT_VIBRATE;
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(CadastrarKm.ALARM_TYPE, notification);
        }
}

package br.fernando.controlekm.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import br.fernando.controlekm.CadastrarKm;
import br.fernando.controlekm.R;
import br.fernando.controlekm.Utilitario;

public class AlarmReceiverManutencao extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private Notification notification;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra =intent.getExtras();
        int alarmId = extra.getInt("ALARM_MANUTENCAO");
        if (alarmId == 0) {
            CadastrarKm.enableBootReceiver(context);
            Intent notificationIntent = new Intent(context, Utilitario.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Utilitario.class);
            stackBuilder.addNextIntent(notificationIntent);
            pendingIntent = stackBuilder.getPendingIntent(CadastrarKm.ALARM_MANUTENCAO,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            notification = builder.setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Você precisa fazer a manutenção da sua moto!")
                    .setTicker(context.getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.new_ic_controlekm)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Você precisa fazer a manutenção da sua moto!"))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent).build();
            notification.defaults |= notification.DEFAULT_SOUND;
            notification.defaults |= notification.DEFAULT_VIBRATE;
            notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(CadastrarKm.ALARM_MANUTENCAO, notification);
        } else {
            notificationManager.cancel(CadastrarKm.ALARM_MANUTENCAO);
        }
    }

}

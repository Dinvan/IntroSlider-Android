package org.snowcorp.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //https://raw.githubusercontent.com/facebook/fresco/master/proguard-fresco.pro
    Button btnGenNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGenNotification = (Button) findViewById(R.id.btnGenNotification);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        MediaStore.Images.Thumbnails thumbnails = new MediaStore.Images.Thumbnails();

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (anim != null) {
                    // app-specific logic to enable animation starting
                    anim.start();
                }
            }
        };

        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setUri("http://i.imgur.com/jLBIIZg.gif")
                        .setAutoPlayAnimations(false)
                        .setControllerListener(controllerListener)
                        .build();
        draweeView.setController(controller);


        Animatable animatable = draweeView.getController().getAnimatable();
        if (animatable != null) {
            animatable.start();
            // later
            //   animatable.stop();
        }


        btnGenNotification.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onClick(View view) {

                generateNotification(MainActivity.this,"Message "+notifications.size());
            }
        });
    }


     static ArrayList<String> notifications = new ArrayList<>();
    public   void generateNotification(Context context, String messageBody) {
        Intent deleteIntent = new Intent(ACTION_NOTIFICATION_DELETE);
        PendingIntent   mDeletePendingIntent =  PendingIntent.getBroadcast(context, 0,deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        startWFApplication().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.ic_ab_attach)
                .setContentTitle("Title")
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Title - Notification");
        notifications.add(messageBody);
        inboxStyle.setSummaryText("You have "+notifications.size()+" Notifications.");
        // Moves events into the expanded layout

        for (int i=0; i < notifications.size(); i++) {
            if(i<7){
                inboxStyle.addLine(notifications.get(i));
            }else{
                break;
            }

        }
        // Moves the expanded layout object into the notification object.
        notificationBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final StatusBarNotification[] activeNotifications;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activeNotifications = notificationManager.getActiveNotifications();
            final int numberOfNotifications = activeNotifications.length;
            inboxStyle.setSummaryText("You have "+numberOfNotifications+" Notifications.");
            notificationBuilder.setStyle(inboxStyle);
        }

        notificationBuilder.setDeleteIntent(mDeletePendingIntent);
        notificationManager.notify(0, notificationBuilder.build());

    }
    public static Intent startWFApplication(){
        Intent launchIntent = new Intent();
        launchIntent.setComponent(new ComponentName("your.package", "Yyour.package.servicename"));
        return launchIntent;
    }


    protected static final String ACTION_NOTIFICATION_DELETE
            = "com.example.android.activenotifications.delete";

    private BroadcastReceiver mDeleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notifications.clear();
            Log.e("onReceive","onReceive");
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mDeleteReceiver, new IntentFilter(ACTION_NOTIFICATION_DELETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mDeleteReceiver);
    }
}

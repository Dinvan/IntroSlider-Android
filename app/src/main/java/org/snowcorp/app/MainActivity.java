package org.snowcorp.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

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
                genNotification();
            }
        });
    }

    int i = 0;
    Notification notification;
    Notification.Builder builder;

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)


    public void genNotification() {
        i++;
        Intent resultIntent = new Intent(MainActivity.this, VideoPlayer.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent piResult = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_ONE_SHOT);

            builder = new Notification.Builder(MainActivity.this);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("contentTitle")
                    .setContentText("contentText")
                    .setContentIntent(piResult)
                    .setGroup("din");
        notification = new Notification.InboxStyle(builder)
                .addLine("Line Number " + i)
                .setBigContentTitle("New Message")
                .setSummaryText("Message " + i)
                .build();


      showNotification(MainActivity.this,notification,1);
    }

    private void showNotification(Context context, Notification notification, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification);
    }


}

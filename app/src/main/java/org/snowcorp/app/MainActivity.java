package org.snowcorp.app;
import android.graphics.drawable.Animatable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

public class MainActivity extends AppCompatActivity {
//https://raw.githubusercontent.com/facebook/fresco/master/proguard-fresco.pro
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        MediaStore.Images.Thumbnails thumbnails=new MediaStore.Images.Thumbnails();

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

    }
}

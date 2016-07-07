package com.just.learnintentservice;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

public class MainActivity extends AppCompatActivity {
    private static ImageView image;
    private Button playBtn;
    private RevImageThread revImageThread;
    private static Bitmap bitmap;
    private static final int COMPLETED = 0x111;
    private MyHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView)findViewById(R.id.video_iv);
        playBtn = (Button)findViewById(R.id.button);

        handler = new MyHandler();

    }

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == COMPLETED) {
                bitmap = (Bitmap)msg.obj;
                image.setImageBitmap(bitmap);
                super.handleMessage(msg);
            }
        }
    }

    public void startTrans(View view) {
        revImageThread = new RevImageThread(handler);
        new Thread(revImageThread).start();

        SuperActivityToast.create(MainActivity.this, "正在等待连接．．．．．．^_^",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }
}

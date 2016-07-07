package com.just.learnintentservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 视频接收线程.
 */
public class RevImageThread implements Runnable {
    private Socket socket;
    private ServerSocket serverSocket;

    //向UI线程发送消息
    private Handler handler;

    private Bitmap bitmap;
    private static final int COMPLETED = 0x111;

    public RevImageThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        byte [] buffer = new byte[1024];
        int len = 0;

        try {
            serverSocket = new ServerSocket(8899);
        } catch (IOException e) {
            e.printStackTrace();
        }



        while (true){
            try {
//                Log.d("Jay","等待连接");

                socket = serverSocket.accept();
//                Log.d("Jay","已链接");

                //初始化输入流
                InputStream ins = socket.getInputStream();

                //初始化输出流
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();

                //读取输入流的数据写在输出流上
                while( (len=ins.read(buffer)) != -1){
                    outStream.write(buffer, 0, len);
                }

                //关闭输入流
                ins.close();

                //将输出流转化为数组
                byte data[] = outStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //调用Handler发送数据
                Message msg =handler.obtainMessage();
                msg.what = COMPLETED;
                msg.obj = bitmap;
                handler.sendMessage(msg);


                outStream.flush();

                //关闭流
                outStream.close();
                if(!socket.isClosed()){
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

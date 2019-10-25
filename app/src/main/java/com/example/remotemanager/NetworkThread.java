package com.example.remotemanager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkThread extends HandlerThread {

    private Handler mHandler;
    private Socket socket;
    private OutputStream out;
    private ObjectOutputStream oos;
    private InputStream in;
    private ObjectInputStream ois;
    private Handler uiHandler;

    @Override
    public boolean quit() {
            try {
                if (oos != null) oos.close();
                if (out != null) out.close();
                if (ois != null) ois.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return super.quit();
    }

    private static final String HOST = "192.168.1.2";
    private static final int PORT = 9876;

    public NetworkThread(String name, Handler uiHandler) {
        super(name);
        this.uiHandler = uiHandler;
    }

    public void setConnection()  {
        if (mHandler != null) {
            mHandler.post(new Connection());
        }
    }

    private void sendCommand(Command command) {
        if (oos != null) {
            try {
                oos.writeObject(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareHandler(){
        mHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Command command = (Command) msg.obj;
                sendCommand(command);
                return true;
            }
        });
    }

    public void send(Command command) {
        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            message.obj = command;
            mHandler.sendMessage(message);
        }
    }

    private class Connection implements Runnable{
        @Override
        public void run() {
            try {
                socket = new Socket(HOST, PORT);
                out = socket.getOutputStream();
                oos = new ObjectOutputStream(out);
                in = socket.getInputStream();
                ois = new ObjectInputStream(in);

                double currentVolumeLevel = ois.readDouble();
                Message message = uiHandler.obtainMessage();
                message.obj = currentVolumeLevel;
                uiHandler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

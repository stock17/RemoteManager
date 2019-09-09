package com.example.remotemanager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkThread extends HandlerThread {

    private Handler mHandler;
    private Socket socket;
    private OutputStream out;
    private ObjectOutputStream oout;


    private static final String HOST = "192.168.1.2";
    private static final int PORT = 9876;

    public NetworkThread(String name) {
        super(name);
    }

    public void setConnection() throws IOException {
        socket = new Socket(HOST, PORT);
        out = socket.getOutputStream();
        oout = new ObjectOutputStream(out);
    }

    private void sendCommand(Command command) {
        if (oout != null) {
            try {
                oout.writeObject(command);
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



}

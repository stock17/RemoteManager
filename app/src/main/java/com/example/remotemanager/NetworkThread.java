package com.example.remotemanager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class NetworkThread extends HandlerThread {

    private Handler mHandler;

    public NetworkThread(String name) {
        super(name);
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


    private void sendCommand(Command command) {
        //TODO
    }
}

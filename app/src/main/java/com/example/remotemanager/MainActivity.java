package com.example.remotemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ncorti.slidetoact.SlideToActView;
import it.beppi.knoblibrary.Knob;

public class MainActivity extends AppCompatActivity {

    NetworkThread networkThread;
    Handler uiHandler;

    @Override
    protected void onDestroy() {
        networkThread.quit();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SlideToActView sta = (SlideToActView) findViewById(R.id.slide_view);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                networkThread.send(new Command(Command.Type.SLEEP, 0));
                sta.resetSlider();
            }
        });


        final Knob knob = (Knob) findViewById(R.id.knob);
        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                double level = (double) state / 100;
                networkThread.send(new Command(Command.Type.VOLUME_LEVEL, level));
            }
        });

        uiHandler = new Handler(getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int state = (int) ((double) msg.obj * 100);
                knob.setState(state);
                return true;
            }
        });

        networkThread = new NetworkThread("networkThread", uiHandler);
        networkThread.start();
        networkThread.prepareHandler();
        networkThread.setConnection();
    }
}


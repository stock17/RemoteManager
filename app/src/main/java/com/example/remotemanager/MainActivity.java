package com.example.remotemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ncorti.slidetoact.SlideToActView;
import com.yurima.remoteutils.Command;

import it.beppi.knoblibrary.Knob;

public class MainActivity extends AppCompatActivity {

    private final int LEFT_BUTTON_CODE = 0x25;
    private final int RIGHT_BUTTON_CODE = 0x27;
    private final int SPACE_BUTTON_CODE = 0x20;

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

        Button spaceButton = (Button) findViewById(R.id.space_button);
        spaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkThread.send(new Command(Command.Type.PRESS_KEY, SPACE_BUTTON_CODE));
            }
        });

        Button leftButton = (Button) findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkThread.send(new Command(Command.Type.PRESS_KEY, LEFT_BUTTON_CODE));
            }
        });

        Button rightButton = (Button) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkThread.send(new Command(Command.Type.PRESS_KEY, RIGHT_BUTTON_CODE));
            }
        });


        buttonEffect(leftButton);
        buttonEffect(rightButton);
        buttonEffect(spaceButton);


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

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe000000,PorterDuff.Mode.DARKEN);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}


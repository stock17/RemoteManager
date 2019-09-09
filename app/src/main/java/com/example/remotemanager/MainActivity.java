package com.example.remotemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import it.beppi.knoblibrary.Knob;

public class MainActivity extends AppCompatActivity {

    NetworkThread networkThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkThread = new NetworkThread("networkThread");
        networkThread.start();
        networkThread.prepareHandler();

        Knob knob = (Knob) findViewById(R.id.knob);
//        knob.setState(firstState);
        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                double level = (double) state / 100;
                networkThread.send(new Command(Command.Type.VOLUME_LEVEL, level));
            }
        });
    }
}

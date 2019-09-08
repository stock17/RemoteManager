package com.example.remotemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import it.beppi.knoblibrary.Knob;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Knob knob = (Knob) findViewById(R.id.knob);
//        knob.setState(firstState);
        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
            }
        });
    }
}

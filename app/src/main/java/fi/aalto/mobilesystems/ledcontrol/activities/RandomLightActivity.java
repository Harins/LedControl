package fi.aalto.mobilesystems.ledcontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import fi.aalto.mobilesystems.ledcontrol.R;


public class RandomLightActivity extends AppCompatActivity {
    private PHHueSDK sdk;
    private static final int MAX_HUE = 65535;

    int DELAY;
    CheckBox ShotCheckBox;
    Button StartBtn, CancelBtn;
    EditText EtTime;
    Timer timer;
    DiskoLight task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disko_light);
        ShotCheckBox = (CheckBox) findViewById(R.id.shot);
        EtTime = (EditText) findViewById(R.id.edt_time);
        StartBtn = (Button) findViewById(R.id.start);
        CancelBtn = (Button) findViewById(R.id.cancel);
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (timer != null) {
                    timer.cancel();
                }
                String mEditText = EtTime.getText().toString();
                if(mEditText.matches("[0-9]+")) {
                    DELAY = Integer.parseInt(EtTime.getText().toString());
                } else {
                    DELAY = 1000;
                }


                timer = new Timer();
                task = new DiskoLight();

                if (ShotCheckBox.isChecked()){
                    //single call
                    timer.schedule(task, DELAY);
                } else {
                    //delay 1000ms
                    timer.schedule(task, DELAY, DELAY);
                    Toast.makeText(RandomLightActivity.this, "task repeated with delay " + DELAY, Toast.LENGTH_LONG).show();
                }
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        });

    }

    class DiskoLight extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    randomLights();
                }
            });
        }

    }

    public void randomLights() {
        this.sdk = PHHueSDK.getInstance();
        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        Random rand = new Random();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            this.sdk.getSelectedBridge().updateLightState(light, lightState);
        }
    }
}


package hkust.summercamp.airshipcontrol;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.lang.Thread.UncaughtExceptionHandler;

public class MainActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener, OnSeekBarChangeListener {
    final int MAX_DUTY_CYCLE;
    final int MIN_DUTY_CYCLE;
    private UncaughtExceptionHandler _unCaughtExceptionHandler;
    Controller controller;
    int currentMotorIndex;
    private UncaughtExceptionHandler defaultUEH;
    ImageButton mbtnDown;
    ImageButton mbtnLeft;
    ImageButton mbtnRight;
    ImageButton mbtnStop;
    ImageButton mbtnUp;
    int[] motorSpeed;
    ArrayAdapter<String> motorsAdapter;
    SeekBar mseekBarMotorSpeed;
    SeekBar mseekBarMotorSpeed1;
    SeekBar mseekBarMotorSpeed2;
    Spinner mspMotor;
    TextView mspeedMax;
    TextView mspeedMid;
    TextView mspeedMin;
    Switch mswConn;
    TextView mtxtStatus;
    int[] seekBarPos;

    /* renamed from: hkust.summercamp.airshipcontrol.MainActivity.1 */
    class C00561 implements UncaughtExceptionHandler {
        C00561() {
        }

        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            MainActivity.this.controller.MotorStart(0, 0);
            MainActivity.this.controller.MotorStart(1, 0);
            MainActivity.this.controller.MotorStart(2, 0);
            MainActivity.this.controller.MotorStart(3, 0);
            MainActivity.this.controller.MotorStart(4, 0);
        }
    }

    public MainActivity() {
        this.MIN_DUTY_CYCLE = 0;
        this.MAX_DUTY_CYCLE = MotionEventCompat.ACTION_MASK;
        this.seekBarPos = new int[]{MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK, MotionEventCompat.ACTION_MASK};
        this.motorSpeed = new int[6];
        this._unCaughtExceptionHandler = new C00561();
    }

    private void AppInitialization() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this._unCaughtExceptionHandler);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0057R.layout.activity_main);
        this.controller = new Controller(this);
        if (this.controller.AutoStartBT()) {
            this.mtxtStatus = (TextView) findViewById(C0057R.id.txtStatus);
            this.mbtnDown = (ImageButton) findViewById(C0057R.id.btnDown);
            this.mbtnDown.setOnClickListener(this);
            this.mbtnDown.setEnabled(false);
            this.mbtnStop = (ImageButton) findViewById(C0057R.id.btnStop);
            this.mbtnStop.setOnClickListener(this);
            this.mbtnStop.setEnabled(false);
            this.mswConn = (Switch) findViewById(C0057R.id.swConn);
            this.mswConn.setOnCheckedChangeListener(this);
            this.mseekBarMotorSpeed = (SeekBar) findViewById(C0057R.id.seekbarMotorSpeed);
            this.mseekBarMotorSpeed.setEnabled(true);
            this.mseekBarMotorSpeed.setMax(510);
            this.mseekBarMotorSpeed.setProgress(MotionEventCompat.ACTION_MASK);
            this.mseekBarMotorSpeed.setOnSeekBarChangeListener(this);
            this.mseekBarMotorSpeed1 = (SeekBar) findViewById(C0057R.id.seekBar1);
            this.mseekBarMotorSpeed1.setEnabled(true);
            this.mseekBarMotorSpeed1.setMax(510);
            this.mseekBarMotorSpeed1.setProgress(MotionEventCompat.ACTION_MASK);
            this.mseekBarMotorSpeed1.setOnSeekBarChangeListener(this);
            this.mseekBarMotorSpeed2 = (SeekBar) findViewById(C0057R.id.seekBar2);
            this.mseekBarMotorSpeed2.setEnabled(true);
            this.mseekBarMotorSpeed2.setMax(510);
            this.mseekBarMotorSpeed2.setProgress(MotionEventCompat.ACTION_MASK);
            this.mseekBarMotorSpeed2.setOnSeekBarChangeListener(this);
            Integer i = Integer.valueOf(MotionEventCompat.ACTION_MASK);
            this.mspeedMin.setText("-" + i.toString());
            this.mspeedMid.setText("0");
            this.mspeedMax.setText(i.toString());
            AppInitialization();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0057R.id.btnStop:
                for (int i = 0; i < 3; i++) {
                    this.controller.MotorStart(i, 0);
                }
            case C0057R.id.btnDown:
                this.controller.MotorStart(1, 0);
                this.controller.MotorStart(2, 0);
            default:
        }
    }

    public void onCheckedChanged(CompoundButton btn, boolean set) {
        if (btn == this.mswConn && set) {
            new AsyncTask<Void, Void, Void>() {
                protected void onPreExecute() {
                    MainActivity.this.controller.Connect();
                }

                protected void onPostExecute(Void result) {
                    MainActivity.this.mbtnUp.setEnabled(true);
                    MainActivity.this.mbtnDown.setEnabled(true);
                    MainActivity.this.mbtnLeft.setEnabled(true);
                    MainActivity.this.mbtnRight.setEnabled(true);
                    MainActivity.this.mbtnStop.setEnabled(true);
                    MainActivity.this.mseekBarMotorSpeed.setEnabled(true);
                    MainActivity.this.mseekBarMotorSpeed1.setEnabled(true);
                    MainActivity.this.mseekBarMotorSpeed2.setEnabled(true);
                }

                protected Void doInBackground(Void... params) {
                    do {
                    } while (!MainActivity.this.controller.IsConnected());
                    return null;
                }
            }.execute(new Void[0]);
            this.mtxtStatus.setText("Bluetooth Turned On!");
        } else if (btn == this.mswConn && !set) {
            new AsyncTask<Void, Void, Void>() {
                protected void onPreExecute() {
                    MainActivity.this.controller.Disconnect();
                }

                protected void onPostExecute(Void result) {
                    MainActivity.this.mbtnUp.setEnabled(false);
                    MainActivity.this.mbtnDown.setEnabled(false);
                    MainActivity.this.mbtnLeft.setEnabled(false);
                    MainActivity.this.mbtnRight.setEnabled(false);
                    MainActivity.this.mbtnStop.setEnabled(false);
                }

                protected Void doInBackground(Void... params) {
                    do {
                    } while (MainActivity.this.controller.IsConnected());
                    return null;
                }
            }.execute(new Void[0]);
            this.mtxtStatus.setText("Bluetooth Turned Off!");
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        this.currentMotorIndex = position;
        this.mtxtStatus.setText("Motor " + (this.currentMotorIndex + 1) + " selected");
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == C0057R.id.seekbarMotorSpeed) {
            this.controller.MotorStart(0, progress);
        } else if (seekBar.getId() == C0057R.id.seekBar1) {
            this.controller.MotorStart(1, progress);
        } else if (seekBar.getId() == C0057R.id.seekBar2) {
            this.controller.MotorStart(2, progress);
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

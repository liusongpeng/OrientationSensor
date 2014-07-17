package com.kouno.orientationsensor;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private TextView pitchValue;
	private TextView rollValue;
	private SensorManager sManager;
	private Sensor accelSensor;
	private Sensor magneticSensor;
	private float[] accel;
	private float[] magnetic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pitchValue = (TextView)findViewById(R.id.pitchValue);
		rollValue = (TextView)findViewById(R.id.rollValue);
		sManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		//Sensorオブジェクトの初期化
		List<Sensor> list;
		list = sManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(list.size() > 0) accelSensor = list.get(0);
		list = sManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		if(list.size() > 0) magneticSensor = list.get(0);
		
		accel = new float[3];
		magnetic = new float[3];
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//リスナの登録
		sManager.registerListener(seListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sManager.registerListener(seListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		sManager.unregisterListener(seListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private SensorEventListener seListener = new SensorEventListener() {
		private long lastComputedTime = 0;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				accel[0] = event.values[0];
				accel[1] = event.values[1];
				accel[2] = event.values[2];
			}else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
				magnetic[0] = event.values[0];
				magnetic[1] = event.values[1];
				magnetic[2] = event.values[2];
			}
			
			long tempTime = System.currentTimeMillis();
			if(tempTime - lastComputedTime > 1000){
				float[] orientationValues = ComputeOrientation.computeOrientation(accel, magnetic);
				pitchValue.setText(Double.toString((360 * orientationValues[1]) / (2 * Math.PI)));
				rollValue.setText(Double.toString((360 * orientationValues[2]) / (2 * Math.PI)));
				lastComputedTime = tempTime;
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
	};
}

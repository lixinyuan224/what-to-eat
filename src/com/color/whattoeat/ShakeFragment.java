package com.color.whattoeat;

import java.util.Random;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ShakeFragment extends Fragment {
	@ViewInject(R.id.food_tv)
	private TextView tv;
	
	private Vibrator mVibrator;
	private SensorManager mManager = null;
	private Sensor mSensor = null;
	private SensorEventListener mListener = null;
	public boolean flag=true;
	public int count=0;
	public int mcount=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.shake_layout, null);
		ViewUtils.inject(this, v);
		mVibrator=(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		mManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		mSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 
		mListener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
			 
			public void onSensorChanged(SensorEvent event) {
				if(!flag){
					return;
				}
				
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
//				LogUtils.i("x->"+x+"  y->"+y+"  z->"+z);
				if(MainActivity.foodlist.size()>0){
					if(x>18||y>18||z>19){
						mcount=0;
						if(count>10){
							count=0;
							mVibrator.vibrate(50);
							Random r=new Random(System.currentTimeMillis());
							int i=r.nextInt(MainActivity.foodlist.size());
							tv.setText(MainActivity.foodlist.get(i).getName());
							mManager.unregisterListener(mListener);
						}else{
							count++;
						}
					}else{
						mcount++;
						if(mcount>25){
							count=0;
						}
					}
				}
			}
		};
		mManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
		return v;
	}
	
	@OnClick(R.id.food_tv)
	public void clean(View v){
		tv.setText("");
		mManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onDestroy() {
		mManager.unregisterListener(mListener);
		super.onDestroy();
	}
	
}

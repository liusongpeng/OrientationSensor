package com.kouno.orientationsensor;

import android.hardware.SensorManager;

public class ComputeOrientation {
	public static float[] computeOrientation(float[] accel, float[] magnetic){
		float[] inR = new float[16];
		float[] I = new float[16];
		float[] outR = new float[16];
		float[] values = new float[3];

		SensorManager.getRotationMatrix(inR, I, accel, magnetic);
		SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
		SensorManager.getOrientation(outR, values);
		return values;
	}
}

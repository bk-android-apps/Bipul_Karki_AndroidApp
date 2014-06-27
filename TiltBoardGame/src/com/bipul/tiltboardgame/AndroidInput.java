package com.bipul.tiltboardgame;

import android.content.Context;

public class AndroidInput implements Input {
	AccelerometerHandler accelHandler;
	Context context;

	public AndroidInput(Context context) {
		this.context = context;
		accelHandler = new AccelerometerHandler(context);
	}

	public float getAccelX() {
		return accelHandler.getAccelX();
	}

	public float getAccelY() {
		return accelHandler.getAccelY();
	}

	public float getAccelZ() {
		return accelHandler.getAccelZ();
	}

}

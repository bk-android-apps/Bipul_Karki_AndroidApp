package com.bipul.tiltboardgame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback {

	private GameLoop runner;	
	private Game game;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		game.onTouchEvent(event);
		return true;
	}
	
	/*public void drawGame(){
		Bitmap button = BitmapFactory.decodeResource(getResources(), R.drawable.ballofsteel);
		Canvas canvas = sHolder.lockCanvas();		
		if(canvas != null){
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(button, 50, 50, null);
			sHolder.unlockCanvasAndPost(canvas);
		}
		Log.d("TAG", "drawGame is running.");
	}*/
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {	
		game = new Game(holder, getResources(), getContext());
		runner = new GameLoop(game);
		runner.start();
		//drawGame();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(runner != null){
			runner.shutdown();
			while(runner != null){
				try {
					runner.join();
					runner = null;
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
}


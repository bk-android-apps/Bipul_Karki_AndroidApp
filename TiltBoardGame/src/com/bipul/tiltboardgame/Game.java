package com.bipul.tiltboardgame;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class Game {
	private SurfaceHolder holder;
	private Resources resources;
	private AndroidInput input;
	Vibrator v;
	Bitmap ballImage;
	Bitmap ballShadow;
	Bitmap block_a, block_b, block_c; // more blocks can be added
	Bitmap blockShadow;
	Bitmap hole;
	float ballPosX = 800.0f;
	float ballPosY = 200.0f;
	float ballSpdX = 0.0f;
	float ballSpdY = 0.0f;
	int scrWidth = 0;
	int scrHeight = 0;
	Random rand;
	Rect ballRect, block_a_Rect, block_b_Rect, block_c_Rect, holeRect;
	int valueA, valueX1, valueY1, valueX2, valueY2, valueX3, valueY3;  //random variables
	int holeCurrentX_min = 0;
	int holeCurrentX_max = 0;
	int holeCurrentY_min = 0;
	int holeCurrentY_max = 0;
	int ballCurrentX_min = 0;
	int ballCurrentX_max = 0;
	int ballCurrentY_min = 0;
	int blockCurrentY_max = 0;
	int blockCurrentX_min = 0;
	int blockCurrentX_max = 0;
	int blockCurrentY_min = 0;
	int ballCurrentY_max = 0;
	Paint textPaint;
	int state = 0;  // o to run or -1 to stop

	public Game(SurfaceHolder holder, Resources resources, Context context) {
		this.holder = holder;
		this.resources = resources;
		this.v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		input = new AndroidInput(context);
		rand = new Random();
		valueA = rand.nextInt(419); // pick random hole location for the hole image
		valueX1 = rand.nextInt(599)+50;
		valueY1 = rand.nextInt(419);
		valueX2 = rand.nextInt(599)+50;
		valueY2 = rand.nextInt(419);
		valueX3 = rand.nextInt(599)+50;
		valueY3 = rand.nextInt(419);
		textPaint = new Paint();
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.BLUE);
		textPaint.setTextSize(30);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}

	public boolean checkCollisionWithHole(Rect ball, Rect hole){
		if (hole.contains(ball.left, ball.top) || hole.contains(ball.left, ball.bottom)){
			return true;
		}
		return false;
	}
	
	public boolean checkCollisionWithBlockRightSide(Rect ball, Rect block) {
		if (block.contains(ball.left, ball.top) || block.contains(ball.left, ball.bottom)){
			return true;
		}
		return false;
	}
	
	public boolean checkCollisionWithBlockLeftSide(Rect ball, Rect block) {
		if(block.contains(ball.right, ball.top) || block.contains(ball.right, ball.bottom)){
			return true;
		}
		return false;
	}


	public void loadImages(){
		
		ballImage = BitmapFactory.decodeResource(resources,
				R.drawable.ball);
		ballShadow = BitmapFactory.decodeResource(resources,
				R.drawable.ballshadow);
		block_a = BitmapFactory.decodeResource(resources,  
				R.drawable.block);
		block_c = block_b = block_a; // refer to same block image
		blockShadow = BitmapFactory.decodeResource(resources,
				R.drawable.blockshadow);
		hole = BitmapFactory.decodeResource(resources, R.drawable.hole);
		
	}
	public void draw() {

		loadImages();
		if (state != -1) {

			Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
				scrWidth = canvas.getWidth();
				scrHeight = canvas.getHeight();

				ballSpdX = input.getAccelX();
				ballSpdY = input.getAccelY();
				ballPosX += ballSpdX;
				ballPosY += ballSpdY;

				canvas.drawColor(Color.LTGRAY); // screen background color light gray

				if (ballPosX >= (scrWidth - 34)) { // minus width of the ball
													// image
					ballPosX = scrWidth - 35; // 1 pixel for bounce effect
				}
				if (ballPosX < 0) {
					ballPosX = 1; // 1 pixel for bounce effect
				}
				if (ballPosY >= (scrHeight - 34)) { // minus width of the ball
													// height
					ballPosY = scrHeight - 35;
				}
				if (ballPosY < 0) {
					ballPosY = -1;
				}

				canvas.drawBitmap(hole, 0, valueA, null);  //places hole in random place 
				canvas.drawBitmap(block_a, valueX1, valueY1, null);  //place blocks in random place
				canvas.drawBitmap(block_b, valueX2, valueY2, null);
				canvas.drawBitmap(block_b, valueX3, valueY3, null);
				
				//hole positions
				setHoleRectangle(0, valueA);
				// ball positions
				setBallRectangle(ballPosX, ballPosY);
				
				if (checkCollisionWithHole(ballRect, holeRect) == true) {
					// Log.d("BK", "Ball is in the hole");
					v.vibrate(150); // vibrate the device for 150ms
					canvas.drawBitmap(ballImage, 2, holeCurrentY_min + 1, null); // ball and the hole alignment
					canvas.drawText("great!...Tap to play again or go back to reshuffle", 80,
							scrHeight / 2, textPaint);
					state = -1;  //stop the game
					holder.unlockCanvasAndPost(canvas);
					return;
				}
				setBlockA_Rectangle(valueX1, valueY1);
				if (checkCollisionWithBlockRightSide(ballRect, block_a_Rect) == true){
					ballPosX = valueX1 +block_a.getWidth();
					ballPosY += ballSpdY;
				}
				if (checkCollisionWithBlockLeftSide(ballRect, block_a_Rect) == true){
					ballPosX = valueX1 - (block_a.getWidth()+5);
					ballPosY += ballSpdY;
				}
				setBlockB_Rectangle(valueX2, valueY2);
				if (checkCollisionWithBlockRightSide(ballRect, block_b_Rect) == true){
					ballPosX = valueX2 +block_b.getWidth();
					ballPosY += ballSpdY;
				}
				if (checkCollisionWithBlockLeftSide(ballRect, block_b_Rect) == true){
					ballPosX = valueX2 - (block_b.getWidth()+5);
					ballPosY += ballSpdY;
				}
				setBlockC_Rectangle(valueX3, valueY3);
				if (checkCollisionWithBlockRightSide(ballRect, block_c_Rect) == true){
					ballPosX = valueX3 +block_c.getWidth();
					ballPosY += ballSpdY;
				}
				if (checkCollisionWithBlockLeftSide(ballRect, block_c_Rect) == true){
					ballPosX = valueX3 - (block_c.getWidth()+5);
					ballPosY += ballSpdY;
				}
				//draw ball
				canvas.drawBitmap(ballShadow, ballPosX-13, ballPosY+2, null); //offset shadow
				canvas.drawBitmap(ballImage, ballPosX, ballPosY, null);

			}

			holder.unlockCanvasAndPost(canvas);
		}

	}

	private void setHoleRectangle(int x, int y) {
		holeCurrentX_min = x;
		holeCurrentX_max = holeCurrentX_min + hole.getWidth();
		holeCurrentY_min = y;
		holeCurrentY_max = holeCurrentY_min + hole.getHeight();
		holeRect = new Rect();
		holeRect.left = holeCurrentX_min;
		holeRect.top = holeCurrentY_min;
		holeRect.right = holeCurrentX_max;
		holeRect.bottom = holeCurrentY_max;
		
	}

	private void setBlockA_Rectangle(int blockCurrentX2, int blockCurrentY2) {
		blockCurrentX_min = blockCurrentX2;
		blockCurrentX_max = blockCurrentX_min + block_a.getWidth();
		blockCurrentY_min = blockCurrentY2;
		blockCurrentY_max = blockCurrentY_min + block_a.getHeight();
		block_a_Rect = new Rect();
		block_a_Rect.left = blockCurrentX_min;
		block_a_Rect.top = blockCurrentY_min;
		block_a_Rect.right = blockCurrentX_max;
		block_a_Rect.bottom = blockCurrentY_max;
		
	}
	private void setBlockB_Rectangle(int blockCurrentX2, int blockCurrentY2) {
		blockCurrentX_min = blockCurrentX2;
		blockCurrentX_max = blockCurrentX_min + block_b.getWidth();
		blockCurrentY_min = blockCurrentY2;
		blockCurrentY_max = blockCurrentY_min + block_b.getHeight();
		block_b_Rect = new Rect();
		block_b_Rect.left = blockCurrentX_min;
		block_b_Rect.top = blockCurrentY_min;
		block_b_Rect.right = blockCurrentX_max;
		block_b_Rect.bottom = blockCurrentY_max;
		
	}
	private void setBlockC_Rectangle(int blockCurrentX2, int blockCurrentY2) {
		blockCurrentX_min = blockCurrentX2;
		blockCurrentX_max = blockCurrentX_min + block_c.getWidth();
		blockCurrentY_min = blockCurrentY2;
		blockCurrentY_max = blockCurrentY_min + block_c.getHeight();
		block_c_Rect = new Rect();
		block_c_Rect.left = blockCurrentX_min;
		block_c_Rect.top = blockCurrentY_min;
		block_c_Rect.right = blockCurrentX_max;
		block_c_Rect.bottom = blockCurrentY_max;
		
	}


	private void setBallRectangle(float ballPosX2, float ballPosY2) {
		ballCurrentX_min = (int) ballPosX;
		ballCurrentX_max = (int) ballPosX + ballImage.getWidth();
		ballCurrentY_min = (int) ballPosY;
		ballCurrentY_max = (int) ballPosY + ballImage.getHeight();
		ballRect = new Rect();
		ballRect.left = ballCurrentX_min;
		ballRect.top = ballCurrentY_min;
		ballRect.right = ballCurrentX_max;
		ballRect.bottom = ballCurrentY_max;
		
	}

	public void onTouchEvent(MotionEvent event) {
		state = 0;
		ballPosX = 800;
		ballPosY = 200;
	}
	
}

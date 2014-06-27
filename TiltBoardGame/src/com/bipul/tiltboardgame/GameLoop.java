package com.bipul.tiltboardgame;

public class GameLoop extends Thread {
	
	private Game game;
	
	public GameLoop(Game game){
		this.game = game;
	}
	
	private volatile boolean running = true;

	@Override
	public void run() {
		// Game loop
		
		while (running) {
			//Log.d("TAG", "Thread is running.");
			//Draw stuff.
			game.draw();		
		}
	}

	@Override
	public synchronized void start() {
		super.start();
		
	}

	public void shutdown() {
		running = false;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

package com.hoan.Sound;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
	public MediaPlayer mPlayer;

	public boolean mPlaying = false;
	public boolean mLoop = false;
	public boolean complet = false;

	public Sound(Context ctx, int resID) {
		mPlayer = MediaPlayer.create(ctx, resID);
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				mPlaying = false;
				complet = true;
				System.out.println("setOnCompletionListener");
				if (mLoop) {
					mp.start();
				}
			}

		});
	}

	public synchronized void play() {
		if (mPlaying)
			return;

		if (mPlayer != null) {
			mPlaying = true;
			mPlayer.start();
		}
	}

	public synchronized void replay() {
		if (mPlaying)
			return;

		if (mPlayer != null) {
			mPlaying = true;
			mPlayer.seekTo(0);
			mPlayer.start();
		}
	}

	public synchronized void stop() {
		try {
			mLoop = false;
			if (mPlaying) {
				mPlaying = false;
				mPlayer.pause();
			}

		} catch (Exception e) {
		}
	}

	public synchronized void loop() {
		mLoop = true;
		mPlaying = true;
		mPlayer.start();
	}

	//synchronized
	
	public synchronized void release() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}


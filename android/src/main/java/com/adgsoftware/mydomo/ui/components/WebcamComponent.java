//package com.adgsoftware.mydomo.ui.components;
//
//import com.adgsoftware.mydomo.R;
//import com.adgsoftware.mydomo.ui.activities.StreamActivity;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnBufferingUpdateListener;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.media.MediaPlayer.OnPreparedListener;
//import android.media.MediaPlayer.OnVideoSizeChangedListener;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.Toast;
//
//public class WebcamComponent extends AbstractComponent implements
//OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
//OnVideoSizeChangedListener, SurfaceHolder.Callback {
//
//
//	
//	private static final String TAG = "MediaPlayerDemo";
//
//	private int mVideoWidth = 320;
//
//	private int mVideoHeight = 200;
//
//	private MediaPlayer mMediaPlayer;
//
//	private SurfaceView mPreview;
//
//	private SurfaceHolder holder;
//
//	private String path;
//
//	//private Bundle extras;
//
//	private static final String MEDIA = "media";
//
//	private static final int LOCAL_AUDIO = 1;
//
//	private static final int STREAM_AUDIO = 2;
//
//	private static final int RESOURCES_AUDIO = 3;
//
//	private static final int LOCAL_VIDEO = 4;
//
//	private static final int STREAM_VIDEO = 5;
//
//	private boolean mIsVideoSizeKnown = false;
//
//	private boolean mIsVideoReadyToBePlayed = false;
//
//	public WebcamComponent(Context context) {
//		super(context);
//		setContentView(R.layout.streaming);
//
//		mPreview = (SurfaceView) findViewById(R.id.surface_view);
//
//		holder = mPreview.getHolder();
//
//		holder.addCallback(this);
//
//		//holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//		//extras = getIntent().getExtras();
//	}
//	
//	private void playVideo() {
//
//		doCleanUp();
//		try {
//
//			path = "rtsp://media-us-2.soundreach.net/slcn_lifestyle.sdp";
////			path = "rtsp://172.16.33.61:8554";
//			if (path == "") {
//
//				// Tell the user to provide a media file URL.
//				Toast
//
//				.makeText(
//
//						StreamActivity.this,
//
//						"Please edit MediaPlayerDemo_Video Activity, "
//								+ "and set the path variable to your media file path."
//
//								+ " Your media file must be stored on sdcard.",
//
//						Toast.LENGTH_LONG).show();
//			}
//
//			// Create a new media player and set the listeners
//
//			mMediaPlayer = new MediaPlayer();
//
//			mMediaPlayer.setDataSource(path);
//
//			mMediaPlayer.setDisplay(holder);
//
//			mMediaPlayer.prepare();
//
//			mMediaPlayer.setOnBufferingUpdateListener(this);
//
//			mMediaPlayer.setOnCompletionListener(this);
//
//			mMediaPlayer.setOnPreparedListener(this);
//
//			mMediaPlayer.setOnVideoSizeChangedListener(this);
//
//			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//		}
//
//		catch (Exception e) {
//
//			Log.e(TAG, "error: " + e.getMessage(), e);
//
//		}
//	}
//
//	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
//
//		Log.d(TAG, "onBufferingUpdate percent:" + percent);
//
//	}
//
//	public void onCompletion(MediaPlayer arg0) {
//
//		Log.d(TAG, "onCompletion called");
//
//	}
//
//	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//
//		Log.v(TAG, "onVideoSizeChanged called");
//
//		if (width == 0 || height == 0) {
//
//			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
//					+ ")");
//
//			return;
//		}
//
//		mIsVideoSizeKnown = true;
//
//		mVideoWidth = width;
//
//		mVideoHeight = height;
//
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//
//			startVideoPlayback();
//		}
//
//	}
//
//	public void onPrepared(MediaPlayer mediaplayer) {
//
//		Log.d(TAG, "onPrepared called");
//
//		mIsVideoReadyToBePlayed = true;
//
//		Log.v(TAG, "demarre ou pas?" + (mIsVideoSizeKnown));
//		
////		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//
//			startVideoPlayback();
////		}
//
//	}
//
//	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
//
//		Log.d(TAG, "surfaceChanged called");
//
//	}
//
//	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
//
//		Log.d(TAG, "surfaceDestroyed called");
//
//	}
//
//	public void surfaceCreated(SurfaceHolder holder) {
//
//		Log.d(TAG, "surfaceCreated called");
//
//		playVideo();
//
//	}
//
//	// TODO call it from the controller activity
//	protected void onPause() {
//
//		// activity.onPause(): super.onPause();
//
//		releaseMediaPlayer();
//		doCleanUp();
//
//	}
//
//	// TODO call it from the controller activity
//	protected void onDestroy() {
//
//		// activity.onDestroy: super.onDestroy();
//
//		releaseMediaPlayer();
//		doCleanUp();
//
//	}
//
//	private void releaseMediaPlayer() {
//
//		if (mMediaPlayer != null) {
//
//			mMediaPlayer.release();
//
//			mMediaPlayer = null;
//
//		}
//	}
//
//	private void doCleanUp() {
//
//		mVideoWidth = 0;
//
//		mVideoHeight = 0;
//
//		mIsVideoReadyToBePlayed = false;
//
//		mIsVideoSizeKnown = false;
//
//	}
//
//	private void startVideoPlayback() {
//
//		Log.v(TAG, "startVideoPlayback");
//
//		holder.setFixedSize(mVideoWidth, mVideoHeight);
//
//		mMediaPlayer.start();
//
//	}
//
//
//}

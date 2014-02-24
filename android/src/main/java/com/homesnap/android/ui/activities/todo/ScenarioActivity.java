package com.homesnap.android.ui.activities.todo;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Scenario screen.
 * Allows to use pre-defined scenarios.
 */
public class ScenarioActivity extends Activity
implements
OnBufferingUpdateListener, OnCompletionListener,
OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback
{
	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private Bundle extras;
	private static final String MEDIA = "media";
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textview = new TextView(this);
        textview.setText("TODO: Scenario tab");
        setContentView(textview);
       try {
    	   
       VideoView vv = new VideoView(this);
       setContentView(vv);
        
       
       
        String url = "http://192.168.1.238"; // your URL here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare(); // might take long! (for buffering, etc)
        mediaPlayer.start();
       
        
       } catch (Exception e) {
		// TODO: handle exception
    	   e.printStackTrace();
       }
       
       
       super.onCreate(savedInstanceState);
       FrameLayout fl = new FrameLayout(this);
       setContentView(fl);
       SurfaceView mPreview = new SurfaceView(this);
       fl.addView(mPreview);
//       mPreview = (SurfaceView) findViewById(R.id.surface);
       holder = mPreview.getHolder();
       holder.addCallback(this);
       holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
       extras = getIntent().getExtras();
        
    }


private void playVideo(Integer Media) {
    doCleanUp();
    try {



        /*
         * TODO: Set path variable to progressive streamable mp4 or
         * 3gpp format URL. Http protocol should be used.
         * Mediaplayer can only play "progressive streamable
         * contents" which basically means: 1. the movie atom has to
         * precede all the media data atoms. 2. The clip has to be
         * reasonably interleaved.
         * 
         */
        String path = "";



        // Create a new media player and set the listeners
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepare();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        


    } catch (Exception e) {
        Log.e(TAG, "error: " + e.getMessage(), e);
    }
}

public void onBufferingUpdate(MediaPlayer arg0, int percent) {
    Log.d(TAG, "onBufferingUpdate percent:" + percent);

}

public void onCompletion(MediaPlayer arg0) {
    Log.d(TAG, "onCompletion called");
}

public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    Log.v(TAG, "onVideoSizeChanged called");
    if (width == 0 || height == 0) {
        Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
        return;
    }
    mIsVideoSizeKnown = true;
    mVideoWidth = width;
    mVideoHeight = height;
    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
        startVideoPlayback();
    }
}

public void onPrepared(MediaPlayer mediaplayer) {
    Log.d(TAG, "onPrepared called");
    mIsVideoReadyToBePlayed = true;
    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
        startVideoPlayback();
    }
}

public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
    Log.d(TAG, "surfaceChanged called");

}

public void surfaceDestroyed(SurfaceHolder surfaceholder) {
    Log.d(TAG, "surfaceDestroyed called");
}


public void surfaceCreated(SurfaceHolder holder) {
    Log.d(TAG, "surfaceCreated called");
    playVideo(extras.getInt(MEDIA));


}

@Override
protected void onPause() {
    super.onPause();
    releaseMediaPlayer();
    doCleanUp();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    releaseMediaPlayer();
    doCleanUp();
}

private void releaseMediaPlayer() {
    if (mMediaPlayer != null) {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}

private void doCleanUp() {
    mVideoWidth = 0;
    mVideoHeight = 0;
    mIsVideoReadyToBePlayed = false;
    mIsVideoSizeKnown = false;
}

private void startVideoPlayback() {
    Log.v(TAG, "startVideoPlayback");
    holder.setFixedSize(mVideoWidth, mVideoHeight);
    mMediaPlayer.start();
}

}

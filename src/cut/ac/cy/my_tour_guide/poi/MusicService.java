package cut.ac.cy.my_tour_guide.poi;
import cut.ac.cy.my_tour_guide.helpers.MusicResources;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class MusicService extends Service{
	private static final String TAG = "MusicService";
	private MediaPlayer mPlayer;
	private AudioManager am;
	
	private static final int forwardTime = 4000; //millisecond
	private static final int backwardTime = 4000;
	private boolean audio_started = false;
	
	private final IBinder mBinder = new LocalBinder();
	
	/**epistrefei to antikeimeno tis klasis audiopservice gia na mporei na xeirizetai
	 * autos pou tin kalei tis methodous tis
	 *
	 */
	public class LocalBinder extends Binder{
		MusicService getService(){
			return MusicService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind has been called");
		return mBinder;
	}
	
	/**kaleitai mono mia fora stin arxi pou dimiourgeitai to service
	 * 
	 */
	@Override
	public void onCreate(){
		Log.i(TAG, "service created");
		int audioRes = getResources().getIdentifier(
				MusicResources.getMusicResources(), "raw", "cut.ac.cy.my_tour_guide");
		mPlayer = MediaPlayer.create(this, audioRes);	
	}
	
	/**
	 * kaleitai otan kapoio activity kalei to startservice. An polla activities to kaloun kai exei ksekiniese
	 * to service mono auti i methodos ekteleitai kai oxi i onCreate
	 */
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.i(TAG, "Service onStartCommand executed.");
		return START_STICKY;
	}

	@Override
    public void onDestroy() {
    	Log.i(TAG, "service destroyed");
        mPlayer.release();
        mPlayer = null;
    }

    public void playPauseAudio() {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		} else {
			if (audio_started) {
				mPlayer.start();
			} else {
				requestAudioFocus();
			}
		}

	}
	
    public void stopAudio(){
    	if (mPlayer.isPlaying()){
    		mPlayer.pause();
    		setAudioPosition(0);
    	}
    }
    
    public void seekForward(){
    	if(getAudioPosition() + forwardTime <=mPlayer.getDuration()){
    		mPlayer.seekTo(getAudioPosition() + forwardTime);
    	}else{
    		mPlayer.seekTo(mPlayer.getDuration());
    	}
    }
    
    public void seekBackward(){
    	if(getAudioPosition() - backwardTime >= 0){
    		mPlayer.seekTo(getAudioPosition() - backwardTime);
    	}else{
    		mPlayer.seekTo(0);
    	}
    }
    
    @Override
    public boolean onUnbind(Intent intent){
    	Log.i(TAG,"onUnbind");
    	return false;
    	
    }
    
    public void setAudioPosition(int audioPosition){
    	mPlayer.seekTo(audioPosition);
    }
    
    public int getAudioPosition(){
    	return mPlayer.getCurrentPosition();
    }
    
    public boolean isAudioPlaying(){
    	return mPlayer.isPlaying();
    }
    
  //pernei to focus(stamatane ta alla) tis mousikis apo alla programmata pou trexoun ekeini tin stigmi
  	public void requestAudioFocus() {
  		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
  		// Request audio focus for playback
  		int result = am.requestAudioFocus(focusChangeListener,
  		// Use the music stream.
  				AudioManager.STREAM_MUSIC,
  				// Request transient focus.
  				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);		
  		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
  			mPlayer.setVolume(1f, 1f);
  			mPlayer.start();
  		}
  	}

  	private OnAudioFocusChangeListener focusChangeListener = new OnAudioFocusChangeListener() {

  		public void onAudioFocusChange(int focusChange) {
  			// nothing here
  		}

  	};

}
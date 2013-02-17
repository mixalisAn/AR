package cut.ac.cy.my_tour_guide.poi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.data.ARData;
import cut.ac.cy.my_tour_guide.poi.MusicService.LocalBinder;



public class OldPoiInfo extends Activity {
	private Intent intent;
	static final String AUDIO_POSITION = "audiocurrentposition";
	static final String AUDIO_STATE = "audiostate";
	private MusicService mService;
	private static double geoLat;
	private static double geoLng;
	private static String markerName = null;
	private static String markerUrl = null;
	private static String markerAddress = null;
	private boolean mBind = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("activity", "activity started");
		setContentView(R.layout.poi_info);

		//get extras of marker
		markerName = getIntent().getStringExtra("markerName");
		markerUrl = getIntent().getStringExtra("markerUrl");
		markerAddress = getIntent().getStringExtra("markerAddress");
		
		//get user current location
		geoLat = ARData.getCurrentLocation().getLatitude(); // convert to
																  // microdegrees
		geoLng = ARData.getCurrentLocation().getLongitude();
		
		
		initLayout("Rialto Theater");
		//setVolumeControlStream(AudioManager.STREAM_MUSIC); // gia na allazei o
															// ixos apo ta
															// koumpia
		intent = new Intent(this, MusicService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);

		if (savedInstanceState != null) {
			// Restore value of members from saved state
			mService.setAudioPosition(savedInstanceState.getInt(AUDIO_POSITION));
			mService.setAudioStarted(savedInstanceState.getBoolean(AUDIO_STATE));
		}

	}
	
	/*
	 * public void onResume() { super.onResume(); if (audio_started) {
	 * mPlayer.start(); } }
	 */
	@Override
	public void onStart() {
		super.onStart();

		Log.e("activity", "activity on start");
		
		if (mBind && mService.isAudioStarted()) {
			mService.playPauseAudio();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("activity", "activity pause");
		if (mBind && mService.isAudioStarted() ) {
			mService.playPauseAudio();
		}
	}



	// apothikeuei plirofories otan i efarmogi katastrefetai kai
	// ksanadimiourgeitai
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the audio current position
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt(AUDIO_POSITION, mService.getAudioPosition());
		savedInstanceState.putBoolean(AUDIO_STATE, mService.isAudioStarted());
	}

	public void playAudio(View view) {
		if(mBind){
		mService.playPauseAudio();
		}
	}
	

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBind = true;
		}
		public void onServiceDisconnected(ComponentName arg0) {
			mBind = false;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("activity", "activity on destory");
		
		if(mBind){
			unbindService(mConnection);
			mBind = false;
		}
		
	}
	
	public void initLayout(String mName) {

		TextView markerTitle = (TextView) findViewById(R.id.markerTitle);
		//TextView imageTitle = (TextView) findViewById(R.id.imageTitle);
		TextView markerInfo = (TextView) findViewById(R.id.markerInfo);
		
		markerTitle.setText(markerName);
		//imageTitle.setText(markerName);
		//markerInfo.setText(R.string.marker_info1);

	}
	//button listeners
	//anoigei to site gia to wikipedia gia to antistoixo marker
	public void openUrl(View view){
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
		          Uri.parse(markerUrl));
		startActivity(browserIntent);

	}
	//moirazetai plirofories gia to antistoixo marker mesw allwn efarmogwn
	//prepei na to prosarmwsw gia to kathe marker kai na allaksw to periexomeno twn pliroforiwn
	public void shareInfo(View view){
		
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Here is the share content body";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	//kanei navigation gia to kathe marker apo to simeio poy einai o xristis prepei na to valw na to kanei dinamika
	public void startNavigation(View view){
		Log.w("Naviation method", "Geolat: " + geoLat + "GeoLng: " + geoLng);
		String uri = "http://maps.google.com/maps?saddr="+ geoLat +","+ geoLng +" &daddr=39.635452,22.417772 &directionsmode=walking;";
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
	}
}

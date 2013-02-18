package cut.ac.cy.my_tour_guide.poi;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.gallery.GridFragment;
import cut.ac.cy.my_tour_guide.poi.MusicService.LocalBinder;

/**
 * fragmentactivity exei to lifecycle apo tin kanoniki activity apla exei kai
 * merikes alles methodous pou tin voithoun gia na epikoinwnei me to fragment
 * 
 */
public class PoiActivity extends SherlockFragmentActivity implements
		OnClickListener {
	private static final String TAG = "Poi Activity";
	private ViewPager viewPager;
	private TabsAdapter tabsAdapter;
	private View transparentView;
	private LinearLayout musicLayout;
	private ImageView backwardView;
	private ImageView stopView;
	private ImageView pausePlayView;
	private ImageView forwardView;
	private MusicService mService;
	private boolean mBound = false;
	private Intent intentService = null;

	private boolean showMusicPlayer = false;
	private boolean audioExists = false;
	private boolean audioStarted = false;
	private boolean audioPlaying = false;
	private int audioPosition;

	private long markerId;
	private String markerResName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "on Create has been called");
		setContentView(R.layout.poi_info_main);
		// setup action bar for tabs
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// actionBar.setDisplayShowHomeEnabled(false);
		// actionBar.setDisplayShowTitleEnabled(false);

		Intent intent = getIntent();
		markerId = intent.getLongExtra("Id", 0);
		markerResName = intent.getStringExtra("Res Name");

		if (isAudioResourceExist()) {
			MusicResources.setMusicResources(markerResName);
			intentService = new Intent(this, MusicService.class);
			startService(intentService);
		}

		viewPager = (ViewPager) findViewById(R.id.pager);
		tabsAdapter = new TabsAdapter(this, viewPager, actionBar);
		transparentView = (View) findViewById(R.id.transparentView);
		musicLayout = (LinearLayout) findViewById(R.id.musicLayout);
		backwardView = (ImageView) findViewById(R.id.backward);
		stopView = (ImageView) findViewById(R.id.stop);
		pausePlayView = (ImageView) findViewById(R.id.pausePlay);
		forwardView = (ImageView) findViewById(R.id.forward);

		musicLayout.setBackgroundColor(Color.argb(180, 00, 00, 00));
		if (savedInstanceState != null) {
			showMusicPlayer = savedInstanceState
					.getBoolean("Music Player Visibility");
			if (showMusicPlayer) {
				musicLayout.setVisibility(LinearLayout.VISIBLE);
			}
			audioPlaying = savedInstanceState.getBoolean("Music Player State");
			if (audioPlaying){
				pausePlayView.setImageResource(R.drawable.pause);
			}

		}
		Tab tab1 = actionBar.newTab().setText("ABOUT");

		Tab tab2 = actionBar.newTab().setText("PHOTO");

		Tab tab3 = actionBar.newTab().setText("COMPARE");

		tabsAdapter.addTab(tab1, PoiAboutFragment.class, null);
		tabsAdapter.addTab(tab2, GridFragment.class, null);
		tabsAdapter.addTab(tab3, CompareNowAndThen.class, null);

		backwardView.setOnClickListener(this);
		stopView.setOnClickListener(this);
		pausePlayView.setOnClickListener(this);
		forwardView.setOnClickListener(this);
		transparentView.setOnClickListener(this);
	}

	private boolean isAudioResourceExist() {
		int resResult = getResources().getIdentifier(markerResName, "raw",
				"cut.ac.cy.my_tour_guide");
		switch (resResult) {
		case 0:
			audioExists = false;
			return audioExists;
		default:
			audioExists = true;
			return audioExists;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "on Start executed");
		if (audioExists) {
			// Bind to service
			Intent intent = new Intent(this, MusicService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	/*
	 * @Override protected void onResume() { super.onResume(); if (mBound &&
	 * audioStarted && audioPosition > 0) {
	 * mService.setAudioPosition(audioPosition); } }
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "On Pause executed");
		if (audioExists) {
			//stop service
			if (isFinishing()) {
				Log.i(TAG, "isFinishing executed");
				if(mBound){
					unbindService(mConnection);
					mBound = false;
				}
				
				stopService(intentService);
			} else if (isNotTopActivity()) {
				Log.i(TAG, "is Not top Activity executed");
				audioPosition = mService.getAudioPosition();
				audioStarted = mService.isAudioStarted();
				mService.stopAudio();
			}
		}
		if(mBound){
			unbindService(mConnection);
			mBound = false;
		}
	}

	// elegxei an kapoia alli activity exei anoiksei
	private boolean isNotTopActivity() {
		Context context = getApplicationContext();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		Log.i(TAG, String.valueOf(taskInfo.size()));
		if (!taskInfo.isEmpty()) {
			ComponentName topActivity = taskInfo.get(0).topActivity;
		
			if (!topActivity.getPackageName().equals(context.getPackageName())) {

				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "OnDestroy has been called!");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean("Music Player Visibility", showMusicPlayer);
		outState.putBoolean("Music Player State", audioPlaying);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.poi, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_music:
			if (audioExists) {
				showMusicPlayer = !showMusicPlayer;
				musicLayout
						.setVisibility((showMusicPlayer) ? LinearLayout.VISIBLE
								: LinearLayout.GONE);
				if (showMusicPlayer) {
					transparentView.setVisibility(View.VISIBLE);
				}
			} else {
				Toast.makeText(this, "No Audio Available!", Toast.LENGTH_LONG)
						.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	//auti i methodos xrisimopoieitai gia na mporei to fragment PoiAboutFragment otan kaleitai 
	//i activity poy anoigei ton browser tote na stamataei to tragoudi
	public void pauseAudioForFragment(){
		if(mBound && mService.isAudioPlaying()){
			audioPosition = mService.getAudioPosition();
			audioStarted = mService.isAudioStarted();
			mService.playPauseAudio();
		}
	}
	
	public long getMarkerId() {
		return markerId;
	}

	public String getMarkerResName(){
		return markerResName;
	}
	
	/**
	 * Defines callbacks for service binding, passed to bindService() i iBinder
	 * einai ousiastika i localBinder pou exei tin methodo pou kaleiitai
	 * argotera des to Musicservice gia info
	 */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			Log.i(TAG, "Service binded");
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			restoreAudioIfPaused();
		}

		private void restoreAudioIfPaused() {
			if (audioStarted && audioPosition > 0 && mBound) {
				mService.setAudioPosition(audioPosition);
				mService.playPauseAudio();
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	/**
	 * 
	 * Tabs adapter gia tin action bar
	 * 
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager,
				ActionBar actionBar) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = actionBar;
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		public void onPageScrollStateChanged(int state) {
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}

	
	
	@Override
	public void onClick(View view) {
		if (mBound) {
			switch (view.getId()) {
			case R.id.transparentView:
				if (showMusicPlayer) {
					showMusicPlayer = !showMusicPlayer;
					musicLayout
							.setVisibility((showMusicPlayer) ? LinearLayout.VISIBLE
									: LinearLayout.GONE);
					transparentView.setVisibility(View.GONE);
				}
				break;
			case R.id.backward:
				mService.seekBackward();
				break;
			case R.id.stop:
				if (mService.isAudioStarted()) {
					mService.stopAudio();
					pausePlayView.setImageResource(R.drawable.play);
				}
				break;
			case R.id.pausePlay:
				if (mService.isAudioPlaying()) {
					pausePlayView.setImageResource(R.drawable.play);
					audioPlaying = false;
				} else {
					pausePlayView.setImageResource(R.drawable.pause);
					audioPlaying = true;
				}
				mService.playPauseAudio();
				break;
			case R.id.forward:
				mService.seekForward();
				break;
			}
		}
	}
}

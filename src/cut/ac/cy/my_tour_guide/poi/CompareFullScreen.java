package cut.ac.cy.my_tour_guide.poi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import cut.ac.cy.my_tour_guide.R;

public class CompareFullScreen extends SherlockFragmentActivity{
	private static final int NUM_ITEMS = 2;
	private static final String TAG = "CompareFullScreen";
	private static final String INIT_RES = "markerResName";
	private static final String INIT_SELECTION ="selectedRes";
	private static String[] mResources = new String[2];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_fullscreen);
		String resName = getIntent().getStringExtra(INIT_RES);
		int position = getIntent().getIntExtra(INIT_SELECTION, -1);
		Log.i(TAG, "Position: " + String.valueOf(position));
		initResourses(resName);
		MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
		ViewPager mPager = (ViewPager) findViewById(R.id.pager2);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(80);
		mPager.setCurrentItem(position);
		
	}

	
	private void initResourses(String resName) {
		mResources[0] = resName + "_present";
		mResources[1] = resName + "_past";
	}


	public static class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return CompareFullScreenFragment.newInstance(mResources[position]);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}
		
	}
	
	
	
}

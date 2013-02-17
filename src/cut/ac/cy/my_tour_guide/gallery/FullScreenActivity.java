package cut.ac.cy.my_tour_guide.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import cut.ac.cy.my_tour_guide.R;
//fragment activity einai gia na ipostirizoun kai oi palioteres ekdoseis android fragments
public class FullScreenActivity extends SherlockFragmentActivity{
	private ImagePagerAdapter mAdapter;
	private ViewPager mPager;
	private static String[] mStrings;
	private ImageDownloader imageDownloader;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		Log.e("FullscreenActivity", "started");
		int position = getIntent().getIntExtra("Image Position", -1);
		mStrings = getIntent().getStringArrayExtra("Urls");
		imageDownloader = new ImageDownloader(this, getSupportFragmentManager());
		Log.i("Full Screen Activity", "The number of urls is: " + mStrings.length);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mStrings.length);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(80);
		mPager.setCurrentItem(position);
	}
	
	
	
	public static class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            return FullScreenFragment.newInstance(mStrings[position]);
        }
    }
	
	public ImageDownloader getImageDownloader(){
		return imageDownloader;
	}
}

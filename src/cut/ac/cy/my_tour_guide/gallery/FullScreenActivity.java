package cut.ac.cy.my_tour_guide.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;


import cut.ac.cy.my_tour_guide.R;
//fragment activity einai gia na ipostirizoun kai oi palioteres ekdoseis android fragments
public class FullScreenActivity extends SherlockFragmentActivity{
    private static final String IMAGE_CACHE_DIR = "images";
	private ImagePagerAdapter mAdapter;
	private ViewPager mPager;
	private static String[] mStrings;
	private ImageFetcher imageFetcher;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		
		// Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width) / 2;
		
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        
		Log.e("FullscreenActivity", "started");
		int position = getIntent().getIntExtra("Image Position", -1);
		mStrings = getIntent().getStringArrayExtra("Urls");
		imageFetcher = new ImageFetcher(this, longest);
		imageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
		imageFetcher.setImageFadeIn(false);
		
		Log.i("Full Screen Activity", "The number of urls is: " + mStrings.length);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mStrings.length);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(80);
		mPager.setCurrentItem(position);
	}
	

    @Override
    public void onResume() {
        super.onResume();
        imageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageFetcher.setExitTasksEarly(true);
        imageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageFetcher.closeCache();
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
	
	public ImageFetcher getImageFethcer(){
		return imageFetcher;
	}
}

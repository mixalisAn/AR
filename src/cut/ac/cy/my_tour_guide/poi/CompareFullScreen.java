package cut.ac.cy.my_tour_guide.poi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.gallery.ImageCache;
import cut.ac.cy.my_tour_guide.gallery.ImageFetcher;

public class CompareFullScreen extends SherlockFragmentActivity{
	private static final int NUM_ITEMS = 2;
	private static final String IMAGE_CACHE_DIR = "images";
	private static final String TAG = "CompareFullScreen";
	private static final String INIT_RES = "markerCompareUrls";
	private static final String INIT_SELECTION ="selectedRes";
	//private static String[] mResources = new String[2];
	private static String[] compareUrls = new String[2];
	private ImageFetcher mImageFetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.compare_fullscreen);
		
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
        cacheParams.setMemCacheSizePercent(0.20f); // Set memory cache to 20% of app memory
        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);
        
        
		compareUrls = getIntent().getStringArrayExtra(INIT_RES);
		int position = getIntent().getIntExtra(INIT_SELECTION, -1);
		Log.i(TAG, "Position: " + String.valueOf(position));
		//initResourses(resName);
		MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
		ViewPager mPager = (ViewPager) findViewById(R.id.pager2);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(80);
		mPager.setCurrentItem(position);
		
	}
	
	/**
     * Called by the ViewPager child fragments to load images via the one ImageFetcher
     */
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }


	/*
	private void initResourses(String resName) {
		mResources[0] = resName + "_present";
		mResources[1] = resName + "_past";
	}
	*/

	public static class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return CompareFullScreenFragment.newInstance(compareUrls[position]);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}
		
	}
	
	
	
}

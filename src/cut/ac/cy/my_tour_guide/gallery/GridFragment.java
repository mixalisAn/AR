package cut.ac.cy.my_tour_guide.gallery;

import java.sql.SQLException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.BuildConfig;
import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.database.DBHandler;
import cut.ac.cy.my_tour_guide.gallery.ImageCache.ImageCacheParams;
import cut.ac.cy.my_tour_guide.poi.PoiActivity;

public class GridFragment extends SherlockFragment {
	 private static final String TAG = "ImageGridFragment";
	    private static final String IMAGE_CACHE_DIR = "images";

	    private int mImageThumbSize;
	    private int mImageThumbSpacing;
	    private ImageAdapter mAdapter;
	    private ImageFetcher mImageFetcher;
	    private String[] mFullScreenUrls;
	    private String[] mGridStrings;
	    private long markerId;
	    
	    private boolean download;

	    /**
	     * Empty constructor as per the Fragment documentation
	     */
	    public GridFragment() {}

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Bundle bundle = getArguments();
	        download = bundle.getBoolean("download");
	        Log.d(TAG, "Download " + String.valueOf(download));
	        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
	        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

	        mAdapter = new ImageAdapter(getActivity());

	        ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

	        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

	        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
	        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
	       
	        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
	        //PauseImageFetcher();
	    }

	    @Override
	    public View onCreateView(
	            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	Log.i(TAG, "On Create view has been called");
	        final View v = inflater.inflate(R.layout.fragment_grid, container, false);
	        final GridView mGridView = (GridView) v.findViewById(R.id.gridview);
	        
	        //edw pername ton elegxo toy fragment stin poiActivity
			//to getTag einai methodos poy xrisimopoieitai apo to fragment
			((PoiActivity)getActivity()).setGridFragmentTag(getTag());
			
	        markerId = ((PoiActivity) getActivity()).getMarkerId();
			DBHandler db = new DBHandler(getActivity());
			try {
				db.open();

				Cursor cursor = db.getPoiImagesUrls(markerId);

				if (cursor != null) {
					cursor.moveToFirst();
					int numOfUrls = cursor.getCount();
					mGridStrings = new String[numOfUrls];
					mFullScreenUrls = new String[numOfUrls];
					for (int i = 0; !cursor.isAfterLast(); i++, cursor.moveToNext()) {
						mFullScreenUrls[i] = cursor.getString(0);
						mGridStrings[i] = cursor.getString(1);	
					}
				}
				db.close();
				cursor.close();
			} catch (SQLException e) {
				Log.e(TAG, "error while in database");
				e.printStackTrace();
			}
	        mGridView.setAdapter(mAdapter);
	        mGridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent intent = new Intent(getActivity(),
							FullScreenActivity.class);
					intent.putExtra("Image Position", (int)id);
					intent.putExtra("Urls", mFullScreenUrls);
					startActivity(intent);
				}
			});
	        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
	                // Pause fetcher to ensure smoother scrolling when flinging
	                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
	                    mImageFetcher.setPauseWork(true);
	                } else {
	                    mImageFetcher.setPauseWork(false);
	                }
	            }

	            @Override
	            public void onScroll(AbsListView absListView, int firstVisibleItem,
	                    int visibleItemCount, int totalItemCount) {
	            }
	        });

	        // This listener is used to get the final width of the GridView and then calculate the
	        // number of columns and the width of each column. The width of each column is variable
	        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
	        // of each view so we get nice square thumbnails.
	        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
	                new ViewTreeObserver.OnGlobalLayoutListener() {
	                    @Override
	                    public void onGlobalLayout() {
	                        if (mAdapter.getNumColumns() == 0) {
	                            final int numColumns = (int) Math.floor(
	                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
	                            if (numColumns > 0) {
	                                final int columnWidth =
	                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
	                                mAdapter.setNumColumns(numColumns);
	                                mAdapter.setItemHeight(columnWidth);
	                                if (BuildConfig.DEBUG) {
	                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
	                                }
	                            }
	                        }
	                    }
	                });

	        return v;
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        mImageFetcher.setExitTasksEarly(false);
	        mAdapter.notifyDataSetChanged();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        mImageFetcher.setPauseWork(false);
	        mImageFetcher.setExitTasksEarly(true);
	        mImageFetcher.flushCache();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        mImageFetcher.closeCache();
	    }

	    
	    /**
	     * The main adapter that backs the GridView. This is fairly standard except the number of
	     * columns in the GridView is used to create a fake top row of empty views as we use a
	     * transparent ActionBar and don't want the real top row of images to start off covered by it.
	     */
	    private class ImageAdapter extends BaseAdapter {

	        private final Context mContext;
	        private int mItemHeight = 0;
	        private int mNumColumns = 0;
	        private GridView.LayoutParams mImageViewLayoutParams;

	        public ImageAdapter(Context context) {
	            super();
	            mContext = context;
	            mImageViewLayoutParams = new GridView.LayoutParams(
	                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	        }

	        @Override
	        public int getCount() {
	            // Size + number of columns for top empty row
	            return mGridStrings.length;
	        }

	        @Override
	        public Object getItem(int position) {
	            return mGridStrings[position];
	        }

	        @Override
	        public long getItemId(int position) {
	            return position ;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup container) {
	        
	            // Now handle the main ImageView thumbnails
	            ImageView imageView;
	            if (convertView == null) { // if it's not recycled, instantiate and initialize
	                imageView = new RecyclingImageView(mContext);
	                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	                imageView.setLayoutParams(mImageViewLayoutParams);
	            } else { // Otherwise re-use the converted view
	                imageView = (ImageView) convertView;
	            }

	            // Check the height matches our calculated column width
	            if (imageView.getLayoutParams().height != mItemHeight) {
	                imageView.setLayoutParams(mImageViewLayoutParams);
	            }
	            
	            // Finally load the image asynchronously into the ImageView, this also takes care of
	            // setting a placeholder image while the background thread runs
	            if(download){
	            	mImageFetcher.loadImage(mGridStrings[position], imageView);

	            }else{
	            	mImageFetcher.loadImage(null, null);
	            }
            	return imageView;
	        }

	        /**
	         * Sets the item height. Useful for when we know the column width so the height can be set
	         * to match.
	         *
	         * @param height
	         */
	        public void setItemHeight(int height) {
	            if (height == mItemHeight) {
	                return;
	            }
	            mItemHeight = height;
	            mImageViewLayoutParams =
	                    new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
	            mImageFetcher.setImageSize(height);
	            notifyDataSetChanged();
	        }

	        public void setNumColumns(int numColumns) {
	            mNumColumns = numColumns;
	        }

	        public int getNumColumns() {
	            return mNumColumns;
	        }
	    }
	    

		public void PauseImageFetcher(){
			Log.i(TAG, "Pause image fetcher called");
			if(mImageFetcher != null){
				mImageFetcher.setPauseWork(true);
			}
		}
		
		public void ResumeImageFetcher(){
			Log.i(TAG, "Resume image fetcher called");
			if(mImageFetcher != null){
				mImageFetcher.setPauseWork(false);
			}
		}
		
		public void clearCachesWhenExit(){
			if(mImageFetcher != null){
				mImageFetcher.clearCache();
			}
		}
	    
}

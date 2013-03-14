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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
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
	private static final String TAG = "GridFragment";
	private static final String IMAGE_CACHE_DIR = "images";

	
	 private int imageThumbSize;
	 private int imageThumbSpacing;

	// ---the images to display---
	private String[] mStrings;
	private ImageFetcher imageFetcher;
	GridView gridView;
	ImageAdapter adapter;
	ImageView imageview;
	private long markerId;

	public GridFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setHasOptionsMenu(true);
		Log.i(TAG, "Fagmented created");
		imageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
	    imageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

	    ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

	    cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

	    // The ImageFetcher takes care of loading images into our ImageView children asynchronously
	    imageFetcher = new ImageFetcher(getActivity(), imageThumbSize);
	    imageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "fragment view changed");
		final View v = inflater.inflate(R.layout.fragment_grid, container,
				false);
	
		
		markerId = ((PoiActivity) getActivity()).getMarkerId();
		DBHandler db = new DBHandler(getActivity());
		try {
			db.open();

			Cursor cursor = db.getPoiImagesUrls(markerId);

			if (cursor != null) {
				cursor.moveToFirst();
				int numOfUrls = cursor.getCount();
				mStrings = new String[numOfUrls];
				for (int i = 0; !cursor.isAfterLast(); i++, cursor.moveToNext()) {
					mStrings[i] = cursor.getString(0);
				}
			}
			db.close();
		} catch (SQLException e) {
			Log.e(TAG, "error while in database");
			e.printStackTrace();
		}
		
		Log.i(TAG, "The number of urls is: " + mStrings.length);
		// o adapter enwnei to kathe stoixeio apo to mstrings sto gridview
		adapter = new ImageAdapter(getActivity());
		gridView = (GridView) v.findViewById(R.id.gridview);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						FullScreenActivity.class);
				intent.putExtra("Image Position", position);
				intent.putExtra("Urls", mStrings);
				startActivity(intent);
			}
		});
		gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    imageFetcher.setPauseWork(true);
                } else {
                    imageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }
        });
		
		gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (adapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    gridView.getWidth() / (imageThumbSize + imageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (gridView.getWidth() / numColumns) -   imageThumbSpacing;
                                adapter.setNumColumns(numColumns);
                                adapter.setItemHeight(columnWidth);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
                                }
                            }
                        }
                    }
                });
		return v;
	}
	
	/*
	 * public void update(Drawable draw, int position) { ImageView view =
	 * (ImageView) adapter.getItem(position); view.setImageDrawable(draw);
	 * adapter.notifyDataSetChanged(); }
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
            return mStrings.length;
        }

        @Override
        public Object getItem(int position) {
            return mStrings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            // Now handle the main ImageView thumbnails
            ImageView imageView = (ImageView) convertView;
            if (imageView == null) { // if it's not recycled, instantiate and initialize
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } 

            // Check the height matches our calculated column width
            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
            imageFetcher.loadImage(mStrings[position], imageView);
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
            imageFetcher.setImageSize(height);
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
		if(imageFetcher != null){
			imageFetcher.setPauseWork(true);
		}
	}
}

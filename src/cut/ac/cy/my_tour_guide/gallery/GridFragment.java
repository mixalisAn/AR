package cut.ac.cy.my_tour_guide.gallery;

import java.sql.SQLException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.database.DBHandler;
import cut.ac.cy.my_tour_guide.poi.PoiActivity;

public class GridFragment extends SherlockFragment {
	private static final String TAG = "GridFragment";
	public LruCache<String, Bitmap> mRetainedCache;
	// ---the images to display---
	private String[] mStrings;
	private ImageDownloader imageDownloader;
	GridView gridView;
	ImageAdapter adapter;
	ImageView imageview;
	private long markerId;

	public GridFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Log.i(TAG, "Fagmented created");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		adapter = new ImageAdapter(getActivity(), mStrings, imageDownloader);
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

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "fragment view changed");
		final View v = inflater.inflate(R.layout.fragment_grid, container,
				false);
		gridView = (GridView) v.findViewById(R.id.gridview);
		// me to getActivity exei prosvasi stin activity to fragment
		imageDownloader = new ImageDownloader(getActivity(), getActivity()
				.getSupportFragmentManager());

		return v;
	}

	/*
	 * @Override public void onCreateOptionsMenu(Menu menu, MenuInflater
	 * inflater) { // TODO Auto-generated method stub
	 * inflater.inflate(R.menu.fragment_menu, menu); }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * ImagesSave imSave;
	 * 
	 * switch(item.getItemId()){ case R.id.save_images: imSave = new
	 * ImagesSave(getActivity().getApplicationContext(), getFragmentManager(),
	 * mStrings, "Test"); imSave.saveImages(); getActivity().sendBroadcast(new
	 * Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
	 * Environment.getExternalStorageDirectory()))); break; case
	 * R.id.redownload: imageDownloader.clearCache(); break; }
	 * 
	 * return super.onOptionsItemSelected(item); }
	 */
	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG,"onPause has been called");

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume has been called");
	}
	/*
	 * public void update(Drawable draw, int position) { ImageView view =
	 * (ImageView) adapter.getItem(position); view.setImageDrawable(draw);
	 * adapter.notifyDataSetChanged(); }
	 */
}

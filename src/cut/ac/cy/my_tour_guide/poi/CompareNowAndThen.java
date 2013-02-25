package cut.ac.cy.my_tour_guide.poi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;

public class CompareNowAndThen extends SherlockFragment implements
		OnClickListener {
	private static final String TAG = "Comapre Now And Then";
	private static final String RES_NAME = "markerResName";
	private static final String SELECTED_RES = "selectedRes";
	private ImageView pastImageView;
	private ImageView presentImageView;
	private TextView textViewPast;
	private TextView textViewPresent;
	private String markerResName;
	private boolean textVisibility = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate has been called");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView has been called");
		markerResName = ((PoiActivity) getActivity()).getMarkerResName();
		final View view = inflater.inflate(R.layout.compare_now_and_then,
				container, false);
		
		// set the imageresources for the imageviews
		presentImageView = (ImageView) view
				.findViewById(R.id.present);
		pastImageView = (ImageView) view.findViewById(R.id.past);
		textViewPast = (TextView) view.findViewById(R.id.textViewPast);
		textViewPresent = (TextView) view.findViewById(R.id.textViewPresent);
		
		int presentRes = getResources().getIdentifier(
				markerResName + "_present", "drawable",
				"cut.ac.cy.my_tour_guide");
		presentImageView.setImageResource(presentRes);
		
		int pastRes = getResources().getIdentifier(markerResName + "_past",
				"drawable", "cut.ac.cy.my_tour_guide");
		pastImageView.setImageResource(pastRes);
		textViewPast.setBackgroundColor(Color.argb(180, 00, 00, 00));
		textViewPresent.setBackgroundColor(Color.argb(180, 00, 00, 00));
		
		presentImageView.setOnClickListener(this);
		pastImageView.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		if (textVisibility) {
			textVisibility = false;
			textViewPast.setVisibility(View.GONE);
			textViewPresent.setVisibility(View.GONE);
		} else {
			switch (view.getId()) {
			case R.id.present:
				intent = new Intent();
				intent.setClass(getActivity(), CompareFullScreen.class);
				intent.putExtra(RES_NAME, markerResName);
				intent.putExtra(SELECTED_RES, 0);
				startActivity(intent);
				break;
			case R.id.past:
				intent = new Intent();
				intent.setClass(getActivity(), CompareFullScreen.class);
				intent.putExtra(RES_NAME, markerResName);
				intent.putExtra(SELECTED_RES, 1);
				startActivity(intent);
				break;
			}
		}
	}
}

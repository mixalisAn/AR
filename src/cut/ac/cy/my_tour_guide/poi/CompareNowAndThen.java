package cut.ac.cy.my_tour_guide.poi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;

public class CompareNowAndThen extends SherlockFragment{
	private static final String TAG="Comapre Now And Then";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate has been called");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView has been called");
		View view = inflater.inflate(R.layout.compare_now_and_then, container, false);

		return view;
	}

	
	
}

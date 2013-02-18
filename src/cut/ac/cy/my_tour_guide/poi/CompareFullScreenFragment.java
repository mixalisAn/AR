package cut.ac.cy.my_tour_guide.poi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;

public class CompareFullScreenFragment extends SherlockFragment{
	private static final String IMAGE_RES = "resName";
	private ImageView imageView;
	private String resName;
	
	static CompareFullScreenFragment newInstance(String resName){
		CompareFullScreenFragment fragment = new CompareFullScreenFragment();
		
		Bundle args = new Bundle();
		args.putString(IMAGE_RES, resName);
		fragment.setArguments(args);
		return fragment;
	}
	
	public CompareFullScreenFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resName = getArguments() != null ? getArguments().getString(IMAGE_RES) : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.compare_fullscreen_fragment, container, false);
		imageView = (ImageView) view.findViewById(R.id.imageViewCompare);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int resResult = getResources().getIdentifier(resName, "drawable",
				"cut.ac.cy.my_tour_guide");
		imageView.setImageResource(resResult);
	}
}

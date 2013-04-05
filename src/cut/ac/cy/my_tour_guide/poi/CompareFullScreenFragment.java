package cut.ac.cy.my_tour_guide.poi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.gallery.ImageFetcher;
import cut.ac.cy.my_tour_guide.gallery.Utils;

public class CompareFullScreenFragment extends SherlockFragment{
	private static final String IMAGE_RES = "data";
	private ImageView imageView;
	private String markerUrl;
	   private ImageFetcher mImageFetcher;
	
	static CompareFullScreenFragment newInstance(String data){
		CompareFullScreenFragment fragment = new CompareFullScreenFragment();
		
		Bundle args = new Bundle();
		args.putString(IMAGE_RES, data);
		fragment.setArguments(args);
		return fragment;
	}
	
	public CompareFullScreenFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		markerUrl = getArguments() != null ? getArguments().getString(IMAGE_RES) : null;
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
		/*int resResult = getResources().getIdentifier(resName, "drawable",
				"cut.ac.cy.my_tour_guide");
		imageView.setImageResource(resResult);*/
		
		 // Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        if (CompareFullScreen.class.isInstance(getActivity())) {
            mImageFetcher = ((CompareFullScreen) getActivity()).getImageFetcher();
            mImageFetcher.loadImage(markerUrl, imageView);
        }

        // Pass clicks on the ImageView to the parent activity to handle
        if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
            imageView.setOnClickListener((OnClickListener) getActivity());
        }
		
	}
}

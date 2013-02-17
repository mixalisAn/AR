package cut.ac.cy.my_tour_guide.gallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;

public class FullScreenFragment extends SherlockFragment{
	private static final String IMAGE_DATA_EXTRA = "resId";
	private String mImageUrl;
	private ImageView mImageView;
	private ImageDownloader imageDownloader;
	
	static FullScreenFragment newInstance(String imageUrl) {
        final FullScreenFragment f = new FullScreenFragment();
        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imageUrl);
        f.setArguments(args);
        return f;
    }
	
	 // Empty constructor, required as per Fragment docs
    public FullScreenFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // image_detail_fragment.xml contains just an ImageView
        final View v = inflater.inflate(R.layout.fragment_fullscreen, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (FullScreenActivity.class.isInstance(getActivity())) {
            imageDownloader = ((FullScreenActivity) getActivity()).getImageDownloader();
            imageDownloader.download(mImageUrl, mImageView);
        }
        // Load image into ImageView
    }
}

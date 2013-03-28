package cut.ac.cy.my_tour_guide.gallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import cut.ac.cy.my_tour_guide.R;

public class FullScreenFragment extends SherlockFragment{
	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private String mImageUrl;
    private ImageView mImageView;
    private ImageFetcher mImageFetcher;

    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static FullScreenFragment newInstance(String imageUrl) {
        final FullScreenFragment f = new FullScreenFragment();

        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imageUrl);
        f.setArguments(args);

        return f;
    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    public FullScreenFragment() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link ImageDetailFragment#newInstance(String)} to create this fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        final View v = inflater.inflate(R.layout.fragment_fullscreen, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        if (FullScreenActivity.class.isInstance(getActivity())) {
            mImageFetcher = ((FullScreenActivity) getActivity()).getImageFetcher();
            mImageFetcher.loadImage(mImageUrl, mImageView);
        }

        // Pass clicks on the ImageView to the parent activity to handle
        if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
            mImageView.setOnClickListener((OnClickListener) getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageView != null) {
            // Cancel any pending image work
            ImageWorker.cancelWork(mImageView);
            mImageView.setImageDrawable(null);
        }
    }
    
}

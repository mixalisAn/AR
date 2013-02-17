package cut.ac.cy.my_tour_guide.poi;

import java.sql.SQLException;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.data.ARData;
import cut.ac.cy.my_tour_guide.database.DBHandler;

public class PoiAboutFragment extends SherlockFragment implements
		OnClickListener {
	private static final String TAG = "Poi About Fragment";
	// Views and values
	private ScrollView scrollView = null;
	private ImageView markerMainImageView = null;
	private TextView markerTitleView = null;
	private TextView markerAddressView = null;
	private TextView markerLinkView1 = null;
	private TextView markerLinkView2 = null;
	private TextView markerLinkView3 = null;
	private TextView markerInfoView = null;
	private Button readMoreButton = null;
	private TableRow wikipediaLink = null;
	private TableRow navigationLink = null;
	private TableRow shareLink = null;
	private int defaultTextHeight = 0;
	private int textHeight = 0;
	private String buttonText = "Show more";

	// for 3tabs problem see below
	private int[] defaultScrollPosition = new int[2];
	private boolean onCreateExecuted;
	// marker variables
	private long markerId;
	private DBHandler db;
	private String markerName;
	private Double markerLat;
	private Double markerLng;
	private String markerLink;
	private String markerAddress;
	private String markerDesc;
	private String markerResName;

	// current Location
	private Double currentLocationLat;
	private Double currentLocationLng;

	/**
	 * edw ginetai arxikopoiisi twn variables ektws apo autes pou exoun sxesi me
	 * to interface autes ginontai sto oncreateview
	 * 
	 * me to setretaininstance den kalountai oi oncreate kai on destroy oi alles
	 * ektelountai kanonika apla apothikeuei tis metavlites. prepei na
	 * ksanaginei to gui
	 **/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		onCreateExecuted = true; // gia to problima me to trito tab pou
									// dimiourgei mono to oncreateview sto proto
									// tab
		Log.i(TAG, "onCreate has been called");
		currentLocationLat = ARData.getCurrentLocation().getLatitude();
		currentLocationLng = ARData.getCurrentLocation().getLongitude();
		/**
		 * Auto xrisimopoeitai giati otan eimaste sto 30 tab kai kanoume orientation change
		 * tote sto prwto ekteleitai i oncreate xwris an ekteleitai i oncreateview
		 * etsi otan ekane pali allagi sto orientation den apothikeue tis sostes times sto onsaveinstance
		 * enw twra kathe fora tis pernei i on create kai tis ksanapernaei stis metavlites gia na apothikeutoun 
		 * sosta
		 */
		if (savedInstanceState != null) {
			Log.i(TAG, "Restore values in on create!");
			buttonText = savedInstanceState.getString("Button Text");
			textHeight = savedInstanceState.getInt("Text Height");
			defaultScrollPosition = savedInstanceState
					.getIntArray("Scroll Position");

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(TAG, "On Create view is being called" + textHeight);
		final View view = inflater.inflate(R.layout.poi_info, container, false);

		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		markerMainImageView = (ImageView) view
				.findViewById(R.id.markerMainImage);
		markerTitleView = (TextView) view.findViewById(R.id.markerTitle);
		markerAddressView = (TextView) view.findViewById(R.id.markerAddress);
		markerLinkView1 = (TextView) view.findViewById(R.id.markerLink1);
		markerLinkView2 = (TextView) view.findViewById(R.id.markerLink2);
		markerLinkView3 = (TextView) view.findViewById(R.id.markerLink3);
		markerInfoView = (TextView) view.findViewById(R.id.markerInfo);
		readMoreButton = (Button) view.findViewById(R.id.readMoreButton);
		wikipediaLink = (TableRow) view.findViewById(R.id.wikipediaLink);
		navigationLink = (TableRow) view.findViewById(R.id.navigationLink);
		shareLink = (TableRow) view.findViewById(R.id.shareLink);

		
		// restore saved instance gia na epanaferoume tis metavlites ekei pou
		// itan allios kanoume arxikopoiisi
		/**
		 * Xrisimopoieitai gia na kanoume elegxo an den exei ektelestei i oncretate alla mono i on create view
		 * pou ginetai otan apo to trito tab pame sto deutero tote na perasei tis iparxouses times stis metavlites afou
		 * den exoun allaksei kai na min perasei tis kainourgies pou einai lathos
		 */
		if (onCreateExecuted) {
			defaultTextHeight = markerInfoView.getLayoutParams().height;
			if (savedInstanceState != null) {
				Log.i(TAG, "Restore values!");
				buttonText = savedInstanceState.getString("Button Text");
				textHeight = savedInstanceState.getInt("Text Height");
				readMoreButton.setText(buttonText);
				markerInfoView.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, textHeight));
				final int[] position = savedInstanceState
						.getIntArray("Scroll Position");
				if (position != null)
					scrollView.post(new Runnable() {
						public void run() {
							scrollView.scrollTo(position[0], position[1]);
						}
					});
			} else {
				Log.i(TAG, "Initialize values!");
				textHeight = defaultTextHeight;
				readMoreButton.setText(buttonText);
			}
		}else{
			readMoreButton.setText(buttonText);
			markerInfoView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, textHeight));
			if (defaultScrollPosition != null)
				scrollView.post(new Runnable() {
					public void run() {
						scrollView.scrollTo(defaultScrollPosition[0], defaultScrollPosition[1]);
					}
				});
		}
		wikipediaLink.setOnClickListener(this);
		navigationLink.setOnClickListener(this);
		shareLink.setOnClickListener(this);
		readMoreButton.setOnClickListener(this);
		onCreateExecuted = false;

		return view;
	}

	/**
	 * to getActivity epistrefei tin activity pou kalerse to fragment kai einai
	 * isi me context se autin tin periptwsi
	 */

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "On Activity created is being called");
		markerId = ((PoiActivity) getActivity()).getMarkerId();
		db = new DBHandler(getActivity());
		try {
			db.open();

			Cursor cursor = db.getPoi(markerId);

			if (cursor != null)
				setMarkerVariables(cursor);

			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**to xrisimopoiw gia na diorthwsw to problima me to trito tab
	 * apothikeuw to scrolarisma gia meta.
	 * Provlima me to 3o tab
	 * 
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		defaultScrollPosition[0] = scrollView.getScrollX();
		defaultScrollPosition[1] = scrollView.getScrollY();
	}

	private void setMarkerVariables(Cursor cursor) {
		cursor.moveToFirst();
		this.markerName = cursor.getString(0);
		this.markerLat = cursor.getDouble(1);
		this.markerLng = cursor.getDouble(2);
		this.markerLink = cursor.getString(3);
		this.markerAddress = cursor.getString(4);
		this.markerDesc = cursor.getString(5);
		this.markerResName = cursor.getString(6);

		setGuiExtraInfo();
	}

	private void setGuiExtraInfo() {
		markerMainImageView.setImageResource(getResources().getIdentifier(
				markerResName, "drawable", "cut.ac.cy.my_tour_guide"));
		markerTitleView.setText(markerName);
		markerAddressView.setText(markerAddress);
		markerLinkView1.setText(markerLink);
		markerLinkView2.setText(markerAddress);
		markerLinkView3.setText(markerName);
		markerInfoView.setText(markerDesc);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.wikipediaLink:
			Intent webIntent = new Intent();
			webIntent.setClass(getActivity(), PoiBrowser.class);
			webIntent.putExtra("Link", markerLink);
			startActivity(webIntent);
			break;
		case R.id.navigationLink:
			if (isGoogleMapsInstalled()) {
				String uri = "http://maps.google.com/maps?saddr="
						+ currentLocationLat + "," + currentLocationLng
						+ "&daddr=" + markerLat + "," + markerLng + "";
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(uri));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "Google maps are not installed",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.shareLink:
			String shareBody = getShareBody();

			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					markerName);
			sharingIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
			break;
		case R.id.readMoreButton:
			changeTextSize(textHeight);
			break;
		}
	}

	private String getShareBody() {
		String shareBody = "Link: " + markerLink + "\n";
		shareBody += "\nAddress: " + markerAddress + "\n";
		shareBody += "\nDescription:" + markerDesc + "\n";
		return shareBody;
	}

	private void changeTextSize(int tHeight) {
		if (tHeight == LinearLayout.LayoutParams.WRAP_CONTENT) {
			markerInfoView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, defaultTextHeight));
			buttonText = "Show more";
		} else if (tHeight == defaultTextHeight) {
			markerInfoView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			buttonText = "Show less";
		}
		textHeight = markerInfoView.getLayoutParams().height;
		readMoreButton.setText(buttonText);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "Save Instance");
		super.onSaveInstanceState(outState);

		outState.putInt("Text Height", textHeight);
		outState.putString("Button Text", buttonText);
		if (scrollView == null) {
			outState.putIntArray("Scroll Position", new int[] {
					defaultScrollPosition[0], defaultScrollPosition[1] });
		} else {
			outState.putIntArray(
					"Scroll Position",
					new int[] { scrollView.getScrollX(),
							scrollView.getScrollY() });
		}
	}
	
	
	
	/**
	 * check if google maps are installed if not the activity won't start
	 */
	public boolean isGoogleMapsInstalled() {
		try {
			getActivity().getPackageManager().getApplicationInfo(
					"com.google.android.apps.maps", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
}

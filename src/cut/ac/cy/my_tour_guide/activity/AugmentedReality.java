package cut.ac.cy.my_tour_guide.activity;

import java.text.DecimalFormat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.camera.Preview;
import cut.ac.cy.my_tour_guide.capture_image.CaptureImage;
import cut.ac.cy.my_tour_guide.data.ARData;
import cut.ac.cy.my_tour_guide.data.LocalDataSource;
import cut.ac.cy.my_tour_guide.maps.MapActivity;
import cut.ac.cy.my_tour_guide.poi.PoiActivity;
import cut.ac.cy.my_tour_guide.ui.Marker;

/**
 * This class extends the SensorsActivity and is designed tie the AugmentedView
 * and zoom bar together.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class AugmentedReality extends SensorsActivity implements
		OnTouchListener, OnClickListener {
	private static final String TAG = "AugmentedReality";
	private static final int REQUEST_CODE = 1;
	public static final String PREFS_NAME = "VariableStorage";
	private int photoNumInc;
	private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

	private static final String END_TEXT = FORMAT
			.format(AugmentedReality.MAX_ZOOM) + " km";

	protected static WakeLock wakeLock = null;
	protected static Preview camPreview = null;
	protected static Camera camera = null;
	protected static SeekBar myZoomBar = null;
	protected static TextView zoomBarText = null;
	protected static Button captureButton = null;
	protected static Button gMapsButton = null;
	protected static Button menuButton = null;
	protected static String[] menuItemsValues = { "Pois Categories",
			"Hide Radar", "Show zoombar", "Gps Settings", "Exit" };
	// protected static VerticalTextView endLabel = null;
	protected static RelativeLayout zoomLayout = null;
	protected static AugmentedView augmentedView = null;
	private static LocalDataSource localData;

	public static final float MAX_ZOOM = 20; // in KM
	// ti kanoun auta?
	public static final float ONE_PERCENT = MAX_ZOOM / 100f;
	public static final float TEN_PERCENT = 10f * ONE_PERCENT;
	public static final float TWENTY_PERCENT = 2f * TEN_PERCENT;
	public static final float EIGHTY_PERCENTY = 4f * TWENTY_PERCENT;

	public static boolean portrait = false;
	public static boolean useCollisionDetection = true;
	public static boolean showRadar = true;
	public static boolean showZoomBar = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// eginan allages kai mpike dikos mou kwdikas

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.home_screen);

		camPreview = (Preview) findViewById(R.id.cameraPreview);
		captureButton = (Button) findViewById(R.id.buttonCamera);
		gMapsButton = (Button) findViewById(R.id.buttonMaps);
		menuButton = (Button) findViewById(R.id.buttonMenu);
		// set up listeners
		// buttonListeners();
		captureButton.setOnClickListener(this);
		gMapsButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);

		FrameLayout liveLayout = (FrameLayout) findViewById(R.id.liveImage);

		augmentedView = new AugmentedView(this);
		augmentedView.setOnTouchListener(this);
		augmentedView.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		liveLayout.addView(augmentedView);

		myZoomBar = (SeekBar) findViewById(R.id.zoombar);
		zoomBarText = (TextView) findViewById(R.id.zoombarText);
		myZoomBar.setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);
		zoomBarText.setText(END_TEXT);
		zoomLayout = (RelativeLayout) findViewById(R.id.zoomLayout);
		zoomLayout.setVisibility(RelativeLayout.GONE);

		updateDataOnZoom();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
		localData = new LocalDataSource(this.getResources(),
				getApplicationContext());

		ARData.addMarkers(localData.getMarkers()); // edw se perptwsi pou einai
													// oloi miden epistrefei
													// tous markers oi opoioi
													// den einai
													// null alla einai miden ara
													// kanei return i
													// getMarkers()
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		camera = Camera.open();

		// CameraDisplay.setCameraDisplayOrientation(this,
		// Camera.CameraInfo.CAMERA_FACING_BACK, camera);

		camPreview.setCamera(camera);
		wakeLock.acquire();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
		if (camera != null) {
			camPreview.setCamera(null);
			camera.release();
			camera = null;
		}
		wakeLock.release();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("selectedCategories")) {
				long[] categoriesId = data.getExtras().getLongArray(
						"selectedCategories");
				ARData.addCategorizedMarkers(localData
						.getCategorizedMarkers(categoriesId));
				Toast.makeText(this, "Update Markers", Toast.LENGTH_LONG)
						.show();
			}
		} else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE) {
			ARData.addCategorizedMarkers(localData.getMarkers()); // otan einai ola ta categories apenergopoimena tote epistrefei
																	// markers pou den einai null alla size = 0. tote stin addcategorizedmarkers
																	// diagrafei olous tous markers apo tin lista
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSensorChanged(SensorEvent evt) {
		super.onSensorChanged(evt);

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER
				|| evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			augmentedView.postInvalidate();
		}
	}

	private OnSeekBarChangeListener myZoomBarOnSeekBarChangeListener = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			updateDataOnZoom();
			camPreview.invalidate();
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			// Ignore
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			updateDataOnZoom();
			camPreview.invalidate();
		}
	};

	private static float calcZoomLevel() {
		int myZoomLevel = myZoomBar.getProgress();
		float myout = 0;

		float percent = 0;
		if (myZoomLevel <= 25) {
			percent = myZoomLevel / 25f;
			myout = ONE_PERCENT * percent;
		} else if (myZoomLevel > 25 && myZoomLevel <= 50) {
			percent = (myZoomLevel - 25f) / 25f;
			myout = ONE_PERCENT + (TEN_PERCENT * percent);
		} else if (myZoomLevel > 50 && myZoomLevel <= 75) {
			percent = (myZoomLevel - 50f) / 25f;
			myout = TEN_PERCENT + (TWENTY_PERCENT * percent);
		} else {
			percent = (myZoomLevel - 75f) / 25f;
			myout = TWENTY_PERCENT + (EIGHTY_PERCENTY * percent);
		}

		return myout;
	}

	/**
	 * Called when the zoom bar has changed.
	 */
	protected void updateDataOnZoom() {
		float zoomLevel = calcZoomLevel();
		ARData.setRadius(zoomLevel);
		ARData.setZoomLevel(FORMAT.format(zoomLevel));
		ARData.setZoomProgress(myZoomBar.getProgress());
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean onTouch(View view, MotionEvent me) {
		// See if the motion event is on a Marker
		for (Marker marker : ARData.getMarkers()) {
			if (marker.handleClick(me.getX(), me.getY())) {
				if (me.getAction() == MotionEvent.ACTION_UP)
					markerTouched(marker);
				return true;
			}
		}

		return super.onTouchEvent(me);
	};

	protected void markerTouched(Marker marker) {
		Intent intent = new Intent(this, PoiActivity.class);
		intent.putExtra("Id", marker.getId());
		intent.putExtra("Res Name", marker.getResName());

		startActivity(intent);
		Log.w(TAG, "markerTouched() not implemented.");
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.buttonCamera:
			// retrieve photonum gia na to steiloume stin camera gia na to
			// apothikeusei stin photo afou to auksisei kata 1
			SharedPreferences appPrefs = getSharedPreferences(PREFS_NAME,
					MODE_PRIVATE);
			// getapplicationcontext() gia metepeita xrisi apo to scanfile
			CaptureImage image = new CaptureImage(getApplicationContext());
			photoNumInc = appPrefs.getInt("photoIncrement", 1);
			if (camera != null) {
				image.takePicture(camera, photoNumInc);
			}
			// save photo number to preffile gia na diatirithei to noumero
			// meta apo kill
			SharedPreferences appPrefsEdit = getSharedPreferences(PREFS_NAME,
					MODE_PRIVATE);
			SharedPreferences.Editor editor = appPrefsEdit.edit();
			System.out.println("Photo increment" + image.getPhotoNum());
			editor.putInt("photoIncrement", image.getPhotoNum());

			// Commit the edits!
			editor.commit();
			break;
		case R.id.buttonMaps:
			// elegxos an yparxoun ta google play services sto kinito kai einai
			// updated kai stin idia version
			// me tis efarmogis
			int result = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getApplicationContext());
			if (result == ConnectionResult.SUCCESS) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MapActivity.class);
				startActivity(intent);
			} else {
				GooglePlayServicesUtil.getErrorDialog(result, this, 1);
			}
			break;
		case R.id.buttonMenu:
			createMenuDialog();
			break;
		}

	}

	private void createMenuDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Menu options");
		builder.setItems(menuItemsValues, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
					switch(which){
					case 0:
						Intent intent = new Intent(getApplicationContext(), MarkersCategories.class);
						startActivityForResult(intent, REQUEST_CODE);
						dialog.dismiss();
						break;
					case 1:
						showRadar = !showRadar;
						menuItemsValues[which] = (((showRadar) ? "Hide" : "Show") + " Radar");
						dialog.dismiss();
					break;
					case 2:
						showZoomBar = !showZoomBar;
						menuItemsValues[which] = (((showZoomBar) ? "Hide" : "Show") + " Zoom Bar");
						zoomLayout.setVisibility((showZoomBar) ? RelativeLayout.VISIBLE
								: RelativeLayout.GONE);
						dialog.dismiss();
						break;
					case 3:
						startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dialog.dismiss();
						break;
					case 4:
						finish();
						break;
				}
				
			}
						
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}

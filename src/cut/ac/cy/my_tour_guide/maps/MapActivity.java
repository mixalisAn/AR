package cut.ac.cy.my_tour_guide.maps;

import java.sql.SQLException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.data.ARData;
import cut.ac.cy.my_tour_guide.database.DBHandler;
import cut.ac.cy.my_tour_guide.dialogs.DownloadDialog;
import cut.ac.cy.my_tour_guide.helpers.MapMarker;
import cut.ac.cy.my_tour_guide.helpers.MapParcelableHashMap;
import cut.ac.cy.my_tour_guide.poi.ConnectionStatusReceiver;
import cut.ac.cy.my_tour_guide.poi.PoiActivity;
/**
 * 
 * @author Michail Anastasiou
 *
 */
public class MapActivity extends SherlockFragmentActivity implements OnInfoWindowClickListener{
	private static final String TAG = "MapActivity";
	private ConnectivityManager connManager = null;
	private ConnectionStatusReceiver connectionReceiver = null;

	private LatLng myCurrentLocation;
	private SupportMapFragment mapFragment;
	private UiSettings uiSettings;
	private GoogleMap map;
	private DBHandler db = null;
	private Marker selectedMarker;
	private static final int NO_INTERNET = 0;
	private static final int MOBILE_INTERNET = 1;
	private static final int INTERNET = 2;
	/**
	 * Xrisimopoiountai mono primatives giauto kai Integer. I java kanei autoboxing giauto eite valw
	 * map("kati", 3) eite map("kati", new Integer(3)) einai to idio
	 */
	private MapParcelableHashMap mapMarkers;

	private int mapDisplayOpt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.maps);

		connManager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		connectionReceiver = new ConnectionStatusReceiver();

		db = new DBHandler(this);

		mapFragment = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map));

		// an einai i prwti fora tote kane setRetainInstance kai min pareis to
		// map
		// apo to mapFragragment alla mesa apo to setMapifNeeded

		if (savedInstanceState == null) {
			mapFragment.setRetainInstance(true);
		} else {
			map = mapFragment.getMap();
			mapMarkers = savedInstanceState.getParcelable("Markers Data");
		}
		//na figoun auta logika den xreiazontai
		setMapIfNeeded();
		setMapSettings();
		
		map.setOnInfoWindowClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// set default map display
		SharedPreferences mapDisplay = PreferenceManager
				.getDefaultSharedPreferences(this);

		mapDisplayOpt = Integer.parseInt(mapDisplay.getString(
				"map_display_preference", "0"));
		setMapIfNeeded();
		setMapType(mapDisplayOpt);
		setMapSettings();
		boolean isConnected = checkNetworkConnection();
		if (!isConnected) {
			connectionAlert();
		}
		registerBroadcastReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterBroadcastReceiver();

	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("Markers Data", mapMarkers);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mapDisplay:
			mapDisplay();

			return true;
		case R.id.network_connection:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			return true;
		case R.id.settings:
			Intent intent = new Intent(this, MapSettings.class);
			startActivity(intent);
			return true;
		case R.id.info:
			Intent intent2 = new Intent(this, MapAbout.class);
			startActivity(intent2);
			return true;
		default:
			return super.onOptionsItemSelected(item); // se periptwsi pou den
														// xeirizomaste to menu
														// item prepei na
														// valoume auto
		}
	}

	// setting the pins
	private void setMapIfNeeded() {
		long markerId;
		double lat, lng;
		String title, markerRes, mapMarkerId, pastUrl, presentUrl;
		LatLng markerCoords;
		
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			mapMarkers = new MapParcelableHashMap();
			if (map != null) {
				Log.i(TAG, "Map initialization!");
				// my current location
				if(ARData.getCurrentLocation() == ARData.hardFix){
					lat = 34.677178;
					lng = 33.045097;
				}else{
					lat = ARData.getCurrentLocation().getLatitude();
					lng = ARData.getCurrentLocation().getLongitude();
				}
				myCurrentLocation = new LatLng(lat, lng);
				
				// map.addMarker(new
				// MarkerOptions().position(myCurrentLocation));
				// other markers location
				try {
					db.open();
					Cursor cursor = db.getPins();
					if (cursor != null) {
						cursor.moveToFirst();
						do {
							markerId = cursor.getLong(0);
							title = cursor.getString(1);
							lat = cursor.getDouble(2);
							lng = cursor.getDouble(3);
							markerRes = cursor.getString(4);
							pastUrl = cursor.getString(5);
							presentUrl = cursor.getString(6);
							markerCoords = new LatLng(lat, lng);
							//pernei to id apo ton mapMarker diaforetiko tou marker pou vazei ekeini tin wra kai to pernaei sto hashmap
							mapMarkerId = map.addMarker(new MarkerOptions().position(markerCoords)
									.title(title).snippet("Click to see more info!")).getId();
							mapMarkers.put(mapMarkerId, new MapMarker(markerId, markerRes, pastUrl, presentUrl));
						} while (cursor.moveToNext());
					}

					db.close();
					cursor.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(
						myCurrentLocation, 15));
				// Zoom in, animating the camera.
				//map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
				map.setMyLocationEnabled(true);
				
			}
		}

	}

	private void setMapSettings() {
		uiSettings = map.getUiSettings();
		SharedPreferences mapSettings = PreferenceManager
				.getDefaultSharedPreferences(this);
		Log.i(TAG, "Shared Preferences " + mapSettings);
		uiSettings.setZoomControlsEnabled(mapSettings.getBoolean(
				"zoom_buttons", true));
		uiSettings.setCompassEnabled(mapSettings.getBoolean("compass", true));
		uiSettings.setMyLocationButtonEnabled(mapSettings.getBoolean(
				"mylocation", true));
		uiSettings.setZoomGesturesEnabled(mapSettings.getBoolean(
				"zoom_gesture", true));
		uiSettings.setScrollGesturesEnabled(mapSettings.getBoolean(
				"scroll_gesture", true));
		uiSettings.setRotateGesturesEnabled(mapSettings.getBoolean(
				"rotate_gesture", true));
		uiSettings.setTiltGesturesEnabled(mapSettings.getBoolean(
				"tilt_gesture", true));
	}

	public void mapDisplay() {
		final String[] choices = { "Normal", "Hybrid", "Satellite", "Terrain" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog title
		builder.setTitle(R.string.mapDisplay).setSingleChoiceItems(choices,
				mapDisplayOpt, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int selection) {

						mapDisplayOpt = selection;
						setMapType(mapDisplayOpt);
						dialog.dismiss();

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setMapType(int type) {
		SharedPreferences mapDisplay = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		SharedPreferences.Editor editor = mapDisplay.edit();
		
		switch (type) {
		case 0:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			editor.putString("map_display_preference", String.valueOf(type));
			editor.commit();
			break;
		case 1:
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			editor.putString("map_display_preference", String.valueOf(type));
			editor.commit();
			break;
		case 2:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			editor.putString("map_display_preference", String.valueOf(type));
			editor.commit();
			break;
		case 3:
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			editor.putString("map_display_preference", String.valueOf(type));
			editor.commit();
			break;
		}
	}

	// Show alert dialog for network disconnected
	public void connectionAlert() {

		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setCancelable(true);
		// 2. Chain together various setter methods to set the dialog
		// characteristics
		builder.setMessage("No internet connection!").setTitle("Info");

		// Add the buttons
		builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				return;

			}
		});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private boolean checkNetworkConnection() {
		// Details gia to active network
		NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

		boolean isConnected = ((activeNetwork != null) && (activeNetwork
				.isConnectedOrConnecting()));

		return isConnected;
	}

	private int checkNetworkConnectionAndType() {
		final ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isConnected()) {
            return 0;
        }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            return 1;
        }else{
        	return 2;
        }
	}
	
	// register broadcast receiver
	public void registerBroadcastReceiver() {
		this.registerReceiver(connectionReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
		Log.i(TAG, "Register connection status Receiver");
	}

	public void unregisterBroadcastReceiver() {
		this.unregisterReceiver(connectionReceiver);
		Log.i(TAG, "Unregister connection status Receiver");
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		selectedMarker = marker;

		switch(checkNetworkConnectionAndType()){
		case NO_INTERNET:
			startPoiActivity(false);
			break;
		case MOBILE_INTERNET:
			DownloadDialog dialog = new DownloadDialog();
			dialog.show(getSupportFragmentManager(), "Dialog Download");
			
			break;
		case INTERNET:
			startPoiActivity(true);
			break;
			default:
				startPoiActivity(true);
				break;
		}
	}

	private void startPoiActivity(boolean download){
		Intent intent = new Intent(this, PoiActivity.class);
		intent.putExtra("Id", mapMarkers.get(selectedMarker.getId()).getId());
		intent.putExtra("Res Name", mapMarkers.get(selectedMarker.getId()).getResName());
		intent.putExtra("Compare Urls", mapMarkers.get(selectedMarker.getId()).getCompareUrls());
		intent.putExtra("download", download);

		startActivity(intent);
	}
	
	public void networkDialogSelection(int selection){
		switch(selection){
		case DialogInterface.BUTTON_POSITIVE:
			startPoiActivity(true);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			startPoiActivity(false);
			break;
			default:
				startPoiActivity(true);
				break;
		}
	}
	
}

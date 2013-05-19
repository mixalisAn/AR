package cut.ac.cy.my_tour_guide.activity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.common.LowPassFilter;
import cut.ac.cy.my_tour_guide.common.Matrix;
import cut.ac.cy.my_tour_guide.data.ARData;

/**
 * This class extends Activity and processes sensor data and location data.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class SensorsActivity extends SherlockFragmentActivity implements
		SensorEventListener {

	private static final String TAG = "SensorsActivity";
	private static final AtomicBoolean computing = new AtomicBoolean(false);

	private static final int TWO_MINUTES = 1000 * 60 * 2; // einai gia to best
															// locationUpdate
	private static final int MIN_TIME = 30 * 1000;
	private static final int MIN_DISTANCE = 3;
	private static Location currentBestLocation;

	private static final float temp[] = new float[9]; // Temporary rotation
														// matrix in Android
														// format
	private static final float rotation[] = new float[9]; // Final rotation
															// matrix in Android
															// format
	private static final float grav[] = new float[3]; // Gravity (a.k.a
														// accelerometer data)
	private static final float mag[] = new float[3]; // Magnetic
	/*
	 * Using Matrix operations instead. This was way too inaccurate, private
	 * static final float apr[] = new float[3]; //Azimuth, pitch, roll
	 */

	private static final Matrix worldCoord = new Matrix();
	private static final Matrix magneticCompensatedCoord = new Matrix();
	private static final Matrix xAxisRotation = new Matrix();
	private static final Matrix yAxisRotation = new Matrix();
	private static final Matrix mageticNorthCompensation = new Matrix();

	private static GeomagneticField gmf = null;
	private static float smooth[] = new float[3];
	private static SensorManager sensorMgr = null;
	private static List<Sensor> sensors = null;
	private static Sensor sensorGrav = null;
	private static Sensor sensorMag = null;
	private static LocationManager locationMgr = null;
	private static GpsLocationListener gpsListener;
	private static NetworkLocationListener networkListener;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gpsListener = new GpsLocationListener();
		networkListener = new NetworkLocationListener();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart() {
		super.onStart();
		float neg90rads = (float) Math.toRadians(-90);

		// Counter-clockwise rotation at -90 degrees around the x-axis
		// [ 1, 0, 0 ]
		// [ 0, cos, -sin ]
		// [ 0, sin, cos ]
		xAxisRotation.set(1f, 0f, 0f, 0f, FloatMath.cos(neg90rads),
				-FloatMath.sin(neg90rads), 0f, FloatMath.sin(neg90rads),
				FloatMath.cos(neg90rads));

		// Counter-clockwise rotation at -90 degrees around the y-axis
		// [ cos, 0, sin ]
		// [ 0, 1, 0 ]
		// [ -sin, 0, cos ]
		yAxisRotation.set(FloatMath.cos(neg90rads), 0f,
				FloatMath.sin(neg90rads), 0f, 1f, 0f,
				-FloatMath.sin(neg90rads), 0f, FloatMath.cos(neg90rads));

		try {
			sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if (sensors.size() > 0)
				sensorGrav = sensors.get(0);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
			if (sensors.size() > 0)
				sensorMag = sensors.get(0);

			sensorMgr.registerListener(this, sensorGrav,
					SensorManager.SENSOR_DELAY_UI);
			sensorMgr.registerListener(this, sensorMag,
					SensorManager.SENSOR_DELAY_UI);

			locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					MIN_TIME, MIN_DISTANCE, gpsListener);
			
			
			if(isGpsEnabled()){
				try{
					locationMgr.removeUpdates(networkListener);
				}catch(Exception e){
					//nothing here
				}
			}else{
				locationMgr.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
						networkListener);
			}
			try {

				try {
					Location gps = locationMgr
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					Location network = locationMgr
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (gps != null)
						//onLocationChanged(gps);
						updateLocation(gps);
					else if (network != null)
						//onLocationChanged(network);
						updateLocation(network);
					else
						//onLocationChanged(ARData.hardFix);
						updateLocation(ARData.hardFix);
				} catch (Exception ex2) {
					//onLocationChanged(ARData.hardFix);
					updateLocation(ARData.hardFix);
				}
				
				gmf = new GeomagneticField((float) ARData.getCurrentLocation()
						.getLatitude(), (float) ARData.getCurrentLocation()
						.getLongitude(), (float) ARData.getCurrentLocation()
						.getAltitude(), System.currentTimeMillis());

				float dec = (float) Math.toRadians(-gmf.getDeclination());

				synchronized (mageticNorthCompensation) {
					// Identity matrix
					// [ 1, 0, 0 ]
					// [ 0, 1, 0 ]
					// [ 0, 0, 1 ]
					mageticNorthCompensation.toIdentity();

					// Counter-clockwise rotation at negative declination around
					// the y-axis
					// note: declination of the horizontal component of the
					// magnetic field
					// from true north, in degrees (i.e. positive means the
					// magnetic
					// field is rotated east that much from true north).
					// note2: declination is the difference between true north
					// and magnetic north
					// [ cos, 0, sin ]
					// [ 0, 1, 0 ]
					// [ -sin, 0, cos ]
					mageticNorthCompensation.set(FloatMath.cos(dec), 0f,
							FloatMath.sin(dec), 0f, 1f, 0f,
							-FloatMath.sin(dec), 0f, FloatMath.cos(dec));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex1) {
			try {
				if (sensorMgr != null) {
					sensorMgr.unregisterListener(this, sensorGrav);
					sensorMgr.unregisterListener(this, sensorMag);
					sensorMgr = null;
				}
				if (locationMgr != null) {
					locationMgr.removeUpdates(gpsListener);
					locationMgr.removeUpdates(networkListener);
					locationMgr = null;
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStop() {
		super.onStop();

		try {
			try {
				sensorMgr.unregisterListener(this, sensorGrav);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				sensorMgr.unregisterListener(this, sensorMag);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sensorMgr = null;

			try {
				locationMgr.removeUpdates(gpsListener);
				locationMgr.removeUpdates(networkListener);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			locationMgr = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSensorChanged(SensorEvent evt) {
		if (!computing.compareAndSet(false, true))
			return;

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			smooth = LowPassFilter.filter(0.5f, 1.0f, evt.values, grav);
			grav[0] = smooth[0];
			grav[1] = smooth[1];
			grav[2] = smooth[2];
		} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			smooth = LowPassFilter.filter(2.0f, 4.0f, evt.values, mag);
			mag[0] = smooth[0];
			mag[1] = smooth[1];
			mag[2] = smooth[2];
		}

		// // Find real world position relative to phone location ////
		// Get rotation matrix given the gravity and geomagnetic matrices
		SensorManager.getRotationMatrix(temp, null, grav, mag);

		// Translate the rotation matrices from Y and -Z (landscape)
		// SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y,
		// SensorManager.AXIS_MINUS_X, rotation);
		// SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X,
		// SensorManager.AXIS_MINUS_Z, rotation);
		int defaultRotation = getRotation();
	
		if(defaultRotation == Configuration.ORIENTATION_LANDSCAPE){
			SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Z,
					SensorManager.AXIS_Y, rotation);
		}else{
			SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y,
					SensorManager.AXIS_MINUS_Z, rotation);
		}
		

		/*
		 * Using Matrix operations instead. This was way too inaccurate, //Get
		 * the azimuth, pitch, roll SensorManager.getOrientation(rotation,apr);
		 * float floatAzimuth = (float)Math.toDegrees(apr[0]); if
		 * (floatAzimuth<0) floatAzimuth+=360; ARData.setAzimuth(floatAzimuth);
		 * ARData.setPitch((float)Math.toDegrees(apr[1]));
		 * ARData.setRoll((float)Math.toDegrees(apr[2]));
		 */

		// Convert from float[9] to Matrix
		worldCoord
				.set(rotation[0], rotation[1], rotation[2], rotation[3],
						rotation[4], rotation[5], rotation[6], rotation[7],
						rotation[8]);

		// // Find position relative to magnetic north ////
		// Identity matrix
		// [ 1, 0, 0 ]
		// [ 0, 1, 0 ]
		// [ 0, 0, 1 ]
		magneticCompensatedCoord.toIdentity();

		synchronized (mageticNorthCompensation) {
			// Cross product the matrix with the magnetic north compensation
			magneticCompensatedCoord.prod(mageticNorthCompensation);
		}

		// The compass assumes the screen is parallel to the ground with the
		// screen pointing
		// to the sky, rotate to compensate.
		magneticCompensatedCoord.prod(xAxisRotation);

		// Cross product with the world coordinates to get a mag north
		// compensated coords
		magneticCompensatedCoord.prod(worldCoord);

		// Y axis
		magneticCompensatedCoord.prod(yAxisRotation);

		// Invert the matrix since up-down and left-right are reversed in
		// landscape mode
		magneticCompensatedCoord.invert();

		// Set the rotation matrix (used to translate all object from lat/lon to
		// x/y/z)
		ARData.setRotationMatrix(magneticCompensatedCoord);

		computing.set(false);
	}

	private class GpsLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			updateLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	}
	
	private class NetworkLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			updateLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor == null)
			throw new NullPointerException();

		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			Log.e(TAG, "Compass data unreliable");
		}
	}
	
	public void updateLocation(Location location) {
		if (checkBestLocationUpdate(location)) {
			currentBestLocation = location;
			TextView provider = (TextView) findViewById(R.id.providerTextView);
			TextView accuracy = (TextView) findViewById(R.id.providerAccuracyTextView);
			/*
			 * TextView gpsAltitude =
			 * (TextView)findViewById(R.id.mylocationTestTextView);
			 * gpsAltitude.setText("Gps: " +
			 * String.valueOf(location.getAltitude()));*/
			 
			provider.setText(location.getProvider());
			accuracy.setText("+/- " + String.valueOf(location.getAccuracy()));

			ARData.setCurrentLocation(location);
			gmf = new GeomagneticField((float) ARData.getCurrentLocation()
					.getLatitude(), (float) ARData.getCurrentLocation()
					.getLongitude(), (float) ARData.getCurrentLocation()
					.getAltitude(), System.currentTimeMillis());

			float dec = (float) Math.toRadians(-gmf.getDeclination());

			synchronized (mageticNorthCompensation) {
				mageticNorthCompensation.toIdentity();

				mageticNorthCompensation.set(FloatMath.cos(dec), 0f,
						FloatMath.sin(dec), 0f, 1f, 0f, -FloatMath.sin(dec),
						0f, FloatMath.cos(dec));
			}
		}
		
	}

	private boolean checkBestLocationUpdate(Location location) {
		if (currentBestLocation == null)
			return true;

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;
		
		// If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    //boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 90;

	    // Check if the old and new location are from the same provider
	    //boolean isFromSameProvider = isSameProvider(location.getProvider(),
	      //      currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate) {
	        return true;
	    } //else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        //return true;
	    //}
	    return false;

	}
	/*
	/** Checks whether two providers are the same 
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	*/
	private boolean isGpsEnabled(){
		/*final boolean gpsEnabled =
				 locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!gpsEnabled){
			GpsSettingsDialog dialog = new GpsSettingsDialog();
			dialog.show(getSupportFragmentManager(), "GpsSettings Dialog");
		}*/
		return locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	
	private int getRotation() {
		WindowManager lWindowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);

		Configuration cfg = getResources().getConfiguration();
		int lRotation = lWindowManager.getDefaultDisplay().getRotation();

		if( (((lRotation == Surface.ROTATION_0) ||(lRotation == Surface.ROTATION_180)) &&   
		(cfg.orientation == Configuration.ORIENTATION_LANDSCAPE)) ||
		(((lRotation == Surface.ROTATION_90) ||(lRotation == Surface.ROTATION_270)) &&    
		(cfg.orientation == Configuration.ORIENTATION_PORTRAIT))){

		  return Configuration.ORIENTATION_LANDSCAPE;
		  }     

		  return Configuration.ORIENTATION_PORTRAIT;
		}
}

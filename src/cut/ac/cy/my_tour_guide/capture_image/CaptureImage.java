package cut.ac.cy.my_tour_guide.capture_image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CaptureImage {
	private static final String TAG = "CaptureImageActivity";
	private String album = "My Tour Guide";
	private String folder = "Camera pictures";
	private String photoName = "DSC_1000";
	private int photoNum;
	Context context;
	Camera mCamera;
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;

	public CaptureImage(Context context) {
		this.context = context;
	}

	public void takePicture(Camera camera, int photoNum) {
		mCamera = camera;
		this.photoNum = photoNum;
		this.photoNum++;
		photoName += Integer.toString(photoNum);
		mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// TODO Do something when the shutter closes.
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Do something with the image RAW data.
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// check the status of external storageSystem.out.println(photoName + "\n");
			
			try {
				checkExternalStorage();
			
				File path = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
								+ File.separator + album);

				try {
					// make sure the picture directory exists
					if (!path.exists()) {
						path.mkdirs();
					} 
						path = new File(path + File.separator + folder);
						if (!path.exists()) {
							path.mkdirs();
						}
					
					File file = new File(path, photoName + ".jpg");

					OutputStream outStream = new FileOutputStream(file);
					outStream.write(data);
					outStream.close();
					// Tell the media scanner about the new file so that it is
					// immediately available to the user.
					MediaScannerConnection
							.scanFile(
									context,
									new String[] { file.toString() },
									null,
									new MediaScannerConnection.OnScanCompletedListener() {
										public void onScanCompleted(
												String path, Uri uri) {
											Log.i("ExternalStorage", "Scanned "
													+ path + ":");
											Log.i("ExternalStorage", "-> uri="
													+ uri);
										}
									});
				} catch (FileNotFoundException e) {
					Log.e(TAG, "File Note Found", e);
				} catch (IOException e) {
					Log.e(TAG, "IO Exception", e);
				} finally {
					// restart frozen camera preview after picture taken
					mCamera.startPreview();
				}
			} catch (ExternalStorageException e1) {
				mCamera.startPreview();
				e1.printStackTrace();
			}
		}
	};

	// get the incremented photo num
	public int getPhotoNum() {
		return photoNum;
	}

	public void checkExternalStorage() throws ExternalStorageException {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;

			Toast.makeText(context, "External Storage Hasn't Write Licence",
					Toast.LENGTH_LONG).show();

			throw new ExternalStorageException("Non writable external storage.");
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;

			Toast.makeText(context, "External Storage Not Available",
					Toast.LENGTH_LONG).show();

			throw new ExternalStorageException("No external storage available.");
		}
	}

}

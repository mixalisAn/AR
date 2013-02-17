package cut.ac.cy.my_tour_guide.gallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

public class ImagesSave {
	private Context context;
	private String[] imagesUrls;
	private String album;
	private ImageCache imageCache;
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	private FragmentManager fm;

	public ImagesSave() {

	}

	public ImagesSave(Context cx, FragmentManager fm, String[] images,
			String album) {
		context = cx;
		this.fm = fm;
		imagesUrls = images;
		this.album = album;
	}

	public void saveImages() {
		imageCache = ImageCache.findOrCreateCache(fm, context);
		new CopyImagesFromDiskCache().execute(imagesUrls);
		MediaScannerConnection
		.scanFile(
				context,
				new String[] { getFilePath() + "/*".toString() },
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
		Toast.makeText(context, "Succesfully saved", Toast.LENGTH_LONG).show();
	}

	public File getFilePath() {
		File path = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ File.separator + album);
	

		if (path != null) {
			if (!path.exists()) {
				path.mkdirs();
			}
		}
		return path;
	}

	private class CopyImagesFromDiskCache extends AsyncTask<String, Void, Void> {
		private Bitmap bitmap;
		private File path;
		private File file;

		@Override
		protected Void doInBackground(String... urls) {
			try {
				checkExternalStorage();
				
				path = getFilePath();
				//get bitmap from diskCache and save to the disk
				for (int i = 0; i < urls.length; i++) {
					bitmap = imageCache.getBitmapFromDiskCache(urls[i]);
					if(bitmap !=null){
						file = new File(path, getImageName(urls[i]) + ".jpg");
						FileOutputStream fOut = new FileOutputStream(file);

					    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					    fOut.flush();
					    fOut.close();
					}else{
						bitmap = ImageDownloader.downloadBitmap(urls[i]);
						file = new File(path, getImageName(urls[i]) + ".jpg");
						FileOutputStream fOut = new FileOutputStream(file);

					    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
					    fOut.flush();
					    fOut.close();
					}
				}
				
			} catch (ExternalStorageException e) {
				cancel(true);
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				cancel(true);
				e.printStackTrace();
			} catch (IOException e) {
				cancel(true);
				e.printStackTrace();
			}
			
			
			return null;
		}

	}
	//get the name of the image from url
	public String getImageName(String url){
		int sep;
		int dot;
		
		sep = url.lastIndexOf("/");
		dot = url.lastIndexOf(".");
		
		return url.substring(sep + 1, dot);
		
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

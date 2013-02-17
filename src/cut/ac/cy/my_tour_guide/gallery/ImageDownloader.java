/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cut.ac.cy.my_tour_guide.gallery;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import cut.ac.cy.my_tour_guide.BuildConfig;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {
	private static final String LOG_TAG = "ImageDownloader";
	private static final String HTTP_TAG = "HttpDisk";
	private static String FILENAME = "temp_file";
	private String damagedUrl = null;
	private ErrorImages errorImage;
	private List<ErrorImages> errorImages = new ArrayList<ErrorImages>();
	private static Activity activity;
	private static Context cx = null;
	private ImageCache imCache = null;
	// http cache
	private DiskLruCache HttpDiskCache;
	private final Object HttpDiskCacheLock = new Object();
	private boolean HttpDiskCacheStarting = true;
	private static final String DISK_CACHE_SUBDIR = "http";

	// Default disk cache size
	private static final int HTTP_DISK_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
	private static final int IO_BUFFER_SIZE = 8 * 1024;
	private static final int DISK_CACHE_INDEX = 0;

	private static final int MESSAGE_CLEAR = 0;

	public ImageDownloader(Activity a, FragmentManager fm) {
		activity = a;
		cx = activity.getBaseContext();
		// psaxnei na vrei an iparxei cache apo to fragment retain kata tin
		// allagi orientation
		imCache = ImageCache.findOrCreateCache(fm, cx);
		File diskCacheDir = ImageCache.getDiskCacheDir(cx, DISK_CACHE_SUBDIR);
		if (diskCacheDir != null) {
			if (!diskCacheDir.exists()) {
				diskCacheDir.mkdirs();
			}
		}
		new InitDiskCache().execute(diskCacheDir);
	}

	class InitDiskCache extends AsyncTask<File, Void, Void> {
		// Set up disk cache
		File diskCacheDir;

		@Override
		protected Void doInBackground(File... params) {
			synchronized (HttpDiskCacheLock) {

				diskCacheDir = params[0];
				if (ImageCache.getUsableSpace(diskCacheDir) > HTTP_DISK_CACHE_SIZE) {
					try {
						HttpDiskCache = DiskLruCache.open(diskCacheDir, 1, 1,
								HTTP_DISK_CACHE_SIZE);
						HttpDiskCacheStarting = false;
						HttpDiskCacheLock.notifyAll();
						if (BuildConfig.DEBUG) {
							Log.d(HTTP_TAG, "Http Disk cache initialized");
						}
					} catch (final IOException e) {
						diskCacheDir = null;
						Log.e(HTTP_TAG, "initDiskCache - " + e);
					}

				}
			}
			return null;
		}
	}

	private Bitmap processBitmap(String data) {
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "processBitmap - " + data);
		}

		final String key = ImageCache.hashKeyForDisk(data);
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		DiskLruCache.Snapshot snapshot;
		synchronized (HttpDiskCacheLock) {
			// Wait for disk cache to initialize
			while (HttpDiskCacheStarting) {
				try {
					HttpDiskCacheLock.wait();
				} catch (InterruptedException e) {
				}
			}

			if (HttpDiskCache != null) {
				try {
					snapshot = HttpDiskCache.get(key);
					if (snapshot == null) {
						if (BuildConfig.DEBUG) {
							Log.d(LOG_TAG,
									"processBitmap, not found in http cache, downloading...");
						}
						DiskLruCache.Editor editor = HttpDiskCache.edit(key);
						if (editor != null) {
							if (downloadUrlToStream(data,
									editor.newOutputStream(DISK_CACHE_INDEX))) {
								editor.commit();
							} else {
								editor.abort();
							}
						}
						snapshot = HttpDiskCache.get(key);
					}
					if (snapshot != null) {
						fileInputStream = (FileInputStream) snapshot
								.getInputStream(DISK_CACHE_INDEX);
						fileDescriptor = fileInputStream.getFD();
					}
				} catch (IOException e) {
					Log.e(LOG_TAG, "processBitmap - " + e);
				} catch (IllegalStateException e) {
					Log.e(LOG_TAG, "processBitmap - " + e);
				} finally {
					if (fileDescriptor == null && fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}

		Bitmap bitmap = null;
		if (fileDescriptor != null) {
			DeviceProperties device = new DeviceProperties(activity);

			bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor,
					device.getDeviceWidth(), device.getDeviceHeight());
		}
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException e) {
			}
		}
		return bitmap;
	}

	protected Bitmap processBitmap(Object data) {
		return processBitmap(String.valueOf(data));
	}

	/**
	 * Download a bitmap from a URL and write the content to an output stream.
	 * 
	 * @param urlString
	 *            The URL to fetch
	 * @return true if successful, false otherwise
	 */
	public boolean downloadUrlToStream(String urlString,
			OutputStream outputStream) {
		disableConnectionReuseIfNecessary();
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(),
					IO_BUFFER_SIZE);
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			Log.e(LOG_TAG, "Error in downloadBitmap - " + e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
			}
		}
		return false;
	}

	/**
	 * Workaround for bug pre-Froyo, see here for more info:
	 * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	 */
	public static void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	/**
	 * Download the specified image from the Internet and binds it to the
	 * provided ImageView. The binding is immediate if the image is found in the
	 * cache and will be done asynchronously otherwise. A null bitmap will be
	 * associated to the ImageView if an error occurs.
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void download(String url, ImageView imageView) {

		Bitmap bitmap = imCache.getBitmapFromMemCache(url);

		if (bitmap == null) {
			forceDownload(url, imageView);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */

	
	private void forceDownload(String url, ImageView imageView) {
		// State sanity: url is guaranteed to never be null in
		// DownloadedDrawable and cache keys.
		if (url == null) {
			imageView.setImageDrawable(null);
			return;
		}

		if (cancelPotentialDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
					cx.getResources(), task);
			imageView.setImageDrawable(downloadedDrawable);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, url);
			} else {
				task.execute(url);
			}
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that
	 * case.
	 */
	private static boolean cancelPotentialDownload(String url,
			ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(
			ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public static Bitmap downloadBitmap(String url) {
		final int IO_BUFFER_SIZE = 4 * 1024;

		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = AndroidHttpClient.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();

					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					Log.e("Bitmap Download", "bitmap download....");
					DeviceProperties device = new DeviceProperties(activity);
					return BitmapFactory.decodeStream(new FlushedInputStream(
							inputStream));
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {

			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];

			Bitmap bitmap = imCache.getBitmapFromDiskCache(url);
			Log.e("Disk fetch url", url);

			if (bitmap == null) {
				Log.e("Disk fetch bitmap", url);
				bitmap = processBitmap(url);
			}
			imCache.addBitmapToMemoryCache(url, bitmap);
			imCache.addBitmapToDiskCache(url, bitmap);
			return bitmap;
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with
				// it
				// Or if we don't use any bitmap to task association
				// (NO_DOWNLOADED_DRAWABLE mode)
				if ((this == bitmapDownloaderTask)) {
					if (url.equals(damagedUrl)) {
						errorImage = new ErrorImages();
						errorImage.setErrorImage(url, imageView);
						errorImages.add(errorImage);
					}
					if (imageView == null) {
						Log.e("Image View", "null imageview");
					}
					imageView.setImageBitmap(bitmap);
					damagedUrl = null;
				} else {
					Log.e("Task", "error");
				}
			}
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download
	 * is in progress.
	 * 
	 * <p>
	 * Contains a reference to the actual download task, so that a download task
	 * can be stopped if a new binding is required, and makes sure that only the
	 * last started download process can bind its result, independently of the
	 * download finish order.
	 * </p>
	 */
	private static class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapWorkerTaskReference;

		public DownloadedDrawable(Resources res,
				BitmapDownloaderTask bitmapWorkerTask) {
			super(res);
			bitmapWorkerTaskReference = new WeakReference<BitmapDownloaderTask>(
					bitmapWorkerTask);
		}

		public BitmapDownloaderTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/*
	 * static class DownloadedDrawable extends ColorDrawable { private final
	 * WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;
	 * 
	 * public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
	 * super(Color.BLACK); bitmapDownloaderTaskReference = new
	 * WeakReference<BitmapDownloaderTask>( bitmapDownloaderTask); }
	 * 
	 * public BitmapDownloaderTask getBitmapDownloaderTask() { return
	 * bitmapDownloaderTaskReference.get(); } }
	 */
	// ipologizei to poso mikroteri prepei na kanei tin eikona gia na xwraei
	// stin othoni tis siskeyis kai na min gemizei tin mnimi
	// na ginoune merikes allages ston algorithmo
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		System.out.println(options.inSampleSize + "\n");
		System.out.println(reqWidth + "\n");
		System.out.println(reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory
				.decodeFileDescriptor(fileDescriptor, null, options);
	}

	//
	/*
	 * palios kwdikas public static Bitmap decodeBitampFromResource(InputStream
	 * is, int reqWidth, int reqHeight) { // First decode with
	 * inJustDecodeBounds=true to check dimensions final BitmapFactory.Options
	 * options = new BitmapFactory.Options();
	 * 
	 * 
	 * // kanonika tha vriskei to megethos tis photo alla kapoio problima //
	 * iparxei me ton sixronismo
	 * 
	 * 
	 * 
	 * 
	 * // Calculate inSampleSize options.inSampleSize =
	 * calculateInSampleSize(800 , 1200, reqWidth, reqHeight);
	 * 
	 * System.out.println(options.inSampleSize + "\n");
	 * System.out.println(reqWidth + "\n"); System.out.println(reqHeight); //
	 * Decode bitmap with inSampleSize set options.inJustDecodeBounds = false;
	 * 
	 * 
	 * return BitmapFactory.decodeStream(new FlushedInputStream(is), null,
	 * options);
	 * 
	 * 
	 * 
	 * }
	 */
	public void redownloadErrorImages() {
		ErrorImages errorImage;
		Log.e("Redownload", String.valueOf(errorImages.size()));

		for (int i = 0; i < errorImages.size(); i++) {
			errorImage = errorImages.get(i);
			Log.e("Redownload", errorImage.getErrorUrl());
			download(errorImage.getErrorUrl(), errorImage.getErrorImageView());
		}
	}

	public void clearCache() {
		new CacheAsyncTask().execute();
	}

	protected class CacheAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			clearCacheInternal();
			return null;
		}

	}

	protected void clearCacheInternal() {
		if (imCache != null) {
			imCache.clearCache();
		}
	}
}

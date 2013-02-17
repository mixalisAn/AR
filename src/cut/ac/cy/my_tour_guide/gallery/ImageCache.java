package cut.ac.cy.my_tour_guide.gallery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cut.ac.cy.my_tour_guide.BuildConfig;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageCache {
	private static final String TAG = "ImageCache";
	private Context context = null;
	private LruCache<String, Bitmap> mMemoryCache;

	// disk cache variables
	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;
	private static final String DISK_CACHE_SUBDIR = "thumbnails";

	// Default disk cache size
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	// Compression settings when writing images to disk cache
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;
	private static final int DISK_CACHE_INDEX = 0;

	public ImageCache(Context cx) {
		context = cx;
		initCaches();
	}

	// find if there is an existing cache
	public static ImageCache findOrCreateCache(FragmentManager fm, Context cx) {

		final RetainFragment mRetainFragment = RetainFragment
				.findOrCreateRetainFragment(fm);

		// See if we already have an ImageCache stored in RetainFragment
		ImageCache imageCache = (ImageCache) mRetainFragment.getCache();

		// No existing ImageCache, create one and store it in RetainFragment
		if (imageCache == null) {
			Log.e(TAG, "No existing cache");
			imageCache = new ImageCache(cx);
			mRetainFragment.setCache(imageCache);
		}

		return imageCache;
	}

	public void initCaches() {
		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number
				// of items.
				return getSizeInBytes(bitmap);
			}
		};

		// Set up disk cache
		File diskCacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
		if (diskCacheDir != null) {
			if (!diskCacheDir.exists()) {
				diskCacheDir.mkdirs();
			}
		}
		new InitDiskCache().execute(diskCacheDir);
	}

	// init disk cache
	class InitDiskCache extends AsyncTask<File, Void, Void> {
		// Set up disk cache
		File diskCacheDir;

		@Override
		protected Void doInBackground(File... params) {
			synchronized (mDiskCacheLock) {
				if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
					diskCacheDir = params[0];
					if (getUsableSpace(diskCacheDir) > DEFAULT_DISK_CACHE_SIZE) {
						try {
							mDiskLruCache = DiskLruCache.open(diskCacheDir, 1,
									1, DEFAULT_DISK_CACHE_SIZE);
							mDiskCacheStarting = false;
							mDiskCacheLock.notifyAll();
							if (BuildConfig.DEBUG) {
								Log.d(TAG, "Disk cache initialized");
							}
						} catch (final IOException e) {
							diskCacheDir = null;
							Log.e(TAG, "initDiskCache - " + e);
						}
					}
				}
			}
			return null;
		}
	}

	// get disk case directory
	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(
				context).getPath()
				: context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	public static int getSizeInBytes(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	// add bitmap to memory cache
	public void addBitmapToMemoryCache(String url, Bitmap bitmap) {
		if (url == null || bitmap == null) {
			return;
		}

		// Add to memory cache
		if (mMemoryCache != null && mMemoryCache.get(url) == null) {
			mMemoryCache.put(url, bitmap);
		}
	}

	// add bitmap to disk cache
	// o parakatw kwdikas einai apo to
	// http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
	// to source code pou exei
	// add bitmap to disk cache
	public void addBitmapToDiskCache(String url, Bitmap bitmap) {
		if (url == null || bitmap == null) {
			return;
		}
		synchronized (mDiskCacheLock) {
			// Add to disk cache
			if (mDiskLruCache != null) {
				final String key = hashKeyForDisk(url);
				OutputStream out = null;
				try {
					DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
					if (snapshot == null) {
						final DiskLruCache.Editor editor = mDiskLruCache
								.edit(key);
						if (editor != null) {
							out = editor.newOutputStream(DISK_CACHE_INDEX);
							bitmap.compress(DEFAULT_COMPRESS_FORMAT,
									DEFAULT_COMPRESS_QUALITY, out);
							editor.commit();
							out.close();
						}
					} else {
						snapshot.getInputStream(DISK_CACHE_INDEX).close();
					}
				} catch (final IOException e) {
					Log.e(TAG, "addBitmapToCache - " + e);
				} catch (Exception e) {
					Log.e(TAG, "addBitmapToCache - " + e);
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
					}
				}
			}
		}
	}

	// get bitmap from memory cache
	public Bitmap getBitmapFromMemCache(String url) {
		if (mMemoryCache != null) {
			Bitmap memBitmap = mMemoryCache.get(url);
			if (memBitmap != null) {
				return memBitmap;
			}
		}
		return null;
	}

	// get bitmap from disk cache
	public Bitmap getBitmapFromDiskCache(String url) {
		final String key = hashKeyForDisk(url);
		synchronized (mDiskCacheLock) {
			while (mDiskCacheStarting) {
				try {
					mDiskCacheLock.wait();
				} catch (InterruptedException e) {
				}
			}
			if (mDiskLruCache != null) {
				InputStream inputStream = null;
				try {
					final DiskLruCache.Snapshot snapshot = mDiskLruCache
							.get(key);
					if (snapshot != null) {
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "Disk cache hit");
						}
						inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
						if (inputStream != null) {
							final Bitmap bitmap = BitmapFactory
									.decodeStream(inputStream);
							return bitmap;
						}
					}
				} catch (final IOException e) {
					Log.e(TAG, "getBitmapFromDiskCache - " + e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
					}
				}
			}
			return null;
		}
	}

	/**
	 * A hashing method that changes a string (like a URL) into a hash suitable
	 * for using as a disk filename.
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	// methods for different android versions
	@TargetApi(9)
	public static long getUsableSpace(File path) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	@TargetApi(8)
	public static File getExternalCacheDir(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	// diateirei tin cache se periptwsi allagwn orientation
	// smoother chagnes
	static class RetainFragment extends Fragment {
		private static final String TAG = "RetainFragment";
		private Object object = null;

		public RetainFragment() {
		}

		public static RetainFragment findOrCreateRetainFragment(
				FragmentManager fm) {
			RetainFragment fragment = (RetainFragment) fm
					.findFragmentByTag(TAG);
			if (fragment == null) {
				fragment = new RetainFragment();
				// ksekinaei ena kainourgio trasaction
				fm.beginTransaction().add(fragment, TAG)
						.commitAllowingStateLoss();
			}

			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}

		public void setCache(Object object) {
			this.object = object;
		}

		public Object getCache() {
			return object;
		}
	}
	
	public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Memory cache cleared");
            }
        }

        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Disk cache cleared");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "clearCache - " + e);
                }
                mDiskLruCache = null;
                initCaches();
            }
        }
    }
}

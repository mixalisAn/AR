package cut.ac.cy.my_tour_guide.database;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends DBSchemaVariables{
	static final String TAG = "DBHandler";			
	
	
	final Context context;
	
	DatabaseHelper DBHelper;
	static SQLiteDatabase db;
	
	public DBHandler(Context cx){
		context = cx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		long id;
		int i = 0;
		ContentValues poiValue, urlValue;
		
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, "Creating database!");
			db.execSQL(CREATE_TABLE_POI);
			db.execSQL(CREATE_TABLE_IMAGES_URLS);
			System.out.println("Creating db : ");
			for(PoiData poi : initialRows){
				poiValue = insertInitialPOIS(poi.getName() , poi.getLatitude() , poi.getLongtitude() , poi.getAltitude() , poi.getLink() , poi.getAddress() , poi.getDescription() , poi.getResName());
				id = db.insert(POI_TABLE, null, poiValue);
				for(String url : ImagesUrls.urls[i]){
					urlValue = insertInitialUrls(id , url);
					db.insert(IMAGES_URLS_TABLE, null, urlValue);
				}
				i++;
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all data");
			db.execSQL("DROP TABLE IF EXISTS " + POI_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + IMAGES_URLS_TABLE);
			onCreate(db);
		}
		
	}
	
	//open the database
	public DBHandler open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		db.execSQL(FOREIGN_KEY_ENABLE);
		return this;
	}
	
	//close the database
	public void close(){
		DBHelper.close();
	}
	
	public static long insertPOI(String name, double lat, double lng, double alt , String link ,  String address , String description, String resName){
		ContentValues values = new ContentValues();
		values.put(POI_COLUMN_NAME, name);
		values.put(POI_COLUMN_LAT, lat);
		values.put(POI_COLUMN_LNG, lng);
		values.put(POI_COLUMN_ALT, alt);
		values.put(POI_COLUMN_LINK, link);
		values.put(POI_COLUMN_ADDRESS, address);
		values.put(POI_COLUMN_DESC, description);
		values.put(POI_COLUMN_RES_NAME, resName);
		return db.insert(POI_TABLE, null, values);
	}
	
	public static ContentValues insertInitialPOIS(String name, double lat, double lng, double alt , String link ,  String address , String description, String resName){
		ContentValues values = new ContentValues();
		values.put(POI_COLUMN_NAME, name);
		values.put(POI_COLUMN_LAT, lat);
		values.put(POI_COLUMN_LNG, lng);
		values.put(POI_COLUMN_ALT, alt);
		values.put(POI_COLUMN_LINK, link);
		values.put(POI_COLUMN_ADDRESS, address);
		values.put(POI_COLUMN_DESC, description);
		values.put(POI_COLUMN_RES_NAME, resName);
		return values;
	}
	
	public static long insertURL(long poiId, String url){
		ContentValues values = new ContentValues();
		values.put(IMAGES_URLS_COLUMN_ENTRY_ID, poiId);
		values.put(IMAGES_URLS_COLUMN_URL, url);
		return db.insert(IMAGES_URLS_TABLE, null, values);
	}
	
	
	public static ContentValues insertInitialUrls(long poiId, String url){
		ContentValues values = new ContentValues();
		values.put(IMAGES_URLS_COLUMN_ENTRY_ID, poiId);
		values.put(IMAGES_URLS_COLUMN_URL, url);
		return values;
	}
	
	public Cursor getMarkers(){
		String[] projection = {
				POI_COLUMN_ENTRY_ID,
				POI_COLUMN_NAME,
				POI_COLUMN_LAT,
				POI_COLUMN_LNG,
				POI_COLUMN_ALT,
				POI_COLUMN_RES_NAME
		};
		
		Cursor mCursor = db.query(POI_TABLE, projection, null, null, null, null, null);
		
		return mCursor;
	}
	
	public Cursor getPins(){
		String[] projection = {
				POI_COLUMN_NAME,
				POI_COLUMN_LAT,
				POI_COLUMN_LNG
		};
		
		Cursor mCursor = db.query(POI_TABLE, projection, null, null, null, null, null);
		return mCursor;
	}
	
	public Cursor getPoi(long id) throws SQLException{
		String[] projection = {
				POI_COLUMN_NAME,
				POI_COLUMN_LAT,
				POI_COLUMN_LNG,
				POI_COLUMN_LINK,
				POI_COLUMN_ADDRESS,
				POI_COLUMN_DESC,
				POI_COLUMN_RES_NAME
		};
		
		Cursor mCursor = db.query(POI_TABLE, projection, POI_COLUMN_ENTRY_ID + "=" + id, null, null, null, null);
		
		return mCursor;
	}
	
	public Cursor getPoiImagesUrls(long id){
		String[] projection = {
				IMAGES_URLS_COLUMN_URL
		};
		
		Cursor mCursor = db.query(IMAGES_URLS_TABLE, projection, IMAGES_URLS_COLUMN_ENTRY_ID + "=" + id, null, null, null, null);
		
		return mCursor;
		
	}
	
	public boolean deletePoi(){
		return db.delete(POI_TABLE, POI_COLUMN_ENTRY_ID + "= 1", null) > 0;
	}
	
};

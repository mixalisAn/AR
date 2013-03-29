package cut.ac.cy.my_tour_guide.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.database.DBHandler;
import cut.ac.cy.my_tour_guide.ui.Marker;

/**
 * This class should be used as a example local data source. It is an example of
 * how to add data programatically. You can add data either programatically,
 * SQLite or through any other source.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class LocalDataSource extends DataSource {
	private List<Marker> cachedMarkers = new ArrayList<Marker>();
	private DBHandler db;
	private Context context;
	private Resources res;
	
	//den prepei na xrisimopoieitai pleon
	/*// dikia mou gia na xrisimopoiithei apo tin Maps
	public LocalDataSource() {

	}
*/
	public LocalDataSource(Resources res, Context cx) {
		if (res == null)
			throw new NullPointerException();
		context = cx;
		db = new DBHandler(context);
		this.res = res; 
	}

	public List<Marker> getMarkers() {
		try {
			db.open();
			Cursor cursor = db.getMarkers();
			cachedMarkers.clear();
			if (cursor != null) {
				//to cursor.moveToFirst() xrisimopooieitai giati an paei sto 1o kai den iparxei tote
				//tha crasharei
				if (cursor.moveToFirst()) {
					do {
						Log.i("LocalDataSource",
								String.valueOf(cursor.getLong(0))
										+ cursor.getString(1)
										+ cursor.getString(5)
										+ String.valueOf(cursor.getLong(6)));

						cachedMarkers.add(new Marker(cursor.getLong(0), cursor
								.getString(1), cursor.getDouble(2), cursor
								.getDouble(3), cursor.getDouble(4), cursor
								.getString(5), cursor.getLong(6), Color.GREEN,
								getBitmap(cursor.getString(7))));
					} while (cursor.moveToNext());
				}
			}

			db.close();
			cursor.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cachedMarkers;
	}

	

	private Bitmap getBitmap(String name) {
		if(name == null){
			return BitmapFactory.decodeResource(res, R.drawable.info);
		}else{
			name = name.toLowerCase();
			name = name.replace(" ", "_");
			int resResult = context.getResources().getIdentifier(name, "drawable",
					"cut.ac.cy.my_tour_guide");
			return BitmapFactory.decodeResource(res, resResult);
		}
			
		
	}
	/*
	public List<Marker> getCategorizedMarkers(long[] categoriesId) {
		List<Marker> cachedMarkers = new ArrayList<Marker>();
		try {
			db.open();
			Cursor cursor = db.getSpecificCategoriesMarkers(categoriesId);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						cachedMarkers.add(new Marker(cursor.getLong(0), cursor
								.getString(1), cursor.getDouble(2), cursor
								.getDouble(3), cursor.getDouble(4), cursor
								.getString(5), cursor.getLong(6), Color.GREEN,
								getBitmap(cursor.getString(7))));
					} while (cursor.moveToNext());
				}
			}
			db.close();
			cursor.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cachedMarkers;
	}
	*/
}

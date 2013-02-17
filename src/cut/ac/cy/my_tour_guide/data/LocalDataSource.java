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
 * This class should be used as a example local data source. It is an example 
 * of how to add data programatically. You can add data either programatically, 
 * SQLite or through any other source.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap monument = null;
    private DBHandler db ;
   
    //dikia mou gia na xrisimopoiithei apo tin Maps
    public LocalDataSource(){	
    	
    }
    
    public LocalDataSource(Resources res, Context cx) {
        if (res==null) throw new NullPointerException();
        db = new DBHandler(cx);
        createIcon(res);
    }
    
    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
        monument=BitmapFactory.decodeResource(res, R.drawable.marker_icon);
    }
    
    public List<Marker> getMarkers() {
    	try {
			db.open();
			Cursor cursor = db.getMarkers();
			
	    	if(cursor != null){
	    		cursor.moveToFirst();

	    		do{
	    			cachedMarkers.add(new Marker(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5), Color.GREEN, monument));
	    		}while(cursor.moveToNext());
	    	}
			
	    	db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return cachedMarkers;
    }
}

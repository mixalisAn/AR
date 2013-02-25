package cut.ac.cy.my_tour_guide.activity;

import java.sql.SQLException;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import cut.ac.cy.my_tour_guide.database.DBHandler;
import cut.ac.cy.my_tour_guide.helpers.CategoriesAdapter;

public class MarkersCategories extends ListActivity{
	private DBHandler db ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DBHandler(this);
		
		String[] categories = getCategories();
		//ListView listView = getListView();
		//listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked , categories));
		setListAdapter(new CategoriesAdapter(this, categories));
	}

	
	private String[] getCategories(){
		String[] categories = null;
		try {
			db.open();
			Cursor cursor = db.getCategories();
			if(cursor != null){
				categories = new String[cursor.getCount()];
				cursor.moveToFirst();
				for(int i=0; !cursor.isAfterLast(); i++, cursor.moveToNext()){
					categories[i] = cursor.getString(0);
				};
			}
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categories;
	}
	
}

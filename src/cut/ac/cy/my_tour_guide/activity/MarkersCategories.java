package cut.ac.cy.my_tour_guide.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;

import cut.ac.cy.my_tour_guide.database.DBHandler;
import cut.ac.cy.my_tour_guide.helpers.CategoriesAdapter;
import cut.ac.cy.my_tour_guide.helpers.CategoriesRowDetails;

public class MarkersCategories extends SherlockListActivity {
	private DBHandler db;
	private ArrayAdapter<CategoriesRowDetails> listAdapter;
	private long[] selectedCategories;
	List<CategoriesRowDetails> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DBHandler(this);

		// String[] categories = getCategories();
	    categories = getCategories();
		listAdapter = new CategoriesAdapter(this, categories);
		setListAdapter(listAdapter);
	}



	@Override
	public void finish() {
		for(int i=0; i < categories.size(); i++){
			if(categories.get(i).isSelected()){
				selectedCategories[i] = categories.get(i).getCategoryId();
				Log.i("MarkersCategories" , String.valueOf(selectedCategories[i]));
			}
		}
		if(selectedCategories.length > 0){
			Intent data = new Intent();
			data.putExtra("selectedCategories", selectedCategories);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

	private List<CategoriesRowDetails> getCategories() {
		List<CategoriesRowDetails> categories = new ArrayList<CategoriesRowDetails>();
		try {
			db.open();
			Cursor cursor = db.getCategories();
			if (cursor != null) {
				cursor.moveToFirst();
				selectedCategories = new long[cursor.getCount()];
				do {
					categories.add(new CategoriesRowDetails(
							cursor.getLong(0), cursor.getString(1), true));
				} while (cursor.moveToNext());
			}
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}


	
	/**
	 * se periptwsi poy to aferaisw auto na vgalw kai to focusable kai clickable
	 * apo to checkbox
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		CategoriesRowDetails row = listAdapter.getItem(position);
		row.toggleSelected();
		CategoriesAdapter.ViewHolder holder = (CategoriesAdapter.ViewHolder) v
				.getTag();
		holder.checkBox.setChecked(row.isSelected());
	}




}

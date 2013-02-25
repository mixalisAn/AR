package cut.ac.cy.my_tour_guide.helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cut.ac.cy.my_tour_guide.R;

public class CategoriesAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] categories;
	
	
	
	public CategoriesAdapter(Activity context,
			String[] values) {
		super(context, R.layout.categories_row, R.id.textViewCategory, values);
		this.context = context;
		this.categories = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.categories_row, null);
			ViewHolder viewHolder = new ViewHolder();
		    viewHolder.textView = (TextView) rowView.findViewById(R.id.textViewCategory);
		    viewHolder.imageView = (ImageView) rowView
		          .findViewById(R.id.imageViewCategory);
		      rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
	    String category = categories[position];
	    holder.textView.setText(category);
	    
	    category = category.toLowerCase();
	    category = category.replace(" ", "_");
	    
	    int resResult = context.getResources().getIdentifier(category, "drawable",
				"cut.ac.cy.my_tour_guide");
	    
	    if(resResult == 0){
	    	//auto na allaxtei me kapoio default icon se periptwsi lathous
	    	holder.imageView.setImageResource(R.drawable.stop);
	    }else{
	    	holder.imageView.setImageResource(resResult);
	    }
	    
		return rowView;
	}

	//Auti i class einai gia logous optimazation. des vongella tutorial. Den xrisimopoiei to layoutInflater pouy einai akrivo alla pernei 
	//to tag apo tin viewholder gia to kathe view.
	static class ViewHolder{
		public TextView textView;
		public ImageView imageView;
	}
	
}

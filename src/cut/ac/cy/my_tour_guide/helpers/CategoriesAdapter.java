package cut.ac.cy.my_tour_guide.helpers;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import cut.ac.cy.my_tour_guide.R;
/**
 * 
 * Pws akrivws douleuei o sigekrimenos adapter. Gia kather row tis listview vazoume ws tag ton holder pou exei mesa ta imageview, textview kai
 * checkedbox to opoio exei ws tag to CategoriesRowDetails. Otan allazei row sto scroll to convertview den einai null. Etsi vazoume prwta sto 
 * tag toy checkbox to categoriesrowdetails apo to antistoixo position kai meta pernoume to tag apo to row gia na tou allaksoume ta stoixeia 
 * tou. To checkbox prepei omws na exei to apothikeumeno check apo to sigekrimeno checkbox
 *
 */
public class CategoriesAdapter extends ArrayAdapter<CategoriesRowDetails>{
	private final Activity context;
	private final List<CategoriesRowDetails> categories;

	
	public CategoriesAdapter(Activity context,
			List<CategoriesRowDetails> categories) {
		super(context, R.layout.categories_row, R.id.textViewCategory, categories);
		this.context = context;
		this.categories = categories;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.categories_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) rowView.findViewById(R.id.imageViewCategory);
		    viewHolder.textView = (TextView) rowView.findViewById(R.id.textViewCategory);
		    viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBoxCategory);
		    viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					CategoriesRowDetails element = (CategoriesRowDetails) viewHolder.checkBox.getTag();
					element.setSelected(buttonView.isChecked());
				}
			});
			rowView.setTag(viewHolder);
			viewHolder.checkBox.setTag(categories.get(position));
		}else{
			((ViewHolder) rowView.getTag()).checkBox.setTag(categories.get(position));
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
	   
	    holder.textView.setText(categories.get(position).getName());
	 
	    int resResult = context.getResources().getIdentifier(categories.get(position).getResName(), "drawable",
				"cut.ac.cy.my_tour_guide");
	    
	    if(resResult == 0){
	    	//auto na allaxtei me kapoio default icon se periptwsi lathous
	    	holder.imageView.setImageResource(R.drawable.stop);
	    }else{
	    	holder.imageView.setImageResource(resResult);
	    }
	    Log.i("CategoriesAdapter", String.valueOf(categories.get(position).isSelected()) + "position = " + String.valueOf(position));
	    holder.checkBox.setChecked(categories.get(position).isSelected());
	    Log.i("CategoriesAdapter", String.valueOf(holder.checkBox.isChecked()));
		return rowView;
	}

	//Auti i class einai gia logous optimazation. des vongella tutorial. Den xrisimopoiei to layoutInflater pouy einai akrivo alla pernei 
	//to tag apo tin viewholder gia to kathe view.
	public static class ViewHolder{
		public ImageView imageView;
		public TextView textView;
		public CheckBox checkBox;
	}
	
}

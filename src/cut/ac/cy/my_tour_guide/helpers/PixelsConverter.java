package cut.ac.cy.my_tour_guide.helpers;

import android.content.Context;
import android.util.TypedValue;

public class PixelsConverter {

	Context context;
	
	public PixelsConverter(Context context){
		this.context = context;
	}
	
	public float pixelsToDips(float value){
		//return getDensity() * value;
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getApplicationContext().getResources().getDisplayMetrics());
	}
	
	public float pixelsToSp(float value){
		//float scaledDensity = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
		//return value/scaledDensity;
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getApplicationContext().getResources().getDisplayMetrics());
	}
	
	public float getDensity(){
		return context.getApplicationContext().getResources().getDisplayMetrics().density;
	}
}

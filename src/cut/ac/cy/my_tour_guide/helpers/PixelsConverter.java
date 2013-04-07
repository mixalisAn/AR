package cut.ac.cy.my_tour_guide.helpers;

import android.content.Context;

public class PixelsConverter {

	Context context;
	
	public PixelsConverter(Context context){
		this.context = context;
	}
	
	public float pixelsToDips(float value){
		return getDensity() * value;
	}
	
	public float pixelsToSp(float value){
		float scaledDensity = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
		return value/scaledDensity;
	}
	
	public float getDensity(){
		return context.getApplicationContext().getResources().getDisplayMetrics().density;
	}
}

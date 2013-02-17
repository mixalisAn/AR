package cut.ac.cy.my_tour_guide.gallery;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DeviceProperties {
	private Activity activity;
	private int width;
	private int height;
	
	public DeviceProperties(Activity a){
		activity = a;
		getDeviceScreenDimensions();
	}
	
	public void getDeviceScreenDimensions(){
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
	}
	
	public int getDeviceWidth(){
		return width;
	}
	
	public int getDeviceHeight(){
		return height;
	}
}

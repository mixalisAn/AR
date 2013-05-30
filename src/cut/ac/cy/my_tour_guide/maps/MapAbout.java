package cut.ac.cy.my_tour_guide.maps;

import cut.ac.cy.my_tour_guide.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Michalis Anastasiou
 * 
 */
//this class is for displaying the informations about the map 
//it contains only a layout and it uses dialog theme in android manifest
public class MapAbout extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_about);
	}
}

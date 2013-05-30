package cut.ac.cy.my_tour_guide.maps;


import cut.ac.cy.my_tour_guide.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * @author Michalis Anastasiou
 * 
 */

public class MapSettings extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.map_preference);
	}

}

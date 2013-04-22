package cut.ac.cy.my_tour_guide.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import cut.ac.cy.my_tour_guide.R;
import cut.ac.cy.my_tour_guide.gallery.Utils;

public class About extends SherlockFragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		Utils.enableStrictMode();
		setContentView(R.layout.about);
	}
	
}

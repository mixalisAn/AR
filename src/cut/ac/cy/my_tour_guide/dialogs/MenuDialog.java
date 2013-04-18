package cut.ac.cy.my_tour_guide.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockDialogFragment;

import cut.ac.cy.my_tour_guide.activity.About;
import cut.ac.cy.my_tour_guide.activity.AugmentedReality;
import cut.ac.cy.my_tour_guide.helpers.MarkersCategories;

public class MenuDialog extends SherlockDialogFragment{
	private static final int REQUEST_CODE = 1;
	protected static String[] menuItemsValues = { "Pois Categories",
		"Hide Radar", "Show zoombar", "Gps Settings", "About" };
	
	public MenuDialog(){
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Menu options");
		builder.setItems(menuItemsValues, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					switch(which){
					case 0:
						Intent intent1 = new Intent(getActivity(), MarkersCategories.class);
						getActivity().startActivityForResult(intent1, REQUEST_CODE);
						dialog.dismiss();
						break;
					case 1:
						AugmentedReality.showRadar = !AugmentedReality.showRadar;
						menuItemsValues[which] = (((AugmentedReality.showRadar) ? "Hide" : "Show") + " Radar");
						dialog.dismiss();
					break;
					case 2:
						AugmentedReality.showZoomBar = !AugmentedReality.showZoomBar;
						menuItemsValues[which] = (((AugmentedReality.showZoomBar) ? "Hide" : "Show") + " Zoom Bar");

						((AugmentedReality)getActivity()).getZoomLayout().setVisibility((AugmentedReality.showZoomBar) ? RelativeLayout.VISIBLE
								: RelativeLayout.GONE);
						dialog.dismiss();
						break;
					case 3:
						getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dialog.dismiss();
						break;
					case 4:
						Intent intent2 = new Intent(getActivity(), About.class);
						startActivity(intent2);
						break;
				}
				
			}
						
		});
		return builder.create();
	}
}

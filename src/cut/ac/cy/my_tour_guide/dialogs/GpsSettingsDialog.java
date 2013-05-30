package cut.ac.cy.my_tour_guide.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.provider.Settings;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * 
 * @author Michalis Anastasiou
 * 
 */

public class GpsSettingsDialog extends SherlockDialogFragment implements OnClickListener{

	public GpsSettingsDialog(){
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Gps Disabled");
		builder.setMessage("Enable Gps in order to get accurate results!");
		builder.setPositiveButton("Ok", this);
		builder.setNegativeButton("Cancel", this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case Dialog.BUTTON_POSITIVE:
			getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			
			break;
		}
		dialog.dismiss();
	}	
}

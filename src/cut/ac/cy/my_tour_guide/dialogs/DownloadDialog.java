package cut.ac.cy.my_tour_guide.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import cut.ac.cy.my_tour_guide.maps.MapActivity;

public class DownloadDialog extends SherlockDialogFragment implements OnClickListener{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Download Images");
		builder.setMessage("Downloading via mobile network may incur additional charges. Proceed anyway?");
		builder.setPositiveButton("Ok", this);
		builder.setNegativeButton("Cancel", this);

		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		MapActivity callingActivity = (MapActivity)getActivity();
		callingActivity.networkDialogSelection(which);
		dialog.dismiss();
	}



}

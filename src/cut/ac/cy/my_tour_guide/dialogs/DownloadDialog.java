package cut.ac.cy.my_tour_guide.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import cut.ac.cy.my_tour_guide.poi.PoiActivity;

public class DownloadDialog extends SherlockDialogFragment implements
		OnClickListener {

	public DownloadDialog() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setTitle("Download images");
		builder.setMessage("Downloading via mobile network may incur additional charges. Proceed anyway?");
		builder.setPositiveButton("Ok", this);
		builder.setNegativeButton("Cancel", this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		PoiActivity callingActivity = (PoiActivity)getActivity();
		callingActivity.networkDialogSelection(which);
	}

}

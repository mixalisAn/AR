package cut.ac.cy.my_tour_guide.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ErrorDialog extends SherlockDialogFragment implements DialogInterface.OnClickListener{
	
	public ErrorDialog(){
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Application Incompatible");
		builder.setMessage("Cannot share content with this app.Please select another one");
		builder.setPositiveButton("Ok", this);
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}
	
	
	
}

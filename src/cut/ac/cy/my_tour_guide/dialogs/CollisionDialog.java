package cut.ac.cy.my_tour_guide.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import cut.ac.cy.my_tour_guide.activity.AugmentedReality;
import cut.ac.cy.my_tour_guide.ui.Marker;

public class CollisionDialog extends SherlockDialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
	private List<Marker> collisionMarkers = new ArrayList<Marker>();
	private String[] markersNames;
	private boolean[] checkedItems;
	private ConfirmationListener listener;
	
	public CollisionDialog(){
	}

	public interface ConfirmationListener{
		public void onOkClick(List<Marker> removedMarkers);
	}
	
	public void setConfirmationDialogFragmentListener(ConfirmationListener listener){
		this.listener = listener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		collisionMarkers = ((AugmentedReality)getActivity()).getSelectedCollisionMarkers();
		checkedItems = new boolean[collisionMarkers.size()];
		markersNames = new String[collisionMarkers.size()];
		
		for(int i=0; i<collisionMarkers.size(); i++){
			markersNames[i] = collisionMarkers.get(i).getName();
		}
		
		builder.setTitle("Select markers to remain");
		builder.setMultiChoiceItems(markersNames, null, this);
		builder.setPositiveButton("Ok", this);
		builder.setNegativeButton("Cancel", this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		List<Marker> removedMarkers = new ArrayList<Marker>();
		if(listener != null){
			switch(which){
			case DialogInterface.BUTTON_POSITIVE:
				for(int i=0; i < checkedItems.length; i++ ){
					if(!checkedItems[i]){
						removedMarkers.add(collisionMarkers.get(i));
					}
				}
				System.out.println("Epilecthike to position :  " + String.valueOf(removedMarkers.size())); 
				listener.onOkClick(removedMarkers);
				//((AugmentedReality)getActivity()).setRemovedCollisionMarkers(removedMarkers);
				break;
			default:
				dialog.dismiss();
				break;
			}
		}
		
		
	
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		checkedItems[which] = isChecked;
	}
	
	
	
}

package cut.ac.cy.my_tour_guide.poi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.widget.Toast;
/**
 * 
 * @author Michalis Anastasiou
 * 
 */
public class ConnectionStatusReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras  = intent.getExtras();
		
		NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
		
		State state = info.getState();
		
		if( state  == State.DISCONNECTED){
			Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
		}
	}

}

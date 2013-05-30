package cut.ac.cy.my_tour_guide.helpers;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 * @author Michalis Anastasiou
 *
 */
public class MapParcelableHashMap implements Parcelable{
	private HashMap<String, MapMarker> mapMarkersData;
	
	public MapParcelableHashMap(){
		mapMarkersData = new HashMap<String, MapMarker>();
	}
	
	public MapParcelableHashMap(Parcel in){
		mapMarkersData = new HashMap<String, MapMarker>();
		readFromParcel(in);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MapParcelableHashMap createFromParcel(Parcel in) {
            return new MapParcelableHashMap(in);
        }
 
        public MapParcelableHashMap[] newArray(int size) {
            return new MapParcelableHashMap[size];
        }
    };
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mapMarkersData.size());
		for(String key : mapMarkersData.keySet()){
			out.writeString(key);
			out.writeValue(mapMarkersData.get(key));
		}
	}

	
	public void readFromParcel(Parcel in){
		int count = in.readInt();
		for (int i = 0; i < count; i++){
			mapMarkersData.put(in.readString(), (MapMarker)in.readValue(MapMarker.class.getClassLoader()));
		}
	}
	
	public MapMarker get(String key) {
        return mapMarkersData.get(key);
    }
 
    public void put(String key, MapMarker marker) {
        mapMarkersData.put(key, marker);
    }
}

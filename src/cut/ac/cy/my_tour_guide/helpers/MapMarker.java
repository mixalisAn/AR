package cut.ac.cy.my_tour_guide.helpers;

import java.io.Serializable;

public class MapMarker implements Serializable{
	/**
	 * Default number gia tis seriazable classes
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String resName;
	
	
	public MapMarker(){}
	
	public MapMarker(long id, String resName){
		this.id = id;
		this.resName = resName;
	}
	
	public long getId(){
		return id;
	}
	
	public String getResName(){
		return resName;
	}
}

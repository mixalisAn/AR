package cut.ac.cy.my_tour_guide.helpers;

public class MapMarker {
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

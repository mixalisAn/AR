package cut.ac.cy.my_tour_guide.database;

public class PoiData {
	private String name;
	private double latitude;
	private double longtitude;
	private double altitude;
	private String link;
	private String address;
	private String description;
	private String resName;
	
	
	public PoiData(String name, double d, double e, double f , String link , String address , String desc, String resName){
		this.name = name;
		this.latitude = d;
		this.longtitude = e;
		this.altitude = f;
		this.link = link;
		this.address = address;
		this.description = desc;
		this.resName = resName;
	}
	
	public String getName(){
		return name;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongtitude(){
		return longtitude;
	}
	
	public double getAltitude(){
		return altitude;
	}
	
	public String getLink(){
		return link;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getResName(){
		return resName;
	}
}

package cut.ac.cy.my_tour_guide.helpers;

import java.io.Serializable;
/**
 * 
 * @author Michalis Anastasiou
 *
 */
public class MapMarker implements Serializable{
	/**
	 * Default number gia tis seriazable classes
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String resName;
	private String[] compareUrls = new String[2];
	
	
	public MapMarker(){}
	
	public MapMarker(long id, String resName, String pastUrl, String presentUrl){
		this.id = id;
		this.resName = resName;
		compareUrls[0] = pastUrl;
		compareUrls[1] = presentUrl;
	}
	
	public long getId(){
		return id;
	}
	
	public String getResName(){
		return resName;
	}
	
	public String[] getCompareUrls(){
		return compareUrls;
	}
}

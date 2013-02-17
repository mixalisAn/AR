package cut.ac.cy.my_tour_guide.poi;

/**otan einai final i class den mporei na kanei inherit kapoia alli
 * 
 *
 */
//Ayti i klasi einai gia na mporei na parei to service to resource apo tin activity
//den mporei na to parei allios giati xreiazomaste mono bind

public final class MusicResources {
	private static String resourceName; 
	
	public synchronized static void setMusicResources(String resource){
		resourceName = resource;
	}
	
	public synchronized static String getMusicResources(){
		return	resourceName; 
	}
	
}

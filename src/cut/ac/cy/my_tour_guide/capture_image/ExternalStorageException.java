package cut.ac.cy.my_tour_guide.capture_image;

/**
 * 
 * @author Michalis Anastasiou
 * 
 */

public class ExternalStorageException extends Exception{

	private static final long serialVersionUID = 1L;

	public ExternalStorageException() {
	}
	
	public ExternalStorageException(String message){
		super(message);
	}
}

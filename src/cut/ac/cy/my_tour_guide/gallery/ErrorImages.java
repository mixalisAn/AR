package cut.ac.cy.my_tour_guide.gallery;

import android.widget.ImageView;

public class ErrorImages {
	private String url = null;
	private ImageView imView = null;
	
	public ErrorImages() {
		// TODO Auto-generated constructor stub
	}

	public void setErrorImage(String url, ImageView imageView){
		this.url = url;
		imView = imageView;
	}
	
	public String getErrorUrl(){
		return url;
	}
	
	public ImageView getErrorImageView(){
		return imView;
	}
}

package cut.ac.cy.my_tour_guide.gallery;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter{

	private Activity activity;
	private String[] data;
	public ImageDownloader imageDownloader;

	public ImageAdapter(Activity a, String[] d, ImageDownloader imDownloader) {
		activity = a;
		data = d;
		imageDownloader = imDownloader;
	}

	// ---returns the number of images---
	public int getCount() {
		return data.length;
	}

	// ---returns the item---
	public Object getItem(int position) {
		return position;
	}

	// ---returns the ID of an item---
	public long getItemId(int position) {
		return position;
	}
	//kanontas override tin getview allazoume to return type apo to view gia to item(na emfanizei eikona px)
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = (ImageView) convertView; //anakeiklwnontai ta views apo ton adapter kai pernei to palio view ama iparxei an einai swstou tipou kai den einai null
		if (imageView == null)
			imageView = new ImageView(activity.getApplicationContext());   //an den iparxei tote dimiourgei to kanourgio imageview gia to application context
		
		imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageDownloader.download(data[position], imageView);
		
		return imageView;
	}

	
	
}

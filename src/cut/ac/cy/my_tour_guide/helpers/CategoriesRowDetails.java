package cut.ac.cy.my_tour_guide.helpers;

public class CategoriesRowDetails {

	private String name;
	private String resName;
	private boolean selected;
	
	public CategoriesRowDetails(String name, boolean isSelected){
		setName(name);
		setResName(name);
		setSelected(isSelected);
	}

	private void setName(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	private synchronized void setResName(String name) {
		
		 name = name.toLowerCase();
		 name = name.replace(" ", "_");
		 this.resName = name;
	}
	
	public synchronized String getResName(){
		return resName;
	}

	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public void toggleSelected(){
		selected = !selected;
	}
}

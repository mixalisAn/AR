package cut.ac.cy.my_tour_guide.database;

public class CategoriesData {
	private String category;
	private boolean selected;
	
	public CategoriesData(String category, boolean selected){
		this.category = category;
		this.selected = selected;
	}
	
	
	public String getCategory(){
		return category;
	}
	
	public boolean getSelected(){
		return selected;
	}
}

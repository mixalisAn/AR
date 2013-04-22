package cut.ac.cy.my_tour_guide.database;

import java.util.ArrayList;
import java.util.List;

public class DBSchemaVariables {
	
	static final String DATABASE_NAME = "MyTourGuide";
	static final int DATABASE_VERSION = 44;
	static final String FOREIGN_KEY_ENABLE = "PRAGMA foreign_keys = ON";
	//TABLE POI VARIABLES
	static final String POI_TABLE = "pois";
	static final String POI_COLUMN_ENTRY_ID = "_id";
	static final String POI_COLUMN_NAME = "name";
	static final String POI_COLUMN_LAT = "latitude";
	static final String POI_COLUMN_LNG = "longtitude";
	static final String POI_COLUMN_ALT = "altitude";
	static final String POI_COLUMN_LINK = "link";
	static final String POI_COLUMN_ADDRESS = "address";
	static final String POI_COLUMN_DESC = "description";
	static final String POI_COLUMN_RES_NAME = "resources_name";
	static final String POI_COLUMN_CATEGORY_ID = "category_id";
	static final String POI_COLUMN_PAST_URL = "past_url";
	static final String POI_COLUMN_PRESENT_URL = "present_url";
	
	//TABLE IMAGES URLS
	static final String IMAGES_URLS_TABLE = "images_urls";
	static final String IMAGES_URLS_COLUMN_ENTRY_ID = "_id";
	static final String IMAGES_FULLSCREEN_URLS_COLUMN_URL = "fullscreen_url";
	static final String IMAGES_GRID_URLS_COLUMN_URL = "grid_url";
	
	
	//TABLE CATEGORIES
	static final String CATEGORIES_TABLE = "categories";
	static final String CATEGORIES_COLUMN_ENTRY_ID = "_id";
	static final String CATEGORIES_COLUMN_CATEGORY = "category";
	static final String CATEGORIES_COLUMN_SELECTED = "selected";
	
	//CREATE TABLES
	static final String CREATE_TABLE_CATEGORIES = 
			"CREATE TABLE " + CATEGORIES_TABLE + " (" + CATEGORIES_COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			CATEGORIES_COLUMN_CATEGORY + " TEXT NOT NULL, " + CATEGORIES_COLUMN_SELECTED + " INTEGER NOT NULL);";              //evala integer anti gia bool giati den ipostirizei bool i sqlite
	
	static final String CREATE_TABLE_POI = 
			"CREATE TABLE " + POI_TABLE + " (" + POI_COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			POI_COLUMN_NAME + " TEXT NOT NULL, " + POI_COLUMN_LAT + " REAL NOT NULL, " + POI_COLUMN_LNG + " REAL NOT NULL," +
			POI_COLUMN_ALT + " REAL NOT NULL, " + POI_COLUMN_LINK + " TEXT, " +  POI_COLUMN_ADDRESS + " TEXT, " + 
			POI_COLUMN_DESC + " TEXT, " + POI_COLUMN_RES_NAME + " TEXT, " + POI_COLUMN_CATEGORY_ID + " INTEGER REFERENCES " +
			CATEGORIES_TABLE + "(" + CATEGORIES_COLUMN_ENTRY_ID + ")," + POI_COLUMN_PAST_URL + " TEXT NOT NULL, " + POI_COLUMN_PRESENT_URL + " TEXT NOT NULL);"; 
			
	static final String CREATE_TABLE_IMAGES_URLS = 
			"CREATE TABLE " + IMAGES_URLS_TABLE + " (" + IMAGES_URLS_COLUMN_ENTRY_ID + " INTEGER REFERENCES " + POI_TABLE + "(" +
			POI_COLUMN_ENTRY_ID + ") ON DELETE CASCADE, " +	IMAGES_FULLSCREEN_URLS_COLUMN_URL + " TEXT NOT NULL, " + IMAGES_GRID_URLS_COLUMN_URL +
			" TEXT NOT NULL, " + "PRIMARY KEY( " + IMAGES_URLS_COLUMN_ENTRY_ID + ", " + IMAGES_FULLSCREEN_URLS_COLUMN_URL + "));";
	
	
	
	//INSERT INITIAL ROWS
	static final List<PoiData> initialPois = new ArrayList<PoiData>();
	
	static final List<CategoriesData> initialCategories = new ArrayList<CategoriesData>();
	
	public DBSchemaVariables(){
		initializePois();
		initializeCategories();
	}

	//na allaksw tis katigories tis exw valei mono gia testing
	//6 ROWS INITIALIZATION
	private void initializePois() {
		initialPois.add(new PoiData("Agia Zoni", 34.684168, 33.043946 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"agia_zoni", 1, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Agia Napa", 34.673828, 33.044343 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"agia_napa", 1, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Katholiki", 34.675612, 33.041722 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"katholiki", 1, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Tzami Tzezit", 34.672288, 33.038287 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"tzami_tzezit", 1, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Continental", 34.674492, 33.046194 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"continental", 4, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Agora", 34.676014, 33.043066 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"agora", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Town Hall", 34.6749, 33.044131 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"town_hall", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));

		initialPois.add(new PoiData("Public Library", 34.6802, 33.049278 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"public_library", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Castle", 34.672262, 33.04179 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"castle", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("District Administration", 34.678919, 33.043933 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"district_administration", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Historical Museum", 34.684731,33.054887 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"historical_museum", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Pattikeio", 34.674397, 33.042224 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"pattikeio", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Sxiza", 34.673841, 33.043962 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"sxiza", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Library", 34.675568, 33.044796 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"library", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Molos", 34.671282, 33.044026 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"molos", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Enaerios", 34.684468, 33.059527 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"enaerios", 3, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Agiou Andreou", 34.67415,33.044804 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"agiou_andreou", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Anexartisias", 34.675407, 33.04632 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"anexartisias", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Genethliou Mitela", 34.673146, 33.043356 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"genethliou_mitela", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Agiou Andreou - Ifigenias", 34.674825, 33.045858 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"agiou_andreou_ifigenias", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Saripolou", 34.674639, 33.043707 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"saripolou", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Vasiliou Makedonos", 34.677533,33.046642 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"vasiliou_makedonos", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Makariou", 34.686974, 33.03889, 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"makariou", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Agiris", 34.6728, 33.041098 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"agiris", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Eirinis", 34.67374, 33.041854 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"eirinis", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Kitiou Kiprianou", 34.674829,33.042887 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"kitiou_kiprianou", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Paraliakos", 34.674909,33.047031 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"paraliakos", 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		
		initialPois.add(new PoiData("Rialto Theater", 34.679574, 33.045813 , 0 , "http://www.rialto.com.cy/" , "19 Andrea Drousioti Str.",
				"The Castle of Limassol cab be found on the western edge of the historic city center and its purpose was to guard and protect both the port and the city itself. The present edifice dates back to the Ottoman period (1590) and it was in this architectural phase that the remains of the earlier" +
				"and larger medieval castle were incorporated. Although the oldest written reference to the Castle of Limassol dates back to 1228, archaeological evidence indicates that it existed during the Byzantine period. Today, the castle houses the Medieval Museum of Cyprus." +
				"The castle has a rectangular shape and the thickness of its walls is two meters. Access to the interior of the castle is provided through the guard office at the entrance. Once inside, there is a descending staircase on the right hand side, which leads to a large underground cross-vaulted room." + "" +
				"This room connects to the roof of the castle via spiral staircase. Attached on the eastern end of the underground room is a large rectangular three-storey structure which is connected to the former via a narrow corridor. The basement of this structure is comprised of three long vaulted rooms. The corresponding rooms on the ground and first floors have a level roof while the side ones are subdivided into small cells connected via arches with the main room, which functions as a corridor." +
				"In the narrow corridor which connects the large underground room with the ground floor of the three-store structure, a marble pedestal of Early Christian date was excavated, having the most likely come from a small church, as well as the floor of a Middle Byzantine edifice (10th – 11th cent.). In the northern room of the ground floor, four square bases of a colonnade are visible at floor-level, denoting the position of an Early Christian or Middle Byzantine basilica which was most likely " +
				"incorporated in the Byzantine basilica which was most likely incorporated in the Byzantine phase of the castle. On the eastern side of the tri-sectioned vaulted ground floor, a great apse can also be seen at floor-level, probably belonging to a single-aisled church of French gothic architecture at the beginning of the 13th century. It is probably a chapel of the primary fortifications of the Knight Templars on the island of Cyprus. The spiral staircase preserved in the southwestern corner was probably part of this church, leading too its roof." +
				"The destruction of the medieval phase of the castle happened during the Genoese raids in 1373 where they are reported to have assailed the castle and torched the town. During the 14th century travelers also report that the city was in shambles and almost uninhabited. It appears that the castle underwent repairs by the beginning of the 15th century, since it was an element of the city’s defences against Genoese attacks in 1402 and 1408. A 14th-century tombstone, found during the construction of the Church of Panagia Katholliki and depicting a castle with three towers, may refer to the form of the castle during this period." +
				"In 1413, the castle successfully withstood the first attacks by the Egyptian Mamelukes, but could not stand against the attack of 1425, most likely due to the damage incurred from the first attack as well as the subsequent earthquakes." +
				"A new earthquake severely hit the monument in 1491. Finally, 4-5 years before the Ottoman invasion of 1570, due to the Venetians having decided to shift their defence to the Kyrenia-Nicosia-Famagusta axis, the castle was torn down to prevent it being taken over by the invaders. The destruction was then made complete by subsequent earth tremors in the vicinity. The remains were incorporated into the Ottoman building phase of 1590. That was then the cells of the ground and first storeys were adapted and used as a prison, from at least the 19th century until 1950. Control of the castle was then given to the Department of Antiquities," +
				"which used it as the Limassol District Archaeological Museum and since 1987 the castle house the Cyprus Medieval Museum." +
				"In the Castle of Limassol, which happens to be both a Museum and an Ancient Monument, artefacts are displayed which reflect the political, economic, social and artistic development of Cyprus as well as everyday life of the island from the 3rd to the 19th century A.D."
				,"rialto_theater", 8, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Patixio", 34.681204,33.043624 , 0 , "http://www.google.com/" , "19 Andrea Drousioti Str.",
				"The building was made in 1979. No more info available"
				,"patixeio", 8, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		
		
	}
	
	//8 rows initialization
	//check names because they are used as drawable resource with
	//lowercase and spaces replaced by _
	private void initializeCategories() {
		initialCategories.add(new CategoriesData("Churches", true));
		initialCategories.add(new CategoriesData("Cinemas", true));
		initialCategories.add(new CategoriesData("General", true));
		initialCategories.add(new CategoriesData("Hotels", true));
		initialCategories.add(new CategoriesData("Important Buildings", true));
		initialCategories.add(new CategoriesData("Molos", true));
		initialCategories.add(new CategoriesData("Roads", true));
		initialCategories.add(new CategoriesData("Theaters", true));
	}

}

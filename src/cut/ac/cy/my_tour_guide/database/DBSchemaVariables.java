package cut.ac.cy.my_tour_guide.database;

import java.util.ArrayList;
import java.util.List;

public class DBSchemaVariables {
	
	static final String DATABASE_NAME = "MyTourGuide";
	static final int DATABASE_VERSION = 48;
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
	//36 ROWS INITIALIZATION
	private void initializePois() {
		initialPois.add(new PoiData("Agia Zoni", 34.684168, 33.043946 , 0 , null , "Agias Zonis",
				"The building was made in 1979. No more info available"
				,"agia_zoni", 1, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agia_zoni-90'_3.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agia_zoni_present.jpg"));
		
		initialPois.add(new PoiData("Agia Napa", 34.673828, 33.044343 , 0 , null , "Agiou Andreou - Genethliou Mitela",
				"The building was made in 1979. No more info available"
				,"agia_napa", 1, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agia_napa-70'_3.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agia_napa_present.jpg"));
		
		initialPois.add(new PoiData("Katholiki", 34.675612, 33.041722 , 0 , null , "Ellados",
				"The building was made in 1979. No more info available"
				,"katholiki", 1, "http://www.cut.ac.cy/images/cut/galleries/other/0001/katholiki-30'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/katholiki_present.jpg"));
		
		initialPois.add(new PoiData("Tzami Tzezit", 34.672288, 33.038287 , 0 , null , "Angira",
				"The building was made in 1979. No more info available"
				,"tzami_tzezit", 1, "http://www.cut.ac.cy/images/cut/galleries/other/0001/tzami_tzetit-70'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/tzami_tzezit_present.jpg"));
		
		initialPois.add(new PoiData("Hellas", 34.678989,33.051349 , 0 , null , "28th Oktomvriou",
				"The building was made in 1979. No more info available"
				,"hellas", 2, "http://www.cut.ac.cy/images/cut/galleries/other/0001/hellas-70'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/hellas_present.jpg"));
		
		initialPois.add(new PoiData("Continental", 34.674492, 33.046194 , 0 , null , "Spirou Araouzou",
				"The building was made in 1979. No more info available"
				,"continental", 4, "http://www.cut.ac.cy/images/cut/galleries/other/0001/continental-40'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/continental_present.jpg"));
		
		initialPois.add(new PoiData("Europa", 34.684351,33.059554 , 0 , null , "28th Oktomvriou",
				"The building was made in 1979. No more info available"
				,"europa", 4, "http://www.cut.ac.cy/images/cut/galleries/other/0001/europa-80'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/europa_present.jpg"));
		
		initialPois.add(new PoiData("Agora", 34.676014, 33.043066 , 0 , null , "Saripolou",
				"The building was made in 1979. No more info available"
				,"agora", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agora-70'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agora_present.jpg"));
		
		initialPois.add(new PoiData("Aktaion", 34.675398, 33.048734 , 0 , null , "Molos",
				"The building was made in 1979. No more info available"
				,"aktaion", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/aktaion-1926_3.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/aktaion_present.jpg"));
		
		initialPois.add(new PoiData("City Hall", 34.6749, 33.044131 , 0 , "http://www.limassolmunicipal.com.cy/index_en.html" , "Arxiepiskopou Kiprianou",
				"The building was made in 1979. No more info available"
				,"city_hall", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/city_hall_1.jpg","http://www.cut.ac.cy/images/cut/galleries/other/0001/city_hall_present.jpg"));
		
		initialPois.add(new PoiData("Public Baths", 34.680513, 33.054061 , 0 , null , "28th Oktomvriou",
				"The building was made in 1979. No more info available"
				,"public_baths", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_baths-1926%20_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_baths_present.jpg"));
		
		initialPois.add(new PoiData("Public Library", 34.6802, 33.049278 , 0 , "http://www.limassolmunicipal.com.cy/publiclibrary/index_en.html" , "Agiou Andreou",
				"The building was made in 1979. No more info available"
				,"public_library", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_library_4.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_library_present.jpg"));
		
		initialPois.add(new PoiData("Castle", 34.672262, 33.04179 , 0 , "http://en.wikipedia.org/wiki/Limassol_Castle" , "Vasilisis Veregarias",
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
				,"castle", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/castle-1940_4.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/castle_present.jpg"));
		
		initialPois.add(new PoiData("District Administration", 34.678919, 33.043933 , 0 , null , "Anexartisias",
				"The building was made in 1979. No more info available"
				,"district_administration", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/district_administration-30'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/district_administration_present.jpg"));
		
		initialPois.add(new PoiData("Historical Archive", 34.684731,33.054887 , 0 , null , "Lord Byron",
				"The building was made in 1979. No more info available"
				,"historical_archive", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/historical_archive-%2030'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/historical_archive_present.jpg"));
		
		initialPois.add(new PoiData("Pattiki", 34.674366, 33.042221 , 0 , null , "Spartis - Ellados",
				"The building was made in 1979. No more info available"
				,"pattiki", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/patiki-60_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/patiki_present.jpg"));
		
		initialPois.add(new PoiData("Trigono Sxiza", 34.673832, 33.043933 , 0 , null , "Agiou Andreou - Genethliou Mitela",
				"The building was made in 1979. No more info available"
				,"trigono_sxiza", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/trigono_sxiza_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/trigono_sxiza_present.jpg"));
		
		initialPois.add(new PoiData("Old Law Court", 34.675568, 33.044796 , 0 , null , "Ifigenias",
				"The building was made in 1979. No more info available"
				,"old_law_court", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/old_law_court-60'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/old_law_court_present.jpg"));
		//sto molos exei mpei pier giati iparxei provlima me tin katigoria molos
		initialPois.add(new PoiData("Molos", 34.670901, 33.044445 , 0 , null , "Molos",
				"The building was made in 1979. No more info available"
				,"pier", 6, "http://www.cut.ac.cy/images/cut/galleries/other/0001/molos_10.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/molos_present.jpg"));
		
		initialPois.add(new PoiData("Carnival", 34.675482, 33.04769 , 0 , null , "Spirou Araouzou",
				"The building was made in 1979. No more info available"
				,"carnival", 3, "http://www.cut.ac.cy/images/cut/galleries/other/0001/carnival_8.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/carnival_present.jpg"));
		
		initialPois.add(new PoiData("Enaerios", 34.684197, 33.060356 , 0 , null , "28th Oktomvriou",
				"The building was made in 1979. No more info available"
				,"enaerios", 3, "http://www.cut.ac.cy/images/cut/galleries/other/0001/enaerios-1926_5.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/enaerios_present.jpg"));
		

		initialPois.add(new PoiData("Old Limassol", 34.673179, 33.042052 , 0 , "http://en.wikipedia.org/wiki/Limassol" , "Agiou Andreou",
				"The building was made in 1979. No more info available"
				,"old_limassol", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/old_limassol_5.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/limassol_present.jpg"));
		
		initialPois.add(new PoiData("Agiou Andreou", 34.67415, 33.044804 , 0 , null , "Agiou Andreou",
				"The building was made in 1979. No more info available"
				,"agiou_andreou", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agiou_andreou-80'_7.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agiou_andreou_present.jpg"));
		
		initialPois.add(new PoiData("Anexartisias", 34.675407, 33.04632 , 0 , null , "Anexartisias",
				"The building was made in 1979. No more info available"
				,"anexartisias", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/anexartisias-40'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/anexartisias_present.jpg"));
		
		initialPois.add(new PoiData("Genethliou Mitela", 34.673146, 33.043356 , 0 , null , "Genethliou Mitela",
				"The building was made in 1979. No more info available"
				,"genethliou_mitela", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/genethliou_mitela-80'_5.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/genethliou_mitela_present.jpg"));
		
		initialPois.add(new PoiData("Agiou Andreou - Ifigenias", 34.674825, 33.045858 , 0 , null , "Agiou Andreou - Ifigenias",
				"The building was made in 1979. No more info available"
				,"agiou_andreou_ifigenias", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agiou_andreou_ifigenias-60'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agiou_andreou_ifigenias_present.jpg"));
		
		initialPois.add(new PoiData("Saripolou", 34.674639, 33.043707 , 0 , null , "Saripolou",
				"The building was made in 1979. No more info available"
				,"saripolou", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/saripolou-80'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/saripolou_present.jpg"));
		
		initialPois.add(new PoiData("Vasiliou Makedonos", 34.677533,33.046642 , 0 , null , "Vasileiou Makedonos",
				"The building was made in 1979. No more info available"
				,"vasileiou_makedonos", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/vasileiou_makedonos-60'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/vasileiou_makedonos_present.jpg"));
		
		initialPois.add(new PoiData("Makariou", 34.686974, 33.03889, 0 , null , "Makariou",
				"The building was made in 1979. No more info available"
				,"makariou", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/makariou-1955_1.jpg","http://www.cut.ac.cy/images/cut/galleries/other/0001/makariou_present.jpg"));
		
		initialPois.add(new PoiData("Agiris", 34.6728, 33.041098 , 0 , null , "Angiris",
				"The building was made in 1979. No more info available"
				,"angiris", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/angiris%20-75'-76'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/angiris_present.jpg"));
		
		initialPois.add(new PoiData("Eirinis", 34.67374, 33.041854 , 0 , null , "Eirinis",
				"The building was made in 1979. No more info available"
				,"eirinis", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/eirinis_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/eirinis_present.jpg"));
		
		initialPois.add(new PoiData("Kitiou Kiprianou", 34.674829,33.042887 , 0 , null , "Kitiou Kiprianou",
				"The building was made in 1979. No more info available"
				,"kitiou_kiprianou", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/kitiou_kirpianou-70'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/kitiou_kiprianou_present.jpg"));
		
		initialPois.add(new PoiData("Paraliakos", 34.674909,33.047031 , 0 , null , "Spirou Araouzou",
				"The building was made in 1979. No more info available"
				,"paraliakos", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/paraliakos-50'_7.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/paraliakos_present.jpg"));
		
		
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
				,"rialto_theater", 8, "", ""));
		
		initialPois.add(new PoiData("Maxim", 34.678736, 33.048884 , 0 , null , "Agiou Andreou",
				"The building was made in 1979. No more info available"
				,"maxim", 8, "http://www.cut.ac.cy/images/cut/galleries/other/0001/maxim-40'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/maxim_present.jpg"));
		
		initialPois.add(new PoiData("Patixio", 34.681204,33.043624 , 0 , null , "Agias Zonis",
				"The building was made in 1979. No more info available"
				,"patixio", 8, "http://www.cut.ac.cy/images/cut/galleries/other/0001/patixio-60'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/patixio_present.jpg"));
		
		
		
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

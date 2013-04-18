package cut.ac.cy.my_tour_guide.database;

import java.util.ArrayList;
import java.util.List;

public class DBSchemaVariables {
	
	static final String DATABASE_NAME = "MyTourGuide";
	static final int DATABASE_VERSION = 29;
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
				,"rialto_theater", 9, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Boho Chic", 34.679521, 33.046065 , 0 , "https://www.facebook.com/BohoChicTapasBar" , "24 andrea drousioti Street, 3040 Limassol, Cyprus",
				"The Boho Chic photo "
				, "boho_chic", 1, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Plateia Irown", 34.679192, 33.045794 , 0 , null , null , 
				"The Platia Irown is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous “David” plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "platia_irown", 5, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		

		initialPois.add(new PoiData("Estia", 34.67902, 33.046151 , 0 , "http://www.estianet.gr/" , "pavlou mela 53" , 
				"The Estia is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous “David” plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "estia" , 3, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		
		initialPois.add(new PoiData("Zappeio", 34.678976, 33.04544 , 0 , "https://www.facebook.com/pages/Zappeio/298666980161503" , "irown 36" , 
				"The Zappeio is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous “David” plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "zappeio", 4, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
		
		initialPois.add(new PoiData("Town Hall", 34.6749, 33.044193 , 0 , "http://en.wikipedia.org/wiki/Limassol" , "Arxiepiskopou kiprianou 36" , 
				"The Town Hall is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous “David” plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "town_hall" , 7, "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg"));
	}
	
	//9 rows initialization
	//check names because they are used as drawable resource with
	//lowercase and spaces replaced by _
	private void initializeCategories() {
		initialCategories.add(new CategoriesData("Carnival", true));
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

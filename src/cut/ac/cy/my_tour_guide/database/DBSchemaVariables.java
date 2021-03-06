package cut.ac.cy.my_tour_guide.database;

import java.util.ArrayList;
import java.util.List;

public class DBSchemaVariables {
	
	static final String DATABASE_NAME = "MyTourGuide";
	static final int DATABASE_VERSION = 1;
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
	
	//TABLE IMAGES URLS
	static final String IMAGES_URLS_TABLE = "images_urls";
	static final String IMAGES_URLS_COLUMN_ENTRY_ID = "_id";
	static final String IMAGES_URLS_COLUMN_URL = "url";
	
	//CREATE TABLES
	static final String CREATE_TABLE_POI = 
			"CREATE TABLE " + POI_TABLE + " (" + POI_COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			POI_COLUMN_NAME + " TEXT NOT NULL, " + POI_COLUMN_LAT + " REAL NOT NULL, " + POI_COLUMN_LNG + " REAL NOT NULL," +
			POI_COLUMN_ALT + " REAL NOT NULL, " + POI_COLUMN_LINK + " TEXT, " +  POI_COLUMN_ADDRESS + " TEXT, " + 
			POI_COLUMN_DESC + " TEXT, " + POI_COLUMN_RES_NAME + " TEXT);"; 
			
	static final String CREATE_TABLE_IMAGES_URLS = 
			"CREATE TABLE " + IMAGES_URLS_TABLE + " (" + IMAGES_URLS_COLUMN_ENTRY_ID + " INTEGER REFERENCES " + POI_TABLE + "(" +
			POI_COLUMN_ENTRY_ID + ") ON DELETE CASCADE, " +	IMAGES_URLS_COLUMN_URL + " TEXT NOT NULL, " + 
			"PRIMARY KEY( " + IMAGES_URLS_COLUMN_ENTRY_ID + ", " + IMAGES_URLS_COLUMN_URL + "));";
	
	//INSERT INITIAL ROWS
	static final List<PoiData> initialRows = new ArrayList<PoiData>();
	
	
	public DBSchemaVariables(){
		initializeRows();
	}

	//5 ROWS INITIALIZATION
	private void initializeRows() {
		initialRows.add(new PoiData("Rialto Theater", 34.679574, 33.045813 , 0 , "http://www.rialto.com.cy/" , "19 Andrea Drousioti Str.",
				"The Limassol Castle is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous �David� plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				,"rialto_theater"));
		
		initialRows.add(new PoiData("Boho Chic", 34.679521, 33.046065 , 0 , "https://www.facebook.com/BohoChicTapasBar" , "24 andrea drousioti Street, 3040 Limassol, Cyprus",
				"The Boho Chic is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous �David� plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "boho_chic"));
		
		initialRows.add(new PoiData("Plateia Irown", 34.679192, 33.045794 , 0 , null , null , 
				"The Platia Irown is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous �David� plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "platia_irown"));
		

		initialRows.add(new PoiData("Estia", 34.67902, 33.046151 , 0 , "http://www.estianet.gr/" , "pavlou mela 53" , 
				"The Estia is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous �David� plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "estia"));
		
		
		initialRows.add(new PoiData("Zappeio", 34.678976, 33.04544 , 0 , "https://www.facebook.com/pages/Zappeio/298666980161503" , "irown 36" , 
				"The Zappeio is located at the west end of the sea-front behind the old port, within the old town of Limassol. Originally, the castle stood on the seashore beside the river Garyllis; but the river altered its course in the 16th century and is now 100 yards further west." + 
				"According to Etienne Lusignan, the original Castle was erected by Guy de Lusignan in 1193 on the site of an earlier Byzantine fortress. Excavations within the Castle revealed a marble podium from an early Christian basilica and the floor of a Middle Byzantium monument dated from the 10th or 11th century. Old castle area in " +
				"the beginning of 12th century included ancient church of St.George, where Richard the Lionhearted, King of England celebrated his marriage with princess Berengaria of Navarre in 1191. In 1228  the Roman Emperor Frederick the Second arrived in Limassol, took over the town and used the Limassol Castle as the prison for the hostages." +
				"In 1308 the Castle had been given up to the knights Templars for administration purposes on behalf of the crown. In 1373, the Genoese conquered the Castle and torched Limassol town and it is likely that the Castle suffered serious damage. Further devastation of Limassol by the Genoese in 1370 made necessary the considerable rebuilding." + 
				"It involved a total restructuring of the interior. The keep became the single lofty hall we see today, being given a ribbed and vaulted roof carried on eight sturdy wall pilasters and a central column. The keep had lacked a cellar; now an ample one was provided under the chambers. Everything was done to withstand renewed attacks by the Genoese in 1402 and 1408." + 
				" In 1413 the Castle survived the first attack of the Mamelukes. However, the structure did receive severe damage as a result of earthquakes, leading to its conquest in 1425 by the Mamelukes in their second attack on the city. Extensive reconstruction then took place at the beginning of the 16th century. The gothic arches of the underground chamber and the openings " +
				"on the side walls can be attributed to this period. In 1538 the Venetians partly dismantled the Castle being concerned that it is an easy prey to Turkish raiders. Thirty years later, under imminent threat of the Turkish invasion, Limassol castle was not only repaired but immensely strengthened in order to withstand cannon-fire. The Ottomans conquered Cyprus in 1576 " +
				"and incorporated the remains of the Castle into a new Ottoman Fort, which was strengthened considerably with walls measuring 2 meters thick. The underground chamber and the first floor were transformed into prison cells and the Castle remained in use as a prison until 1950. From 1950 considerable maintenance work in the Castle were implemented the Limassol Castle was " +
				"used as a district archaeological museum. On 28th March 1987 after another renovation and maintenance work the Limassol Castle became the Mediaeval Museum of Cyprus. Now it contains exhibits dated from 4th to 19th century such as gold, silver and bronze objects, including the famous �David� plates from 7th century Lambousa, collection of ancient coins, scissors, keys, " +
				"buckles and jewellery dating from the 13th century, mediaeval pottery from Italy, works of Islamic art, exhibition of weapons, Frankish and Venetian tombstones and many other rarities."
				, "zappeio"));
	}
		
}

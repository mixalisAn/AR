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
				"The most exquisite church of the island. In 1891, the construction of the church of St. Napa as it stands today, started on the site of an older and smaller church that was built in 1738; the church was completed and given for use to the public in 1903. Its iconostasis, made of marble, was sculpted by A. Thymopoulos, " +
				"while its majestic frescoes were painted by the Greek artists Michael Koufos and Otto Giavopoulos."
				,"agia_napa", 1, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agia_napa-70'_3.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agia_napa_present.jpg"));
		
		initialPois.add(new PoiData("Katholiki", 34.675612, 33.041722 , 0 , null , "Ellados",
				"The church of Katholiki (the older one) which used to be the Latin Cathedral of the Franciscan Order. In 1864, it was passed on the hands of the Orthodox Church and was fundamentally restored. It was demolished in 1970 for the construction of a “grander” one, dedicated to Panaghia Pantanassa (The Omnipotent Virgin). " + 
				"On the left bell-tower, one can see the clock donated by Iphigenia I. Karageorghiades, wife of the Mayor of Limassol Karageorghiades (1885 - 1908)."
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
				"The 1st Municipal Marker was built in 1918 during the mayoralty of Spyros Araouzos based on designs of Zacharias Vondas. Several extensions and alterations were made but the most significant one was that of 1932 – 1933 when it was roofed and the central spaces of the market, " + 
				"the groceries, the butcher’s etc. were rented. However, reactions to this project were not rare at this time. In an article in the newspaper Salpix of Limassol on August 2nd 1920, the renowned George Frangoudes from Limassol wrote:" + 
				"The invention of one and only market in the whole town is of Limassol ingenuity, for neither in any other town of Cyprus nor in any other place in both hemispheres is there such a thing. Nothing can be sold outside; thank God eggs and fish are still permitted to be sold on the streets." + 
				"Thus, the citizen of Synachorion is obliged to travel for half an hour every day to buy a cucumber.' He concluded, I find this unpopular, undemocratic, tyrannical. Let's face it; in building a municipal market no ornamental purpose was achieved. How come in such an innovative town our schools" +
				"were built in a cul-de-sac, the church of St. Napa in an inadequate triangle and the new market with only half of its facade facing the square, something entirely of bad taste?"
				,"agora", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agora-70'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agora_present.jpg"));
		
		initialPois.add(new PoiData("Aktaion", 34.675398, 33.048734 , 0 , null , "Molos",
				"Among the many places of entertainment for the citizens of Limassol since the beginning of this century, the Aktaion coffee-house stood out. With half of the structure standing on wooden stilts in the sea, it was built by the businessman and captain Theodoros Mavros. It had days of glory participating in every cultural, artistic and recreational event of the town ranging from cinema and theatre to ball nights and cabaret shows. It was here that female artists from many nation, French, Spanish, Germans, Austrians and Italians, offered their charm visual and other pleasures to the patrons of the Aktaion. It was demolished in the 1950’s " + 
				"for the embellishment and the ‘expansion of the mall…’"
				,"aktaion", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/aktaion-1926_3.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/aktaion_present.jpg"));
		
		initialPois.add(new PoiData("City Hall", 34.6749, 33.044131 , 0 , "http://www.limassolmunicipal.com.cy/index_en.html" , "Arxiepiskopou Kiprianou",
				"The building was made in 1979. No more info available"
				,"city_hall", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/city_hall_1.jpg","http://www.cut.ac.cy/images/cut/galleries/other/0001/city_hall_present.jpg"));
		
		initialPois.add(new PoiData("Public Baths", 34.680513, 33.054061 , 0 , null , "28th Oktomvriou",
				"The public Baths on the beach opposite the GSO stadium. They were built in the early 1930’s during the mayoralty of Chr. Hadjipavlou on the same site where once stood the Municipal Old People’s Home and formerly the first Quarantine-Hospital of Cyprus. In 1948, Mayor Ploutis Servas had the Public Baths " + 
				"extended and embellished."
				,"public_baths", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_baths-1926%20_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_baths_present.jpg"));
		
		initialPois.add(new PoiData("Public Library", 34.6802, 33.049278 , 0 , "http://www.limassolmunicipal.com.cy/publiclibrary/index_en.html" , "Agiou Andreou",
				"The building was made in 1979. No more info available"
				,"public_library", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_library_4.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/public_library_present.jpg"));
		
		initialPois.add(new PoiData("Castle", 34.672262, 33.04179 , 0 , "http://en.wikipedia.org/wiki/Limassol_Castle" , "Vasilisis Veregarias",
				"The Castle of Limassol can be found on the western edge of the historic city center and its purpose was to guard and protect both the port and the city itself. The present edifice dates back to the Ottoman period (1590) and it was in this architectural phase that the remains of the earlier" +
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
				"All the official government buildings as well as many other houses were decorated with English flags on the occasion of the Coronation of King George VI and Elizabeth. "
				,"historical_archive", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/historical_archive-%2030'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/historical_archive_present.jpg"));
		
		initialPois.add(new PoiData("Pattiki", 34.674366, 33.042221 , 0 , null , "Spartis - Ellados",
				"The building was made in 1979. No more info available"
				,"pattiki", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/patiki-60_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/patiki_present.jpg"));
		
		initialPois.add(new PoiData("Trigono Sxiza", 34.673832, 33.043933 , 0 , null , "Agiou Andreou - Genethliou Mitela",
				"The building was made in 1979. No more info available"
				,"trigono_sxiza", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/trigono_sxiza_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/trigono_sxiza_present.jpg"));
		
		initialPois.add(new PoiData("Old Law Court", 34.675568, 33.044796 , 0 , null , "Ifigenias",
				"The Courts of Limassol were built in 1911. They are considered characteristic examples of colonial architecture. On the other side of the pavement, all the attorneys used to gather with Evelthon Pitsillides as their leader. He was more commonly known by his pen-name Eskios Pefkis, and his humor dominated in various jokes that originated there and later on speared throughout the town."
				,"old_law_court", 5, "http://www.cut.ac.cy/images/cut/galleries/other/0001/old_law_court-60'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/old_law_court_present.jpg"));
		//sto molos exei mpei pier giati iparxei provlima me tin katigoria molos
		initialPois.add(new PoiData("Molos", 34.670901, 33.044445 , 0 , null , "Molos",
				"The building was made in 1979. No more info available"
				,"pier", 6, "http://www.cut.ac.cy/images/cut/galleries/other/0001/molos_10.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/molos_present.jpg"));
		
		initialPois.add(new PoiData("Carnival", 34.675482, 33.04769 , 0 , null , "Spirou Araouzou",
				"According to testimony the routes of the Limassol carnival can be traced back to the rituals and mysteries of ‘Old Limassol’, from pre-Christian times, to ancient Amathus and the Cronia and Dionysia Festivals " +
				"but in particular the ceremonies dedicated to Adonis. One historic connection of these ancient happenings with the modern carnival is the following: during carnival celebrations and parades of the last years of the " +
				"previous century and for many years later, a usual carnival figure was that of a pregnant woman, who, with sobs and loud cries and aided by men disguised in women’s clothes, was trying to give birth. This repeated carnival " +
				"image prompted the people of Limassol to react to this ‘disgusting spectacle’. One particular year, around 1908-1909, the principal of Pancyprian Gymnasium and later on professor at the University of Athens, the philologist-historian " +
				"Michael Volonakis, present at the carnival. When he realized the disgust ant the outcry of the people of Limassol, he explained to them how important this seemingly naïve imitation was as far as history was concerned. The sight you are " +
				"looking at has historical significance and I am watching it with surprise and respect. It seems that the custom of the carnival in your town originates from this: when the mythical hero Theseus was returning triumphantly from Crete, where " +
				"he had killed Minotaur, his ship affected by adverse winds drifted and anchored at the harbor of Amathus. In this voyage he was not alone. He was accompanied by Ariadne, the daughter of King Minos, who had helped him get out of the Labyrinth. " +
				"Theseus stayed for a few days as a guest of the King of Amathus and, soon afterwards, departed from Cyprus leaving Ariadne behind. The princess was pregnant by Theseus and when labour pains began, the court officials with their wives thought it proper, " +
				"in order to honour the status of the foreign princess, to gather in the confirmed woman’s room and all together imitate her movements, screams and exclamations in order to show their sympathy towards the abandoned woman. This imitation was a polite way to " +
				"reduce the painful moments of her highness. Every year the citizens of Amathus celebrated this noble event by play-acting the scene of the birth. This custom was repeated, as it seems, for many centuries; it was passed on and later adopted by the people of " +
				"Amathus who relocated to New Limassol and it has remained as one of the celebrations of today’s carnival. The historically plausible interpretation made the people of Limassol, on the one hand, to appreciate this ancient custom and to stop considering this " +
				"imitation as ‘disgusting’ spectacle, and on the other hand, to think of it as irrefutable proof of the link between antiquity and modern tradition."
				,"carnival", 3, "http://www.cut.ac.cy/images/cut/galleries/other/0001/carnival_8.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/carnival_present.jpg"));
		
		initialPois.add(new PoiData("Enaerios", 34.684197, 33.060356 , 0 , null , "28th Oktomvriou",
				"The mines of the Cyprus Asbestos Company were opened in 1907 establishing a dynamic working community in the area. IN 1916, an Italian company started the construction of the elevated railway, which was put into operation in 1918, for the direct transportation of " +
				"the ore to Limassol. On the left, the photograph depicts the wagons of the railway, while, on the right, one can see the warehouses of asbestos in the area which is still known today as ‘Enaerios’ (elevated railway). The warehouses, a unique example of industrial " +
				"architecture, which could appropriately have been converted into versatile cultural places (like the Theodosios warehouses), were demolished in the late 1970’s. only the pier bearing the same name was spared."
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
				"Corner of Gnethlis Mitellas street (residence of the benefactor of the town, Genethlis Mitela) and Commandaria street, where the exports of commandaria and other wines took place. The product ended up at the warehouses of the Customs House and at the larger pier. "
				,"genethliou_mitela", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/genethliou_mitela-80'_5.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/genethliou_mitela_present.jpg"));
		
		initialPois.add(new PoiData("Agiou Andreou - Ifigenias", 34.674825, 33.045858 , 0 , null , "Agiou Andreou - Ifigenias",
				"The building was made in 1979. No more info available"
				,"agiou_andreou_ifigenias", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/agiou_andreou_ifigenias-60'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/agiou_andreou_ifigenias_present.jpg"));
		
		initialPois.add(new PoiData("Saripolou", 34.674639, 33.043707 , 0 , null , "Saripolou",
				"The building was made in 1979. No more info available"
				,"saripolou", 7, "http://www.cut.ac.cy/images/cut/galleries/other/0001/saripolou-80'_1.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/saripolou_present.jpg"));
		
		initialPois.add(new PoiData("Vasiliou Makedonos", 34.677533,33.046642 , 0 , null , "Vasileiou Makedonos",
				"Vasileiou Macedonos street – “Quartier Latin”. One of the most important streets of Limassol. In this street outstanding figures of all walks of life of the town were born, lived or resided temporarily: Artists, intellectuals, politicians, merchants etc. For this reason it was also called half seriously half jokingly “Quartier Latin” after the well known quarter of artists in old Paris."
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
				"On one of the recent autumn evenings, I was passing by the large municipal square near the Kesogloudion quarter in one of the best areas of Limassol opposite the large square. We saw a human mass working on a majestic building. Last year, " + 
				"Messrs Giordamlis gave our town a large and remarkable closed theatre. This year, towards the end of next December, Chryssochoos Bros., sparing neither money nor pain, just as the former ones, will give our town another artistic and beautiful " + 
				"theatre which will be big enough to seat 1500 spectators. The theatre was designed by the experienced engineer, Mr.Cousver of the Cyprus Contracting Company, who stated that it would be one of the finest theaters in the Orient. It is being built as " +
				"an amphitheater, so that a large circle will hold about 400 seats. The stage will be a large well decorated basement which will be used as a smoking-room. Thus, the discomfort caused by the smoke, which can be observed in all our theatres because of the " + 
				"absence of special smoking-rooms, will cease. Inside the theatre, four luxurious balconies, which will be used by dignitaries, are being built on either side, which are going to be properly decorated with mirrors, velvet fittings and carpets. When they are " +
				"not used by dignitaries, these will be rented to individuals for a substantial amount of money. Automatic telephones will be installed connecting the box-office with the prompter’s box, the stage, the manager’s office and the operator’s office. The theatre, " +
				"apart from the seats, the stage equipment and the stage settings, will cost £2500. As foreman, Messrs Chryssochoos Bros. appointed the experienced and tireless Mr. Nick Kaimaklioti. The master craftsman of our town, Mr. Gregory Demetri, was hired to work on " +
				"the concrete parts of the building. The seats of the theatre, which are most modern, were ordered from Europe. The rialto was inaugurated on March 26th, 1932.Alithia, November 30th, 1932"
				,"rialto_theater", 8, "http://www.cut.ac.cy/images/cut/galleries/other/0001/rialto-60'_2.jpg", "http://www.cut.ac.cy/images/cut/galleries/other/0001/rialto_present.jpg"));
		
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

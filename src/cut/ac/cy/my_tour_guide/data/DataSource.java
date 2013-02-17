package cut.ac.cy.my_tour_guide.data;

import java.util.List;

import cut.ac.cy.my_tour_guide.ui.Marker;

/**
 * This abstract class should be extended for new data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public abstract class DataSource {

    public abstract List<Marker> getMarkers();
}

package cut.ac.cy.my_tour_guide.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;


import cut.ac.cy.my_tour_guide.data.ARData;
import cut.ac.cy.my_tour_guide.helpers.CollisionDetector;
import cut.ac.cy.my_tour_guide.ui.Marker;
import cut.ac.cy.my_tour_guide.ui.Radar;

/**
 * This class extends the View class and is designed draw the zoom bar, radar
 * circle, and markers on the View.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Michalis Anastasiou
 */
public class AugmentedView extends View {

    private static final AtomicBoolean drawing = new AtomicBoolean(false);
    
    private CollisionDetector detector;
    private static Radar radar ;
    private static final List<Marker> cache = new ArrayList<Marker>();
    private static final TreeSet<Marker> updated = new TreeSet<Marker>();
    
    public AugmentedView(Context context) {
        super(context);
        radar = new Radar(context);
    }
    
    public void setCollisionDetector(CollisionDetector detector){
    	this.detector = detector;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
    	List<Marker> collisionMarkers = null;
        if (canvas == null) return;

        if (drawing.compareAndSet(false, true)) {
            // Get all the markers
            List<Marker> collection = ARData.getMarkers();

            // Prune all the markers that are out of the radar's radius (speeds
            // up drawing and collision detection)
            cache.clear();
            for (Marker m : collection) {
                m.update(canvas, 0, 0);
                if (m.isOnRadar() && m.isInView()) cache.add(m);
            }
            collection = cache;

            if (AugmentedReality.useCollisionDetection) {
            	collisionMarkers = adjustForCollisions(canvas, collection);
                detector.collisionMarkers(collisionMarkers);
            }
            
            
            // Draw AR markers in reverse order since the last drawn should be
            // the closest
            ListIterator<Marker> iter = collection.listIterator(collection.size());
            while (iter.hasPrevious()) {
                Marker marker = iter.previous();
                marker.draw(canvas);
            }

            // Radar circle and radar markers
            if (AugmentedReality.showRadar) radar.draw(canvas);
            drawing.set(false);
        }
    }

    private static List<Marker> adjustForCollisions(Canvas canvas, List<Marker> collection) {
        updated.clear();
        List<Marker> collisionMarkers = new ArrayList<Marker>();
        // Update the AR markers for collisions
        for (Marker marker1 : collection) {
            if (updated.contains(marker1) || !marker1.isInView())
                continue;

            int collisions = 1;
            for (Marker marker2 : collection) {
                if (marker1.equals(marker2) || updated.contains(marker2) || !marker2.isInView())
                    continue;

                if (marker1.isMarkerOnMarker(marker2)) {
                    collisions++;
                    updated.add(marker2);
                    collisionMarkers.add(marker2);
                }
            }
            if(collisions > 1)
            	collisionMarkers.add(marker1);
            updated.add(marker1);
        }
       return collisionMarkers;
    }
}

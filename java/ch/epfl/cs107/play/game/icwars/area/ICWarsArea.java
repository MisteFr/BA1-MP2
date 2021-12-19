package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsArea extends Area {

    private final List<Unit> unitsList = new LinkedList<>();

    public static final float SCALE_FACTOR = 10.f; //Set to a higher value to see more of the Area

    protected abstract void createArea();

    public abstract DiscreteCoordinates getDefaultCursorPosition();

    public abstract DiscreteCoordinates getEnemySpawnPosition();

    /**
     * @param unit
     * Add a unit to the area and to the unitsList.
     */
    public void addUnit(Unit unit){
        registerActor(unit);
        unitsList.add(unit);
    }

    /**
     * @param unit
     * Remove a unit from the area and from the unitsList.
     */
    public void removeUnit(Unit unit){
        unregisterActor(unit);
        unitsList.remove(unit);
    }

    /**
     * @return List<Unit>
     */
    public List<Unit> getUnitsList(){
        return unitsList;
    }

    /**
     * Reset the area
     */
    public void resetArea(){
        unitsList.clear();
    }

    /**
     * Return the distance between two points p1 and p2
     *
     * @param p1 first point
     * @param p2 second point
     * @return distance between the two point p1 and p2
     */
    public static double getDistance(DiscreteCoordinates p1, DiscreteCoordinates p2){
        return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem){
        if(super.begin(window, fileSystem)) {
            ICWarsBehavior behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();

            return true;
        }
        return false;
    }
}

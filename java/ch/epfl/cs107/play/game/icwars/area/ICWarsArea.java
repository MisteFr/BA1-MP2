package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.City;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsArea extends Area {

    private final List<Unit> UNITS_LIST = new LinkedList<>();
    private final List<City> CITIES_LIST = new LinkedList<>();
    private final static int CITY_HEALING_AMOUNT = 2;

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
        UNITS_LIST.add(unit);
    }

    /**
     * @param city
     * Add a city to the area and to the unitsList.
     */
    public void addCity(City city){
        registerActor(city);
        CITIES_LIST.add(city);
    }

    /**
     * @param unit
     * Remove a unit from the area and from the unitsList.
     */
    public void removeUnit(Unit unit){
        unregisterActor(unit);
        UNITS_LIST.remove(unit);
    }

    /**
     * @return List<Unit>
     */
    public List<Unit> getUnitsList(){
        return UNITS_LIST;
    }

    /**
     * Reset the area
     */
    public void resetArea(){
        UNITS_LIST.clear();
    }

    /**
     * Heals units that are on a friendly city at the end of a turn.
     */
    public void healUnits(){
        for(City c: CITIES_LIST){
            DiscreteCoordinates cPos = new DiscreteCoordinates(c.getPosition());
            for(Unit u: getUnitsList()){
                DiscreteCoordinates unitPos = new DiscreteCoordinates(u.getPosition());

                if(u.getFaction() != c.getFaction() && cPos.equals(unitPos)){
                    u.heal(CITY_HEALING_AMOUNT);
                }
            }
        }
    }

    /**
     * Return if an entity is in the range of another one
     *
     * @param p1 first point
     * @param p2 second point
     * @param range
     * @return if p2 is in the range of 1
     */
    public static boolean isInRange(DiscreteCoordinates p1, DiscreteCoordinates p2, int range){
            return ((Math.abs(p2.x - p1.x) <= range) && (Math.abs(p2.y - p1.y) <= range));
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

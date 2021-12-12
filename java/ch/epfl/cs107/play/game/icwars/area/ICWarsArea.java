package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsArea extends Area {

    private ICWarsBehavior behavior;
    private List<Unit> unitsList = new LinkedList<>();

    public static final float SCALE_FACTOR = 10.f; //Set to a higher value to see more of the Area

    protected abstract void createArea();

    public abstract DiscreteCoordinates getDefaultCursorPosition();

    public abstract DiscreteCoordinates getEnemySpawnPosition();

    /**
     * @param unit
     * Add an unit to the area and to the unitsList.
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
        unitsList.remove(unit);
        System.out.println("unregister?");
        unregisterActor(unit);
    }

    /**
     * @return List<Unit>
     */
    public List<Unit> getUnitsList(){
        return unitsList;
    }

    public void resetArea(){
        unitsList.clear();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem){
        if(super.begin(window, fileSystem)) {
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
}

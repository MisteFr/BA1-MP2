package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsArea extends Area {

    private ICWarsBehavior behavior;
    private List<Unit> unitsList = new LinkedList<>();

    public static final float SCALE_FACTOR = 10.f; //Set to a higher value to see more of the Area

    protected abstract void createArea();

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

    /**
     * Register an unit : will be added at next update
     * @param unit (Unit): the unit to register, not null
     */
    public void registerUnit(Unit unit){
        unitsList.add(unit);
    }

    /**
     * Unregister an unit : will be removed at next update
     * @param unit (Unit): the unit to unregister, not null
     */
    public void unregisterUnit(Unit unit){
        unitsList.remove(unit);
    }
}

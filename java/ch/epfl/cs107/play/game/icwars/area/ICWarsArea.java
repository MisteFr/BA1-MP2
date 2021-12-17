package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
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
     * Returns an array containing the availability of the 8 neighbours cell of the cell at
     * coordinates <code>(x, y)</code>.
     * <p>
     * The cells values are returned such that their indices corresponds to the following
     * diagram:<br>
     * ------------- <br>
     * | 7 | 0 | 1 | <br>
     * ------------- <br>
     * | 6 | _ | 2 | <br>
     * ------------- <br>
     * | 5 | 4 | 3 | <br>
     * ------------- <br>
     * <p>
     * @param unit The unit we want to move
     * @param x the x coordinate of the cell we want to move to
     * @param y the y coordinate of the cell we want to move to
     * @return an array of booleans such that c[i] is true if we can move there, and false if not
     */
    public boolean[] getNeighboursAvailability(Unit unit, int x, int y){
        assert ((0 <= x && x <= getWidth()) && (0 <= y && y <= getHeight()));
        boolean[] neighbours = new boolean[8];

        List<DiscreteCoordinates> c0 = new LinkedList<DiscreteCoordinates>();
        c0.add(new DiscreteCoordinates(new Vector((float)x, (float)y + 1)));
        List<DiscreteCoordinates> c1 = new LinkedList<DiscreteCoordinates>();
        c1.add(new DiscreteCoordinates(new Vector((float)x + 1, (float)y + 1)));
        List<DiscreteCoordinates> c2 = new LinkedList<DiscreteCoordinates>();
        c2.add(new DiscreteCoordinates(new Vector((float)x + 1, (float)y)));
        List<DiscreteCoordinates> c3 = new LinkedList<DiscreteCoordinates>();
        c3.add(new DiscreteCoordinates(new Vector((float)x + 1, (float)y - 1)));
        List<DiscreteCoordinates> c4 = new LinkedList<DiscreteCoordinates>();
        c4.add(new DiscreteCoordinates(new Vector((float)x, (float)y - 1)));
        List<DiscreteCoordinates> c5 = new LinkedList<DiscreteCoordinates>();
        c5.add(new DiscreteCoordinates(new Vector((float)x - 1, (float)y - 1)));
        List<DiscreteCoordinates> c6 = new LinkedList<DiscreteCoordinates>();
        c6.add(new DiscreteCoordinates(new Vector((float)x - 1, (float)y)));
        List<DiscreteCoordinates> c7 = new LinkedList<DiscreteCoordinates>();
        c7.add(new DiscreteCoordinates(new Vector((float)x - 1, (float)y + 1)));

        if(behavior.canEnter(unit, c0)) {
            neighbours[0] = true;
        }
        if(behavior.canEnter(unit, c1)) {
            neighbours[1] = true;
        }
        if(behavior.canEnter(unit, c2)) {
            neighbours[2] = true;
        }
        if(behavior.canEnter(unit, c3)) {
            neighbours[3] = true;
        }
        if(behavior.canEnter(unit, c4)) {
            neighbours[4] = true;
        }
        if(behavior.canEnter(unit, c5)) {
            neighbours[5] = true;
        }
        if(behavior.canEnter(unit, c6)) {
            neighbours[6] = true;
        }
        if(behavior.canEnter(unit, c7)) {
            neighbours[7] = true;
        }

        // for the cells that have not been explicitly initialized, i.e. those that have something on them,
        // i.e. those we can't move to
        for (int i = 0; i < neighbours.length; i++){
            if (!neighbours[i]){
                neighbours[i] = false;
            }
        }

        return neighbours;
    }

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

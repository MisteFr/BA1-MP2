package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;

import java.util.ArrayList;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor {
    protected PlayState currentState;
    protected Unit selectedUnit = null;
    protected ArrayList<Unit> unitsList = new ArrayList<Unit>();


    ICWarsPlayer(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units){
        super(owner, coordinates, factionType);
        currentState = PlayState.IDLE;

        for(Unit unit : units){
            unitsList.add(unit);
        }
        for(Unit unit : unitsList){
            owner.registerActor(unit);
        }
    }

    public enum PlayState {
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION
    }

    @Override
    public void update(float deltaTime) {
        for(Unit unit: unitsList){
            if(unit.getHp() <= 0){
                getOwnerArea().unregisterActor(unit);
                unitsList.remove(unit);
            }
        }
        super.update(deltaTime);
    }

    @Override
    public void leaveArea() {
        for(Unit unit: unitsList){
            getOwnerArea().unregisterActor(unit);
        }
        super.leaveArea();
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Return whether the player is defeated or not
     * @return (boolean)
     */
    public boolean isDefeated() {
        if(unitsList.size() == 0){
            return  true;
        }

        return false;
    }

    /**
     * Called when player turn starts
     */
    public void startTurn(){
        currentState = PlayState.NORMAL;
        for (Unit unit : unitsList){
            unit.setAvailable(true);
        }
        this.centerCamera();
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        if (currentState == PlayState.SELECT_CELL){
            this.currentState = PlayState.NORMAL;
        }
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if (!super.changePosition(newPosition) || false){ //todo : change false to check if a node exists in action range
            return false;
        }

        //todo : does it work if we just copy this from the superclass ?
        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        setCurrentPosition(newPosition.toVector());
        getOwnerArea().enterAreaCells(this, getCurrentCells());

        return true;
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    public PlayState getCurrentState(){
        return currentState;
    }
}

package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

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
        this.centerCamera();
        //todo : units become available

    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }
}

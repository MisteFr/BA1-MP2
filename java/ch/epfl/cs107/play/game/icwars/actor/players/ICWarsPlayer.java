package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {
    protected PlayState currentState;
    protected Unit selectedUnit = null;
    protected ArrayList<Unit> unitsList = new ArrayList<Unit>();


    ICWarsPlayer(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units){
        super(owner, coordinates, factionType);
        setCurrentState(PlayState.IDLE);

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
        setCurrentState(PlayState.NORMAL);
        for (Unit unit : unitsList){
            unit.setAvailable(true);
        }
        centerCamera();
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        if (getCurrentState() == PlayState.SELECT_CELL){
            setCurrentState(PlayState.NORMAL);
        }
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /**
     * Get the current PlayState of the player
     */
    public PlayState getCurrentState(){
        return currentState;
    }

    /**
     * Set the current PlayState of the player
     * @param state (PlayState): new state
     */
    public void setCurrentState(PlayState state){
        System.out.println("New state is " + state);
        currentState = state;
    }


    // Interactor interface methods
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor visitor){
        ((ICWarsInteractionVisitor)visitor).interactWith(this);
    }


}

package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {
    private PlayState currentState;
    private PlayState previousState;
    protected Unit selectedUnit = null;
    protected List<Unit> unitsList = new LinkedList<>();


    ICWarsPlayer(ICWarsArea area, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units) {
        super(area, coordinates, factionType);
        setCurrentState(PlayState.IDLE);

        for (Unit unit : units) {
            area.addUnit(unit);
            unitsList.add(unit);
        }
    }

    public enum PlayState {
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION, PAUSED
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        ICWarsArea area = (ICWarsArea) getOwnerArea();

        for (int i = 0; i < area.getUnitsList().size(); i++) {
            if (area.getUnitsList().get(i).getHp() == 0) {
                unitsList.remove(area.getUnitsList().get(i));
                area.removeUnit(area.getUnitsList().get(i));
            }
        }
    }

    /**
     * Pause the player and switch it to state NORMAL
     * We save the previous state too.
     */
    public void pause(){
        previousState = getCurrentState();
        setCurrentState(PlayState.PAUSED);
    }

    /**
     * Resumt the player and switch it back to its last state before pausing
     */
    public void resume(){
        setCurrentState(previousState);
    }

    @Override
    public void leaveArea() {
        ICWarsArea area = (ICWarsArea) getOwnerArea();
        for (Unit unit : unitsList) {
            area.removeUnit(unit);
        }
        unitsList.clear();

        super.leaveArea();
    }

    /**
     * Return whether the player is defeated or not
     *
     * @return (boolean)
     */
    public boolean isDefeated() {
        return unitsList.size() == 0;
    }

    /**
     * Called when player turn starts
     */
    public void startTurn() {
        setCurrentState(PlayState.NORMAL);
        for (Unit unit : unitsList) {
            unit.setAvailable(true);
        }
        centerCamera();
    }

    public boolean unitsAvailableRemaining() {
        for (Unit unit : unitsList) {
            if(unit.isAvailable()){
                return true;
            }
        }
        return false;
    }

    /**
     * Called when player turn ends
     */
    public void endTurn() {
        selectedUnit = null;
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        if (getCurrentState() == PlayState.SELECT_CELL) {
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
    public PlayState getCurrentState() {
        return currentState;
    }

    /**
     * Set the current PlayState of the player
     *
     * @param state (PlayState): new state
     */
    public void setCurrentState(PlayState state) {
        currentState = state;
    }

    /**
     * Get the unitsList of the player
     */
    public List<Unit> getUnitsList() {
        return unitsList;
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
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor visitor) {
        ((ICWarsInteractionVisitor) visitor).interactWith(this);
    }
}

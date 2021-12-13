package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;

public class RealPlayer extends ICWarsPlayer {
    private final static int MOVE_DURATION = 8;
    private final ICWarsPlayerGUI gui;
    private final ICWarsInteractionHandler handler = new ICWarsInteractionHandler();
    private Sprite sprite;
    private Action currentAction;

    public RealPlayer(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units) {
        super(owner, coordinates, factionType, units);
        if(factionType == ICWarsFactionType.ALLY){
            sprite = new Sprite("icwars/allyCursor", 1.f, 1.f,this);
        }else if (factionType == ICWarsFactionType.ENEMY){
            sprite = new Sprite("icwars/enemyCursor", 1.f, 1.f,this);
        }

        gui = new ICWarsPlayerGUI(10f, this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getOwnerArea().getKeyboard();

        switch(getCurrentState()){
            case NORMAL:

                moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
                moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
                moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
                moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

                if (keyboard.get(Keyboard.ENTER).isReleased()){
                    setCurrentState(PlayState.SELECT_CELL);
                } else if (keyboard.get(Keyboard.TAB).isReleased()){
                    setCurrentState(PlayState.IDLE);
                }
                break;
            case SELECT_CELL:

                moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
                moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
                moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
                moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

                if (selectedUnit != null && selectedUnit.isAvailable()){
                    setCurrentState(PlayState.MOVE_UNIT);
                }
                break;
            case MOVE_UNIT:

                moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
                moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
                moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
                moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

                if (keyboard.get(Keyboard.ENTER).isReleased()){
                    if (selectedUnit.isAvailable()){
                        selectedUnit.changePosition(new DiscreteCoordinates(getPosition()));
                        selectedUnit.setAvailable(false);
                        setCurrentState(PlayState.ACTION_SELECTION);
                    }
                } else if (keyboard.get(Keyboard.TAB).isReleased()){
                    setCurrentState(PlayState.NORMAL);
                }
                break;
            case ACTION_SELECTION:
                for(Action act: selectedUnit.getActionsList()){
                    if (keyboard.get(act.getKey()).isDown()){
                        currentAction = act;
                        setCurrentState(PlayState.ACTION);
                    }
                }
                break;
            case ACTION:
                currentAction.doAction(deltaTime, this, keyboard);
                break;
        }
    }
    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, ch.epfl.cs107.play.window.Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
                //gui.setCell();
            }
        }
    }



    /**
     * Leave an area by unregister this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /*
    public void selectUnit(int position){
        if(position < unitsList.size()){
            this.selectedUnit = unitsList.get(position);
            gui.setSelectedUnit(this.selectedUnit);
        }
    }
     */

    @Override
    public void draw(Canvas canvas) {
        if(getCurrentState() != PlayState.IDLE){
            sprite.draw(canvas);
            gui.draw(canvas);
        }
        if(getCurrentState() == PlayState.ACTION){
            currentAction.draw(canvas);
        }
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        gui.setHoveredUnit(null);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }

    private class ICWarsInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            if (getCurrentState() == PlayState.SELECT_CELL && (unit.getFaction() == getFaction()) && selectedUnit != unit) {
                selectedUnit = unit;
                gui.setSelectedUnit(unit);
            }

            gui.setHoveredUnit(unit);
        }

        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            gui.setCell(cell.getType());
        }
    }
}

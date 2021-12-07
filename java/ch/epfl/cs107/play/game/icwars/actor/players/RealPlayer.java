package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class RealPlayer extends ICWarsPlayer {
    private Sprite sprite;
    private final static int MOVE_DURATION = 8;
    private ICWarsPlayerGUI gui;
    private ICWarsInteractionHandler handler = new ICWarsInteractionHandler();

    public RealPlayer(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units) {
        super(owner, coordinates, factionType, units);
        if(factionType == ICWarsFactionType.ALLY){
            sprite = new Sprite("icwars/allyCursor", 1.f, 1.f,this);
        }else if (factionType == ICWarsFactionType.ENEMY){
            sprite = new Sprite("icwars/enemyCursor", 1.f, 1.f,this);
        }

        gui = new ICWarsPlayerGUI(10f, this);
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
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

                if (selectedUnit != null){
                    setCurrentState(PlayState.MOVE_UNIT);
                }
                break;
            case MOVE_UNIT:

                moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
                moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
                moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
                moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

                if (keyboard.get(Keyboard.ENTER).isReleased()){
                    this.selectedUnit.changePosition(new DiscreteCoordinates(getPosition()));
                    this.selectedUnit.setAvailable(false);

                    setCurrentState(PlayState.NORMAL);
                } else if (keyboard.get(Keyboard.TAB).isReleased()){
                    setCurrentState(PlayState.NORMAL);
                }
                break;
            case ACTION:
                //todo: write some code here later
                break;
            case ACTION_SELECTION:
                //todo: write some code here later
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
            }
        }
    }



    /**
     * Leave an area by unregister this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     * @param area (Area): initial area, not null
     */
    public void enterArea(Area area){
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        resetMotion();
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
        sprite.draw(canvas);
        gui.draw(canvas);
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
        other.acceptInteraction(handler);
    }

    private class ICWarsInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            if (getCurrentState() == PlayState.SELECT_CELL && (unit.getFaction() == getFaction())) {
                System.out.println("You selected " + unit.getName());
                selectedUnit = unit;
                gui.setSelectedUnit(unit);
            }
        }
    }
}

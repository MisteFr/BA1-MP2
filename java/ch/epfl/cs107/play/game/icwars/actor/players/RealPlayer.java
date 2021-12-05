package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class RealPlayer extends ICWarsPlayer {
    private Sprite sprite;
    private final static int MOVE_DURATION = 8;
    private ICWarsPlayerGUI gui;

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

        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        switch(currentState){
            case NORMAL:
                if (keyboard.get(Keyboard.ENTER).isDown()){
                    currentState = PlayState.SELECT_CELL;
                } else if (keyboard.get(Keyboard.TAB).isDown()){
                    currentState = PlayState.IDLE;
                }
                break;
            case SELECT_CELL:
                if (selectedUnit != null){
                    currentState = PlayState.MOVE_UNIT;
                }
                break;
            case MOVE_UNIT:
                if (keyboard.get(Keyboard.ENTER).isDown()){
                    //todo move the unit and mark as used
                    //this.selectedUnit.move();

                    currentState = PlayState.NORMAL;
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
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }
}

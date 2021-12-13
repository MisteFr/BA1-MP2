package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class AIPlayer extends ICWarsPlayer {

    private final static int MOVE_DURATION = 8;
    private final AIPlayer.ICWarsInteractionHandler handler = new AIPlayer.ICWarsInteractionHandler();
    private Sprite sprite;
    private boolean counting;
    private float counter;
    private Action currentAction;

    public AIPlayer(ICWarsArea area, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units) {
        super(area, coordinates, factionType, units);
        if(factionType == ICWarsFactionType.ALLY){
            sprite = new Sprite("icwars/allyCursor", 1.f, 1.f,this);
        }else if (factionType == ICWarsFactionType.ENEMY){
            sprite = new Sprite("icwars/enemyCursor", 1.f, 1.f,this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(getCurrentState() != PlayState.IDLE){
            sprite.draw(canvas);
        }
    }

    @Override
    public boolean isCellInteractable() {
        return true;
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
    public void interactWith(Interactable other) {
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch(getCurrentState()){
            case NORMAL:
                setCurrentState(PlayState.SELECT_CELL);
                break;
            case SELECT_CELL:
                //move to nearest unit
                boolean foundUnitAvailable = false;
                for(Unit unit: unitsList){
                    if(unit.isAvailable()){
                        foundUnitAvailable = true;
                        selectedUnit = unit;
                    }
                }

                if(foundUnitAvailable){
                    DiscreteCoordinates positionUnit = new DiscreteCoordinates(selectedUnit.getPosition());

                    System.out.println(positionUnit);
                    orientate(selectedUnit.getOrientation());
                    //move(MOVE_DURATION);
                    changePosition(positionUnit);

                    while (true){
                        if(waitFor(1000000, deltaTime)){
                            System.out.println("we are here");
                            setCurrentState(PlayState.MOVE_UNIT);
                            break;
                        }
                    }
                }else{
                    //AIPlayer played all its units, this will end AIPlayer turns
                    setCurrentState(PlayState.IDLE);
                }
                break;
            case MOVE_UNIT:
                //we are looking for the nearest unit from the opponents units

                ICWarsArea area = (ICWarsArea) getOwnerArea();
                List<Unit> areaUnits = area.getUnitsList();
                DiscreteCoordinates selectedUnitPosition = new DiscreteCoordinates(selectedUnit.getPosition());
                float moveRadius = selectedUnit.getMoveRadius();

                boolean foundEnemyUnits = false;

                for(int i = 0; i < areaUnits.size(); ++i){
                    if(areaUnits.get(i).getFaction() != getFaction()) {
                        foundEnemyUnits = true;
                        DiscreteCoordinates opponentUnitPosition = new DiscreteCoordinates(areaUnits.get(i).getPosition());

                        if (getDistance(selectedUnitPosition, opponentUnitPosition) <= moveRadius) {

                            System.out.println("IN THE RADIUS");
                            //we can move next to it
                            selectedUnit.changePosition(opponentUnitPosition);
                            selectedUnit.setAvailable(false);
                        }else{
                            System.out.println("TOO FAR");
                            //we need to go as far as we can
                            int x = opponentUnitPosition.x;
                            int y = opponentUnitPosition.y;
                            if(Math.abs(opponentUnitPosition.x - selectedUnitPosition.x) > moveRadius){
                                System.out.println("x need to be changed");
                                if(opponentUnitPosition.x > selectedUnitPosition.x){
                                    x = (int) (selectedUnitPosition.x + moveRadius);
                                }else{
                                    x = (int) (selectedUnitPosition.x - moveRadius);
                                }
                                if(x < 0){
                                    x = 0;
                                }else if(x > (getOwnerArea().getWidth() - 1)){
                                    x = getOwnerArea().getWidth() - 1;
                                }
                            }
                            if(Math.abs(opponentUnitPosition.y - selectedUnitPosition.y) > moveRadius){
                                System.out.println("y need to be changed");
                                if(opponentUnitPosition.y > selectedUnitPosition.y){
                                    y = (int) (selectedUnitPosition.y + moveRadius);
                                }else{
                                    y = (int) (selectedUnitPosition.y - moveRadius);
                                }
                                if(y < 0){
                                    y = 0;
                                }else if(y > (getOwnerArea().getHeight() - 1)){
                                    y = getOwnerArea().getHeight() - 1;
                                }
                            }

                            DiscreteCoordinates newTeleportPosition = new DiscreteCoordinates(x, y);
                            selectedUnit.changePosition(newTeleportPosition);
                            selectedUnit.setAvailable(false);
                        }
                        System.out.println("Unit MOVED");

                        waitFor(20, deltaTime);
                        setCurrentState(PlayState.ACTION_SELECTION);
                        break;
                    }
                }

                if(!foundEnemyUnits){
                    //opponent doesn't have any units left, this will end the AIPlayer turn.
                    setCurrentState(PlayState.IDLE);
                }
                break;

            case ACTION_SELECTION:
                for(Action act: selectedUnit.getActionsList()){
                    if(act instanceof Attack){
                        currentAction = act;
                        setCurrentState(PlayState.ACTION);
                    }
                }
                break;

            case ACTION:
                currentAction.doAutoAction(deltaTime, this);
                break;
        }
    }

    /**
     * Ensures that value time elapsed before returning true
     *
     * @param dt    elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed, false otherwise
     */
    private boolean waitFor(float value, float dt) {
        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                return true;
            }
        } else {
            counter = 0f;
            counting = true;
        }
        return false;
    }

    private double getDistance(DiscreteCoordinates p1, DiscreteCoordinates p2){
        return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }

    private class ICWarsInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            if (getCurrentState() == PlayState.SELECT_CELL && (unit.getFaction() == getFaction()) && selectedUnit != unit) {
                System.out.println("bot is interacting");
                selectedUnit = unit;
            }
        }
    }
}

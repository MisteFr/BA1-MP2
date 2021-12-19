package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class AIPlayer extends ICWarsPlayer {

    private final static int WAIT_DURATION = 2;
    private Sprite sprite;
    private boolean counting;
    private float counter;
    private Action currentAction;

    public AIPlayer(ICWarsArea area, DiscreteCoordinates coordinates, ICWarsFactionType factionType, Unit... units) {
        super(area, coordinates, factionType, units);
        if (factionType == ICWarsFactionType.ALLY) {
            sprite = new Sprite("icwars/allyCursor", 1.f, 1.f, this);
        } else if (factionType == ICWarsFactionType.ENEMY) {
            sprite = new Sprite("icwars/enemyCursor", 1.f, 1.f, this);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (getCurrentState()) {
            case NORMAL:
                setCurrentState(PlayState.SELECT_CELL);
                break;

            case SELECT_CELL:
                //move to nearest unit and select it
                boolean foundUnitAvailable = false;
                for (Unit unit : unitsList) {
                    if (unit.isAvailable()) {
                        foundUnitAvailable = true;
                        selectedUnit = unit;
                    }
                }

                if (foundUnitAvailable) {
                    DiscreteCoordinates positionUnit = new DiscreteCoordinates(selectedUnit.getPosition());
                    changePosition(positionUnit);
                    centerCamera();
                    setCurrentState(PlayState.MOVE_UNIT);
                } else {
                    //AIPlayer played all its units, this will end AIPlayer turns
                    setCurrentState(PlayState.IDLE);
                }
                break;

            case MOVE_UNIT:
                if (waitFor(WAIT_DURATION, deltaTime)) {
                    ICWarsArea area = (ICWarsArea) getOwnerArea();
                    List<Unit> areaUnits = area.getUnitsList();
                    DiscreteCoordinates selectedUnitPosition = new DiscreteCoordinates(selectedUnit.getPosition());
                    float moveRadius = selectedUnit.getMoveRadius();

                    boolean foundEnemyUnits = false;

                    for (int i = 0; i < areaUnits.size(); ++i) {
                        if (areaUnits.get(i).getFaction() != getFaction()) {
                            foundEnemyUnits = true;
                            DiscreteCoordinates opponentUnitPosition = new DiscreteCoordinates(areaUnits.get(i).getPosition());

                            if (ICWarsArea.getDistance(selectedUnitPosition, opponentUnitPosition) <= moveRadius) {
                                if (selectedUnit.changePosition(opponentUnitPosition)) {
                                    changePosition(opponentUnitPosition);
                                    centerCamera();
                                    selectedUnit.setAvailable(false);
                                }
                            } else {
                                int x = opponentUnitPosition.x;
                                int y = opponentUnitPosition.y;
                                if (Math.abs(opponentUnitPosition.x - selectedUnitPosition.x) > moveRadius) {
                                    if (opponentUnitPosition.x > selectedUnitPosition.x) {
                                        x = (int) (selectedUnitPosition.x + moveRadius);
                                    } else {
                                        x = (int) (selectedUnitPosition.x - moveRadius);
                                    }
                                    if (x < 0) {
                                        x = 0;
                                    } else if (x > (getOwnerArea().getWidth() - 1)) {
                                        x = getOwnerArea().getWidth() - 1;
                                    }
                                }
                                if (Math.abs(opponentUnitPosition.y - selectedUnitPosition.y) > moveRadius) {
                                    if (opponentUnitPosition.y > selectedUnitPosition.y) {
                                        y = (int) (selectedUnitPosition.y + moveRadius);
                                    } else {
                                        y = (int) (selectedUnitPosition.y - moveRadius);
                                    }
                                    if (y < 0) {
                                        y = 0;
                                    } else if (y > (getOwnerArea().getHeight() - 1)) {
                                        y = getOwnerArea().getHeight() - 1;
                                    }
                                }

                                DiscreteCoordinates newTeleportPosition = new DiscreteCoordinates(x, y);
                                changePosition(newTeleportPosition);
                                centerCamera();
                                selectedUnit.changePosition(newTeleportPosition);
                                selectedUnit.setAvailable(false);
                            }


                            setCurrentState(PlayState.ACTION_SELECTION);
                            break;
                        }
                    }

                    if (!foundEnemyUnits) {
                        //opponent doesn't have any units left, this will end the AIPlayer turn.
                        setCurrentState(PlayState.IDLE);
                    }
                }
                break;

            case ACTION_SELECTION:
                for (Action act : selectedUnit.getActionsList()) {
                    if (act instanceof Attack) {
                        currentAction = act;
                        setCurrentState(PlayState.ACTION);
                    }
                }
                break;

            case ACTION:
                if (waitFor(WAIT_DURATION, deltaTime)) {
                    currentAction.doAutoAction(deltaTime, this);
                }
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

    @Override
    public void draw(Canvas canvas) {
        if (getCurrentState() != PlayState.IDLE) {
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

    }
}

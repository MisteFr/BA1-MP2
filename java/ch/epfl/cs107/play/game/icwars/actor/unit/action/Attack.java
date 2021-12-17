package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class Attack extends Action {
    private final static String NAME = "(A)ttack";
    private final static int KEY = Keyboard.A;

    private final LinkedList<Integer> enemyUnits = new LinkedList<>();
    private int indexUnitToAttack = 0;
    private final ImageGraphics cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
            new RegionOfInterest(4*18, 26*18, 16, 16));

    public Attack(Unit unit, ICWarsArea owner) {
        super(unit, owner);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getKey() {
        return KEY;
    }

    @Override
    public void draw(Canvas canvas) {
        if(!enemyUnits.isEmpty()){
            owner.setViewCandidate(owner.getUnitsList().get(enemyUnits.get(indexUnitToAttack)));
            cursor.setAnchor(canvas.getPosition().add(1,0));
            cursor.draw(canvas);
        }

    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        int range = actionUnit.getMoveRadius();
        DiscreteCoordinates selectedUnitPosition = new DiscreteCoordinates(actionUnit.getPosition());
        List<Unit> unitsList = owner.getUnitsList();

        enemyUnits.clear();
        for(int i = 0; i < unitsList.size(); ++i){
            if(unitsList.get(i).getFaction() != actionUnit.getFaction()) {
                DiscreteCoordinates unitAreaPosition = new DiscreteCoordinates(unitsList.get(i).getPosition());
                if (getDistance(selectedUnitPosition, unitAreaPosition) <= range) {
                    enemyUnits.add(i);
                }
            }
        }

        if(!enemyUnits.isEmpty()){
            if(keyboard.get(Keyboard.LEFT).isReleased()){
                if((indexUnitToAttack - 1) >= 0){
                    --indexUnitToAttack;
                }else{
                    indexUnitToAttack = (enemyUnits.size() - 1);
                }
            } else if (keyboard.get(Keyboard.RIGHT).isReleased()) {
                if(indexUnitToAttack  < (enemyUnits.size() - 1)){
                    ++indexUnitToAttack;
                }else{
                    indexUnitToAttack = 0;
                }
            } else if (keyboard.get(Keyboard.ENTER).isReleased()) {
                Unit selectedUnitToAttack = owner.getUnitsList().get(enemyUnits.get(indexUnitToAttack));

                actionUnit.dealDamage(selectedUnitToAttack);
                actionUnit.setAvailable(false);
                owner.setViewCandidate(player);
                player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);

                //reset
                enemyUnits.clear();
                indexUnitToAttack = 0;
            }
        }else{
            owner.setViewCandidate(player);
            player.setCurrentState(ICWarsPlayer.PlayState.ACTION_SELECTION);
            return;
        }

        if(keyboard.get(Keyboard.TAB).isReleased()){
            owner.setViewCandidate(player);
        }
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player) {
        int range = actionUnit.getMoveRadius();
        DiscreteCoordinates selectedUnitPosition = new DiscreteCoordinates(actionUnit.getPosition());
        List<Unit> unitsList = owner.getUnitsList();

        int indexUnitWithLowestHp = -1;
        int lowestHpRecorded = Integer.MAX_VALUE;
        enemyUnits.clear();
        for(int i = 0; i < unitsList.size(); ++i){
            if(unitsList.get(i).getFaction() != actionUnit.getFaction()) {
                DiscreteCoordinates unitAreaPosition = new DiscreteCoordinates(unitsList.get(i).getPosition());
                if (getDistance(selectedUnitPosition, unitAreaPosition) <= range) {
                    enemyUnits.add(i);
                    if(unitsList.get(i).getHp() < lowestHpRecorded){
                        indexUnitWithLowestHp = i;
                    }
                }
            }
        }

        if(indexUnitWithLowestHp != -1) {
            Unit selectedUnitToAttack = owner.getUnitsList().get(indexUnitWithLowestHp);

            actionUnit.dealDamage(selectedUnitToAttack);
            actionUnit.setAvailable(false);
            owner.setViewCandidate(player);
            player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);

            enemyUnits.clear();
        }else{
            //we didn't find any unit to attack
            owner.setViewCandidate(player);
            player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);
        }
    }

    /**
     * Return the distance between two points p1 and p2
     *
     * @param p1 first point
     * @param p2 second point
     * @return distance between the two point p1 and p2
     */
    private double getDistance(DiscreteCoordinates p1, DiscreteCoordinates p2){
        return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }
}

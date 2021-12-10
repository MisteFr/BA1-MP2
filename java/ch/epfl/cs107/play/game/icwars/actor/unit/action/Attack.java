package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class Attack extends Action {
    private final static String NAME = "(A)ttack";
    private final static int KEY = Keyboard.A;

    private LinkedList<Integer> enemyUnits = new LinkedList<>();
    private int indexUnitToAttack = 0;
    private ImageGraphics cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
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


        System.out.println(enemyUnits);

        if(!enemyUnits.isEmpty()){
            if(keyboard.get(Keyboard.LEFT).isReleased()){
                if((indexUnitToAttack - 1) >= 0){
                    --indexUnitToAttack;
                    System.out.println("index is now " + indexUnitToAttack);
                }
            } else if (keyboard.get(Keyboard.RIGHT).isReleased()) {
                if(indexUnitToAttack  < (enemyUnits.size() - 1)){
                    ++indexUnitToAttack;
                    System.out.println("index is now " + indexUnitToAttack);
                }
            } else if (keyboard.get(Keyboard.ENTER).isReleased()) {
                Unit selectedUnitToAttack = owner.getUnitsList().get(enemyUnits.get(indexUnitToAttack));
                System.out.println("You are attacking: " + selectedUnitToAttack.getName() + " fac is :" + selectedUnitToAttack.getFaction());
                selectedUnitToAttack.setHp(selectedUnitToAttack.getHp() - actionUnit.getDamage() + selectedUnitToAttack.getUnitCellDefenseStars());
                actionUnit.setAvailable(false);
                owner.setViewCandidate(player);
                player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);
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


    private double getDistance(DiscreteCoordinates p1, DiscreteCoordinates p2){
        return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }
}

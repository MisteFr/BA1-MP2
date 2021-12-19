package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Tank extends Unit {
    private final static String NAME = "Tank";
    private final static int HP_MAX = 10;
    private final static int DAMAGE = 7;
    private final static int MOVE_RADIUS = 4;

    public Tank(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType) {
        super(owner, coordinates, factionType, Tank.MOVE_RADIUS, Tank.DAMAGE, Tank.HP_MAX);
        this.setHp(HP_MAX);
        actionsList.add(new Attack(this, owner));
        actionsList.add(new Wait(this, owner));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void dealDamage(Unit enemy) {
        playShootSound = true;
        enemy.setHp(Math.min(enemy.getHp() , enemy.getHp() - this.getDamage() + enemy.getUnitCellDefenseStars()));
    }

    @Override
    public int getDamage() {
        return DAMAGE;
    }

    @Override
    public void isDealtDamage(int amount) {
        setHp(getHp() - amount);
    }
}

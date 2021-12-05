package ch.epfl.cs107.play.game.icwars.actor.units;

import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Soldat extends Unit {
    private final static String NAME = "Soldier";
    private final static int HP_MAX = 5;
    private final static int DAMAGE = 2;
    private final static int MOVE_RADIUS = 2;

    public Soldat(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType){
        super(owner, coordinates, factionType, Soldat.MOVE_RADIUS, Soldat.DAMAGE, Soldat.HP_MAX);
        this.setHp(HP_MAX);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void dealDamage(Unit enemy){
        enemy.setHp(enemy.getHp() - this.getDamage());
    }

    @Override
    public int getDamage() {
        return DAMAGE;
    }

    @Override
    public void isDealtDamage(int amount){
        if (amount > 0){
            this.hp -= amount;
            if (this.hp <= 0) {
                isAlive = false;
            }
        }
    }
}

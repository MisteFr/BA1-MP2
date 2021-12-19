package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;

public class Soldat extends Unit {
    private final static String NAME = "Soldier";
    private final static int HP_MAX = 5;
    private final static int DAMAGE = 2;
    private final static int MOVE_RADIUS = 2;

    public Soldat(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType) {
        super(owner, coordinates, factionType, Soldat.MOVE_RADIUS, Soldat.DAMAGE, Soldat.HP_MAX);
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
        soundNeedToBePlayed = true;
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

    @Override
    public void bip(Audio audio) {
        super.bip(audio);
        if(soundNeedToBePlayed){
            try{
                SoundAcoustics s = new SoundAcoustics("sounds/soldier_shoot.wav");
                s.shouldBeStarted();
                s.bip(audio);
            }catch (Exception e){
                System.out.println("Something went wrong while playing the sound - " + e.getMessage());
            }
            soundNeedToBePlayed = false;
        }
    }
}

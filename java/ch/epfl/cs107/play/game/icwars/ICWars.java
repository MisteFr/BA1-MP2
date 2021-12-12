package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldat;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;

public class ICWars extends AreaGame {

    //private ICWarsPlayer player; --> old version
    private GameState currentState;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};

    private LinkedList<ICWarsPlayer> playersList = new LinkedList<>();
    private LinkedList<ICWarsPlayer> currentTurnWaitingPlayers = new LinkedList<>();
    private LinkedList<ICWarsPlayer> nextTurnWaitingPlayers = new LinkedList<>();
    private ICWarsPlayer currentPlayer;

    private int areaIndex;
    private int playerIndex;


    public enum GameState {
        INIT, CHOOSE_PLAYER, START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN, END_TURN, END
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        //System.out.println(getCurrentArea());

        switch (currentState){
            case INIT:
                System.out.println("state is INIT");
                startGame(areaIndex, true);
                currentState = GameState.CHOOSE_PLAYER;
                break;

            case CHOOSE_PLAYER:
                System.out.println("state is CHOOSE PLAYER");
                if(currentTurnWaitingPlayers.isEmpty()){
                    currentState = GameState.END_TURN;
                }else{
                    currentPlayer = currentTurnWaitingPlayers.getFirst();
                    currentTurnWaitingPlayers.remove(currentPlayer);
                    currentState = GameState.START_PLAYER_TURN;
                }
                break;

            case START_PLAYER_TURN:
                System.out.println("state is START PLAYER TURN");
                currentPlayer.startTurn();
                currentState = GameState.PLAYER_TURN;
                break;

            case PLAYER_TURN:
                //System.out.println("state is PLAYER TURN");
                if(currentPlayer.getCurrentState() == ICWarsPlayer.PlayState.IDLE){
                    currentState = GameState.END_PLAYER_TURN;
                }
                break;

            case END_PLAYER_TURN:
                System.out.println("state is END PLAYER TURN");
                if(currentPlayer.isDefeated()){
                    System.out.println("calling leave area");
                    currentPlayer.leaveArea();
                }else{
                    nextTurnWaitingPlayers.add(currentPlayer);
                    for(Unit unit: currentPlayer.getUnitsList()){
                        unit.setAvailable(true);
                    }
                    currentState = GameState.CHOOSE_PLAYER;
                }
                break;

            case END_TURN:
                System.out.println("state is END TURN");
                for(ICWarsPlayer player: playersList){
                    if(player.isDefeated()){
                        if(nextTurnWaitingPlayers.contains(player)){
                            nextTurnWaitingPlayers.remove(player);
                        }
                        player.leaveArea();
                        playersList.remove(player);
                    }
                }
                if(playersList.size() <= 1){
                    System.out.println("inférieur ou egal à 1");
                    currentState = GameState.END;
                }else{
                    for(ICWarsPlayer player: nextTurnWaitingPlayers){
                        currentTurnWaitingPlayers.add(player);
                    }
                    nextTurnWaitingPlayers.clear();
                    currentState = GameState.CHOOSE_PLAYER;
                }
                break;

            case END:
                if((areaIndex + 1) < areas.length) {
                    ++areaIndex;
                    resetGame();
                }else{
                    end();
                }
                break;
        }

        Keyboard keyboard = getCurrentArea().getKeyboard();

        if(keyboard.get(Keyboard.N).isReleased()){
            if((areaIndex + 1) < areas.length) {
                ++areaIndex;
                resetGame();
            }else{
                end();
            }
        }

        if(keyboard.get(Keyboard.R).isReleased()){
            areaIndex = 0;
            resetGame();
        }

        /* //commented based on the instructions from the assignment
        if(keyboard.get(Keyboard.U).isDown()){
            ((RealPlayer)player).selectUnit(1);
        }
         */
    }

    private void resetGame(){
        nextTurnWaitingPlayers.clear();
        currentTurnWaitingPlayers.clear();

        for(ICWarsPlayer player: playersList){
            player.leaveArea();
        }
        playersList.clear();

        ICWarsArea area = (ICWarsArea) getCurrentArea();
        area.resetArea();

        currentState = GameState.INIT;
    }

    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
    }

    /**
     * Set area index to 0 and start the game in area 0.
     * Start the turn for our real player.
     * @param restart (boolean): if our intention is to restart the game
     */
    private void startGame(int areaIndex, boolean restart){
        initArea(areas[areaIndex], restart);
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            startGame(areaIndex, true);
            currentState = GameState.CHOOSE_PLAYER;
            return true;
        }
        return false;
    }

    private void initArea(String areaKey, boolean forceInitPlayer) {
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates defaultCursorPosition = area.getDefaultCursorPosition();
        DiscreteCoordinates defaultEnemyPosition = area.getEnemySpawnPosition();

        if(playersList.isEmpty() || forceInitPlayer){
            Tank allyTank = new Tank(area, new DiscreteCoordinates(2,5), ICWarsActor.ICWarsFactionType.ALLY);
            Soldat allySoldier = new Soldat(area, new DiscreteCoordinates(3,5), ICWarsActor.ICWarsFactionType.ALLY);
            Tank enemyTank = new Tank(area, new DiscreteCoordinates(8,5), ICWarsActor.ICWarsFactionType.ENEMY);
            Soldat enemySoldier = new Soldat(area, new DiscreteCoordinates(9,5), ICWarsActor.ICWarsFactionType.ENEMY);

            RealPlayer player1 = new RealPlayer(area, defaultCursorPosition, ICWarsActor.ICWarsFactionType.ALLY, allyTank, allySoldier);
            RealPlayer player2 = new RealPlayer(area, defaultEnemyPosition, ICWarsActor.ICWarsFactionType.ENEMY, enemyTank, enemySoldier);
            playersList.clear();
            playersList.add(player1);
            playersList.add(player2);
        }

        for(ICWarsPlayer player: playersList){
            ((RealPlayer) player).enterArea(area);
            currentTurnWaitingPlayers.add(player);
        }
    }


    public void end() {
        System.out.println("Game Over");
    }

    public String getTitle(){
        return "ICWars";
    }
}


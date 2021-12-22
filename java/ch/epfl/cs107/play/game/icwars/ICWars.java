package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Rocket;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldat;
import ch.epfl.cs107.play.game.icwars.area.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Sound;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.window.swing.SwingWindow;

import java.util.LinkedList;
import java.util.List;

public class ICWars extends AreaGame {

    private int areaIndex;

    private GameState currentState;
    private ICWarsPlayer currentPlayer;

    private boolean isPaused = false;

    private final String[] AREAS = {"icwars/Level0", "icwars/Level1"};
    private final LinkedList<ICWarsPlayer> PLAYERS = new LinkedList<>();
    private final LinkedList<ICWarsPlayer> CURRENT_TURN_WAITING_PLAYERS = new LinkedList<>();
    private final LinkedList<ICWarsPlayer> NEXT_TURN_WAITING_PLAYERS = new LinkedList<>();

    public enum GameState {
        INIT, CHOOSE_PLAYER, START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN, END_TURN, END, GAME_OVER
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        switch (currentState) {
            case INIT:
                startGame(areaIndex);
                currentState = GameState.CHOOSE_PLAYER;
                break;

            case CHOOSE_PLAYER:
                if (CURRENT_TURN_WAITING_PLAYERS.isEmpty()) {
                    currentState = GameState.END_TURN;
                } else {
                    currentPlayer = CURRENT_TURN_WAITING_PLAYERS.getFirst();
                    currentPlayer.endTurn();
                    CURRENT_TURN_WAITING_PLAYERS.remove(currentPlayer);
                    currentState = GameState.START_PLAYER_TURN;
                }
                break;

            case START_PLAYER_TURN:
                currentPlayer.startTurn();
                playSound("pop2", false, false, false, 1f);
                currentState = GameState.PLAYER_TURN;
                break;

            case PLAYER_TURN:
                if (currentPlayer.getCurrentState() == ICWarsPlayer.PlayState.IDLE) {
                    currentState = GameState.END_PLAYER_TURN;
                }
                break;

            case END_PLAYER_TURN:
                if (currentPlayer.isDefeated()) {
                    currentPlayer.leaveArea();
                    PLAYERS.remove(currentPlayer);
                } else {
                    NEXT_TURN_WAITING_PLAYERS.add(currentPlayer);
                    for (Unit unit : currentPlayer.getUnitsList()) {
                        unit.setAvailable(true);
                    }
                }
                currentState = GameState.CHOOSE_PLAYER;
                break;

            case END_TURN:
                ((ICWarsArea) getCurrentArea()).healUnits();

                for (ICWarsPlayer player : PLAYERS) {
                    if (player.isDefeated()) {
                        if (NEXT_TURN_WAITING_PLAYERS.contains(player)) {
                            NEXT_TURN_WAITING_PLAYERS.remove(player);
                        }
                        player.leaveArea();
                        PLAYERS.remove(player);
                    }

                    List<Unit> playerUnitList = player.getUnitsList();
                    for(Unit u : playerUnitList){
                        if(u.getUnitCellType() == ICWarsBehavior.ICWarsCellType.CITY){

                        }
                    }
                }
                if (PLAYERS.size() <= 1) {
                    currentState = GameState.END;
                } else {
                    for (ICWarsPlayer player : NEXT_TURN_WAITING_PLAYERS) {
                        CURRENT_TURN_WAITING_PLAYERS.add(player);
                    }
                    NEXT_TURN_WAITING_PLAYERS.clear();
                    currentState = GameState.CHOOSE_PLAYER;
                }
                break;

            case END:
                if ((areaIndex + 1) < AREAS.length) {
                    ++areaIndex;
                    resetGame();
                } else {
                    end();
                }
                break;
        }

        Keyboard keyboard = getCurrentArea().getKeyboard();

        if (keyboard.get(Keyboard.N).isReleased() && !isPaused()) {
            if ((areaIndex + 1) < AREAS.length) {
                ++areaIndex;
                resetGame();
            } else {
                end();
            }
        }

        if (keyboard.get(Keyboard.R).isReleased() && !isPaused()) {
            areaIndex = 0;
            resetGame();
            playSound("soundtrack", true, true, true, 0.5f);
        }

        if (keyboard.get(Keyboard.P).isReleased()) {
            if(isPaused()){
                requestResume();
            }else{
                requestPause();
            }
        }

        /* //commented based on the instructions from the assignment
        if(keyboard.get(Keyboard.U).isDown()){
            ((RealPlayer)player).selectUnit(1);
        }
         */
    }

    private void resetGame() {
        NEXT_TURN_WAITING_PLAYERS.clear();
        CURRENT_TURN_WAITING_PLAYERS.clear();

        for (ICWarsPlayer player : PLAYERS) {
            player.leaveArea();
        }
        PLAYERS.clear();

        ICWarsArea area = (ICWarsArea) getCurrentArea();
        area.resetArea();

        currentState = GameState.INIT;
    }

    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
        addArea(new GameOver());
        addArea(new PauseMenu());
    }

    /**
     * Set area index to 0 and start the game in area 0.
     * Start the turn for our real player.
     *
     * @param areaIndex (int): area index of the area we want to launch the game in
     */
    private void startGame(int areaIndex) {
        initArea(AREAS[areaIndex]);
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            startGame(areaIndex);
            playSound("soundtrack", true, true,true, 0.5f);
            currentState = GameState.CHOOSE_PLAYER;
            return true;
        }
        return false;
    }

    private void initArea(String areaKey){
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates defaultCursorPosition = area.getDefaultCursorPosition();
        DiscreteCoordinates defaultEnemyPosition = area.getEnemySpawnPosition();


        Tank allyTank = new Tank(area, new DiscreteCoordinates(2, 5), ICWarsActor.ICWarsFactionType.ALLY);
        Soldat allySoldier = new Soldat(area, new DiscreteCoordinates(3, 5), ICWarsActor.ICWarsFactionType.ALLY);
        Rocket allyRocket = new Rocket(area, new DiscreteCoordinates(3, 6), ICWarsActor.ICWarsFactionType.ALLY);

        Tank enemyTank = new Tank(area, new DiscreteCoordinates(8, 5), ICWarsActor.ICWarsFactionType.ENEMY);
        Soldat enemySoldier = new Soldat(area, new DiscreteCoordinates(9, 5), ICWarsActor.ICWarsFactionType.ENEMY);
        Rocket enemyRocket = new Rocket(area, new DiscreteCoordinates(9, 6), ICWarsActor.ICWarsFactionType.ENEMY);

        RealPlayer player1 = new RealPlayer(area, defaultCursorPosition, ICWarsActor.ICWarsFactionType.ALLY, allyTank, allySoldier, allyRocket);
        //RealPlayer player2 = new RealPlayer(area, defaultEnemyPosition, ICWarsActor.ICWarsFactionType.ENEMY, enemyTank, enemySoldier, enemyRocket);
        AIPlayer player2 = new AIPlayer(area, defaultEnemyPosition, ICWarsActor.ICWarsFactionType.ENEMY, enemyTank, enemySoldier, enemyRocket);
        PLAYERS.clear();
        PLAYERS.add(player1);
        PLAYERS.add(player2);


        for (ICWarsPlayer player : PLAYERS) {
            player.enterArea(area);
            CURRENT_TURN_WAITING_PLAYERS.add(player);
        }
    }

    public void playSound(String name, boolean doLoop, boolean fadeIn, boolean shouldStopOther, float soundLevel){
        try{
            SwingWindow windowS = (SwingWindow) getWindow();
            Sound soundToPlay = windowS.getSound("sounds/" + name + ".wav");
            windowS.playSound(soundToPlay, false, soundLevel ,fadeIn, doLoop, shouldStopOther);
        }catch (Exception e){
            System.out.println("Something went wrong while playing the sound - " + e.getMessage());
            //retry for some reasons it doesn't work directly sometimes (needs two or three retries).
            playSound(name, doLoop, fadeIn, shouldStopOther, soundLevel);
        }
    }

    @Override
    public void requestPause() {
        super.requestPause();

        currentPlayer.pause();
        setCurrentArea("icwars/PauseMenu", true);

        isPaused = true;
    }

    @Override
    public void requestResume() {
        super.requestResume();

        currentPlayer.resume();
        setCurrentArea(AREAS[areaIndex], false);

        isPaused = false;
    }

    public void end() {
        System.out.println("Game Over");
        setCurrentArea("icwars/GameOver", true);
        playSound("gameover", false, false, true, 1f);
        currentState = GameState.GAME_OVER;
    }


    @Override
    public boolean isPaused() {
        return super.isPaused() || isPaused;
    }

    public String getTitle() {
        return "ICWars";
    }
}


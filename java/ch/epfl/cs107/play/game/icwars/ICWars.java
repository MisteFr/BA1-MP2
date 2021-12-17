package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldat;
import ch.epfl.cs107.play.game.icwars.area.GameOver;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;

public class ICWars extends AreaGame {

    private GameState currentState;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private final LinkedList<ICWarsPlayer> playersList = new LinkedList<>();
    private final LinkedList<ICWarsPlayer> currentTurnWaitingPlayers = new LinkedList<>();
    private final LinkedList<ICWarsPlayer> nextTurnWaitingPlayers = new LinkedList<>();

    public enum GameState {
        INIT, CHOOSE_PLAYER, START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN, END_TURN, END
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
                if (currentTurnWaitingPlayers.isEmpty()) {
                    currentState = GameState.END_TURN;
                } else {
                    currentPlayer = currentTurnWaitingPlayers.getFirst();
                    currentTurnWaitingPlayers.remove(currentPlayer);
                    currentState = GameState.START_PLAYER_TURN;
                }
                break;

            case START_PLAYER_TURN:
                currentPlayer.startTurn();
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
                    playersList.remove(currentPlayer);
                } else {
                    nextTurnWaitingPlayers.add(currentPlayer);
                    for (Unit unit : currentPlayer.getUnitsList()) {
                        unit.setAvailable(true);
                    }
                }
                currentState = GameState.CHOOSE_PLAYER;
                break;

            case END_TURN:
                for (ICWarsPlayer player : playersList) {
                    if (player.isDefeated()) {
                        if (nextTurnWaitingPlayers.contains(player)) {
                            nextTurnWaitingPlayers.remove(player);
                        }
                        player.leaveArea();
                        playersList.remove(player);
                    }
                }
                if (playersList.size() <= 1) {
                    currentState = GameState.END;
                } else {
                    for (ICWarsPlayer player : nextTurnWaitingPlayers) {
                        currentTurnWaitingPlayers.add(player);
                    }
                    nextTurnWaitingPlayers.clear();
                    currentState = GameState.CHOOSE_PLAYER;
                }
                break;

            case END:
                if ((areaIndex + 1) < areas.length) {
                    ++areaIndex;
                    resetGame();
                } else {
                    end();
                }
                break;
        }

        Keyboard keyboard = getCurrentArea().getKeyboard();

        if (keyboard.get(Keyboard.N).isReleased()) {
            if ((areaIndex + 1) < areas.length) {
                ++areaIndex;
                resetGame();
            } else {
                end();
            }
        }

        if (keyboard.get(Keyboard.R).isReleased()) {
            areaIndex = 0;
            resetGame();
        }

        /* //commented based on the instructions from the assignment
        if(keyboard.get(Keyboard.U).isDown()){
            ((RealPlayer)player).selectUnit(1);
        }
         */
    }

    private void resetGame() {
        nextTurnWaitingPlayers.clear();
        currentTurnWaitingPlayers.clear();

        for (ICWarsPlayer player : playersList) {
            player.leaveArea();
        }
        playersList.clear();

        ICWarsArea area = (ICWarsArea) getCurrentArea();
        area.resetArea();

        currentState = GameState.INIT;
    }

    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
        addArea(new GameOver());
    }

    /**
     * Set area index to 0 and start the game in area 0.
     * Start the turn for our real player.
     *
     * @param areaIndex (int): area index of the area we want to launch the game in
     */
    private void startGame(int areaIndex) {
        initArea(areas[areaIndex]);
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            startGame(areaIndex);
            currentState = GameState.CHOOSE_PLAYER;
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates defaultCursorPosition = area.getDefaultCursorPosition();
        DiscreteCoordinates defaultEnemyPosition = area.getEnemySpawnPosition();


        Tank allyTank = new Tank(area, new DiscreteCoordinates(2, 5), ICWarsActor.ICWarsFactionType.ALLY);
        Soldat allySoldier = new Soldat(area, new DiscreteCoordinates(3, 5), ICWarsActor.ICWarsFactionType.ALLY);
        Tank enemyTank = new Tank(area, new DiscreteCoordinates(8, 5), ICWarsActor.ICWarsFactionType.ENEMY);
        Soldat enemySoldier = new Soldat(area, new DiscreteCoordinates(9, 5), ICWarsActor.ICWarsFactionType.ENEMY);

        RealPlayer player1 = new RealPlayer(area, defaultCursorPosition, ICWarsActor.ICWarsFactionType.ALLY, allyTank, allySoldier);
        //RealPlayer player2 = new RealPlayer(area, defaultEnemyPosition, ICWarsActor.ICWarsFactionType.ENEMY, enemyTank, enemySoldier);
        AIPlayer player2 = new AIPlayer(area, defaultEnemyPosition, ICWarsActor.ICWarsFactionType.ENEMY, enemyTank, enemySoldier);
        playersList.clear();
        playersList.add(player1);
        playersList.add(player2);


        for (ICWarsPlayer player : playersList) {
            player.enterArea(area);
            currentTurnWaitingPlayers.add(player);
        }
    }


    public void end() {
        System.out.println("Game Over");
        setCurrentArea("icwars/GameOver", true);
    }

    public String getTitle() {
        return "ICWars";
    }
}


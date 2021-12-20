
![Logo](https://upload.wikimedia.org/wikipedia/commons/f/f4/Logo_EPFL.svg)

# EPFL IC BA1 MP2

This project is an adaptation of the game called "Advanced Wars" ([Wikipedia page](https://fr.wikipedia.org/wiki/Advance_Wars))

## Nota Bene

As the total size of the archive was too big, we had to submit our ressources folder the switchtube drive under the name
"324366-344200-ressources.zip", as asked by Ms Sam.

## How to play the game

### Start

You can start the game by running Play.main() in your favorite IDE.

### Controls and progress of the game

You will be prompted with the Level 0 of the game. You are able to move the white cursor around the map using the 
directional arrows.

When the cursor is hovering over one of your units (the blue ones), you can select the said unit by pressing *ENTER*. 
This will prompt a list of the possible actions for the unit in the upper right corner of the window.

You can navigate through the action's list using the *UP* and *DOWN* arrows. Once you have decided which action you want
to perform, press *ENTER* to execute the action.

If you selected **ATTACK** but there is no enemy unit to attack in the attack range, the action's list should be 
prompted again for you to be able to select **WAIT** instead.

After performing any action, the unit that did it will be see-through, which indicates it cannot be moved nor perform an
action for the current round.

When you are satisfied with your decisions, or when all of your units have been used (aka you can't do anything anymore),
you can press *TAB* to give the hand to the AI Player.

The AI Player will then play against you using its units until he has no units available, and will then give the hand 
back to you.

This alternance will repeat over and over until one of the two players has no units alive anymore. At this point, the 
game will skip to the next level. If you made it to the last level, after one player has been eliminated, you will be 
prompted with the **GAME OVER** screen.

During any state of the game, there are specific hotkeys you can press :
 - R : restarts the game from the beginning (at level 0 with all units spawned).
 - N : skips to the next level. If the current level was the last one, prompts the **GAME OVER** screen.


## Extensions

### Sounds

We added multiple sounds to the project. 
 - Soundtrack : We used the World of Tanks soundtrack as the main soundtrack, as we thought it fitted right for the 
ambiance of the game. [source](https://www.youtube.com/watch?v=dqk_ZdVf4-o&list=PLslIWpE2CMxQWS5OosmPvx5pmiXNapiz-)
 - Attack sounds : We added sounds to play on attacks for each the tank and the soldier. We found them on royalty-free 
websites : [tank sound](https://mixkit.co/free-sound-effects/tanks/), [soldier sound](https://mixkit.co/free-sound-effects/gun/)
 - Notifications : We added notifications sounds that are played after each action, or whenever a player starts its turn.
Here again, the sounds are royalty-free : [notifications](https://notificationsounds.com/notification-sounds)
 - Game Over : We added a sound to be played when the **GAME OVER** screen is displayed. It is a concatenation of sounds
that have been found on youtube : [sound 1](https://www.youtube.com/watch?v=Gs5QvbD-hF0), [sound 2](https://www.youtube.com/watch?v=DLJwEalHQtw)

### Game Over and Pause menu

We added a **GAME OVER** screen that is displayed when no next level exists. it has been created by us using the Pikaxi 
Intro application on IPad, following the typo used by EPFL for its logo.

Same has been done for the **PAUSE** menu. This menu conserves the current state of the game (when you resume it, you 
will be in the same state as before you paused the game).


## Scenario

After many years of friendship, the collaboration between EPFL and ETHZ has come to an end. The two worldwide-known 
universities are at war to prove to the world that they are better than the other.

As a fellow EPFL supporter, you have been asked by our beloved Director, Martin Vetterli, to help us in this raging war.

Enter the battlefield and defeat ETHZ (orange team) to demonstrate the superiority of your strategy. Make the bombs roar
and the bullets whistle to prove to the world that YOU are the best commanding officer of EPFL's troops.

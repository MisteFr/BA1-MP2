We have adhered to the suggested architecture without significant additions.

## Extensions

###Menus
As part of the extensions, we added menus (PauseMenu and GameOverMenu) that are sub-classes of ICWarsArea.
- For the GameOver menu to work, we added a new GameState GAME_OVER so that end() isn't getting called in a loop when 
the GameState is END.
- For the Pause Menu to work, we added a new GameState 'PAUSED'.  
We implemented ```requestResume()```, ```requestPause()```, ```ìsPause()``` in ICWars to handle the state changes on the
Area side. 
We also added ``pause()`` and ```resume``` in ICWarsPlayer to handle state changes on the player side.



###Sounds
We also added sounds to the game. We wrote a ```playSound()``` method that uses the getSound and playSound methods of 
the SwingWindow class to play sounds.

###Cities
Following the suggested architecture for the realization of the extension of the cities, we added a constant 
```CITY_HEALING_AMOUNT = 2;``` in ICWarsArea.
We also added a new ICWarsFactionType enum type: ```NEUTRAL``` as city can be neutral.


###Other
A small feature we added is that after each action performed by a unit, we check if the player has any units left. 
If they don't have any, we automatically pass the hand to the next player, which makes the game more fluid.  

We also added an ```isInRange(DiscreteCoordinates p1, DiscreteCoordinates p2, double range)``` method in ICWars as
a helper to the Attack action. We use it to check if there is a unit that can be attacked within the range.

## Nota Bene

As the total size of the archive was too big, we had to submit our ressources folder the switchtube drive under the name
"324366-344200-ressources.zip", as asked by Ms Sam.
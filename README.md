TetrisBattleBot
===============

A Facebook Tetris Battle Bot.

for more info on the actual game, see
* [http://www.tetrisfriends.com/](http://www.tetrisfriends.com/)
* [http://apps.facebook.com/tetris_battle/](http://apps.facebook.com/tetris_battle/)


This bot simulates a human playing "Tetris Battle" on a web browser.
The goal of this Tetris game is to maximize the number of subsequently filled
lines.




How to use
----------


### Download / Installation

Click on https://github.com/ippeb/TetrisBattleBot and then click on the ZIP button
just to the left of the repository URL field.



### Run / Usage

Open your Terminal and change your directory to `TetrisBattleBot/classes`.
Now, simply type `java TetrisBattleBot/TetrisWebsiteInteraction`
to run the program.


### Exit the program

Type Ctrl+C in your Terminal to exit the program.


### Detection of the Tetris board

Currently, there is no automated detection of the Tetris board.
When you run the program you are asked to move your cursor to
the upper left corner of the Tetris board (within the first three
seconds). Then you have two seconds to move your cursor to the
lower left corner of the Tetris board. In the Terminal the
coordinates of those two corners are displayed.




Features
--------


### Cross-platform

The key presses are currently simulated using java.awt.Robot and thus
should be OS independent.


### Feedback loop

After every simulated key press (rotation, horizontal or vertical
movement of the Tetromino) the bot analyzes the current Tetris board
and so checks whether the key press was actually executed in the
Flash game. If not, the key press will then be repeated. If a
different Tetris board configuration was detected (maybe the user has typed
a key by mistake) the program adapts to the new Tetris board
configuration.

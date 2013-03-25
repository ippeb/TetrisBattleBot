TetrisBattleBot
===============

A Facebook Tetris Battle Bot.

for more info on the actual game, see
* [http://www.tetrisfriends.com/](http://www.tetrisfriends.com/)
* [http://apps.facebook.com/tetris_battle/](http://apps.facebook.com/tetris_battle/)


This Bot simulates a human playing Tetris Battle on a Web browser. 
The aim of the Tetris game is to maximize the number of subsequent filled
lines.

The key presses are simulated using Quartz Event Services (the C API for 
event taps from the Mac Developer Library) and are compiled using the 
ApplicationServices framework. Hence, the program is currently only 
supported on Mac OS X version 10.4 or upwards.
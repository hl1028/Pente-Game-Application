# Pente-Game-Application
Board.java: the interface of a board

Coordinate.java: the interface of coordinate

Player.java: the interface of player

Stone.java: enum class of stone, containing 3 value: RED, YELLOW, EMPTY

MyBoard.java: my implementation of Board interface, mainly to place stones, check winner, check captures, etc.

MyCoordiante.java: implement Pente requirements on valid coordinate

HumanPlayer.java: a player class that allow user to input "row,column" to play

harry28Player.java: the AI player I design, the idea is trying to capture more and to defense well

Game.java: the main function of this Pente game for user vs AI, when executed, all inputs and outputs are in console. It will ask user if he wants to go first, and repeatly ask for valid coordinates.
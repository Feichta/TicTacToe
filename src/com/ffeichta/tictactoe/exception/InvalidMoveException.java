package com.ffeichta.tictactoe.exception;

/**
 * This Custom Exception throws when server or client receive an invalid move
 * (would be impossible with the GUI)
 * 
 * @author Fabian
 * 
 */
public class InvalidMoveException extends Exception {

	public InvalidMoveException(String msg) {
		super(msg);
	}
}

package com.ffeichta.tictactoe.exception;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * This class is used to handle exceptions. It makes a MessageDialog with a
 * description of the exception
 * 
 * @author Fabian
 * 
 */
public class TicTacToeException {
	public static void behandleException(Component component, Exception e) {
		String message = "An error has occurred in class "
				+ e.getClass().getName() + ". \n" + e.getMessage();
		JOptionPane.showMessageDialog(component, message, e.getClass()
				.getName(), JOptionPane.ERROR_MESSAGE);
	}
}

package com.ffeichta.tictactoe.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

/**
 * This class implements the method uncaughtException() of the interface
 * UncaughtExceptionHandler. It is used to set the default exception handler in
 * the classes TicTacToeClient and TicTacToeServer to intercept runtime
 * exceptions. It makes a MessageDialog with a description of the exception
 * 
 * @author Fabian
 * 
 */
public class RuntimeExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		String message = "An error has occurred in class "
				+ e.getClass().getName() + ". \n" + e.getMessage();
		JOptionPane.showMessageDialog(null, message, e.getClass().getName(),
				JOptionPane.ERROR_MESSAGE);
	}
}

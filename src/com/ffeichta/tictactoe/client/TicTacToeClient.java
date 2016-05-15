package com.ffeichta.tictactoe.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.ffeichta.tictactoe.exception.InvalidMoveException;
import com.ffeichta.tictactoe.exception.RuntimeExceptionHandler;
import com.ffeichta.tictactoe.exception.TicTacToeException;
import com.ffeichta.tictactoe.field.TicTacToe;
import com.ffeichta.tictactoe.gui.TicTacToeJFrame;
import com.ffeichta.tictactoe.sounds.Sound;

/**
 * This is the Tic Tac Toe - client. The first thing what the client does is
 * reading the size of the field from the server. Clients sets the first move.
 * The clients sends his move to the server and waits until the server sends his
 * move. The method setMyMove() sets a move on a certain position for a player.
 * The method getAdversaryMove() gets the move of the server and uses the same
 * socket which setMyMove() has already used to send the move of the server. It
 * also sets the move into the field. The connection between client and server
 * is via port 65535
 * 
 * @author Fabian
 * 
 */
public class TicTacToeClient extends TicTacToe {

	// Size of the field
	private static int fieldsize;
	// IP-Address of the Server
	private static String ipAddress;
	// Port on which runs the server
	private static final int PORT = 65535;
	// ClientSocket handles the client
	private Socket client;

	// Object of this class
	private static TicTacToeClient tictactoeClient = null;
	// Object of the GUI, because the class TicTacToeJFrame is already used for
	// user inputs
	private static TicTacToeJFrame tictactoeFrame = null;

	// Host name of the server machine
	private static String hostNameServer;

	// Used for the feature "Play again?"
	private boolean exit = false;

	public static void main(String[] args) {
		// UncoughtExceptionhandler handles runtime exceptions
		Thread.setDefaultUncaughtExceptionHandler(new RuntimeExceptionHandler());

		// Set the Look and Feel of the application (L&F)
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, this code makes the program use the
			// System L&F
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e1) {
				TicTacToeException.behandleException(null, e1);
			}
		}
		boolean invalidIP = false;
		String ipTemp;
		// Used to ask the user for the IP of the server. It also validate
		// the entered IP and reads the size of the field from the server
		do {
			ipTemp = (String) JOptionPane
					.showInputDialog(
							tictactoeFrame,
							"Type in the IP-Address of the server:\n(You find it at the bottom of the Server-Window)",
							"Tic Tac Toe", JOptionPane.QUESTION_MESSAGE);
			if (ipTemp == null) {
				System.exit(0);
			} else {
				if (ipTemp.length() == 0) {
					invalidIP = true;
				} else {
					invalidIP = false;
					Socket clientSocketTemp;
					try {
						clientSocketTemp = new Socket(ipTemp, PORT);
						fieldsize = clientSocketTemp.getInputStream().read();
						hostNameServer = clientSocketTemp.getInetAddress()
								.getHostName();
					} catch (IOException e1) {
						invalidIP = true;
					}
				}
			}
		} while (invalidIP);
		ipAddress = ipTemp;
		// Play the game as long as the user starts new games
		do {
			tictactoeClient = new TicTacToeClient(fieldsize);
			String title = "Tic Tac Toe";
			if (fieldsize == 10) {
				// The user wants to have it extremely
				title += " (Extreme Edition)";
			}
			title += " - Client";
			tictactoeFrame = new TicTacToeJFrame(title, tictactoeClient);
			tictactoeFrame.repaint();
			// Close the ClientSocket when closing the window
			tictactoeFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					tictactoeClient.close();
					tictactoeFrame.setVisible(false);
					tictactoeFrame.dispose();
					System.exit(0);
				}
			});
			// Play this game as long as there is no winner and someone can win
			do {
				tictactoeFrame.setStatusBarText("It's your turn");
				int numberSelected;
				do {
					numberSelected = tictactoeFrame.getSelectedFieldNumber();
				} while (tictactoeClient.setMyMove(numberSelected) != 0);
				if (!tictactoeClient.someoneCanWin()
						|| tictactoeClient.getWinner() != 0) {
					break;
				}
				tictactoeFrame.setStatusBarText("Waiting for player on "
						+ hostNameServer);
				if (tictactoeClient.getAdversaryMove() != 0) {
					TicTacToeException.behandleException(tictactoeFrame,
							new InvalidMoveException("Invalid move"));
				}
			} while (tictactoeClient.someoneCanWin()
					&& tictactoeClient.getWinner() == 0);
			// Game is finished because there is a winner or no one can win it
			if (tictactoeClient.getWinner() != 0) {
				switch (tictactoeClient.getWinner()) {
				case PLAYER1:
					tictactoeFrame.setStatusBarText("You won. Congratulation!");
					new Sound().playSound();
					break;
				case PLAYER2:
					tictactoeFrame.setStatusBarText("Your teammates won");
				default:
					break;
				}
			} else {
				if (!tictactoeClient.someoneCanWin()) {
					tictactoeFrame.setStatusBarText("No one can win the game");
				}
			}
			// Reads the exit status from the server. The server decides whether
			// is played again. If the server says no or clicks the "About"
			// button, server and client will close
			switch (tictactoeClient.readExitStatus()) {
			case 0:
				tictactoeClient.exit = true;
				break;
			case 1:
				tictactoeClient.exit = false;
				break;
			default:
				break;
			}
			tictactoeClient.close();
			tictactoeClient.client = null;
			tictactoeFrame.setVisible(false);
			tictactoeFrame.dispose();
		} while (!tictactoeClient.exit);
		System.exit(0);
	}

	/**
	 * Constructor that initializes the field
	 * 
	 * @param fieldSize
	 */
	public TicTacToeClient(int fieldSize) {
		super(fieldSize);
	}

	/**
	 * Closes the ClientSocket
	 */
	public void close() {
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
	}

	/**
	 * Sends the move of the client to the server. ClientSocket can't be null.
	 * Return is 0 if it was a success, -1 if the position is invalid, -2 if the
	 * move is already set or -3 if the ClientSocket already exists
	 * 
	 * @param move
	 * @return
	 */
	public int setMyMove(int move) {
		int ret = -3;
		if (client == null) {
			ret = setMovePlayer1(move);
			if (ret == 0) {
				tictactoeFrame.repaint();
				try {
					client = new Socket(ipAddress, PORT);
					client.getOutputStream().write(move);
				} catch (IOException e) {
					TicTacToeException.behandleException(tictactoeFrame, e);
				}
			}
		}
		return ret;
	}

	/**
	 * Waits for the move of the server and sets it if a ClientSocket exists.
	 * Return is 0 if it was a success, -1 if the position is invalid, -2 if the
	 * move is already set or -3 if the ClientSocket doesn't exist
	 * 
	 * @return
	 */
	public int getAdversaryMove() {
		int ret = -3;
		if (client != null) {
			int read;
			try {
				read = client.getInputStream().read();
				ret = setMovePlayer2(read);
			} catch (IOException e) {
				TicTacToeException.behandleException(tictactoeFrame, e);
			}
			tictactoeFrame.repaint();
			tictactoeClient.close();
			client = null;
		}
		return ret;
	}

	/**
	 * Reads a Integer from the server when the game is finished. 0 means that
	 * server and client will close, 1 means play another game
	 * 
	 * @return
	 */
	public int readExitStatus() {
		int ret = 0;
		Socket clientSocketTemp;
		try {
			clientSocketTemp = new Socket(ipAddress, PORT);
			ret = clientSocketTemp.getInputStream().read();
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
		return ret;
	}
}

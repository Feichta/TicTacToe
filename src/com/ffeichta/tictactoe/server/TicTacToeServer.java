package com.ffeichta.tictactoe.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.ffeichta.tictactoe.exception.InvalidMoveException;
import com.ffeichta.tictactoe.exception.RuntimeExceptionHandler;
import com.ffeichta.tictactoe.exception.TicTacToeException;
import com.ffeichta.tictactoe.field.TicTacToe;
import com.ffeichta.tictactoe.gui.InfoBox;
import com.ffeichta.tictactoe.gui.TicTacToeJFrame;
import com.ffeichta.tictactoe.sounds.Sound;

/**
 * This is the Tic Tac Toe - server. The first thing what the server does is
 * sending the size of the field to the server. The server waits for the move of
 * client (client begins) and after that he sends his move. The method
 * getAdversaryMove() waits for the move of the client and setMyMove() uses the
 * same socket which getAdversaryMove() has already used. Both methods set the
 * move into the field. The connection between server and client is via port
 * 65535
 * 
 * @author Fabian
 * 
 */
public class TicTacToeServer extends TicTacToe {

	// Size of the field
	private static int fieldsize;
	// Port on which runs the server
	private static final int PORT = 65535;
	// ServerSocket handles queries of the client
	private ServerSocket server = null;
	// ClientSocket handles sending and receiving moves
	private Socket clientSocket = null;

	// Object of this class
	private static TicTacToeServer tictactoeServer = null;
	// Object of the GUI, because the class TicTacToeJFrame is already used for
	// user inputs
	private static TicTacToeJFrame tictactoeFrame = null;

	// Host name of the client machine
	private static String hostNameClient;

	// Used for the feature "Play again?"
	private boolean exit = false;

	private static boolean isFirstGame = true;

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
				com.ffeichta.tictactoe.exception.TicTacToeException
						.behandleException(null, e1);
			}
		}
		// Asks the user for the size of the field. Only server can do this
		String[] options = { "3", "4", "5", "6", "7", "8", "9", "10" };
		String size = (String) JOptionPane.showInputDialog(tictactoeFrame,
				"Choose the size of the Tic Tac Toe field:", "Tic Tac Toe",
				JOptionPane.QUESTION_MESSAGE, null, options, "3");
		if (size == null) {
			System.exit(0);
		} else {
			fieldsize = Integer.valueOf(size);
		}
		// Play the game as long as the user starts new games
		do {
			tictactoeServer = new TicTacToeServer(fieldsize);
			String title = "Tic Tac Toe";
			if (fieldsize == 10) {
				// The user wants to have it extremely
				title += " (Extreme Edition)";
			}
			title += " - Server";
			tictactoeFrame = new TicTacToeJFrame(title, tictactoeServer);
			tictactoeFrame.repaint();
			// Close the ClientSocket and the ServerSocket when closing the
			// window
			tictactoeFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					tictactoeServer.closeAll();
					tictactoeFrame.setVisible(false);
					tictactoeFrame.dispose();
					System.exit(0);
				}
			});
			String ip = null;
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				TicTacToeException.behandleException(tictactoeFrame, e);
			}
			tictactoeFrame.setStatusBarText("IP-Address: " + ip);
			if (isFirstGame) {
				tictactoeServer.sendSizeToClient();
			}
			isFirstGame = false;
			// Play this game as long as there is no winner and someone can win
			do {
				tictactoeFrame.setStatusBarText("Waiting for player on "
						+ hostNameClient);
				if (tictactoeServer.getAdversaryMove() != 0) {
					TicTacToeException.behandleException(tictactoeFrame,
							new InvalidMoveException("Invalid move"));
				}
				if (!tictactoeServer.someoneCanWin()
						|| tictactoeServer.getWinner() != 0) {
					break;
				}
				tictactoeFrame.setStatusBarText("It's your turn");
				int numberSelected;
				do {
					numberSelected = tictactoeFrame.getSelectedFieldNumber();
				} while (tictactoeServer.setMyMove(numberSelected) != 0);
			} while (tictactoeServer.someoneCanWin()
					&& tictactoeServer.getWinner() == 0);
			// Game is finished because there is a winner or no one can win it
			if (tictactoeServer.getWinner() != 0) {
				switch (tictactoeServer.getWinner()) {
				case PLAYER1:
					tictactoeFrame.setStatusBarText("Your teammates won");
					break;
				case PLAYER2:
					tictactoeFrame
							.setStatusBarText("You won. Contgratulations!");
					new Sound().playSound();
					break;
				default:
					break;
				}
			} else {
				if (!tictactoeServer.someoneCanWin()) {
					tictactoeFrame.setStatusBarText("No one can win the game");
				}
			}
			/**
			 * Asks the user if he wants to play again and sends his answer to
			 * the client. "YES" sends a 1 to the client which means playing
			 * another game. All other user inputs sends a 0 to the client which
			 * means stopping client and server. "About" shows a info box of the
			 * application.
			 */
			Object[] optionsGameFinished = { "YES", "NO", "About" };
			int result = JOptionPane.showOptionDialog(tictactoeFrame,
					"Dou you want to play again?", "Tic Tac Toe",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, optionsGameFinished,
					null);
			switch (result) {
			case JOptionPane.YES_OPTION:
				tictactoeServer.sendExitStatus(1);
				tictactoeServer.exit = false;
				break;

			case JOptionPane.NO_OPTION:
				tictactoeServer.sendExitStatus(0);
				tictactoeServer.exit = true;

				break;

			case JOptionPane.CANCEL_OPTION:
				tictactoeServer.sendExitStatus(0);
				tictactoeServer.exit = true;
				tictactoeFrame.setVisible(false);
				tictactoeFrame.dispose();
				// Shows the info box
				InfoBox infoBox = new InfoBox();
				infoBox.setVisible(true);
				infoBox.dispose();
				break;

			default:
				break;
			}
			tictactoeServer.closeAll();
			tictactoeServer.clientSocket = null;
			tictactoeServer.server = null;
			tictactoeFrame.setVisible(false);
			tictactoeFrame.dispose();
		} while (!tictactoeServer.exit);
		System.exit(0);
	}

	/**
	 * Initializes a new field and creates the ServerSocket
	 * 
	 * @param fieldSize
	 */
	public TicTacToeServer(int fieldSize) {
		super(fieldSize);
		try {
			server = new ServerSocket(PORT, 100, InetAddress.getLocalHost());
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
	}

	/**
	 * Closes the ClientSocket
	 */
	public void close() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
	}

	/**
	 * Closes the ServerSocket and the ClientSocket
	 */
	public void closeAll() {
		try {
			if (server != null) {
				server.close();
			}
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
	}

	/**
	 * Waits for the connection with the client. When the adversary sends his
	 * move it puts the move into the field. Return is 0 if it was a success, -1
	 * if the position is invalid, -2 if the move is already set or -3 if the
	 * ClientSocket already exists
	 * 
	 * @return
	 */
	public int getAdversaryMove() {
		int ret = -3;
		if (clientSocket == null) {
			try {
				clientSocket = server.accept();
				int read = clientSocket.getInputStream().read();
				ret = setMovePlayer1(read);
			} catch (IOException e) {
				TicTacToeException.behandleException(tictactoeFrame, e);
			}
			tictactoeFrame.repaint();
		}
		return ret;
	}

	/**
	 * Sends the move of the server to the client. ClientSocket can't be null.
	 * When was finished sent, the ClientSocket closes. Return is 0 if setting
	 * the move was a success, -1 if the position is invalid, -2 if the move is
	 * already set or -3 if the ClientSocket doesn't exist
	 * 
	 * @param move
	 * @return
	 */
	public int setMyMove(int move) {
		int ret = -3;
		if (clientSocket != null) {
			ret = setMovePlayer2(move);
			if (ret == 0) {
				tictactoeFrame.repaint();
				try {
					clientSocket.getOutputStream().write(move);
					clientSocket.close();
				} catch (IOException e) {
					TicTacToeException.behandleException(tictactoeFrame, e);
				}
				clientSocket = null;
			}
		}
		return ret;
	}

	/**
	 * Sends the size of the field to the client
	 */
	public void sendSizeToClient() {
		Socket clientSocketTemp;
		try {
			clientSocketTemp = server.accept();
			clientSocketTemp.getOutputStream().write(fieldsize);
			hostNameClient = clientSocketTemp.getInetAddress().getHostName();
			tictactoeFrame.setStatusBarText("Waiting for player on "
					+ hostNameClient);
			clientSocketTemp.close();
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
	}

	/**
	 * Sends a Integer when the game is finished. 0 means that server and client
	 * will close, 1 means play another game
	 * 
	 * @param status
	 */
	public void sendExitStatus(int status) {
		Socket clientSocketTemp;
		try {
			clientSocketTemp = server.accept();
			clientSocketTemp.getOutputStream().write(status);
			clientSocketTemp.close();
		} catch (IOException e) {
			TicTacToeException.behandleException(tictactoeFrame, e);
		}
	}
}

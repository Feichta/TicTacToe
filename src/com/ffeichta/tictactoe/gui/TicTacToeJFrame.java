package com.ffeichta.tictactoe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.ffeichta.tictactoe.field.TicTacToe;

/**
 * TicTacToeJFrame represents the GUI of the game Tic Tac Toe in a JFrame. It
 * contains buttons of type TicTacToeJButton and a status bar.
 * 
 * @author Michael Wild
 */
public class TicTacToeJFrame extends JFrame {

	/**
	 * Das Spielfeld der Knöpfe
	 */
	private TicTacToeJButton[][] b = null;
	/**
	 * Status bar
	 */
	private JLabel statusBar = null;
	/**
	 * Object of TicTacToe
	 */
	private TicTacToe tictactoe = null;
	/**
	 * Used to block pressing the buttons if a player has to wait for the other
	 * player
	 */
	private boolean canPress = false;
	/**
	 * Number of the field which the user has selected
	 */
	private int selectedNumber = -1;

	/**
	 * Custom constructor creates the GUI. It needs a title for the frame to
	 * distinguish who is who (server or client). Another parameter is a
	 * reference to the TicTacToe field to determine the set moves and the size
	 * of the field
	 * 
	 * @param titel
	 * @param tictactoe
	 */
	public TicTacToeJFrame(String titel, TicTacToe tictactoe) {
		super(titel);
		// Set the ImageIcon for the application
		Image image = Toolkit.getDefaultToolkit().getImage(
				getClass()
						.getResource("/com/ffeichta/tictactoe/images/app.png"));
		setIconImage(image);

		// Connection to the main programm
		this.tictactoe = tictactoe;

		// Create the status bar
		statusBar = new JLabel("");
		statusBar.setHorizontalAlignment(SwingConstants.CENTER);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(this.tictactoe.getFieldSize(),
				this.tictactoe.getFieldSize()));
		b = new TicTacToeJButton[this.tictactoe.getFieldSize()][this.tictactoe
				.getFieldSize()];
		int feldnummer = 0;
		// Create buttons
		for (int i = 0; i < this.tictactoe.getFieldSize(); i = i + 1)
			for (int j = 0; j < this.tictactoe.getFieldSize(); j = j + 1) {
				b[i][j] = new TicTacToeJButton(feldnummer);
				b[i][j].setFocusable(false);
				feldnummer = feldnummer + 1;
				b[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (canPress) {
							// If a buttons is pressed then this button can
							// pressed again until the next turn
							canPress = false;
							selectedNumber = ((TicTacToeJButton) (e.getSource()))
									.getNumberField();
						}
					}
				});
				p.add(b[i][j]);
			}
		this.getContentPane().add(p, BorderLayout.CENTER);

		int height = 272;
		int width = 310;
		for (int i = 0; i < b.length - 3; i++) {
			height += 85;
			width += 85;
		}
		this.setSize(height, width);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		// Sets the frame into the center of the screen
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	/**
	 * Repaints the Frame. It's used after setting a valid move to set a 'x' or
	 * an 'o' into the button. The 'x' and the 'o' are painted in different
	 * colors
	 */
	public void repaint() {
		for (int i = 0; i < this.tictactoe.getFieldSize(); i = i + 1)
			for (int j = 0; j < this.tictactoe.getFieldSize(); j = j + 1) {
				if (this.tictactoe.getField(i, j) == TicTacToe.PLAYER1) {
					this.b[i][j].setX();
					this.b[i][j].setForeground(Color.BLUE);
				}
				if (this.tictactoe.getField(i, j) == TicTacToe.PLAYER2) {
					this.b[i][j].set0();
					this.b[i][j].setForeground(Color.RED);
				}
			}
		super.repaint();
	}

	/**
	 * Sets the text of the status bar
	 * 
	 * @param text
	 */
	public void setStatusBarText(String text) {
		this.statusBar.setText(text);
	}

	/**
	 * This method waits until the user clicks on a button. The return is the
	 * number of the field of the clicked button
	 * 
	 * @return
	 */
	public int getSelectedFieldNumber() {
		int ret = -1;
		this.canPress = true;
		this.selectedNumber = -1;
		do {
			System.out.println("");
			ret = this.selectedNumber;
		} while (ret == -1);
		this.canPress = false;
		return ret;
	}
}

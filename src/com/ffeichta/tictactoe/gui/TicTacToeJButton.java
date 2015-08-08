package com.ffeichta.tictactoe.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

/**
 * This class extends the class JButton. A TicTacToeJButton contains the
 * character 'x' or 'o' and the number of the field for which this button stays.
 * The text of the button is located always in the middle of the button, also if
 * the size of the button changes
 * 
 * @author Fabian
 */
public class TicTacToeJButton extends JButton {
	/**
	 * Text of the button 'x' or 'o'
	 */
	private String text = null;
	/**
	 * Number of the field for which this button stays
	 */
	private int numberField = -1;

	/**
	 * Constructor needs the number of the field
	 * 
	 * @param numberField
	 */
	public TicTacToeJButton(int numberField) {
		this.numberField = numberField;
		this.setBackground(Color.WHITE);
	}

	/**
	 * This method paints a character into the middle of the button and scales
	 * it to the size of the button
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if (this.text != null) {
			// The font name is "Arial", the style is bold and the size depends
			// on the size of the button
			g.setFont(new Font("Arial", Font.BOLD, this.getHeight()
					- (int) (Math.round(this.getHeight() * 0.25))));
			// Get the width of the character
			FontMetrics fm = g.getFontMetrics();
			Rectangle2D r = fm.getStringBounds(this.text, g);
			// Prints the character into the button
			g.drawString(this.text, (int) (this.getWidth() - r.getWidth()) / 2,
					fm.getAscent());
		}
	}

	/**
	 * If there is no 'x' in the button it sets it to 'x'
	 */
	public void setX() {
		if (this.text == null)
			this.text = "x";
	}

	/**
	 * If there is no 'o' in the button it sets it to 'o'
	 */
	public void set0() {
		if (this.text == null)
			this.text = "o";
	}

	/**
	 * Returns the number of the field for which this button stays
	 * 
	 * @return
	 */
	public int getNumberField() {
		return this.numberField;
	}
}

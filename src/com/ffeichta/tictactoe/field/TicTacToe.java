package com.ffeichta.tictactoe.field;

/**
 * Represents the field of the game Tic Tac Toe. The size of the field can go
 * from 3x3 up to 10x10. You can set a move into a field, find out if someone
 * can still win ask the number of the winner. Player 1 has the number -1 and
 * player 2 has the number -2.<br>
 * 
 * The field is a 2D-Array of type Integer. If the size is 3x3 the field looks
 * as follows:<br>
 * 0 1 2 <br>
 * 3 4 5<br>
 * 6 7 8<br>
 * 
 * If player 1 sets a move to position 0 the result is<br>
 * -1 1 2<br>
 * 3 4 5<br>
 * 6 7 8<br>
 * 
 * @author Fabian
 * 
 */
public class TicTacToe {

	/**
	 * Member variables
	 */
	// Number of player 1
	public static final int PLAYER1 = -1;
	// Number of player 2
	public static final int PLAYER2 = -2;
	// The field
	private int[][] field;

	/**
	 * Initializes the field and fills it with values. If the size is too big or
	 * too small it sets it to a valid value
	 * 
	 * @param fieldSize
	 */
	public TicTacToe(int fieldSize) {
		if (fieldSize < 3) {
			fieldSize = 3;
		}
		if (fieldSize > 10) {
			fieldSize = 10;
		}
		field = new int[fieldSize][fieldSize];
		int numberTemp = 0;
		for (int i = 0; i < fieldSize; i++) {
			for (int j = 0; j < fieldSize; j++) {
				field[i][j] = numberTemp;
				numberTemp++;
			}
		}
	}

	/**
	 * Finds out how the field is set on a certain position. Returns 0 if the
	 * position isn't set, -3 if row or column are too big or to small, PLAYER1
	 * if player 1 already set this field or PLAYER2 if player 2 already set
	 * this field
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public int getField(int row, int column) {
		int ret = 0;
		if (Math.abs(row) > field.length || Math.abs(column) > field.length) {
			ret = -3;
		} else {
			switch (field[row][column]) {
			case PLAYER1:
				ret = PLAYER1;
				break;
			case PLAYER2:
				ret = PLAYER2;
				break;
			default:
				break;
			}
		}
		return ret;
	}

	/**
	 * Sets the move of player 1. The return is 0 if it was successfully, -1 if
	 * the position is invalid and -2 if the move is already set
	 * 
	 * @param move
	 * 
	 * @return
	 */
	public int setMovePlayer1(int move) {
		return setMove(move, PLAYER1);
	}

	/**
	 * Sets the move of player 2. The return is 0 if it was successfully, -1 if
	 * the position is invalid and -2 if the move is already set
	 * 
	 * @param move
	 * 
	 * @return
	 */
	public int setMovePlayer2(int move) {
		return setMove(move, PLAYER2);
	}

	/**
	 * Sets the move on a certain position for a certain player. The return is 0
	 * if it was successfully, -1 if the position is invalid and -2 if the move
	 * is already set
	 * 
	 * @param move
	 * @param numberPlayer
	 * @return
	 */
	private int setMove(int move, int numberPlayer) {
		int ret = 0;
		boolean haveSetMove = false;
		boolean neverSetMove = true;
		if (move > field.length * field.length - 1) {
			ret = -1;
		} else {
			for (int i = 0; i < field.length && !haveSetMove; i++) {
				for (int j = 0; j < field.length && !haveSetMove; j++) {
					if (field[i][j] == move) {
						if (field[i][j] != PLAYER1 && field[i][j] != PLAYER2) {
							field[i][j] = numberPlayer;
							haveSetMove = true;
							neverSetMove = false;
						}
					} else {
						neverSetMove = true;
					}
				}
			}
		}
		if (neverSetMove) {
			ret = -2;
		}
		return ret;
	}

	/**
	 * Returns the winner of the game. The winner can be player 1 or player 2.
	 * If there is no winner the return is 0
	 * 
	 * @return
	 */
	public int getWinner() {
		int ret = 0;
		int resultRows = checkRowsForWin();
		int resultColumns = checkColumnsForWin();
		int resultDiagonals = checkDiagonalsForWin();
		if (resultRows != -3) {
			ret = resultRows;
		}
		if (resultColumns != -3) {
			ret = resultColumns;
		}
		if (resultDiagonals != -3) {
			ret = resultDiagonals;
		}
		return ret;

	}

	/**
	 * Checks if there is a winner in the rows. Returns -3 if no winner is in
	 * the rows or the player who won
	 * 
	 * @return
	 */
	private int checkRowsForWin() {
		int ret = -3;
		for (int i = 0; i < field.length; i++) {
			int row = -3;
			boolean rowIsPossible = false;
			for (int j = 0; j < field.length; j++) {
				if (row == -3) {
					row = field[i][j];
				} else {
					if (row != field[i][j]) {
						rowIsPossible = false;
						break;
					} else {
						rowIsPossible = true;
					}
				}
			}
			if (rowIsPossible) {
				ret = row;
				break;
			}
		}
		return ret;
	}

	/**
	 * Checks if there is a winner in the columns. Returns -3 if no winner is in
	 * the rows or the player who won
	 * 
	 * @return
	 */
	private int checkColumnsForWin() {
		int ret = -3;
		for (int i = 0; i < field.length; i++) {
			int column = -3;
			boolean columnIsPossible = false;
			for (int j = 0; j < field.length; j++) {
				if (column == -3) {
					column = field[j][i];
				} else {
					if (column != field[j][i]) {
						columnIsPossible = false;
						break;
					} else {
						columnIsPossible = true;
					}
				}
			}
			if (columnIsPossible) {
				ret = column;
				break;
			}
		}
		return ret;
	}

	/**
	 * Checks if there is a winner in the diagonals. There are always two
	 * diagonals who can win. Returns -3 if no winner is in the diagonals or the
	 * player who won
	 * 
	 * @return
	 */
	private int checkDiagonalsForWin() {
		int ret = -3;
		int diagonal = -3;
		boolean diagonalIsPossible = false;
		// From top left to bottom right
		if (field[0][0] == PLAYER1 || field[0][0] == PLAYER2) {
			for (int i = 0; i < field.length; i++) {
				if (diagonal == -3) {
					diagonal = field[i][i];
				} else {
					if (diagonal != field[i][i]) {
						diagonalIsPossible = false;
						break;
					} else {
						diagonalIsPossible = true;
					}
				}
			}
			if (diagonalIsPossible) {
				ret = diagonal;
			}
		}
		// From top right to bottom left
		diagonal = -3;
		diagonalIsPossible = false;
		if (field[0][field.length - 1] == PLAYER1
				|| field[0][field.length - 1] == PLAYER2) {
			for (int i = 0; i < field.length; i++) {
				if (diagonal == -3) {
					diagonal = field[i][field.length - 1 - i];
				} else {
					if (diagonal != field[i][field.length - 1 - i]) {
						diagonalIsPossible = false;
						break;
					} else {
						diagonalIsPossible = true;
					}
				}
			}
			if (diagonalIsPossible) {
				ret = diagonal;
			}
		}
		return ret;
	}

	/**
	 * Returns true if someone can still win the game
	 * 
	 * @return
	 */
	public boolean someoneCanWin() {
		return canHorizontalWin() || canVerticalWin() || canDiagonalLeftWin()
				|| canDiagonalRightWin();
	}

	/**
	 * Returns true if there can be a winner in the rows
	 * 
	 * @return
	 */
	private boolean canHorizontalWin() {
		boolean ret = false;
		boolean canWin = true;
		for (int i = 0; i < getFieldSize(); i++) {
			int player = -3;
			for (int j = 0; j < getFieldSize(); j++) {
				if (player == -3
						&& (field[i][j] == PLAYER1 || field[i][j] == PLAYER2)) {
					player = field[i][j];
				} else {
					if (field[i][j] == player || field[i][j] >= 0) {
						canWin = true;
					} else {
						canWin = false;
						break;
					}
				}
			}
			if (player == -3) {
				canWin = true;
			}
			if (canWin) {
				ret = canWin;
			}
		}
		return ret;
	}

	/**
	 * Returns true if there can be a winner in the columns
	 * 
	 * @return
	 */
	private boolean canVerticalWin() {
		boolean ret = false;
		boolean canWin = true;
		for (int i = 0; i < getFieldSize(); i++) {
			int player = -3;
			for (int j = 0; j < getFieldSize(); j++) {
				if (player == -3
						&& (field[j][i] == PLAYER1 || field[j][i] == PLAYER2)) {
					player = field[j][i];
				} else {
					if (field[j][i] == player || field[j][i] >= 0) {
						canWin = true;
					} else {
						canWin = false;
						break;
					}
				}
			}
			if (player == -3) {
				canWin = true;
			}
			if (canWin) {
				ret = canWin;
			}
		}
		return ret;
	}

	/**
	 * Returns true if there can be a winner in the diagonal from top left to
	 * bottom right
	 * 
	 * @return
	 */
	private boolean canDiagonalLeftWin() {
		boolean ret = false;
		boolean canWin = true;
		int player = -3;
		for (int i = 0; i < getFieldSize(); i++) {
			if (player == -3
					&& (field[i][i] == PLAYER1 || field[i][i] == PLAYER2)) {
				player = field[i][i];
			} else {
				if (field[i][i] == player || field[i][i] >= 0) {
					canWin = true;
				} else {
					canWin = false;
					break;
				}
			}
		}
		if (player == -3) {
			canWin = true;
		}
		if (canWin) {
			ret = canWin;
		}
		return ret;
	}

	/**
	 * Returns true if there can be a winner in the diagonal from top right to
	 * bottom left
	 * 
	 * @return
	 */
	private boolean canDiagonalRightWin() {
		boolean ret = false;
		boolean canWin = true;
		int player = -3;
		for (int i = 0; i < getFieldSize(); i++) {
			if (player == -3
					&& (field[i][field.length - 1 - i] == PLAYER1 || field[i][field.length
							- 1 - i] == PLAYER2)) {
				player = field[i][field.length - 1 - i];
			} else {
				if (field[i][field.length - 1 - i] == player
						|| field[i][field.length - 1 - i] >= 0) {
					canWin = true;
				} else {
					canWin = false;
					break;
				}
			}
		}
		if (player == -3) {
			canWin = true;
		}
		if (canWin) {
			ret = canWin;
		}
		return ret;
	}

	/**
	 * Prints the field. The numbers of the players are replaced with 'X' and
	 * 'O'. This method is only used for testing
	 * 
	 * For example:<br>
	 * 01X<br>
	 * 3OX<br>
	 * 678<br>
	 */
	@Override
	public String toString() {
		String ret = "";
		for (int i = 0; i < getFieldSize(); i++) {
			for (int j = 0; j < getFieldSize(); j++) {
				if (field[i][j] == PLAYER1) {
					ret = ret + "X" + "\t";
				} else if (field[i][j] == PLAYER2) {
					ret = ret + "O" + "\t";
				} else {
					ret = ret + field[i][j] + "\t";
				}
			}
			if (i < getFieldSize() - 1) {
				ret = ret + "\n\n";
			}
		}
		return ret;
	}

	/**
	 * Returns the length of the field
	 * 
	 * @return
	 */
	public int getFieldSize() {
		return field.length;
	}
}

/*
 * File: Player.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * Player is a subclass to Entity. It contains a constructor that sets
 * its appearance and initializes its position. The isAvailMove method
 * is overridden and defines the Player's move set.
 *
 */
import javax.swing.*;
import java.awt.*;

class Player extends Entity
{
	//Player Constructor
	public Player()
    {
		entityCoordinate = new Coordinate(this.getRow(), this.getCol());
		setImage(new ImageIcon ("images/player.png"));
    }
	
	public boolean isAvailMove(int indexRow, int indexColumn)
	{
		//Player's move set = 1 or 2 horizonal/vertical tiles
		int rowDiff = this.getRow() - indexRow;
		int columnDiff = this.getCol() - indexColumn;
		int rowDiffABS = Math.abs(rowDiff);
		int colDiffABS = Math.abs(columnDiff);
		int DiffSum = Math.abs(columnDiff) + Math.abs(rowDiff);
		if (colDiffABS == rowDiffABS) return false;
		if (DiffSum == 1 || DiffSum == 2) return true;
		else return false;
	}
}
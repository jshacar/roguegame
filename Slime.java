/*
 * File: Slime.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * Slime is a subclass of Enemy. It has a constructor that customizes its
 * image. The isAvailMove method is overridden and defines the Slime move set.
 *
 */
import javax.swing.*;
import java.util.ArrayList;

class Slime extends Enemy
{
	//Slime Constructor
    public Slime()
    {
		setType("Slime");
		entityCoordinate = new Coordinate(this.getRow(), this.getCol());
		setImage(new ImageIcon ("images/slime.png"));
    }
	
	public boolean isAvailMove(int indexRow, int indexColumn)
	{
		//Slime's move set = 1 horizonal/vertical tile
		int rowDiff = this.getRow() - indexRow;
		int columnDiff = this.getCol() - indexColumn;
		int rowDiffABS = Math.abs(rowDiff);
		int colDiffABS = Math.abs(columnDiff);
		int DiffSum = Math.abs(columnDiff) + Math.abs(rowDiff);
		if (DiffSum == 1) return true;
		else return false;
	}
}
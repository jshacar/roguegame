/*
 * File: Wolf.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * Wolf is a subclass of Enemy. It has a constructor that customizes its
 * image. The isAvailMove method is overridden and defines the Wolf
 * move set.
 *
 */
import javax.swing.*;
import java.util.ArrayList;

class Wolf extends Enemy
{
	//Wolf Constructor
    public Wolf()
    {
		setType("Wolf");
		entityCoordinate = new Coordinate(this.getRow(), this.getCol());
		setImage(new ImageIcon ("images/wolf.png"));
    }
	
	public boolean isAvailMove(int indexRow, int indexColumn)
	{
		//Wolf's move set = L-shape, like a knight in chess
		int rowDiff = this.getRow() - indexRow;
		int columnDiff = this.getCol() - indexColumn;
		int rowDiffABS = Math.abs(rowDiff);
		int colDiffABS = Math.abs(columnDiff);
		int DiffSum = Math.abs(columnDiff) + Math.abs(rowDiff);
		if (rowDiffABS != 0 && colDiffABS != 0 && DiffSum == 3) return true;
		else return false;
	}
}
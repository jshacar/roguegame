/*
 * File: Enemy.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * Enemy is the superclass to the enemy classes Slime, Skeleton, and Wolf.
 * Each enemy must move on its own and thus relies on the findBestMove
 * method. Every enemy has an enemyType so that it can be referenced
 * in output to the user and custom death messages.
 *
 */
import javax.swing.*;
import java.util.ArrayList;

abstract class Enemy extends Entity
{
	private String enemyType;
	
	public void setType(String s){this.enemyType = s;}
	public String getType(){return this.enemyType;}
	
	//Takes all available moves and calculates distance from Player.
	//Best move is the availMove that has the smallest distance.
	//Ties are resolved by a 'coin flip'
	public void findBestMove(Coordinate pPos)
	{
		Coordinate playerPosition = pPos;
		Coordinate bestMove = this.entityCoordinate;
		ArrayList<Coordinate> availMoves = this.getAvailableMoves();
		ArrayList<Integer> moveSums = new ArrayList<Integer>();
		int min = 20;
		
		for (Coordinate c : availMoves)
		{
			int rowDiff = c.getCoordRow() - playerPosition.getCoordRow();
			int colDiff = c.getCoordCol() - playerPosition.getCoordCol();
			int diffSum = Math.abs(rowDiff) + Math.abs(colDiff);
			moveSums.add (diffSum);
			//If same diffSum, then flip a coin
			if (diffSum == min)
			{
				//Heads takes new Coord as bestMove
				if (Math.random() < 0.5){min = diffSum; bestMove = c;}
				//Tails keeps current Coord as bestMove
			}
			//Save min as bestMove
			if (diffSum < min){min = diffSum; bestMove = c;}

		}
		System.out.print("Minimum distance is " + min);
		System.out.println(" best move is " + bestMove.coordToString());
		//Set enemy row,col,and Coord to bestMove position
		this.setRow(bestMove.getCoordRow());
		this.setCol(bestMove.getCoordCol());
		this.setCoord(this.getRow(), this.getCol());
	}
}
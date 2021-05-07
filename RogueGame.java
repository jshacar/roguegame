/*
 * File: RogueGame.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * RogueGame is the main Class and is responsible for creating the graphical
 * instance of the game. Player and Enemy movement and the game time variables
 * are managed here. Game features random level and enemy creation.
 * Main game loop is contained in run().
 * Related Classes: {Entity.java, Player.java, Enemy.java, Slime.java,
 * Skeleton.java, Wolf.java, Coordinate.java}
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random; 

public class RogueGame extends JFrame
{
	//Array of button tiles 9x5, these are the backgrounds
	private JButton [][] tiles = new JButton [5][9];
	//Array of labels that will go on the buttons, where sprites are drawn
	private JLabel [][] tileLabels = new JLabel [5][9];
	private Player p = new Player ();
	private boolean running = false;
	private boolean playerTurn = true;
	private boolean levelClear = false;
	private int currentLevel = 1;
	private int endLevel;
	private Coordinate levelEntrance = new Coordinate();
	private Coordinate levelExit = new Coordinate();
	private ArrayList<Enemy> activeEnemies = new ArrayList<Enemy>();
	private ArrayList<Coordinate> blankSpaces = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> layout1 = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> layout2 = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> layout3 = new ArrayList<Coordinate>();
	private List<ArrayList<Coordinate>> layouts =
			Arrays.asList(layout1, layout2, layout3);
	private List<String> enemyTypes =
			Arrays.asList("Slime", "Skeleton", "Wolf");
			
	//RogueGame - draws the play field and then calls init to start the game
	public RogueGame()
	{
		//Set frame title, dimension, no resize		
		setTitle ("RogueGame - Level " + currentLevel);
		setSize (900, 500);
		//Center frame on screen and prevent resize
		setLocationRelativeTo(null);
		setResizable(false);
		
		//Create a button listener object
		ButtonListener bl = new ButtonListener ();
				
		//Create buttons and put them into 2D tiles array
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(5, 9));
		for (int i = 0; i <5; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				//Create button
				JButton tile = new JButton ( );
				tile.setPreferredSize(new Dimension(100, 100));
				tile.setIcon ( new ImageIcon ("images/dark_tile.png"));
				//Create inner label
				JLabel tileLabel = new JLabel();
				tileLabel.setPreferredSize(new Dimension(40, 40));
				tileLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				//Add label to tile
				tile.add (tileLabel);
				//Add tile to panel
			    gridPanel.add(tile);
				//Put current tile/label into 2D array
				tileLabels[i][j] = tileLabel;
			    tiles[i][j] = tile;
				//Add listener
				tiles[i][j].addActionListener (bl);
			}
		}
		add (gridPanel, BorderLayout.CENTER);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setVisible (true);
		
		//Initialize the game
		init();
	}

	public static void main(String [] args)
    {
		//Contruct an instance of the game
		RogueGame rg = new RogueGame();
	}
	
	public void init()
	{
		//Prompt the user to choose a difficulty
		String[] difficulties = {"Easy", "Medium", "Hard"};
		int n = JOptionPane.showOptionDialog(null,
			"Choose a difficulty:\n" + 
			"Easy = 3 Levels \n" + 
			"Medium = 5 Levels \n" + 
			"Hard = 7 Levels", 
			"Start Game",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			difficulties,
			difficulties[0]);

		switch (difficulties[n])
		{
			case "Easy":
				endLevel = 3;
				break;
			case "Medium":
				endLevel = 5;
				break;
			case "Hard":
				endLevel = 7;
				break;
		}
		currentLevel = 1;
		initLayouts();
		drawRandomLevel(currentLevel);
		start();
	}
	
	public void drawRandomLevel(int currentLevel)
	{
		//Set title to include currentLevel
		setTitle ("RogueGame - Level " + currentLevel +	"/" + endLevel);
		//Reset all tile backgrounds
		clearPlayingField();
		//Reset blankSpaces and activeEnemies
		blankSpaces.clear();
		activeEnemies.clear();
		//Every level starts on player turn
		playerTurn = true;
		levelClear = false;
		
		//Draw levelEntrace and levelExit
		//(Alternate corners on each level)
		if (currentLevel%2 == 0)
		{
		tiles[0][0].setIcon(new ImageIcon ("images/level_start.png"));
		levelEntrance.setCoord(0, 0);
		tiles[4][8].setIcon(new ImageIcon ("images/level_end.png"));
		levelExit.setCoord(4, 8);
		}
		else
		{
		tiles[4][0].setIcon(new ImageIcon ("images/level_start.png"));
		levelEntrance.setCoord(4, 0);
		tiles[0][8].setIcon(new ImageIcon ("images/level_end.png"));
		levelExit.setCoord(0, 8);
		}
		
		//If endLevel levelExit becomes treasure chest
		if (currentLevel == endLevel)
		tiles[levelExit.getCoordRow()][levelExit.getCoordCol()].setIcon
			(new ImageIcon ("images/chest.png"));
			
		//Place the player on the levelEntrace
		p.setRow(levelEntrance.getCoordRow());
		p.setCol(levelEntrance.getCoordCol());
		p.setCoord(levelEntrance.getCoordRow(), levelEntrance.getCoordCol());
		tileLabels[p.getRow()][p.getCol()].setIcon (p.getImage());
		
		//Choose a random layout
		Random rand = new Random(); 
		ArrayList<Coordinate> randLayout = 
			layouts.get(rand.nextInt(layouts.size()));

		//Set blank spots
		for (Coordinate c : randLayout)
		{
			tiles[c.getCoordRow()][c.getCoordCol()].setIcon(new ImageIcon
				("images/blank_space.png"));
			blankSpaces.add (new Coordinate (c.getCoordRow(),c.getCoordCol()));
		}
		//Generate enemies, total = 2 + current level
		generateRandomEnemy(2 + currentLevel);
	}
	
	public void generateRandomEnemy(int enemies)
	{
		//Makes a number of random enemies equal to the passed int
		for (int i = 0; i < enemies; i++)
		{
			int randRow = 0;
			int randCol = 0;
			boolean freeSpaceFound = false;
			Enemy enemy = null;
			Random rand = new Random(); 
			String enemyType = enemyTypes.get
				(rand.nextInt(enemyTypes.size()));
				
			//Create enemy based on random type selection
			if (enemyType.equals("Slime")) enemy = new Slime();
			else if (enemyType.equals("Skeleton")) enemy = new Skeleton();
			else if (enemyType.equals("Wolf")) enemy = new Wolf();
			
			//Find a free space to place new Enemy
			while (!freeSpaceFound)
			{
				randRow = (rand.nextInt(5));
				randCol = (rand.nextInt(9));
				Coordinate coord = new Coordinate (randRow, randCol);
				//Check if space is not Player, other Enemy,
				//blank space or levelExit
				if (coord.isEqual(p.getCoord()) ||
					occupiedByEnemy(randRow, randCol) ||
					isBlankSpace(randRow, randCol) ||
					isLevelExit(coord)) 
				{continue;}
				freeSpaceFound = true;
			}
			//Set enemy position and ad to activeEnemies
			enemy.setRow(randRow); enemy.setCol(randCol);
			enemy.setCoord (randRow, randCol);
			tileLabels[enemy.getRow()][enemy.getCol()].setIcon
				(enemy.getImage());
			activeEnemies.add(enemy);
		}
	}
	
	public void clearPlayingField()
	{
		//For every tile, reset background, border, and tileLabel
		for (int row = 0; row < 5; row++)
		{
            for (int col = 0; col < 9; col++)
            {
				tiles[row][col].setIcon
					(new ImageIcon ("images/dark_tile.png"));
				tiles[row][col].setBorder
					(UIManager.getBorder("Button.border"));
				tileLabels[row][col].setIcon (null);
			}
		}
	}
		
	public void initLayouts()
	{
		//Set coords of blank spaces and add to list
		//Layout 1
		layout1.add (new Coordinate (0, 4));
		layout1.add (new Coordinate (1, 1));
		layout1.add (new Coordinate (1, 7));
		layout1.add (new Coordinate (3, 1));
		layout1.add (new Coordinate (3, 7));
		layout1.add (new Coordinate (4, 4));
		
		//Layout 2
		layout2.add (new Coordinate (0, 3));
		layout2.add (new Coordinate (0, 4));
		layout2.add (new Coordinate (0, 5));
		layout2.add (new Coordinate (2, 1));
		layout2.add (new Coordinate (2, 7));
		layout2.add (new Coordinate (4, 3));
		layout2.add (new Coordinate (4, 4));
		layout2.add (new Coordinate (4, 5));
		
		//Layout 3
		layout3.add (new Coordinate (2, 2));
		layout3.add (new Coordinate (2, 3));
		layout3.add (new Coordinate (2, 4));
		layout3.add (new Coordinate (2, 5));
		layout3.add (new Coordinate (2, 6));
	}
	
	public void start()
	{
		running = true;
		this.run();
	}
	
	public void run()
	{	
		//Start on main game loop
		System.out.println("---START GAME---");
		while (running == true)
		{
			//Player Turn			
			System.out.println("{PLAYER TURN}");
			System.out.println("Player is @ " + p.getCoord().coordToString());
			determineAvailMoves();
			isBlankSpacePlus1();
			System.out.print("Available moves: " +
			p.getAvailableMovesString() + "\n");
			
			//While playerTurn true wait for button click
			while (playerTurn == true)
			{
				try{Thread.sleep(20);} 
				catch(InterruptedException e){}
			}
			
			//If all enemies clear and player steps on levelExit
			//advance to next Level or win the game
			if (levelClear && isLevelExit(p.getCoord()))
			{
				if (currentLevel == endLevel)
				{
					String[] options = {"Yes", "No"};
					int n = JOptionPane.showOptionDialog(null,
					"You win! Play Again?",
					"You Win!",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
		
					switch (options[n])
					{
						case "Yes":
							init();
							break;
						case "No":
							stop();
							break;
					}
				}
				if(running == false) break;
				System.out.println("Level " + currentLevel + " complete!");
				currentLevel++;
				drawRandomLevel(currentLevel);
				continue;
			}
			
			//Enemy Turn
			System.out.println("{ENEMY TURN}");
			
			//If no enemies, levelClear is true
			if (activeEnemies.size() == 0)
			{
				System.out.println("All enemies are defeated!");
				levelClear = true;
			}
			
			//Make move for each enemy in activeEnemies
			for (Enemy e : activeEnemies)
			{
				//If the player is killed, don't move other enemies
				if(running == false) break;
				
				//Add a little delay for appearance
				try{Thread.sleep(300);} 
				catch(InterruptedException ex){}
				
				System.out.println(e.getType() + " is @ " +
					e.getCoord().coordToString());
				determineEnemyAvailMoves(e);
				System.out.print ("Available moves: " +
				e.getAvailableMovesString() + "\n");
				
				
				moveEnemy(e);
			}
			//Pass turn back to player
			playerTurn = true;
		}
	}
	
	public void stop()
	{
		//Reset all tiles to default border
		for (int row = 0; row < 5; row++)
		{
            for (int col = 0; col < 9; col++)
            {
				tiles[row][col].setBorder
					(UIManager.getBorder("Button.border"));
			}
		}
		//Clear player moves, prevents posthumous movement
		p.clearAvailableMoves();
		running = false;
		System.out.println("---GAME OVER!---");
	}
	
	public void movePlayer(int i, int j)
	{
		//Make current position blank
		tileLabels[p.getRow()][p.getCol()].setIcon (null);
		//Set new player row/col
		p.setRow(i); p.setCol(j);
		//Set coord of player object
		p.setCoord(p.getRow(), p.getCol());
		//Move image to new tile
		tileLabels[p.getRow()][p.getCol()].setIcon (p.getImage());
		
		//Clear stored Enemy to delete
		Enemy enemyKilled = null;
		//For each enemy check if Player has landed on it
		for (Enemy e : activeEnemies)
			{
				if (p.getCoord().isEqual(e.getCoord()))
				{
					System.out.println("You defeated a " + e.getType());
					enemyKilled = e;
				}
			}
		//If stored enemy is not empty, remove enemy from list
		if (enemyKilled != null)
		{
			activeEnemies.remove(enemyKilled);
		}
		//Reset borders and clear availMoves
		hideAvailMoves();
		playerTurn = false;
	}
	
	public void moveEnemy(Enemy en)
	{
		//Make current position blank
		tileLabels[en.getRow()][en.getCol()].setIcon (null);
		//Determine best move for the enemy
		//(This also sets the objects position)
		en.findBestMove(p.getCoord());
		//Move image to new tile
		tileLabels[en.getRow()][en.getCol()].setIcon (en.getImage()); 
		//Check to see if enemy has landed on Player
		if (p.getCoord().isEqual(en.getCoord()))
			{
				System.out.println("You got killed by a " +
					en.getType() + "!");
				deathMessage(en);
				stop();
			}
	}
	
	public void determineAvailMoves()
	{
		//Reset borders and clear availMoves
		hideAvailMoves();		
		//Determine availMoves and draw custom border around tiles
		for (int row = 0; row < 5; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				if (isBlankSpace(row, col))
				{
					continue;
				}
				if (p.isAvailMove(row, col))
				{
					tiles[row][col].setBorder
						(BorderFactory.createDashedBorder
					(Color.GRAY, 5, 5, 2, true));  //(thickness, length, space)
					p.addAvailableMove(row, col);
				}
			}
		}
	}
	
	public void hideAvailMoves()
	{
		//Reset all tiles to default border
		for (int row = 0; row < 5; row++)
		{
            for (int col = 0; col < 9; col++)
            {
				tiles[row][col].setBorder
					(UIManager.getBorder("Button.border"));
			}
		}
		//Reset players collection of available moves
		p.clearAvailableMoves();
	}
	
	public void determineEnemyAvailMoves(Enemy en)
	{
		//Reset enemy collection of available moves
		en.clearAvailableMoves();
		//Determine avail moves
		for (int row = 0; row < 5; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				if (occupiedByEnemy(row, col)) continue;
				if (isBlankSpace(row, col)) continue;
				if (en.isAvailMove(row, col))
				{
					en.addAvailableMove(row, col);
				}
			}
		}
	}
	
	public boolean occupiedByEnemy (int row, int col)
	{
		for (Enemy e : activeEnemies)
		{
			if (row == e.getRow() && col == e.getCol()) return true;
		}
		return false;
	}
	
	public boolean isLevelExit(Coordinate c)
	{
		if (c.isEqual(levelExit))return true;
		else return false;
	}
	
	public boolean isBlankSpace (int row, int col)
	{
		for (Coordinate c : blankSpaces)
		{
			if (row == c.getCoordRow() && col == c.getCoordCol()) return true;
		}
		return false;
	}
	public void isBlankSpacePlus1()
	{
		//Create a list of potential avail moves to remove
		ArrayList<Coordinate> coordsToRemove = new ArrayList<Coordinate>();
		
		//For each in availMoves, determine which are 2 away from the player
		for (Coordinate c : p.getAvailableMoves())
		{
			int rowDiff = p.getRow() - c.getCoordRow();
			int columnDiff = p.getCol() - c.getCoordCol();
			int DiffSum = Math.abs(columnDiff) + Math.abs(rowDiff);
			
			//If the move is two away, check for a blank space inbetween
			if (DiffSum == 2)
			{
				if (rowDiff == 2 && isBlankSpace(p.getRow()-1, p.getCol()))
					coordsToRemove.add(c);
				if (rowDiff == -2 && isBlankSpace(p.getRow()+1, p.getCol()))
					coordsToRemove.add(c);
				if (columnDiff == 2 && isBlankSpace(p.getRow(), p.getCol()-1))
					coordsToRemove.add(c);
				if (columnDiff == -2 && isBlankSpace(p.getRow(), p.getCol()+1))
					coordsToRemove.add(c);
			}
		}
		//If coordsToRemove is not empty, remove each og them
		//from availMoves and draw standard border
		if (!coordsToRemove.isEmpty())
		{
			int size = coordsToRemove.size();
			for (int i = size-1; i >= 0; i--)
			{
				p.remAvailableMove(coordsToRemove.get(i).getCoordRow(),
									coordsToRemove.get(i).getCoordCol());
				tiles[coordsToRemove.get(i).getCoordRow()]
					 [coordsToRemove.get(i).getCoordCol()].
					 setBorder(UIManager.getBorder("Button.border"));
			}
		}
		coordsToRemove.clear();
	}
	
	public void deathMessage(Enemy en)
	{
		//Produces a unique Game Over message based on the
		//enemy the player was killed by
		String msg = "";
		if (en.getType().equals("Slime"))
		{
			msg = "Ewwww, so gooey!";
		}
		if (en.getType().equals("Skeleton"))
		{
			msg = "Bad to the bone...";
		}
		if (en.getType().equals("Wolf"))
		{
			msg = "Woof! Woof!";
		}
		
		//Ask the user if they would like to play again
		String[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null,
			msg + "\nPlay Again?",
			"Game Over!",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[0]);
		
		switch (options[n])
		{
			case "Yes":
				init();
				break;
			case "No":
				break;
		}
	}
	
	//ButtonLister finds which button is pressed - if valid move, moves player
	class ButtonListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			Object pushed = e.getSource();
			for (int i = 0; i < 5; i++ ) 
			{
			   for (int j = 0; j < 9; j++) 
			    {
				   if (tiles[i][j] == pushed) 
					{
						Coordinate pressedCoord = new Coordinate (i, j);
						for (Coordinate c : p.getAvailableMoves())
						{
							if (pressedCoord.isEqual(c))
							{
								System.out.println("Valid Move");
								movePlayer(i, j);
								return;
							}
						}
						System.out.println("Invalid Move");
					}
				}
			}
		}
	}
}
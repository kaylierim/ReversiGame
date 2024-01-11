Overview:

This is an implementation of all the methods necessary to play Reversi and a simple textual view of the board. Reversi
is a two player game played on a hexagonal grid (that can be of various sizes). Each player has a color, black or white. 
The objective of the game is to have the majority of your colored tiles on the board at the end of the game. The game 
begins with equal numbers of both colors of discs arranged in the middle of the grid. A legal move is when the player
plays a disc onto an empty tile, and it is adjacent to a disc of the opposite color. Additionally, this disc must 
connect in a straight line of the opposite color's discs to a disc of their color. The discs in-between the placed 
tile and connecting tile will be flipped to the current players color, capturing the opponents discs. The players are
allowed to pass, and let the other player move. If a player has no legal moves, they are required to pass. If both
players pass in a row, the game ends. The game is over when there are no more legal moves to be played by either player.


Key components:

The Reversi interface defines the model's methods and is implemented by ReversiModel. The methods defined within this
interface are relevant to playing a game of Reversi: makeMove(), pass(), and so on. 
Although the ReversiView interface is empty now, its creation shows that the view is open to further implementation in 
later versions of the project.

The Player interface was made to implement multiple types of players such as a human or AI player. The interface defines
a common set of rules that different player implementations must adhere to, allowing various types of players to 
interact with the game model. In the beginning of the game, the user will be prompted to select the two types of players.
The corresponding Player classes will then determine how they want to play the game, make moves, or what strategies they
want to employ. 


Key subcomponents:  

Within the Reversi model class, the game board is represented as a 2D array of Tiles. We chose this 
representation because it efficiently represents a hexagonal game board in a familiar and understandable 
2D array format while still having the needed functionality of a game board. In order to fit a hexagonal 
shape into a 2D array, there are some unused spaces that are represented by nulls (located in the top 
left and bottom right corners to mimic a hexagonal shape). Since the columns of a hexagon are not aligned perfectly, 
the columns of this board correspond with the diagonal of the hexagon the left most diagonal of the visual hexagon 
corresponds to the 0th column in the board array. In each row index (0th index represents the top of the board), the 
column indexes (0th index represents the left most side of the board array) of that row are playable after the cells 
that contain null. A visual representation of the coordinate system can be viewed here: 
https://www.redblobgames.com/grids/hexagons/#map-storage

The Tile enumeration represents every possible tile state in a Reversi game (BLACK, WHITE, and EMPTY). It is used 
primarily in the model class when it is necessary to check whether a specific tile is occupied or empty. 

The class invariant is the 'turn' variable in ReversiModel that represents the current player's turn must always be
Tile.BLACK or Tile.WHITE and it should never be Tile.EMPTY. This invariant is crucial to ensure the consistency of the
game, since a player cannot be empty. This invariant is enforced within the ReversiModel class by initializing the 
'turn' variable to Tile.BLACK in the startGame() method. 'turn' is always updated using the getOpposite() method from 
the Tile class, which will only ever return Tile.BLACK or Tile.WHITE. This ensures that the 'turn' variable is never set 
to Tile.EMPTY.


Source organization: 

The src folder contains the model and view packages. The model package contains the Reversi interface, the 
ReversiModel class that implements the Reversi interface, the Tile enum class, and the Player interface that will be 
used in later projects. The view package contains the ReversiView interface the is implemented by the ReversiTextualView 
class. Additionally, there is a test directory containing the ReversiModelTests class and the ReversiViewTests class. 


Changes for Part 2:

We refactored the model interface into Reversi and ReadonlyReversi. ReadonlyReversi contains all the observation methods 
of the model, while Reversi contains all the mutator methods. Reversi extends ReadonlyReversi. To ReadonlyReversi, we
added a couple more methods: isLegalMove(), getScore(), currentPlayerHasLegalMove(), getScoreOfMove(), getNeighbors(), 
and copyGameBoard(). These methods were missing before because we had methods that returned similar information, and we 
did not need this data in our model implementation. The getNeighbors() method now returns a list of row and column 
coordinates to give more functionality for use in the strategy classes. Additionally, in order for our code to compile, 
we also had to slightly change the checkNeighborsHaveOppositeColor() but it has the same functionality as before.

New Classes/Interfaces:
- All strategy classes, strategy interface, and EXTRA CREDIT

  In our strategy package, we added an interface that represents a fallible strategy for a Reversi game 
  (FallibleReversiStrategy). The strategies implemented are (in order of the best to the worst strategy)
  GoForCorners, AvoidCellsNextToCorners, and CaptureMostPieces. To avoid duplicated code, these strategy 
  class extend the Strategy abstract class where there is a method that calculates the uppermost leftmost 
  coordinate in the available moves for this strategy. The extra credit is done in the GoForCorners and 
  AvoidCellsNextToCorners classes.

- All GUI classes and KEYBOARD INTERACTIONS

The ReversiGraphics class extends JFrame and initializes the starting window. It adds an instance of a HexPanel. 
HexPanel creates the grid of hexagons, adds pieces if they exist, and handles mouse clicks with MouseEventsListener.
If a cell gets clicked on it is highlighted, unless it has a tile on it which does not allow it to be selected.
HexPanel also has a KeyEventsListener, but it has no functionality yet since there is no controller. However, we
have assigned the keys 'm' to make a move on a highlighted tile if the move is allowable and 'p' to pass the move. 
HexPanel additionally uses the Hexagon class to construct the hexagons that make up the grid. 

- Features

The Features interface contains two methods, makeMoveFeatures() and passFeatures(), that will work with keyboard 
input once the controller implements these methods. It is referenced in the KeyEventsListener class (which is located
in the HexPanel class) which takes it in as a parameter when constructing. For makeMoveFeatures(), the coordinates of
the highlighted tile are given so that the controller knows where to make the move to. The Features interface aims to 
decouple the view from the model. 

- ReversiStrategyTests

Tests that our strategies work properly and return the upper left most move if there are multiple available moves.

- ReversiMain

Runs our model and GUI view. 

Changes for part 3:

- Controller and keyboard interactions

We added a controller package with the class ReversiController. It implements two interfaces: ModelFeatures 
and ViewFeatures (Further explained in the next section). This class is used to regulate communication 
between the model and the view. If an event is communicated through the model or the view, the controller
handles its execution. For example, if the current player gets switched to an AI player, the controller
executes its move. In addition, the controller handles an illegal move by telling the view to output an 
error message. The controller also adds itself as a listener to both the view and model. It additionally sets 
the hot keys ('m' and 'p') that are used to interact with the view. 

- Features interfaces

We made two Features interfaces that interact with listeners from the model and view: ViewFeatures which corresponds
to the view and ModelFeatures that corresponds to the model. The ViewFeatures interface contains the methods 
makeMoveFeatures(), passFeatures(), printToConsoleClick(), and printToConsoleKey(). makeMoveFeatures() is called by
the view when there is a highlighted cell and 'm' is pressed. The controller then checks if it is
that players turn before calling the model to actually make the move. passFeatures() works the same, instead when 'p'
is pressed. printToConsoleClick() and printToConsoleKey() print a message into the console when a cell is clicked or
when a move is made/passed via a key press. The ModelFeatures interface contains the method playerChanged(). When
the turn is switched from one player to another, the controller is alerted. Then, the controller allows the new current
player to click and interact with the view. This prevents a player from interacting with the view when it is not their
turn. If it is not the players turn, clicking on the view or pressing keys will not do anything and the view will not
register these actions. 

- Main method/command line input and Player factory

In the ReversiMain class, we process command line input. The first argument needs to be model size, second is player 1
type, third is player 2 type. If the user chooses an AI player, the AI player will use the best strategy move available. 
We made a Player factory class (PlayerCreator) that features a create method. This create method is used in the ReversiMain 
class to return a new instance of the desired player. The main method then creates the necessary components for the Reversi 
game to start by initializing the views, players, and controllers.

- Player interface and human and AI player classes

The Player interface has the method getTileColor() which is implemented by HumanPlayer and AIPlayer. 
The HumanPlayer class is a small concrete class that only takes in a Tile color in its constructor.
On the other hand, the AIPlayer class takes in the read-only model and Tile color in its constructor to choose 
its next move. To choose its next move, if there are any moves present in the strategies, it returns that move's
axial coordinates. If not, it returns an Optional.empty() object (which means there are no moves available for it).

- Changes made to the implementation from Part 2 and new View Interfaces

We added test methods for isLegalMove() and getNeighbors() from the ReadonlyReversi class that were previously missing.
The CaptureMostPieces and ReversiTextualView classes now take in a ReadOnlyModel to ensure that they do not make 
changes to the model itself. Our code previously contained hardcoded System.out.println() that printed out the 
coordinates of clicked cells. Now, we have stub methods in the ViewFeatures interface that use listeners that call 
printToConsoleClick() and printToConsoleKey() which print to the console instead. 

We created two new view interfaces: ReversiGUIView and ReversiTextView. We split up the view interfaces because it
did not make sense for the different types of views to implement the same thing, as the textual view returns a String,
while the GUI view does not return anything and just initializes the JFrame. Both interfaces have methods render() but
have different return values that correspond to the specific responsibilities of that view. 

- Test classes

In this assignment we created PlayerCreatorTests and ReversiFeaturesControllerTests test classes. PlayerCreatorTests
tests the PlayerCreator factory class and ensures that the proper class instances are returned. 
ReversiFeaturesControllerTests tests that the interactions between the view and the controller are updated in the model. 
We tested this by mocking the interactions between the view and the controller by adding features directly instead of
through listeners as it is normally done when the program is running. Following these mocked interactions, we checked
that the model is updated. This test class also checks the model listeners work properly and let the controller know 
when a turn has changed.


# ReversiGame

package boardGameLibrary.boardGame.board;

import boardGameLibrary.eventWrappers.BoardMoveEvent;
import boardGameLibrary.boardGame.move.Move;
import boardGameLibrary.player.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * BoardMoveMaker is responsible for making moves based on a {@link Move} and a
 * {@link Player} who makes the move.
 *
 * BoardMoveMaker is instantiated with a reference to a GameBoard which contains all {@link boardGameLibrary.boardGame.pawn.Pawn}
 * relevant to the game.
 */
public abstract class BoardMoveMaker{

    protected GameBoard board;

    /**
     * A object property for notifying of a move made. The move may be an illegal move, but that is
     * made obvious inside the contained {@link BoardMoveEvent}.
     */
    private ObjectProperty<BoardMoveEvent> boardMoveEventObjectProperty = new SimpleObjectProperty<>();

    public BoardMoveMaker(GameBoard board){
        this.board = board;
    }

    /**
     * Returns a boolean telling whether a given {@link Move} for a given {@link Player} was legal.
     * @param player
     * @param move
     * @return
     */
    public abstract boolean isLegalMove(Player player, Move move);

    /**
     * Returns an array of available legal moves in a current board situation.
     *
     * API for method is not set and is not functioning.
     * @return
     */
    public abstract Move[] getAvailableMoves();

    /**
     * Given a {@link Player} and a {@link Move}, this method will lock the makeMove method with the key field.
     *
     * A move will be made on the {@link GameBoard} field if the given {@link Move} is legal.
     *
     * The field {@link #boardMoveEventObjectProperty} will be notified when a new move has been made.
     * @param player
     * @param move
     */
    public abstract void makeMove(Player player, Move move);

    /**
     * Set start positions on game board, given a number of starting players.
     *
     * Implementation is specified by inheritor.
     * @param players
     */
    public abstract void setStartPawns(Player[] players);

    public ObjectProperty<BoardMoveEvent> getBoardMoveEventObjectProperty(){
        return this.boardMoveEventObjectProperty;
    }

    protected void setMadeMove(boolean wasLegalMove){
        System.out.println("Move Made! Move was " + (wasLegalMove ? "legal" : "not legal"));
        this.getBoardMoveEventObjectProperty().set(new BoardMoveEvent(wasLegalMove));
    }

    public GameBoard getGameBoard(){
        return this.board;
    }

}
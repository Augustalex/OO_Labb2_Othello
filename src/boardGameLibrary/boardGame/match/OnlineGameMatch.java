package boardGameLibrary.boardGame.match;

import boardGameLibrary.boardGame.board.BoardMoveMaker;
import boardGameLibrary.boardGame.move.CalculatedMove;
import boardGameLibrary.eventWrappers.CellClickEvent;
import boardGameLibrary.players.Player;
import javafx.beans.property.ObjectProperty;

import java.util.ArrayList;

/**
 * Created by August on 2016-10-19.
 */
public class OnlineGameMatch implements GameMatch {

    @Override
    public void run() {

    }

    @Override
    public void turn(Player player) {

    }

    @Override
    public BoardMoveMaker getBoardMoveMaker() {
        return null;
    }

    @Override
    public ObjectProperty<CellClickEvent> cellClickProperty() {
        return null;
    }

    @Override
    public ObjectProperty<ArrayList<CalculatedMove>> legalMovesProperty() {
        return null;
    }
}

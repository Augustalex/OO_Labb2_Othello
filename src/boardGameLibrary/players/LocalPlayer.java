package boardGameLibrary.players;
import boardGameLibrary.boardGame.board.BoardMoveMaker;
import boardGameLibrary.boardGame.move.CalculatedMove;
import boardGameLibrary.boardGame.move.Move;
import boardGameLibrary.eventWrappers.CellClickEvent;
import boardGameLibrary.eventWrappers.PlayerMadeMoveEvent;
import boardGameLibrary.players.changeListeners.CellClickListener;
import boardGameLibrary.players.changeListeners.MadeMoveListener;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by August on 2016-09-30.
 */
public class LocalPlayer extends Player {

    public LocalPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Establishes a separate {@link Thread} that will wait for a {@link CellClickEvent} and
     * then create a {@link Move}. This will then be used to apply it to the {@link BoardMoveMaker}.
     * @param boardMoveMaker Used to apply the gathered {@link Move}.
     * @param cellClickProperty Used to create a new {@link Move}.
     * @param legalMovesProperty
     */
    @Override
    public void makeMove(BoardMoveMaker boardMoveMaker, ObjectProperty<CellClickEvent> cellClickProperty, ObjectProperty<ArrayList<CalculatedMove>> legalMovesProperty) {
        ObjectProperty<PlayerMadeMoveEvent> madeMoveProperty = new SimpleObjectProperty<>();

        madeMoveProperty.addListener(new MadeMoveListener(boardMoveMaker));

        //Establishing the Thread that will collect a Move from e CellClickEvent.
        new Thread(() -> {

            Platform.runLater(() -> legalMovesProperty.set(boardMoveMaker.getAvailableMoves(this)));
            //Attaching a one time listener.
            cellClickProperty.addListener(new CellClickListener(madeMoveProperty, this));

        }).start();
    }

}

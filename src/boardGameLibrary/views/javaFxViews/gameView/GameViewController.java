package boardGameLibrary.views.javaFxViews.gameView;

import boardGameLibrary.boardGame.board.GameBoard;
import boardGameLibrary.boardGame.match.GameMatch;
import boardGameLibrary.eventWrappers.CellChangeEvent;
import boardGameLibrary.eventWrappers.CellClickEvent;
import boardGameLibrary.boardGame.pawn.Pawn;
import boardGameLibrary.viewModel.gameBoard.cell.Cell;
import boardGameLibrary.viewModel.gameBoard.GameBoardFactory;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import boardGameLibrary.views.javaFxViews.FXMLViewController;

import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import boardGameLibrary.viewModel.gameBoard.cellMarker.CellMarker;

/**
 * Created by August on 2016-09-29.
 */
public class GameViewController extends FXMLViewController implements Initializable{
    
    private static final String fxmlFileName = "GameView.fxml";

    private GameMatch match;

    @FXML
    private HBox gameBoardContainer;
    
    public GameViewController(GameMatch match){
        super(GameViewController.fxmlFileName);

        this.match = match;
    }

    @Override
    public void loadViewInto(Pane container) {
        this.loadFXLMInto(this.getClass(), this, container);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Game board boundaries
        Dimension boundaries = this.match.getBoardMoveMaker().getGameBoard().getBoundaries();

        //Creating boardGame board
        Pane gameBoard = GameBoardFactory.createBoard(gameBoardContainer, boundaries.width);

        gameBoardContainer.getChildren().setAll(gameBoard);

        //Fixing boardGame board dimension properties
        gameBoardContainer.widthProperty().addListener((e) -> squareBindTo(gameBoard, gameBoardContainer));
        gameBoardContainer.heightProperty().addListener((e) -> squareBindTo(gameBoard, gameBoardContainer));

        squareBindTo(gameBoard, gameBoardContainer);

        // Doing boardGame logic here, hellooo, gosh.
        listenToGameBoardView(gameBoard, this.match.getBoardMoveMaker().getGameBoard(), match.cellClickProperty());
        bindViewToGameBoardModel(this.match.getBoardMoveMaker().getGameBoard(), gameBoard);

        match.run();
    }

    public void bindViewToGameBoardModel(GameBoard board, Pane boardView){
        Dimension boundaries = board.getBoundaries();
        ObservableList<Node> viewCells = boardView.getChildren();

        board.getCellChangeObserver().addListener((ListChangeListener.Change<? extends CellChangeEvent> c) -> {

            for(CellChangeEvent cellChange : c.getList()){

                Point position = cellChange.getPosition();

                System.out.println("FLIPPED PAWN AT " + position);

                Pawn pawn = board.getPawn(position);
                Cell cell = (Cell) viewCells.get((position.y * boundaries.width) + position.x);

                Shape shape = pawn.getDisplayModel().getShape();
                shape.setFill(pawn.getDisplayModel().getPaint());

                cell.markCell(CellMarker.createCellMarker(shape));

            }

        });
    }

    private void fixedBindTo(Region binder, Region container){
        binder.minWidthProperty().bind(container.widthProperty());
        binder.maxWidthProperty().bind(container.widthProperty());

        binder.minHeightProperty().bind(container.heightProperty());
        binder.maxHeightProperty().bind(container.heightProperty());
    }
    
    private void squareBindTo(Region binder, Region container){
        if(container.widthProperty().get() > container.heightProperty().get()){
            bindTwoToOneDimensions(binder, container.heightProperty());
        }
        else{
            bindTwoToOneDimensions(binder, container.widthProperty());
        }
    }
    
    private void bindTwoToOneDimensions(Region binder, ReadOnlyDoubleProperty bindTo){
        bindOneToOneDimension(binder.minWidthProperty(), binder.maxWidthProperty(), bindTo);
        bindOneToOneDimension(binder.minHeightProperty(), binder.maxHeightProperty(), bindTo);
    }
    
    private void bindOneToOneDimension(DoubleProperty minDimension, DoubleProperty maxDimension, ReadOnlyDoubleProperty bindTo){
        minDimension.unbind();
        minDimension.bind(bindTo);

        maxDimension.unbind();
        maxDimension.bind(bindTo);
    }

    private void listenToGameBoardView(Pane boardView, GameBoard board, ObjectProperty<CellClickEvent> cellClickProperty){
        ObservableList<Node> viewCells = boardView.getChildren();

        Dimension boundaries = board.getBoundaries();

        for(int y = 0; y < boundaries.height; y++){
            for(int x = 0; x < boundaries.width; x++){
                final Point position = new Point(x, y);
                viewCells.get((y*boundaries.width) + x).setOnMouseClicked(e -> {
                    System.out.println("CLICK: " + position.x + ", " + position.y);
                    cellClickProperty.set(new CellClickEvent(position));
                });

                Cell cell = (Cell) viewCells.get((y*boundaries.width) + x);
                Shape shape = board.getPawn(position).getDisplayModel().getShape();
                shape.setFill(board.getPawn(position).getDisplayModel().getPaint());
                cell.markCell(CellMarker.createCellMarker(shape));
            }
        }

    }

}

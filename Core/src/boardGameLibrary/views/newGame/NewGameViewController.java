package boardGameLibrary.views.newGame;

import boardGameLibrary.boardGame.match.MatchSetup;
import boardGameLibrary.playerProfileStore.PlayerProfileStore;
import boardGameLibrary.players.LocalPlayer;
import boardGameLibrary.players.Player;
import boardGameLibrary.views.playerSelection.NumberOfPlayersSelection;
import boardGameLibrary.views.playerSelection.PlayerSelectionPane;
import boardGameLibrary.views.FXMLViewController;
import communication.ClientMessageCompiler;
import communication.OpenGameDirectoryClientProxy;
import hostDetails.Host;
import javafx.application.Platform;
import othello.players.GreedyAI;
import othello.players.NaturalAI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utilities.router.Router;

import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
/**
 * Created by August on 2016-09-29.
 */
public class NewGameViewController extends FXMLViewController{

    private static final String fxmlFileName = "NewGameView.fxml";

    private PlayerProfileStore store;

    private Player[] players;

    private NumberOfPlayersSelection numberOfPlayersSelection;
    private PlayerSelectionPane selectionPane = null;

    @FXML
    private VBox newGameContainer;

    @FXML
    private Button back;

    public NewGameViewController(Pane container, PlayerProfileStore store) {
        super(container, NewGameViewController.fxmlFileName);
//
//        try {
//            this.server = new GameServer(1337);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        this.store = store;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButtonActions();

        this.players = getAvailablePlayers();

        this.numberOfPlayersSelection = new NumberOfPlayersSelection(16);
        this.newGameContainer.getChildren().add(numberOfPlayersSelection);


        numberOfPlayersSelection.selectedValue().addListener(e -> {
            try {
                Player player = new LocalPlayer("Hoster:" + System.currentTimeMillis(), Color.BEIGE);
                Host host = new Host(Inet4Address.getLocalHost().getHostAddress(), (int)(2000 + System.currentTimeMillis()%1000));

                OpenGameDirectoryClientProxy.get()
                    .logOnPlayer(host, player, numberOfPlayersSelection.selectedValue().get())
                    .onDelivery(playerDetailsList -> {
                        Player[] players = playerDetailsList.stream().map(
                                ClientMessageCompiler::serverToClientPlayerConverter
                        ).toArray(Player[]::new);

                        Platform.runLater(() -> {
                            setupPlayerSelectionPane(players, numberOfPlayersSelection.selectedValue().get());
                            viewPlayerSelectionUI();
                        });
                    });
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        });


    }

    private void setupPlayerSelectionPane(Player[] playerOptions, int numberOfPlayers){
        LobbyConfiguration lobbyConfiguration = new LobbyConfiguration(null, null, numberOfPlayers);
        this.selectionPane = new PlayerSelectionPane(playerOptions, lobbyConfiguration);
        //
        // this.selectionPane.setSelectionController(new OnlinePlayerSelectionPaneController(this.server));

        //Sets up what happens when all players are selected.
        this.selectionPane
                .chosenPlayersProperty()
                .onAllPlayersSelected((observable, oldValue, newValue) -> {
                    routeToGameView(
                            this.selectionPane
                                    .chosenPlayersProperty()
                                    .removeAndReturnAllSelectedPlayers()
                    );
                });
    }

    private void viewPlayerSelectionUI(){
        this.newGameContainer.getChildren().remove(this.getSelectionPane());
        this.newGameContainer.getChildren().add(selectionPane);
    }

    private void routeToGameView(Player[] chosenPlayers){
        Router.getApplicationRouter().route("GameView", createGameSettings(chosenPlayers));
    }

    private Map<String, Object> createGameSettings(Player[] chosenPlayers){
        Map<String, Object> map = new HashMap<>();
        map.put("MatchSetup", createMatchSetup(chosenPlayers));

        return map;
    }

    private MatchSetup createMatchSetup(Player[] chosenPlayers){
        return new MatchSetup(
                "Othello",
                getSelectionPane().chosenPlayersProperty().removeAndReturnAllSelectedPlayers()
        );
    }
    private void setupButtonActions(){
        back.setOnAction((e)-> Router.getApplicationRouter().previous());
    }

    private Player[] getAvailablePlayers() {
        Player[] p = this.store.toPlayers();

        List<Player> allPlayers = new ArrayList<>();
        Collections.addAll(allPlayers, p);
        allPlayers.add(new NaturalAI("Jacob", Color.BURLYWOOD));
        allPlayers.add(new GreedyAI("Patrick", Color.RED));
        allPlayers.add(new GreedyAI("Felix", Color.ALICEBLUE));
        allPlayers.add(new NaturalAI("Mackan", Color.TURQUOISE));

       // Player[] activePlayers = this.server.activePlayersManager().getActivePlayers();

      //  allPlayers.addAll(Stream.of(activePlayers).collect(Collectors.toList()));

        return allPlayers.stream().toArray(Player[]::new);
    }

    public void setSelectionPane(PlayerSelectionPane pane){
        this.selectionPane = pane;
    }

    public PlayerSelectionPane getSelectionPane(){
        return this.selectionPane;
    }

}


        /*Player[] players = new Player[]{
            new LocalPlayer("August", Color.WHITE),
            new LocalPlayer("Björn", Color.BLACK),
            new NaturalAI("Jacob", Color.BURLYWOOD),
            new NaturalAI("Nick", Color.YELLOW),
            new NaturalAI("Bosco", Color.ORANGE),
            new GreedyAI("Elvir", Color.BLUE),
            new RandomAI("Mackan", Color.TURQUOISE),
            new RandomAI("Carlos", Color.DARKCYAN),
            new GreedyAI("Patrick", Color.RED),
            new NaturalAI("Simon", Color.PURPLE),
            new RandomAI("Viktor", Color.ANTIQUEWHITE),
            new GreedyAI("Victor", Color.ALICEBLUE),
            new NaturalAI("Robin", Color.AZURE),
            new NaturalAI("Johan", Color.CADETBLUE),
            new NaturalAI("Mössjohan", Color.DARKMAGENTA),
            new NaturalAI("Okan", Color.PINK),
            new NaturalAI("Alex", Color.DEEPPINK),
            new NaturalAI("Sebastian", Color.YELLOWGREEN),
            new NaturalAI("Andreas", Color.TOMATO),
            new NaturalAI("Kristoffer", Color.THISTLE)

        };*/

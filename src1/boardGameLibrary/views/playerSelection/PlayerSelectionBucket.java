package boardGameLibrary.views.playerSelection;

import boardGameLibrary.players.Player;
import boardGameLibrary.viewComponents.Bucket;
import javafx.beans.value.ChangeListener;

/**
 * Created by August on 2016-10-24.
 */
public class PlayerSelectionBucket extends Bucket<Player> {

    private ChangeListener<Boolean> allPlayersSelectedHandler = null;
    /**
     * Takes the max capacity of the Bucket as an argument. This capacity
     * is immutable.
     *
     * @param capacity Integer for max capacity of new Bucket.
     */
    public PlayerSelectionBucket(int capacity) {
        super(capacity);

        this.onIsFull((observable, oldValue, newValue) -> {
            PlayerSelectionBucket.this.allPlayersSelectedHandler.changed(
                    null, false, true);
        });
    }

    public void onAllPlayersSelected(ChangeListener<Boolean> listener){
        this.allPlayersSelectedHandler = listener;
    }

    public Player[] removeAndReturnAllSelectedPlayers(){
        Player[] players = this.pour().toArray(new Player[this.capacity]);

        for(Player player: players){
           System.out.println(player.getName());
        }

        System.out.println("HERE \t\tHERE\t\tHERE\t\tHERE");
        return players;
    }

}

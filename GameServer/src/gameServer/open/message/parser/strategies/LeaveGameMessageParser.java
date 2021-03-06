package gameServer.open.message.parser.strategies;

import gameServer.gameEntities.PlayerDetails;
import gameServer.message.parser.IMessageParser;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by August on 2017-01-19.
 */
public class LeaveGameMessageParser implements IMessageParser<Pair<String, PlayerDetails>> {
    private List<String> input;

    private static final int gameIdIndex = 1;

    @Override
    public List<String> getInput() {
        return this.input;
    }

    @Override
    public LeaveGameMessageParser setInput(List<String> input) {
        this.input = input;
        return this;
    }

    @Override
    public boolean isValid() {
        return getInput().size() > 1
                && getInput().get(0).equals("LEAVE");
    }

    @Override
    public Pair<String, PlayerDetails> parse() {
        String gameId = getInput().get(gameIdIndex);

        PlayerDetails player = new PlayerDetailsParser()
                .setInput(getInput().subList(gameIdIndex+1,getInput().size()))
                .parse();

        return new Pair<>(gameId, player);
    }
}

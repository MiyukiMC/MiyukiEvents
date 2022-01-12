package app.miyuki.bukkit.game;

public interface Game<W> {

    String getName();

    void start();

    void stop();

    void onWin(W w);

    void giveReward(W w);

}

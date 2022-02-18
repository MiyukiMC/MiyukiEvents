package app.miyuki.miyukievents.bukkit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGameHistory {

    private final String gameName;
    private int wins;
    private int defeats;
    private int kills;
    private int deaths;

    public int getParticipation() {
        return wins + defeats;
    }

    public double getKDR() {
        if (kills == 0)
            return kills;
        double kdr = (double) kills / (double) deaths;
        return Math.round(kdr * 100.0) / 100.0;
    }

    public double getWinRate() {
        if (wins == 0)
            return wins;
        double winRate = (double) wins / (double) defeats;
        return Math.round(winRate * 10000.0) / 100.0;
    }


}

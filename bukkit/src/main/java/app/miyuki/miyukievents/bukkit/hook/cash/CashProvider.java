package app.miyuki.miyukievents.bukkit.hook.cash;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.Getter;

public class CashProvider {

    @Getter
    private final CashAPI cashAPI = null;

    public CashProvider(MiyukiEvents plugin) {

    }

    public boolean hook() {
        return false;
    }

}

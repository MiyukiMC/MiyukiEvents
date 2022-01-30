package app.miyuki.miyukievents.bukkit.hook.chat.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.chat.ChatHook;
import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class LegendChat extends ChatHook<ChatMessageEvent> {

    public LegendChat(MiyukiEvents plugin) {
        super(plugin);
    }

    @Override
    public void onPlayerChat(ChatMessageEvent event) {

    }

}

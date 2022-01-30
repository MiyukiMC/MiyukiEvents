package app.miyuki.miyukievents.bukkit.hook.chat.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.chat.ChatHook;
import com.nickuc.chat.api.events.PublicMessageEvent;

public class NChat extends ChatHook<PublicMessageEvent> {

    public NChat(MiyukiEvents plugin) {
        super(plugin);
    }

    @Override
    public void onPlayerChat(PublicMessageEvent event) {

    }

}

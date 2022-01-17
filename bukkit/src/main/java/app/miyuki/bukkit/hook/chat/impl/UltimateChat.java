package app.miyuki.bukkit.hook.chat.impl;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.hook.chat.ChatHook;
import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;

public class UltimateChat extends ChatHook<SendChannelMessageEvent> {

    public UltimateChat(MiyukiEvents plugin) {
        super(plugin);
    }

    @Override
    public void onPlayerChat(SendChannelMessageEvent event) {

    }

}

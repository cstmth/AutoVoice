package de.carldressler.autovoice.listeners;

import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelEventListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {

    }
}

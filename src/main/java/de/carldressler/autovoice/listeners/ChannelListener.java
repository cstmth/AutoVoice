package de.carldressler.autovoice.listeners;

import de.carldressler.autovoice.utilities.Logging;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ChannelListener extends ListenerAdapter {
    private final Logger logger = Logging.getLogger(this.getClass());

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {

    }
}

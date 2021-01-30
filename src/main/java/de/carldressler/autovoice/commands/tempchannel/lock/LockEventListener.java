package de.carldressler.autovoice.commands.tempchannel.lock;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LockEventListener extends ListenerAdapter {
    private static final Map<String, Set<String>> lockedChannels = new HashMap<>();

    public boolean isAllowed(Member member, VoiceChannel channel) {
        Set<String> allowedUsers = lockedChannels.get(channel.getId());
        String memberId = member.getId();

        return allowedUsers == null || allowedUsers.contains(memberId);
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        proceed(event.getMember(), event.getChannelJoined());
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        proceed(event.getMember(), event.getChannelJoined());
    }

    private void proceed(Member member, VoiceChannel voiceChannel) {
        if (isAllowed(member, voiceChannel)) {

        } else {
        }
    }
}

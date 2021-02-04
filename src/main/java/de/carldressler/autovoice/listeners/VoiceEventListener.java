package de.carldressler.autovoice.listeners;

import de.carldressler.autovoice.entities.auto.AutoChannel;
import de.carldressler.autovoice.entities.auto.AutoChannelManager;
import de.carldressler.autovoice.entities.temp.TempChannelManager;
import de.carldressler.autovoice.utilities.CooldownManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO => Replace AutoChannelManager with AutoChannelMgr
public class VoiceEventListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        AutoChannel autoChannel = AutoChannelManager.get(event.getChannelJoined());

        createTempChannelCheck(autoChannel, event.getChannelJoined(), event.getMember());
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if (CooldownManager.isOnCooldown(event.getMember().getUser(), false))
            return;

        AutoChannel autoChannel = AutoChannelManager.get(event.getChannelJoined());

        createTempChannelCheck(autoChannel, event.getChannelJoined(), event.getMember());
        deleteTempChannelCheck(autoChannel, event.getChannelLeft());
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        AutoChannel autoChannel = AutoChannelManager.get(event.getChannelLeft());

        deleteTempChannelCheck(autoChannel, event.getChannelLeft());
    }

    private boolean createTempChannelCheck(AutoChannel autoChannel, VoiceChannel channelJoined, Member member) {
        if (autoChannel == null || channelJoined.getParent() == null)
            return false;

            if (autoChannel.getChannel().getId().equals(channelJoined.getId())) {
                TempChannelManager.setupChannel(autoChannel, member);
                CooldownManager.cooldownUser(member.getUser());
                return true;
            }
        return false;
    }

    private void deleteTempChannelCheck(AutoChannel autoChannel, VoiceChannel channelLeft) {
        if (autoChannel == null || channelLeft.getParent() == null)
            return;

        if (autoChannel.getCategory().getId().equals(channelLeft.getParent().getId()) &&
                channelLeft.getMembers().isEmpty())
                channelLeft.delete().queue(suc -> {}, err -> {});
    }
}

/*
Set<String> autoChannelCategoryIds = new HashSet<>();
        Set<String> autoChannelIds = new HashSet<>();

        if (autoChannelSet == null || channelLeft.getParent() == null) {
            return;
        }
        for (AutoChannel ac : autoChannelSet) {
            Category category = ac.getChannel().getParent();
            String id = ac.getId();

            if (category == null) {
                return;
            }
            autoChannelCategoryIds.add(category.getId());
            autoChannelIds.add(id);
        }

        if (autoChannelCategoryIds.contains(channelLeft.getParent().getId()) &&
            !autoChannelIds.contains(channelLeft.getId()) &&
            channelLeft.getMembers().isEmpty()) {
            TempChannelManager.teardownChannel(channelLeft);
        }
 */
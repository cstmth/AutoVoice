package de.carldressler.autovoice.listeners;

import de.carldressler.autovoice.database.AutoChannel;
import de.carldressler.autovoice.managers.AutoChannelManager;
import de.carldressler.autovoice.managers.TempChannelManager;
import de.carldressler.autovoice.utilities.Logging;
import de.carldressler.autovoice.utilities.cooldown.CooldownManager;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VoiceListener extends ListenerAdapter {
    private final Logger logger = Logging.getLogger(this.getClass());
    private final Map<User, Long> tempChannelMoveCooldown = new HashMap<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        logger.debug("GuildVoiceJOIN event fired in " + event.getGuild().getName());
        if (CooldownManager.isOnCooldown(event.getMember().getUser(), true)) {
            return;
        }

        Set<AutoChannel> autoChannels = AutoChannelManager.getAutoChannels(event.getGuild());

        checkCreateTempChannel(autoChannels, event.getChannelJoined(), event.getMember());
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        logger.debug("GuildVoiceMOVE event fired in " + event.getGuild().getName());

        if (
                tempChannelMoveCooldown.get(event.getMember().getUser()) == null ||
                tempChannelMoveCooldown.get(event.getMember().getUser()) < System.currentTimeMillis() + 3000L ||
                CooldownManager.isOnCooldown(event.getMember().getUser(), true)
        ) {
            return;
        }

        Set<AutoChannel> autoChannels = AutoChannelManager.getAutoChannels(event.getGuild());

        checkCreateTempChannel(autoChannels, event.getChannelJoined(), event.getMember());
        checkForDeletion(autoChannels, event.getChannelLeft());
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        logger.debug("GuildVoiceLEAVE event fired in " + event.getGuild().getName());

        Set<AutoChannel> autoChannels = AutoChannelManager.getAutoChannels(event.getGuild());

        checkForDeletion(autoChannels, event.getChannelLeft());
    }

    public void checkCreateTempChannel(Set<AutoChannel> autoChannels, VoiceChannel channelJoined, Member member) {
        if (autoChannels == null)
            return;
        for (AutoChannel ac : autoChannels) {
            String id = ac.getChannelId();
            if (channelJoined.getId().equals(id)) {
                TempChannelManager.createChannelAndMoveUser(ac, member);
                logger.debug(member.getEffectiveName() + " is being put on cooldown for 3s");
                tempChannelMoveCooldown.put(member.getUser(), System.currentTimeMillis());
                CooldownManager.cooldownUser(member.getUser());
            }
        }
    }

    public void checkForDeletion(Set<AutoChannel> autoChannels, VoiceChannel channelLeft) {
        Set<String> autoChannelCategoryIds = new HashSet<>();
        Set<String> autoChannelIds = new HashSet<>();

        if (autoChannels == null || channelLeft.getParent() == null) {
            return;
        }
        for (AutoChannel ac : autoChannels) {
            Category category = ac.getVoiceChannel().getParent();
            String id = ac.getChannelId();

            if (category == null) {
                return;
            }
            autoChannelCategoryIds.add(category.getId());
            autoChannelIds.add(id);
        }

        if (
            autoChannelCategoryIds.contains(channelLeft.getParent().getId()) &&
            !autoChannelIds.contains(channelLeft.getId()) &&
            channelLeft.getMembers().isEmpty()
        ) {
            TempChannelManager.deleteChannel(channelLeft);
        }
    }
}

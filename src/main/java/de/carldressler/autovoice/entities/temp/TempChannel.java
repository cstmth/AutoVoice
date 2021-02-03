package de.carldressler.autovoice.entities.temp;

import de.carldressler.autovoice.entities.AutoChannel;
import de.carldressler.autovoice.managers.AutoChannelMgr;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TempChannel {
    private final String channelId;
    private final VoiceChannel channel;
    private final Category category;
    private final Guild guild;
    private final Member creator;
    private final AutoChannel autoChannel;

    public TempChannel(VoiceChannel voiceChannel, String creatorId) {
        this.channelId = voiceChannel.getId();
        this.channel = voiceChannel;
        this.category = channel.getParent();
        this.guild = channel.getGuild();
        this.creator = guild.getMemberById(creatorId);
        this.autoChannel = AutoChannelMgr.get(voiceChannel);
    }

    public String getChannelId() {
        return channelId;
    }

    public VoiceChannel getChannel() {
        return channel;
    }

    public Category getCategory() {
        return category;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getCreator() {
        return creator;
    }

    public AutoChannel getAutoChannel() {
        return autoChannel;
    }
}

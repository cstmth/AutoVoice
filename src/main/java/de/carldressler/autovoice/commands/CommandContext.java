package de.carldressler.autovoice.commands;

import de.carldressler.autovoice.database.entities.AutoChannel;
import de.carldressler.autovoice.managers.AutoChannelManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Set;

public class CommandContext {
    public GuildMessageReceivedEvent event;
    public Command command;
    public String invocator;
    public List<String> args;
    public TextChannel channel;
    public User user;
    public Member member;
    public Guild guild;
    public Category voiceChannelCategory;
    public VoiceChannel voiceChannel;
    public Set<AutoChannel> autoChannelSet;
    public AutoChannel autoChannel;

    public CommandContext(GuildMessageReceivedEvent event, String invocator, List<String> args) {
        this.event = event;
        this.invocator = invocator;
        this.args = args;
        this.channel = event.getChannel();
        this.user = event.getAuthor();
        this.member = event.getMember();
        this.guild = event.getGuild();
        if (event.getMember() == null || event.getMember().getVoiceState() == null || event.getMember().getVoiceState().getChannel() == null)
            this.voiceChannel = null;
        else
            this.voiceChannel = event.getMember().getVoiceState().getChannel();

        if (voiceChannel == null || voiceChannel.getParent() == null)
            this.voiceChannelCategory = null;
        else
            this.voiceChannelCategory = voiceChannel.getParent();

        autoChannelSet = null;
    }

    public void setAutoChannelProperties() {
        // Step 1 - Setting autoChannelSet
        autoChannelSet = AutoChannelManager.getAutoChannelSet(guild);

        // Step 2 - Setting autoChannel (dependent on callee current voice category)
        if (voiceChannelCategory != null) {
            for (AutoChannel ac : autoChannelSet) {
                if (ac.getCategory().getId().equals(voiceChannelCategory.getId())) {
                    autoChannel = ac;
                }
            }
        }
    }
}

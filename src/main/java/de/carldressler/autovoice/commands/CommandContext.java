package de.carldressler.autovoice.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext {
    public GuildMessageReceivedEvent event;
    public Command command;
    public String alias;
    public List<String> args;
    public TextChannel channel;
    public User user;
    public Member member;
    public Guild guild;
    public VoiceChannel voiceChannel;
    public VoiceChannel autoChannel;

    public CommandContext(GuildMessageReceivedEvent event, String alias, List<String> args) {
        this.event = event;
        this.alias = alias;
        this.args = args;
        this.channel = event.getChannel();
        this.user = event.getAuthor();
        this.member = event.getMember();
        this.guild = event.getGuild();
        if (event.getMember() == null || event.getMember().getVoiceState() == null || event.getMember().getVoiceState().getChannel() == null)
            this.voiceChannel = null;
        else
            this.voiceChannel = event.getMember().getVoiceState().getChannel();
    }

}

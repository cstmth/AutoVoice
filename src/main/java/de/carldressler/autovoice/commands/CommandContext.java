package de.carldressler.autovoice.commands;

import de.carldressler.autovoice.entities.auto.AutoChannel;
import de.carldressler.autovoice.entities.temp.TempChannel;
import de.carldressler.autovoice.entities.auto.AutoChannelManager;
import de.carldressler.autovoice.entities.temp.TempChannelManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext {
    public GuildMessageReceivedEvent event;
    public JDA jda;
    public String invocator;
    public Message message;
    public List<String> args;
    public Command command;

    public TextChannel textChannel;
    public User user;
    public Member member;
    public Guild guild;
    public VoiceChannel voiceChannel;

    public AutoChannel autoChannel;
    public TempChannel tempChannel;

    public CommandContext(GuildMessageReceivedEvent event, String invocator, List<String> args) {
        this.jda = event.getJDA();
        this.event = event;
        this.invocator = invocator;
        this.message = event.getMessage();
        this.args = args;
        this.textChannel = event.getChannel();
        this.user = event.getAuthor();
        this.member = event.getMember();
        this.guild = event.getGuild();
        if (event.getMember() == null || event.getMember().getVoiceState() == null || event.getMember().getVoiceState().getChannel() == null) {
            this.voiceChannel = null;
        } else {
            this.voiceChannel = event.getMember().getVoiceState().getChannel();

            AutoChannel autoChannel = AutoChannelManager.get(voiceChannel);
            TempChannel tempChannel = TempChannelManager.get(voiceChannel);

            if (autoChannel != null) {
                this.autoChannel = autoChannel;
            }
            if (tempChannel != null) {
                this.tempChannel = tempChannel;
            }
        }
    }
}

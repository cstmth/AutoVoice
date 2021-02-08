package de.carldressler.autovoice.listeners;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.autochannel.EmojiCommand;
import de.carldressler.autovoice.commands.autochannel.SetupCommand;
import de.carldressler.autovoice.commands.misc.*;
import de.carldressler.autovoice.commands.tempchannel.DeleteCommand;
import de.carldressler.autovoice.commands.tempchannel.LimitCommand;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {
    static final Map<String, Command> commandMap = new LinkedMap<>();
    static final Map<String, String> aliasMap = new LinkedMap<>();

    static {
        commandMap.put("about", new AboutCommand());
        commandMap.put("delete", new DeleteCommand()); // TODO add to help (and general page)
        commandMap.put("dm", new DmCommand());
        commandMap.put("emoji", new EmojiCommand());
        commandMap.put("help", new HelpCommand());
        commandMap.put("invite", new InviteCommand());
        commandMap.put("limit", new LimitCommand());
        commandMap.put("setup", new SetupCommand());
        commandMap.put("support", new SupportCommand());
        commandMap.put("uptime", new UptimeCommand());

        aliasMap.put("create", "setup");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith(Constants.PREFIX))
            return;

        String messageContent = event.getMessage().getContentRaw();
        List<String> messageSplitted = getSplittedMessage(messageContent);
        String command = messageSplitted.get(0);
        List<String> args = messageSplitted.subList(1, messageSplitted.size());
        CommandContext ctxt = new CommandContext(event, command, args);
        
        if (commandMap.containsKey(ctxt.invocator.toLowerCase()) || aliasMap.containsKey(ctxt.invocator.toLowerCase()))
            callCommand(ctxt);
        else if (messageContent.length() != 1)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.INVALID_COMMAND);
    }

    static void callCommand(CommandContext ctxt) {
        Command command = commandMap.get(ctxt.invocator.toLowerCase());
        if (command == null)
            command = commandMap.get(aliasMap.get(ctxt.invocator.toLowerCase()));
        command.process(ctxt);
    }

    static List<String> getSplittedMessage(String message) {
        String noPrefix = message.substring(Constants.PREFIX.length()).trim();
        return List.of(noPrefix.split(" "));
    }
}

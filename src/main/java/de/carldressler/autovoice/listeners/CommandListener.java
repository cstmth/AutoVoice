package de.carldressler.autovoice.listeners;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.autochannel.GetAutoChannelCommand;
import de.carldressler.autovoice.commands.autochannel.SetupCommand;
import de.carldressler.autovoice.commands.misc.AboutCommand;
import de.carldressler.autovoice.commands.misc.InviteCommand;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CommandListener extends ListenerAdapter {
    static final Map<String, Command> commandMap = new LinkedMap<>();
    static final Map<String, String> aliasMap = new LinkedMap<>();

    static {
        commandMap.put("invite", new InviteCommand());
        commandMap.put("about", new AboutCommand());
        commandMap.put("setup", new SetupCommand());
        commandMap.put("gac", new GetAutoChannelCommand());

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
        
        if (commandMap.containsKey(ctxt.alias) || aliasMap.containsKey(ctxt.alias))
            callCommand(ctxt);
        else
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.INVALID_COMMAND);
    }

    static void callCommand(CommandContext ctxt) {
        Command command = commandMap.get(ctxt.alias);
        if (command == null)
            command = commandMap.get(aliasMap.get(ctxt.alias));
        command.process(ctxt);
    }

    static List<String> getSplittedMessage(String message) {
        String noPrefix = message.substring(Constants.PREFIX.length()).trim();
        return List.of(noPrefix.split(" "));
    }
}

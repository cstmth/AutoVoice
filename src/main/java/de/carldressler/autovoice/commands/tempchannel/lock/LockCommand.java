package de.carldressler.autovoice.commands.tempchannel.lock;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LockCommand extends Command {
    LockAddCommand lockAddCommand = new LockAddCommand();
    LockRemoveCommand lockRemoveCommand = new LockRemoveCommand();

    public LockCommand() {
        super("lock",
            "Makes the temporary channel invite-only. Members can join but are removed immediately and placed in a queue until accepted from a channel administrator",
            Collections.singletonList("superlock"),
            false,
            "lock",
            null);

        Map<String, Command> childCommandMap = new HashMap<>();
        childCommandMap.put("add", new LockAddCommand());
        childCommandMap.put("remove", new LockRemoveCommand());
        addChildCommands(childCommandMap);
    }

    @Override
    public void run(CommandContext ctxt) {

    }

    public void superlockChannel(VoiceChannel channel) {
    }
}

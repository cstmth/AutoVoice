package de.carldressler.autovoice.commands.tempchannel.lock;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;

public class LockRemoveCommand extends Command {
    public LockRemoveCommand() {
        super("lock remove",
            "Removes a user from a locked channel and revokes the access privileges",
            null,
            false,
            "lock remove <mention|ID>",
            "lock remove @CARL#0001");
    }

    @Override
    public void run(CommandContext ctxt) {

    }
}

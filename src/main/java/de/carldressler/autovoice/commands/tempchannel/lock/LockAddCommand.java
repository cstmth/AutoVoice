package de.carldressler.autovoice.commands.tempchannel.lock;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;

public class LockAddCommand extends Command {
    public LockAddCommand() {
        super("lock add",
            "Adds a user to a locked channel allowing the user to access it",
            null,
            false,
            "lock add <mention|ID>",
            "lock add @CARL#0001");
    }

    @Override
    public void run(CommandContext ctxt) {
    }
}

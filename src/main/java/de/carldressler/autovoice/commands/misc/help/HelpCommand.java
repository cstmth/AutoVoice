package de.carldressler.autovoice.commands.misc.help;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(
                "help",
                "Displays all commands, what they are for, how to use them and possible pitfalls",
                null,
                false,
                "help (<command name>)",
                "help setup"
        );
        addFlags(CommandFlag.NOT_IMPLEMENTED);
    }

    @Override
    public void run(CommandContext ctxt) {
    }
}

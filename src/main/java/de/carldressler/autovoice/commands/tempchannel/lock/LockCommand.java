package de.carldressler.autovoice.commands.tempchannel.lock;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.database.entities.LockState;
import de.carldressler.autovoice.database.entities.TempChannel;
import de.carldressler.autovoice.managers.TempChannelManager;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockCommand extends Command {

    public LockCommand() {
        super("lock",
            "Makes the temporary channel invite-only. Members can join but are removed immediately and placed in a queue until accepted from a channel administrator",
            List.of("unlock", "superlock"),
            false,
            "lock",
            null);

        Map<String, Command> childCommandMap = new HashMap<>();

        childCommandMap.put("add", new LockAddCommand());
        childCommandMap.put("remove", new LockRemoveCommand());
        addChildCommands(childCommandMap);
        addFlags(CommandFlag.CHANNEL_ADMIN_REQUIRED,
            CommandFlag.COOLDOWN_APPLIES,
            CommandFlag.GUILD_ONLY,
            CommandFlag.USER_IN_TEMP_CHANNEL);
    }

    @Override
    public void run(CommandContext ctxt) {
        TempChannel tempChannel = TempChannelManager.getTempChannel(ctxt.voiceChannel.getId());
        LockState targetLockState = switch (ctxt.invocator) {
            case "lock" -> LockState.LOCKED;
            case "unlock" -> LockState.UNLOCKED;
            case "superlock" -> LockState.SUPERLOCKED;
            default -> throw new RuntimeException("TempChannel#targetLockState could not be set because the passed ctxt.invocator is invalid");
        };

        if (tempChannel.getLockState() == targetLockState) {
            ctxt.channel.sendMessage(getLockStateIdentical(targetLockState)).queue();
            return;
        }

        if (ctxt.invocator.equals("lock")) {
            lockChannel(ctxt, tempChannel);
        } else if (ctxt.invocator.equals("superlock")) {
            superlockChannel(ctxt, tempChannel);
        } else {
            unlockChannel(ctxt, tempChannel);
        }
    }

    public void lockChannel(CommandContext ctxt, TempChannel tempChannel) {
        tempChannel.setLockState(LockState.LOCKED);
        tempChannel.setLockBypass(true, ctxt.member);
        ctxt.channel.sendMessage(getLocked()).queue();
    }

    public void superlockChannel(CommandContext ctxt, TempChannel tempChannel) {
        tempChannel.setLockState(LockState.SUPERLOCKED);
        tempChannel.setLockBypass(true, ctxt.member);
        ctxt.channel.sendMessage(getSuperlocked()).queue();
    }

    public void unlockChannel(CommandContext ctxt, TempChannel tempChannel) {
        tempChannel.setLockState(LockState.UNLOCKED);
        ctxt.channel.sendMessage(getUnlocked()).queue();
    }

    private MessageEmbed getUnlocked() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(CustomEmotes.SUCCESS + "  Unlocked channel: Everybody welcome!")
            .setDescription("The channel has been unlocked. Anyone can now view and join it without having to be approved first.")
            .build();
    }

    private MessageEmbed getLocked() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(CustomEmotes.SUCCESS + "  Channel locked: Invite-only!")
            .setDescription("The channel has been locked and users must be unlocked before they can join.\n" +
                "\n" +
                "To do this, a user can join the channel for a really short time - they will be immediately removed from the channel and moved to a queue channel. The user then needs to be approved via a DM notification every channel admin receives.\n" +
                "\n" +
                "Note: Channel administrators should allow DMs from the bot (`" + Constants.PREFIX + "help dms`), otherwise there will be no indication of waiting people.")
            .build();
    }

    private MessageEmbed getSuperlocked() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(CustomEmotes.SUCCESS + "  Super secretive! This channel is now invisible")
            .setDescription("The channel has been superlocked. Normal users can now no longer view it, let alone join it. Server moderators or administrators, on the other hand, can see it and also join it.\n" +
                "\n" +
                "To grant joining rights to a user, a channel administrator must use the `" + Constants.PREFIX + "lock add <mention|id>` command. The channel becomes visible to unlocked users and they can join - but they are not automatically moved.")
            .build();
    }

    private MessageEmbed getLockStateIdentical(LockState lockState) {
        String lockStateString = switch (lockState) {
            case UNLOCKED -> "unlocked";
            case LOCKED -> "locked";
            case SUPERLOCKED -> "superlocked";
        };

        return new EmbedBuilder()
            .setColor(Constants.ERROR_COLOR)
            .setTitle(CustomEmotes.ERROR + "  The channel is " + lockStateString + "already")
            .setDescription("The channel is already in the targeted condition. No changes have been made.\n" +
                "\n" +
                "You can use the following commands in connection with the lock mechanism:\n" +
                "- " + Constants.PREFIX + "lock\n" +
                "- " + Constants.PREFIX + "unlock\n" +
                "- " + Constants.PREFIX + "superlock\n" +
                "- " + Constants.PREFIX + "lock add <mention|id>\n" +
                "- " + Constants.PREFIX + "lock remove <mention|id>")
            .build();
    }
}

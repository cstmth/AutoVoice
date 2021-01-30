package de.carldressler.autovoice.database.entities;

import de.carldressler.autovoice.database.DB;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class TempChannel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VoiceChannel channel;
    private final Category category;
    private final Guild guild;
    private final Member creator;
    private LockState lockState;
    private AutoChannel autoChannel; // TODO
    private Set<String> allowedMembers;

    public Logger getLogger() {
        return logger;
    }

    public VoiceChannel getChannel() {
        return channel;
    }

    public Category getCategory() {
        return category;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getCreator() {
        return creator;
    }

    public LockState getLockState() {
        return lockState;
    }

    public Set<String> getAllowedMembers() {
        return allowedMembers;
    }

    public TempChannel(VoiceChannel voiceChannel, String creatorId, int lockStateInt) {
        this.channel = voiceChannel;
        this.category = channel.getParent();
        this.guild = channel.getGuild();
        this.creator = guild.getMemberById(creatorId);
        this.lockState = switch (lockStateInt) {
            default -> LockState.UNLOCKED; // case 0
            case 1 -> LockState.LOCKED;
            case 2 -> LockState.SUPERLOCKED;
        };

        if (this.creator == null)
            logger.warn("TempChannel.creator is null (left guild?)");
    }

    public void setLockState(LockState lockState) {
        int lockInt;

        switch (lockState) {
            default -> lockInt = 0;
            case LOCKED -> lockInt = 1;
            case SUPERLOCKED -> lockInt = 2;
        }
        try {
            String sql = """
                UPDATE temp_channels
                SET is_locked = ?
                WHERE channel_id = ?
                """;
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setInt(1, lockInt);
            prepStmt.setString(2, channel.getId());
            DB.executePreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("Could not UPDATE is_locked for temp channel record", err);
        }
        this.lockState = lockState;

        // TODO => Check if this works...
        if (lockState == LockState.SUPERLOCKED) {
            this.getChannel().upsertPermissionOverride(this.getGuild().getPublicRole())
                .setDeny(Permission.VIEW_CHANNEL)
                .queue();
        }
    }

    public void setLockBypass(boolean mayBypassLock, Member member) {
        // TODO => How can we INSERT or UPDATE depending on whether a record exists already?
    }

    public boolean mayJoin(Member member) {
        // TODO => Returns whether a user has permission to join a channel
        return true;
    }

    public void placeIntoQueue(Member member) {
        // TODO => Place the user into the (or a) queue channel
    }
}

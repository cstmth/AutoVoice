package de.carldressler.autovoice.database.entities;

import de.carldressler.autovoice.database.DB;
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
    private boolean isLocked;
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

    public boolean isLocked() {
        return isLocked;
    }

    public Set<String> getAllowedMembers() {
        return allowedMembers;
    }

    public TempChannel(VoiceChannel voiceChannel, String creatorId, boolean isLocked) {
        this.channel = voiceChannel;
        this.category = channel.getParent();
        this.guild = channel.getGuild();
        this.creator = guild.getMemberById(creatorId);
        this.isLocked = isLocked;

        if (this.creator == null)
            logger.warn("TempChannel.creator is null (left guild?)");
    }

    public void changeLockState(boolean doLock) {
        int lockInt = 0;
        if (doLock)
            lockInt = 1;

        try {
            String sql = """
                UPDATE temp_channels
                SET is_locked = ?
                WHERE channel_id = ?
                """;
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setInt(1, lockInt);
            prepStmt.setString(3, channel.getId());
            DB.executePreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("Could not INSERT new temp channel record in database", err);
        }
        isLocked = true;
    }

    public void unlock() {
        // SQL
        isLocked = false;
    }

    public void setLockBypass(boolean mayBypassLock, Member member) {

    }

    public boolean mayJoin(Member member) {
        if (isLocked)
            return allowedMembers.contains(member.getId());
        else
            return true;
    }
}

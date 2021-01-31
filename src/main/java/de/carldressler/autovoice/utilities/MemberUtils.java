package de.carldressler.autovoice.utilities;

import de.carldressler.autovoice.commands.CommandContext;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class MemberUtils {
    /**
     * Note that this method expects the ID, if present, to be positioned at either pos 0 or pos 1 depending on whether the command is a subcommand
     * @param ctxt The associated CommandContext object
     * @return The possibly-null Member object
     */
    static public Member getMemberFromMentionOrId(CommandContext ctxt) {
        List<Member> mention = ctxt.message.getMentionedMembers();
        Member member = null;

        int expectedIdPosition = 0;
        if (ctxt.command.getParentCommand() != null) {
            expectedIdPosition = 1;
        }

        if (ctxt.args.size() == expectedIdPosition - 1) {
            return null;
        } else if (!mention.isEmpty()) {
            // lock add @CARL#0001
            member = mention.get(0);
        } else {
            // lock add 730190870011183271
            member = ctxt.guild.getMemberById(ctxt.args.get(expectedIdPosition));
        }

        return member;
    }
}

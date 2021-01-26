package de.carldressler.autovoice.utilities;

import de.carldressler.autovoice.Bot;
import net.dv8tion.jda.api.entities.Emote;

public class CustomEmotes {
    public static final String LOGO = Bot.instance.getEmoteById("803594608746561546").getAsMention();

    public static final String SUCCESS = Bot.instance.getEmoteById("803594608902799360").getAsMention();
    public static final String ERROR = Bot.instance.getEmoteById("803594609204789298").getAsMention();
    public static final String INFO = Bot.instance.getEmoteById("803594609237295154").getAsMention();

    public static final String INVITE = Bot.instance.getEmoteById("803594608449552394").getAsMention();
}

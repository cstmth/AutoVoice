package de.carldressler.autovoice.utilities;

import de.carldressler.autovoice.Bot;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CustomEmoji {
    public static final String LOGO = Objects.requireNonNull(Bot.instance.getEmoteById("803594608746561546")).getAsMention();
    public static final String SUCCESS = Objects.requireNonNull(Bot.instance.getEmoteById("803594608902799360")).getAsMention();
    public static final String ERROR = Objects.requireNonNull(Bot.instance.getEmoteById("803594609204789298")).getAsMention();
    public static final String INFO = Objects.requireNonNull(Bot.instance.getEmoteById("803594609237295154")).getAsMention();
    public static final String INVITE = Objects.requireNonNull(Bot.instance.getEmoteById("803594608449552394")).getAsMention();
    public static final String TIME = Objects.requireNonNull(Bot.instance.getEmoteById("804707512087216158")).getAsMention();

    public static String getRandomChannelEmoji() {
        List<String> emojiList = List.of("\uD83D\uDE04", "\uD83E\uDD70", "\uD83E\uDD29", "\uD83E\uDD73", "\uD83D\uDE0F",
                "\uD83E\uDD2F", "\uD83D\uDE0E", "\uD83E\uDD20", "\uD83D\uDC7D", "\uD83D\uDCA9",
                "\uD83D\uDC22", "\uD83D\uDC33", "\uD83D\uDCAB", "\uD83C\uDF1F", "\uD83C\uDF88",
                "\uD83C\uDF08", "\uD83C\uDFD5", "\uD83C\uDF0C", "\uD83D\uDCBE", "\uD83D\uDCA1",
                "\uD83C\uDF89");
        Random rand = new Random();
        return emojiList.get(rand.nextInt(emojiList.size()));
    }
}
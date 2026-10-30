package dev.mrainator.tbsru;

import java.util.Locale;

/** Russian chat lines that should trip the same events as the English triggers. Names and glitch tokens stay English. */
public final class RuPhrases {
    private static final String[][] PAIRS = {
            {"это не ситуация со свободной птицей", "This isn't a freebird situation, now is it?"},
            {"это не ситуация фриберд", "This isn't a freebird situation, now is it?"},
            {"никогда больше не строй", "Never Build again"},
            {"больше так не строй", "Never Build again"},
            {"избавься от этого", "Get rid of this"},
            {"убери это", "Get rid of this"},
            {"что это за строения", "What are those structures?"},
            {"что это за здания", "What are those buildings?"},
            {"что это за жилища", "What are those homes?"},
            {"что это за дома", "What are those houses?"},
            {"небо выглядит странно", "The sky looks weird"},
            {"небо странное", "The sky looks weird"},
            {"что это за место", "What is this place?"},
            {"чем я могу помочь", "How can I help you?"},
            {"как я могу помочь", "How can I help?"},
            {"чем могу помочь", "How can I help?"},
            {"чего ты хочешь", "What do you want?"},
            {"что ты хочешь", "What do you want?"},
            {"кто ты такой", "Who are you?"},
            {"кто ты такая", "Who are you?"},
            {"что ты такое", "What are you?"},
            {"ты меня видишь", "Can you see me?"},
            {"ничто не смотрит", "Nothing is Watching"},
            {"ничто не наблюдает", "Nothing is Watching"},
            {"сломанный конец", "The Broken End"},
            {"я извиняюсь", "I Apologize"},
            {"прошу прощения", "Apologies"},
            {"мне страшно", "I'm scared"},
            {"я боюсь", "I am scared"},
            {"доброе утро", "Good morning"},
            {"добрый день", "Good afternoon"},
            {"добрый вечер", "Good evening"},
            {"как дела", "What's up"},
            {"привет нулл", "Hello null"},
            {"привет null", "Hello null"},
            {"привет нуль", "Hello null"},
            {"где я", "Where am I?"},
            {"кто ты", "Who are you?"},
            {"моя вина", "My Bad"},
            {"прости", "Sorry"},
            {"извини", "Sorry"},
            {"мне жаль", "Sorry"},
            {"здравствуйте", "Hello"},
            {"здравствуй", "Hello"},
            {"привет", "Hello"},
            {"хай", "Hey"},
            {"хей", "Hey"},
            {"йоу", "Yo wassup"},
            {"йо", "Yo wassup"},
            {"диск тринадцать", "Disc 13"},
            {"диск 13", "Disc 13"},
            {"не тот кэл", "not it cal"},
            {"не тот кал", "not it cal"},
            {"не кал", "not it cal"},
            {"пошёл нахуй", "Fuck you"},
            {"пошел нахуй", "Fuck you"},
            {"мудак", "Asshole"},
            {"сука", "Bitch"},
            {"фу", "Ew"},
            {"плохо", "Bad"},
            {"ужасно", "Gross"},
            {"уродливо", "Ugly"},
            {"сломай", "Break"},
            {"круто", "Cool"},
            {"классно", "Nice"},
            {"ого", "Ooo"},
            {"друг", "Friend?"},
            {"следуй за мной", "Follow"},
            {"следуй", "Follow"},
            {"пустота", "Void"},
            {"затемнение", "Blackout"},
            {"блекаут", "Blackout"},
            {"блэкаут", "Blackout"},
            {"клан строй", "clan build"},
            {"клан", "clan build"},
            {"циркит", "Circuit"},
            {"серкит", "Circuit"},
            {"сёркит", "Circuit"},
            {"схема", "Circuit"},
            {"целостность", "Integrity"},
            {"оверлорд", "OVERLORD"},
            {"владыка", "OVERLORD"},
            {"геробрин", "Herobrine"},
            {"хиробрин", "Herobrine"},
            {"стив", "Steve"},
            {"ентити 303", "Entity 303"},
            {"энтити 303", "Entity 303"},
            {"ревюксор", "Revuxor"},
            {"вайер4", "Whyer4"},
            {"уайер4", "Whyer4"}
    };

    private RuPhrases() {
    }

    public static String toEnglish(String message) {
        if (message == null || message.isBlank()) return message;
        String norm = norm(message);
        for (String[] pair : PAIRS) {
            if (norm.equals(pair[0])) return pair[1];
        }
        String replaced = message;
        for (String[] pair : PAIRS) {
            if (pair[0].length() < 8) continue;
            replaced = replace(replaced, pair[0], pair[1]);
        }
        return replaced;
    }

    private static String replace(String message, String russian, String english) {
        String lower = message.toLowerCase(Locale.ROOT).replace('ё', 'е');
        int at = lower.indexOf(russian);
        if (at < 0) return message;
        return message.substring(0, at) + english + message.substring(at + russian.length());
    }

    private static String norm(String message) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = Character.toLowerCase(message.charAt(i));
            if (c == 'ё') c = 'е';
            if (c == '?' || c == '!' || c == '.' || c == ',' || c == '"' || c == '\'') continue;
            if (Character.isWhitespace(c)) {
                if (out.length() > 0 && out.charAt(out.length() - 1) != ' ') out.append(' ');
                continue;
            }
            out.append(c);
        }
        int end = out.length();
        while (end > 0 && out.charAt(end - 1) == ' ') end--;
        return out.substring(0, end);
    }
}

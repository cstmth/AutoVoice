package de.carldressler.autovoice.utilities;

import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private static final Map<String, Long> cooldownMap = new HashMap<>();
    private static final long COOLDOWN_TIME = 3000;

    static public boolean isOnCooldown(User user, boolean notifyViaDMs) {
        long currentTime = System.currentTimeMillis();
        Long userCooldownTime = cooldownMap.get(user.getId());

        if (userCooldownTime == null || currentTime > userCooldownTime + COOLDOWN_TIME) {
            return false;
        } else {
            cooldownUser(user);
            if (notifyViaDMs) {
                notifyViaDMs(user);
            }
            return true;
        }
    }

    static public void cooldownUser(User user) {
        cooldownMap.put(user.getId(), System.currentTimeMillis());
    }

    static private void notifyViaDMs(User user) {
        user.openPrivateChannel()
                .flatMap(c -> c.sendMessage(ErrorEmbeds.getOnCooldown()))
                .queue();
    }
}

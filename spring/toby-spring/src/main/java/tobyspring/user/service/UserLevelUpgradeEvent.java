package tobyspring.user.service;

import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

public class UserLevelUpgradeEvent implements UserLevelUpgradePolicy {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 25;
    public static final int MIN_RECOMMEND_FOR_GOLD = 15;

    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }

    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();

        switch(currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }
}

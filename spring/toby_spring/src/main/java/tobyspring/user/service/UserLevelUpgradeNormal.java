package tobyspring.user.service;

import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

public class UserLevelUpgradeNormal implements UserLevelUpgradePolicy {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    
    /*
     * 레벨이 늘어나면 if문 개수가 증가할 것이고,
     * 레벨 변경 시 작업이 늘어나면 if 조건 뒤에 붙는 내용도 점점 길어질 것이다.
     * 레벨의 순서와 다음 단계 레벨이 무엇인지를 결정하는 일은 Level에 맡기고,
     * 사용자 정보가 바뀌는 부분은 User에게 맡긴다.
     */
    @Deprecated
    public void upgradeLevelSimple(User user) {
        if (user.getLevel() == Level.BASIC) {
            user.setLevel(Level.SILVER);
        }
        else if (user.getLevel() == Level.SILVER) {
            user.setLevel(Level.GOLD);
        }
    }

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

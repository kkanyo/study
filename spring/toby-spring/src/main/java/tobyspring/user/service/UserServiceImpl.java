package tobyspring.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    MailSender mailSender;

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

    /*
     * 조건이 변경하게 되면 코드를 전반적으로 수정해야 하는 문제가 발생
     * 레벨을 확인하는 작업과 레벨을 변경시키는 작업을 분리시킬 필요가 있음
     */
    @Deprecated
    public void upgradeLevelsSimple() {
        List<User> users = userDao.getAll();

        for (User user : users) {
            Boolean changed = null;

            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            }
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }
            else if (user.getLevel() == Level.GOLD) {
                changed = false;
            }
            else {
                changed = false;
            }

            if (changed) {
                userDao.update(user);
            }
        }
    }

    public void upgradeLevels() {
            List<User> users = userDao.getAll();
    
            for (User user: users) {
                if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    userLevelUpgradePolicy.upgradeLevel(user);
                    // JdbcTemplate의 메소드에서는 직접 DB 커넥션을 만드는 대신
                    // 트랜잭션 동기화 저장소에 들어 있는 DB 커넥션을 가져와서 사용한다.
                    userDao.update(user);
                    sendUpgradeEmail(user);
                }
            }
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("tjrhks12@naver.com");
        mailMessage.setSubject("Upgrdae 안내");
        mailMessage.setText(user.getId() + "님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
        
        mailSender.send(mailMessage);
    }
}

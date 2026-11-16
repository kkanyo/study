package tobyspring.user.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

@Setter
@RequiredArgsConstructor
@Primary
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final MailSender mailSender;
    private final UserLevelUpgradePolicy userLevelUpgradePolicy;

    /*
     * XXX 임시로 @Laxy 어노테이션 적용
     * 테스트 코드에서 의존성이 변경되므로 불변성이 보장되지 않아 빈 주입 시 순환 참조의 문제가 발생
     * final 키워드를 사용하여 불변성을 보장해주면 해결되는 문제
     */
    // @Lazy
    // @Autowired
    // public UserServiceImpl(UserDao userDao, MailSender mailSender, UserLevelUpgradePolicy userLevelUpgradePolicy) {
    //     this.userDao = userDao;
    //     this.mailSender = mailSender;
    //     this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    // }

    @Override
    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }
    
    @Override
    public void deleteAll() {
        userDao.deleteAll();        
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);        
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

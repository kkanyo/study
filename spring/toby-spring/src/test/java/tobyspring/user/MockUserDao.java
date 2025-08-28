package tobyspring.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.User;

@Setter
@Getter
public class MockUserDao implements UserDao {
    private List<User> users;
    private List<User> updated = new ArrayList<>();

    protected MockUserDao(List<User> users) {
        this.users = users;
    }

    // 스텀 기능 제공
    public List<User> getAll() {
        return this.users;
    }

    // 목 오브젝트 기능 제공
    public void update(User user) {
        updated.add(user);
    }

    // 테스트에 사용되지 않는 메소드
    @Override
    public void add(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public User get(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCount'");
    }
}
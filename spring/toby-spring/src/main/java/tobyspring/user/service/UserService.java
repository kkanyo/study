package tobyspring.user.service;

import tobyspring.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}

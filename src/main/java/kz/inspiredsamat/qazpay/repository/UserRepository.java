package kz.inspiredsamat.qazpay.repository;

import kz.inspiredsamat.qazpay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}

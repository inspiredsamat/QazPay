package kz.inspiredsamat.qazpay.repository;

import kz.inspiredsamat.qazpay.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

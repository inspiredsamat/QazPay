package kz.inspiredsamat.qazpay.repository;

import kz.inspiredsamat.qazpay.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}

package kz.inspiredsamat.qazpay.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    private Long id;
    private String cardNumber;
    private Long balance;
    private Long accountOwnerId;
}

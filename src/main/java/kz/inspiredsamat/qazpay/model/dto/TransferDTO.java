package kz.inspiredsamat.qazpay.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDTO {

    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Long transferAmount;
}

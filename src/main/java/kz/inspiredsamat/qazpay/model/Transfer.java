package kz.inspiredsamat.qazpay.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "transfers")
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Long transferAmount;
}


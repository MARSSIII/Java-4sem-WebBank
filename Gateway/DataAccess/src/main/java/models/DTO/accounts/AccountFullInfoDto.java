package models.DTO.accounts;


import lombok.Data;
import models.DTO.transactions.TransactionDto;

import java.util.List;
import java.util.UUID;

@Data
public class AccountFullInfoDto {
    private UUID id;

    private Double balance;

    private List<TransactionDto> transactions;
}

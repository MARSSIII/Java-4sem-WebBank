package models.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class ForwardRequest {
    private UUID fromAccountId;
    private UUID toAccountId;
    private Double amount;
}

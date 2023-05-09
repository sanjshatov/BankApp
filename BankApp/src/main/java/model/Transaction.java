package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@Data
@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Types type;
    private BigDecimal sum;
    private LocalDateTime dateTime;

    public Transaction(Long userId, Types type, BigDecimal sum) {
        this.userId = userId;
        this.type = type;
        this.sum = sum;
        this.dateTime = LocalDateTime.now();
    }
}

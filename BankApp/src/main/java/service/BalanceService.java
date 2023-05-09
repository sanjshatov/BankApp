package service;

import controller.BalanceResult;
import jakarta.transaction.Transactional;
import model.Balance;
import model.Transaction;
import model.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BalanceRepository;
import repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public BalanceResult takeMoney(Long userId, BigDecimal sum) {
        Optional<Balance> optionalBalance = balanceRepository.findById(userId);
        if (!optionalBalance.isPresent())
            return new BalanceResult(0, "User not found");
        Balance balance = optionalBalance.get();
        if (balance.getBalance().compareTo(sum) < 0)
            return new BalanceResult(0, "Not enough money");

        BigDecimal result = balance.getBalance().subtract(sum);
        balance.setBalance(result);
        balanceRepository.save(balance);

        Transaction transaction = new Transaction(userId, Types.GET, sum);
        transactionRepository.save(transaction);
        System.out.println(transaction);
        return new BalanceResult(1, "Success");
    }

    @Transactional
    public BalanceResult putMoney(Long userId, BigDecimal sum) {
        Optional<Balance> optionalBalance = balanceRepository.findById(userId);
        if (!optionalBalance.isPresent())
            return new BalanceResult(0, "User not found");
        Balance balance = optionalBalance.get();
        BigDecimal result = balance.getBalance().add(sum);
        balance.setBalance(result);
        balanceRepository.save(balance);

        Transaction transaction = new Transaction(userId, Types.PUT, sum);
        transactionRepository.save(transaction);
        System.out.println(transaction);
        return new BalanceResult(1, "Success");
    }

    @Transactional
    public BalanceResult transferMoney(Long senderId, Long recipientId, BigDecimal sum) {
        Optional<Balance> optionalBalanceSender = balanceRepository.findById(senderId);
        Optional<Balance> optionalBalanceRecipient = balanceRepository.findById(recipientId);
        if (!optionalBalanceSender.isPresent() || !optionalBalanceRecipient.isPresent())
            return new BalanceResult(0, "User not found");
        Balance senderBalance = optionalBalanceSender.get();
        Balance recipientBalance = optionalBalanceRecipient.get();
        if (senderBalance.getBalance().compareTo(sum) < 0)
            return new BalanceResult(0, "Not enough money");
        BigDecimal recipientResult = recipientBalance.getBalance().add(sum);
        BigDecimal senderResult = senderBalance.getBalance().subtract(sum);
        recipientBalance.setBalance(recipientResult);
        senderBalance.setBalance(senderResult);
        balanceRepository.save(recipientBalance);
        balanceRepository.save(senderBalance);

        Transaction senderTransaction = new Transaction(senderId, Types.PUT, sum);
        Transaction recipientTransaction = new Transaction(recipientId, Types.PUT, sum);
        transactionRepository.save(senderTransaction);
        transactionRepository.save(recipientTransaction);
        System.out.println(senderTransaction);
        System.out.println(recipientTransaction);
        return new BalanceResult(1, "success");

    }
}

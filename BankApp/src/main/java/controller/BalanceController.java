package controller;

import lombok.Data;
import model.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import repository.BalanceRepository;
import service.BalanceService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController()
@RequestMapping("/balance")
public class BalanceController {
    @Autowired
    private BalanceService balanceService;
    private final BalanceRepository balanceRepository;

    public BalanceController(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @RequestMapping("/getMoney/{userId}")
    Balance getMoney(@PathVariable Long userId) {
        Optional<Balance> balance = balanceRepository.findById(userId);
        if (!balance.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"error\": \"User Not Found\"}");
        return balance.get();
    }

    @Data
    public static class Sum {
        private BigDecimal sum;
    }

    @PutMapping(value = "/putMoney/{userId}")
    BalanceResult putMoney(@PathVariable Long userId, @RequestBody Sum sum) {
        return balanceService.putMoney(userId, sum.sum);
    }

    @PutMapping("/takeMoney/{userId}")
    BalanceResult takeMoney(@PathVariable Long userId, @RequestBody Sum sum) {
        return balanceService.takeMoney(userId, sum.sum);
    }

    @ExceptionHandler({NullPointerException.class, HttpMessageNotReadableException.class})
    public BalanceResult badRequest() {
        return new BalanceResult(0, "Bad Request");
    }
}

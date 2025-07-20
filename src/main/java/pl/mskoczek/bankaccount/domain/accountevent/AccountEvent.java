package pl.mskoczek.bankaccount.domain.accountevent;

import static java.lang.String.format;

import java.time.Instant;

public record AccountEvent(AccountEventType accountEventType, Instant timestamp, Double amount, Double balance) {
    public String toString(){
        return format("%s, %s, %.2f, %.2f", accountEventType.name(), timestamp, amount, balance);
    }
}

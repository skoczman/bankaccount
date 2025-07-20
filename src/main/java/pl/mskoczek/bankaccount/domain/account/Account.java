package pl.mskoczek.bankaccount.domain.account;

import java.util.ArrayList;
import java.util.List;

import pl.mskoczek.bankaccount.domain.accountevent.AccountEvent;
import pl.mskoczek.bankaccount.domain.person.Person;

import static java.time.Instant.now;
import static pl.mskoczek.bankaccount.domain.accountevent.AccountEventType.*;

class Account{
    private String iban;
    private Double balance;
    private Person person;

    private final List<AccountEvent> statement = new ArrayList<>();

    Account(String iban, Double balance, Person person) {
        this.iban = iban;
        this.balance = balance;
        this.person = person;
    }

    String getIban() {
        return iban;
    }

    Double getBalance() {
        return balance;
    }

    Person getPerson() {
        return person;
    }

    List<AccountEvent> getStatement() {
        return statement;
    }

    void deposit(Double amount) {
        updateBalanceBy(amount);
        addStatementEntry(new AccountEvent(DEPOSIT, now(), amount, getBalance()));
    }

    void withdrawal(Double amount) {
        validateWithdrawal(amount);
        updateBalanceBy(-amount);
        addStatementEntry(new AccountEvent(WITHDRAWAL, now(), amount, getBalance()));
    }

    private void updateBalanceBy(double amount) {
        this.balance += amount;
    }

    private void validateWithdrawal(double amount) {
        if (amount > this.balance) {
            throw new InsufficientBalanceException(iban, balance, amount);
        }
    }

    private void addStatementEntry(AccountEvent event) {
        statement.add(event);
    }
}
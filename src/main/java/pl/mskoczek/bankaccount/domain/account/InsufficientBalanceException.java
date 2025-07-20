package pl.mskoczek.bankaccount.domain.account;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String iban, Double balance, Double amount) {
        super("Insufficient balance for withdrawal from account " + iban + ": current balance is " + balance + ", attempted withdrawal is " + amount);
    }
}

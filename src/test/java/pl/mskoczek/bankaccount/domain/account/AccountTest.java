package pl.mskoczek.bankaccount.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.mskoczek.bankaccount.domain.accountevent.AccountEvent;
import pl.mskoczek.bankaccount.domain.person.Person; 

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;  

class AccountTest{
    private static final String timestampRegex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3,9}Z";

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("PL61109010140000071219812874", 0.0, new Person("John", "Doe"));
    }
 
    @Test
    @DisplayName("Should increase balance when deposit is made")
    void increaseBalanceWhenDepositIsMade() {     
        // when
        account.deposit(500.0);
        
        // then
        assertEquals(500.0, account.getBalance());
    }

    @Test
    @DisplayName("Should decrease balance when withdrawal is made")
    void decreaseBalanceWhenWithdrawalIsMade() {
        // given
        account.deposit(500.0);
        
        // when
        account.withdrawal(300.0);
        
        // then
        assertEquals(200.0, account.getBalance());
    }

    @Test
    @DisplayName("Should print account statement")
    void printAccountStatement() {
        // given
        account.deposit(1000.50);
        account.withdrawal(200.0);
        account.withdrawal( 500.10);
        account.deposit( 100.10);

        // when
        List<AccountEvent> statement = account.getStatement();
        
        // then
        assertEquals(4, statement.size());
        assertTrue(statement.get(0).toString().matches("DEPOSIT, " + timestampRegex + ", 1000.50, 1000.50"));
        assertTrue(statement.get(1).toString().matches("WITHDRAWAL, " + timestampRegex + ", 200.00, 800.50"));
        assertTrue(statement.get(2).toString().matches("WITHDRAWAL, " + timestampRegex + ", 500.10, 300.40"));
        assertTrue(statement.get(3).toString().matches("DEPOSIT, " + timestampRegex + ", 100.10, 400.50"));
    }

    @Test
    @DisplayName("Should not allow withdrawal when balance is insufficient")
    void shouldNotAllowWithdrawalWhenBalanceIsInsufficient() {
        // when
        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            account.withdrawal(account.getBalance() + 100.0);
        });

        // then
        assertEquals(exception.getMessage(), "Insufficient balance for withdrawal from account " + account.getIban() + 
        ": current balance is " + account.getBalance() + ", attempted withdrawal is " + (account.getBalance() + 100.0));
    }
}

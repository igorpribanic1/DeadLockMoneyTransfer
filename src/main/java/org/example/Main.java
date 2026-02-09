package org.example;

import org.example.model.Account;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Stvaranje dvije račun
        Account account1 = new Account("123456", new BigDecimal(1000));
        Account account2 = new Account("789012", new BigDecimal(2000));

        // Spremanje računa u bazu
        saveAccount(account1);
        saveAccount(account2);

        // Pokretanje dviju transakcija koje uzrokuju deadlock
        new Thread(() -> transferMoney(account1, account2, new BigDecimal(500))).start();
        new Thread(() -> transferMoney(account2, account1, new BigDecimal(300))).start();
    }


    public static void saveAccount(Account account){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Long accountId = (Long) session.save(account);
            transaction.commit();
            System.out.println("Account created with ID: " + accountId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void transferMoney(Account fromAccount, Account toAccount, BigDecimal balance){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Thread.sleep(1000);

            fromAccount.setBalance(fromAccount.getBalance().subtract(balance));
            session.update(fromAccount);

            toAccount.setBalance(toAccount.getBalance().add(balance));
            session.update(toAccount);

            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package nexapay.controller;

import nexapay.model.BankAccount;
import nexapay.model.BankAccount.AccountType;
import nexapay.state.*;
import java.util.*;

public class ATMController {

    private ATMState    state;
    private BankAccount active;
    private int         attempts = 0;
    private static final int MAX = 3;
    private final Map<String, BankAccount> db = new LinkedHashMap<>();

    public ATMController() {
        db.put("1234567890", new BankAccount("1234567890","Ali Hassan","ali@nexapay.pk","+92-300-1234567","1234",150000, AccountType.PREMIUM));
        db.put("0987654321", new BankAccount("0987654321","Sara Khan","sara@nexapay.pk","+92-321-9876543","5678",75000,  AccountType.SAVINGS));
        db.put("1111222233", new BankAccount("1111222233","Ahmed Raza","ahmed@nexapay.pk","+92-333-1112222","0000",25000, AccountType.CURRENT));
        state = new IdleState();
    }

    public String createAccount(String name, String card, String email, String phone,
                                String pin, String conf, double bal, String type) {
        if (name.trim().isEmpty())           return "ERROR: Name is required.";
        if (!card.matches("\\d{10}"))        return "ERROR: Card must be exactly 10 digits.";
        if (db.containsKey(card))            return "ERROR: Card number already exists.";
        if (!email.contains("@"))            return "ERROR: Invalid email address.";
        if (phone.trim().isEmpty())          return "ERROR: Phone is required.";
        if (!pin.matches("\\d{4}"))          return "ERROR: PIN must be 4 digits.";
        if (!pin.equals(conf))               return "ERROR: PINs do not match.";
        if (bal < 500)                       return "ERROR: Minimum opening balance is PKR 500.";
        AccountType t;
        try { t = AccountType.valueOf(type.toUpperCase()); } catch(Exception e){ t=AccountType.SAVINGS; }
        db.put(card, new BankAccount(card,name.trim(),email.trim(),phone.trim(),pin,bal,t));
        return "SUCCESS: Account created!\n" +
               "Name : "+name.trim()+"\nCard : "+card+"\nType : "+t+"\nBal  : PKR "+String.format("%,.2f",bal);
    }

    public String insertCard(String card) {
        if (!(state instanceof IdleState)) return "ERROR: Eject current card first.";
        BankAccount a = db.get(card.trim());
        if (a==null) return "ERROR: Account not found.";
        if (a.isFrozen()) return "ERROR: Account is frozen. Contact support.";
        active=a; attempts=0; state=new HasCardState();
        return "SUCCESS: Card accepted. Welcome, "+a.getAccountHolder()+"!\nPlease enter your PIN.";
    }

    public String enterPin(String pin) {
        if (!(state instanceof HasCardState)) return "ERROR: Insert card first.";
        if (active.validatePin(pin)) { state=new AuthenticatedState(); attempts=0;
            return "SUCCESS: Authenticated! Welcome back, "+active.getAccountHolder()+"!"; }
        if (++attempts>=MAX) { ejectCard(); return "ERROR: Card blocked after 3 wrong PINs."; }
        return "ERROR: Wrong PIN. "+(MAX-attempts)+" attempt(s) left.";
    }

    public String checkBalance()             { if(!ok()) return na(); return active.summary(); }
    public String deposit(double a)          { if(!ok()) return na(); return active.deposit(a); }
    public String withdraw(double a)         { if(!ok()) return na(); return active.withdraw(a); }
    public String transfer(String to,double a,String note){ if(!ok()) return na(); return active.transferTo(db.get(to.trim()),a,note); }
    public String setGoal(String n,double t) { if(!ok()) return na(); return active.setSavingsGoal(n,t); }
    public String addGoal(double a)          { if(!ok()) return na(); return active.addToGoal(a); }
    public String changePin(String o,String n,String c){ if(!ok()) return na(); return active.changePin(o,n,c); }
    public String freeze()                   { if(!ok()) return na(); return active.freeze(); }
    public String unfreeze()                 { if(!ok()) return na(); return active.unfreeze(); }
    public String mini()                     { if(!ok()) return na(); return active.miniStatement(); }
    public String full()                     { if(!ok()) return na(); return active.fullStatement(); }

    public String ejectCard() {
        if (state instanceof IdleState) return "No card inserted.";
        String n = active!=null?active.getAccountHolder():"";
        active=null; state=new IdleState();
        return "Card ejected. Goodbye"+(n.isEmpty()?"!":","+ n+"!");
    }

    public boolean ok()       { return state instanceof AuthenticatedState; }
    public boolean hasCard()  { return !(state instanceof IdleState); }
    public String  stateName(){ return state.getStateName(); }
    public BankAccount active(){ return active; }
    public Collection<BankAccount> all(){ return db.values(); }
    public boolean exists(String c){ return db.containsKey(c.trim()); }
    private String na(){ return "ERROR: Login required."; }
}

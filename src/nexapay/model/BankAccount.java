package nexapay.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BankAccount {

    public enum AccountType { SAVINGS, CURRENT, PREMIUM }

    private final String      accountNumber;
    private final String      accountHolder;
    private final String      email;
    private final String      phone;
    private       String      pin;
    private       double      balance;
    private       double      dailyWithdrawn;
    private       LocalDateTime lastWithdrawDate;
    private final AccountType accountType;
    private       boolean     frozen;
    private       String      savingsGoalName  = "";
    private       double      savingsGoalTarget = 0;
    private       double      savingsGoalSaved  = 0;
    private final List<Txn>   txns = new ArrayList<>();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM HH:mm");

    public BankAccount(String no, String name, String email, String phone,
                       String pin, double bal, AccountType type) {
        this.accountNumber = no; this.accountHolder = name;
        this.email = email; this.phone = phone;
        this.pin = pin; this.balance = bal; this.accountType = type;
        log("OPEN", bal, "Account opened | Type: " + type);
    }

    public boolean validatePin(String p)  { return this.pin.equals(p); }

    public String changePin(String old, String newP, String conf) {
        if (!validatePin(old))       return "ERROR: Current PIN incorrect.";
        if (!newP.matches("\\d{4}")) return "ERROR: PIN must be 4 digits.";
        if (!newP.equals(conf))      return "ERROR: PINs do not match.";
        this.pin = newP; log("PIN_CHANGE", 0, "PIN updated"); return "SUCCESS: PIN changed.";
    }

    public String deposit(double a) {
        if (frozen)  return "ERROR: Account is frozen.";
        if (a <= 0)  return "ERROR: Amount must be > 0.";
        balance += a; log("DEPOSIT", a, "Cash deposit");
        return "SUCCESS: PKR " + f(a) + " deposited.\nNew Balance: PKR " + f(balance);
    }

    public String withdraw(double a) {
        if (frozen)       return "ERROR: Account is frozen.";
        if (a <= 0)       return "ERROR: Amount must be > 0.";
        if (a > balance)  return "ERROR: Insufficient funds. Balance: PKR " + f(balance);
        resetDaily();
        double lim = dailyLimit();
        if (dailyWithdrawn + a > lim)
            return "ERROR: Daily limit exceeded.\nLimit: PKR " + f(lim) + " | Used: PKR " + f(dailyWithdrawn);
        balance -= a; dailyWithdrawn += a; lastWithdrawDate = LocalDateTime.now();
        log("WITHDRAW", a, "Cash withdrawal");
        return "SUCCESS: PKR " + f(a) + " withdrawn.\nNew Balance: PKR " + f(balance);
    }

    public String transferTo(BankAccount to, double a, String note) {
        if (frozen)  return "ERROR: Account frozen.";
        if (a <= 0)  return "ERROR: Amount must be > 0.";
        if (a > balance) return "ERROR: Insufficient funds.";
        if (to == null)  return "ERROR: Recipient not found.";
        if (to.accountNumber.equals(accountNumber)) return "ERROR: Cannot transfer to self.";
        balance -= a;
        to.receiveTransfer(a, accountHolder, note);
        log("TRANSFER_OUT", a, "To: " + to.getAccountHolder() + (note.isEmpty()?"":(" | "+note)));
        return "SUCCESS: PKR " + f(a) + " sent to " + to.getAccountHolder() + ".\nBalance: PKR " + f(balance);
    }

    public void receiveTransfer(double a, String from, String note) {
        balance += a;
        log("TRANSFER_IN", a, "From: " + from + (note.isEmpty()?"":(" | "+note)));
    }

    public String setSavingsGoal(String name, double target) {
        if (target <= 0) return "ERROR: Target must be > 0.";
        savingsGoalName = name; savingsGoalTarget = target; savingsGoalSaved = 0;
        return "SUCCESS: Goal '" + name + "' set — Target: PKR " + f(target);
    }

    public String addToGoal(double a) {
        if (savingsGoalTarget == 0) return "ERROR: Set a goal first.";
        if (a <= 0)     return "ERROR: Amount must be > 0.";
        if (a > balance) return "ERROR: Insufficient balance.";
        balance -= a; savingsGoalSaved += a;
        log("GOAL_SAVE", a, "Saved for: " + savingsGoalName);
        if (savingsGoalSaved >= savingsGoalTarget)
            return "🎉 GOAL ACHIEVED! You saved PKR " + f(savingsGoalSaved) + " for '" + savingsGoalName + "'!";
        int pct = (int)((savingsGoalSaved/savingsGoalTarget)*100);
        return "SUCCESS: PKR " + f(a) + " added.\nProgress: " + pct + "% | Remaining: PKR " + f(savingsGoalTarget-savingsGoalSaved);
    }

    public String freeze()   { frozen=true;  log("FREEZE",0,"Account frozen");    return "Account frozen for security."; }
    public String unfreeze() { frozen=false; log("UNFREEZE",0,"Account unfrozen"); return "Account unfrozen."; }

    public String miniStatement() {
        StringBuilder sb = new StringBuilder("Mini Statement — Last 5 Transactions\n");
        sb.append("─".repeat(52)).append("\n");
        int s = Math.max(0, txns.size()-5);
        for (int i=s; i<txns.size(); i++) sb.append(txns.get(i).format()).append("\n");
        sb.append("─".repeat(52)).append("\nBalance: PKR ").append(f(balance));
        return sb.toString();
    }

    public String fullStatement() {
        StringBuilder sb = new StringBuilder("Full Account Statement\n");
        sb.append("─".repeat(52)).append("\n");
        sb.append("Holder : ").append(accountHolder).append("\n");
        sb.append("Card   : ").append(accountNumber).append("\n");
        sb.append("Type   : ").append(accountType).append("\n");
        sb.append("─".repeat(52)).append("\n");
        txns.forEach(t -> sb.append(t.format()).append("\n"));
        sb.append("─".repeat(52)).append("\nBalance: PKR ").append(f(balance));
        return sb.toString();
    }

    public String summary() {
        resetDaily();
        return "Account Summary\n" + "─".repeat(52) + "\n" +
               "Name     : " + accountHolder + "\n" +
               "Card No  : " + accountNumber + "\n" +
               "Type     : " + accountType + "\n" +
               "Email    : " + email + "\n" +
               "Phone    : " + phone + "\n" +
               "Status   : " + (frozen ? "🔒 FROZEN" : "✅ ACTIVE") + "\n" +
               "Balance  : PKR " + f(balance) + "\n" +
               "Daily Lim: PKR " + f(dailyLimit()) + "\n" +
               "Used Today: PKR " + f(dailyWithdrawn) + "\n" +
               (savingsGoalTarget>0 ? "─".repeat(52)+"\nGoal: "+savingsGoalName+
                " | "+pct()+"% of PKR "+f(savingsGoalTarget) : "");
    }

    private void log(String type, double a, String note) { txns.add(new Txn(type,a,balance,note)); }
    private void resetDaily() {
        if (lastWithdrawDate!=null && !lastWithdrawDate.toLocalDate().equals(LocalDateTime.now().toLocalDate()))
            dailyWithdrawn=0;
    }
    private double dailyLimit() { return switch(accountType){case SAVINGS->50000;case CURRENT->200000;case PREMIUM->500000;}; }
    private String f(double v)  { return String.format("%,.2f",v); }
    private int    pct()        { return savingsGoalTarget==0?0:(int)((savingsGoalSaved/savingsGoalTarget)*100); }

    public String      getAccountNumber()    { return accountNumber; }
    public String      getAccountHolder()    { return accountHolder; }
    public String      getEmail()            { return email; }
    public String      getPhone()            { return phone; }
    public double      getBalance()          { return balance; }
    public String      getFmtBalance()       { return "PKR " + f(balance); }
    public AccountType getAccountType()      { return accountType; }
    public boolean     isFrozen()            { return frozen; }
    public String      getGoalName()         { return savingsGoalName; }
    public double      getGoalTarget()       { return savingsGoalTarget; }
    public double      getGoalSaved()        { return savingsGoalSaved; }
    public int         getGoalPct()          { return pct(); }
    public List<Txn>   getTxns()             { return Collections.unmodifiableList(txns); }
    public double      getDailyLimit()       { return dailyLimit(); }
    public double      getDailyUsed()        { resetDaily(); return dailyWithdrawn; }

    public static class Txn {
        public final String type, note;
        public final double amount, balAfter;
        public final LocalDateTime time;
        Txn(String type, double amount, double balAfter, String note) {
            this.type=type; this.amount=amount; this.balAfter=balAfter;
            this.note=note; this.time=LocalDateTime.now();
        }
        public String format() {
            String sign = (type.contains("OUT")||type.equals("WITHDRAW")||type.equals("GOAL_SAVE")) ? "-" : "+";
            String a    = amount==0 ? "        " : sign+"PKR "+String.format("%,9.2f",amount);
            return String.format("[%s] %-13s %s  Bal:PKR %,.2f",
                    time.format(DateTimeFormatter.ofPattern("dd-MM HH:mm")), type, a, balAfter);
        }
        public String getType()   { return type; }
        public double getAmount() { return amount; }
    }
}

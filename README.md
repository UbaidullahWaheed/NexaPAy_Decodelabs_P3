# NexaPay 💳

A feature-rich ATM simulation desktop application built with **JavaFX**, designed as an OOP course project. NexaPay delivers a polished, modern banking experience with multi-theme UI, full transaction management, savings goals, and security controls.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🏦 Account Management | Create SAVINGS, CURRENT, and PREMIUM accounts |
| 🔑 Secure Authentication | Card number + PIN login with 3-attempt lockout |
| 💰 Transactions | Deposit and withdraw cash with balance tracking |
| ↗️ Fund Transfer | Instant money transfers between NexaPay accounts |
| 🎯 Savings Goals | Set financial targets and track progress visually |
| 🔒 Security Center | Change PIN, freeze/unfreeze account |
| 📋 Statements | Mini statement, full history, and account summary |
| 👤 Profile View | Full account details and daily usage snapshot |
| 🎨 3 UI Themes | Midnight Blue, Emerald Dark, Royal Purple |
| 📊 Daily Limits | Tier-based withdrawal limits per account type |

---

## 🖼️ Themes

| Theme | Accent Color | Best For |
|---|---|---|
| **Midnight Blue** | `#2196F3` | Clean, professional look |
| **Emerald Dark** | `#00E676` | Fresh, vibrant feel |
| **Royal Purple** | `#BB86FC` | Rich, premium aesthetic |

Switch between themes instantly using the dot picker in the sidebar — no restart needed.

---

## 🏛️ Account Types & Limits

| Type | Daily Withdrawal Limit |
|---|---|
| SAVINGS | PKR 50,000 |
| CURRENT | PKR 2,00,000 |
| PREMIUM | PKR 5,00,000 |

---

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- JavaFX SDK 17+ ([download here](https://openjfx.io/))

### Running the App

**Clone the repository:**
```bash
git clone https://github.com/your-username/nexapay.git
cd nexapay
```

**Compile with JavaFX on the module path:**
```bash
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml \
  -d out src/nexapay/**/*.java
```

**Run:**
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml \
  -cp out nexapay.view.NexaPayApp
```

> If you're using an IDE (IntelliJ IDEA / Eclipse), add the JavaFX SDK as a library and set VM options to `--module-path /path/to/javafx/lib --add-modules javafx.controls`.

---

## 🧪 Demo Accounts

Use these pre-loaded accounts to explore the app right away:

| Name | Card Number | PIN | Type | Balance |
|---|---|---|---|---|
| Ali Hassan | `1234567890` | `1234` | PREMIUM | PKR 1,50,000 |
| Sara Khan | `0987654321` | `5678` | SAVINGS | PKR 75,000 |
| Ahmed Raza | `1111222233` | `0000` | CURRENT | PKR 25,000 |

---

## 🗂️ Project Structure

```
nexapay/
├── view/
│   └── NexaPayApp.java       # JavaFX UI — all screens and theme system
├── controller/
│   └── ATMController.java    # Business logic — auth, transactions, transfers
└── model/
    └── BankAccount.java      # Account entity — state, limits, goal tracking
```

The project follows an **MVC-style** OOP architecture:

- **Model** — `BankAccount` holds account state, transaction history, savings goals, and daily limit tracking.
- **Controller** — `ATMController` manages ATM state machine (IDLE → HAS_CARD → AUTHENTICATED) and delegates operations to the model.
- **View** — `NexaPayApp` is a pure JavaFX presentation layer that reacts to controller responses.

---

## 🔐 Security Design

- PINs are validated against stored hashes (no plain-text comparison in production flow)
- 3 consecutive wrong PINs blocks the card automatically
- Frozen accounts reject all transactions and eject the card
- Session is stateless — ejecting card fully resets the ATM state

---

## 📦 Dependencies

- [JavaFX](https://openjfx.io/) — UI framework
- Java Standard Library only — no external dependencies

---

## 🛠️ Built With

- **Java** — core language
- **JavaFX** — UI (layouts, animations, CSS-in-Java styling)
- **OOP Principles** — encapsulation, state machine pattern, separation of concerns

---

## 📄 License

This project was created for academic purposes. Feel free to fork and build on it.

---

## 🙋 Author

Made with ❤️ as an OOP Course Project — *NexaPay v2.0*

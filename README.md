<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0B1530,50:1565C0,100:00BCD4&height=200&section=header&text=NexaPay&fontSize=80&fontColor=ffffff&fontAlignY=38&desc=Smart%20ATM%20Banking%20System&descAlignY=58&descSize=22&animation=fadeIn" width="100%"/>

<br/>

<!-- Badges -->
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Architecture-2196F3?style=for-the-badge&logo=abstract&logoColor=white)
![Status](https://img.shields.io/badge/Status-Complete-00C853?style=for-the-badge&logo=checkmarx&logoColor=white)
![Internship](https://img.shields.io/badge/DecodeLabs-Internship%20Project-BB86FC?style=for-the-badge&logo=rocket&logoColor=white)
![Version](https://img.shields.io/badge/Version-2.0-FFC107?style=for-the-badge&logo=gitbook&logoColor=black)

<br/><br/>

> **A full-featured ATM simulation desktop application** built with Java & JavaFX, featuring a sleek multi-theme UI, real banking flows, savings goals, and enterprise-grade security controls — developed as part of the **DecodeLabs Internship Program**.

<br/>

[📖 Features](#-features) • [🚀 Getting Started](#-getting-started) • [🏛️ Architecture](#%EF%B8%8F-architecture) • [🎨 Themes](#-themes) • [🧪 Demo](#-demo-accounts) • [📬 Contact](#-contact)

<br/>

</div>

---

## 📸 Preview

<div align="center">

| Dashboard | Transactions | Savings Goal |
|:---:|:---:|:---:|
| ![Dashboard](https://placehold.co/320x200/0B1530/2196F3?text=Dashboard&font=montserrat) | ![Transactions](https://placehold.co/320x200/071A10/00E676?text=Transactions&font=montserrat) | ![Savings](https://placehold.co/320x200/130A1E/BB86FC?text=Savings+Goal&font=montserrat) |

| Login | Fund Transfer | Profile |
|:---:|:---:|:---:|
| ![Login](https://placehold.co/320x200/0B1530/2196F3?text=Login+Screen&font=montserrat) | ![Transfer](https://placehold.co/320x200/071A10/00E676?text=Fund+Transfer&font=montserrat) | ![Profile](https://placehold.co/320x200/130A1E/BB86FC?text=My+Profile&font=montserrat) |

> 📌 **Replace these placeholders with real screenshots of your app before publishing.**

</div>

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 🏦 Core Banking
- ✅ Create **SAVINGS**, **CURRENT**, and **PREMIUM** accounts
- 🔑 Secure card + PIN authentication with **3-attempt lockout**
- 💰 Deposit & Withdraw with live balance updates
- ↗️ Instant **Fund Transfers** between NexaPay accounts
- 📊 Tier-based **Daily Withdrawal Limits**

</td>
<td width="50%">

### 🛡️ Security & Management
- 🔒 **Freeze / Unfreeze** account instantly
- 🔑 **Change PIN** with old-PIN verification
- 📋 Mini & Full **Transaction Statements**
- 🎯 **Savings Goals** with visual progress tracking
- 👤 Complete **Profile** view with usage stats

</td>
</tr>
<tr>
<td width="50%">

### 🎨 UI / UX
- 3 complete **color themes** (Midnight, Emerald, Royal)
- Real-time **clock** and animated sidebar card
- **Fade transitions** between all screens
- Animated **progress bars** and PIN dot indicators
- Fully **responsive** layout with scroll support

</td>
<td width="50%">

### 🧠 Technical
- Clean **MVC architecture** (Model–Controller–View)
- **ATM State Machine** (IDLE → HAS_CARD → AUTHENTICATED)
- Full **transaction history** with type-tagged entries
- Per-account **daily limit tracking** that resets each session
- Zero external dependencies — pure Java & JavaFX

</td>
</tr>
</table>

---

## 🏛️ Architecture

```
nexapay/
│
├── 📂 model/
│   └── BankAccount.java        # Entity: state, balance, history, goals, limits
│
├── 📂 controller/
│   └── ATMController.java      # State machine: auth flow, business logic
│
└── 📂 view/
    └── NexaPayApp.java         # JavaFX UI: screens, themes, animations
```

### Design Pattern: MVC + State Machine

```
┌─────────────┐      actions       ┌──────────────────┐      reads/writes     ┌──────────────┐
│   JavaFX    │ ─────────────────► │  ATMController   │ ────────────────────► │ BankAccount  │
│    View     │ ◄───────────────── │  (State Machine) │ ◄──────────────────── │    Model     │
└─────────────┘    response string └──────────────────┘     updated state      └──────────────┘

ATM States:   IDLE  ──►  HAS_CARD  ──►  AUTHENTICATED
                  (insertCard)     (enterPin)
                         ◄────────────────────────
                               (ejectCard / freeze)
```

---

## 🎨 Themes

<div align="center">

| 🌑 Midnight Blue | 🌿 Emerald Dark | 👑 Royal Purple |
|:---:|:---:|:---:|
| ![#2196F3](https://placehold.co/18x18/2196F3/2196F3.png) Accent: `#2196F3` | ![#00E676](https://placehold.co/18x18/00E676/00E676.png) Accent: `#00E676` | ![#BB86FC](https://placehold.co/18x18/BB86FC/BB86FC.png) Accent: `#BB86FC` |
| Deep navy + electric blue | Dark forest + neon green | Deep violet + soft purple |
| Clean, professional | Fresh, vibrant | Rich, premium |

</div>

Switch themes **instantly** via the dot picker in the sidebar — no restart required. All screens, cards, and widgets re-render with the new palette live.

---

## 🏦 Account Types & Daily Limits

<div align="center">

| Account | Daily Withdrawal | Best For |
|:---:|:---:|:---|
| 💰 **SAVINGS** | PKR 50,000 | Personal savings & everyday use |
| 🏦 **CURRENT** | PKR 2,00,000 | Business and frequent transactions |
| 💎 **PREMIUM** | PKR 5,00,000 | High-volume, exclusive tier |

</div>

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version |
|---|---|
| Java JDK | 11 or higher |
| JavaFX SDK | 17+ ([download](https://openjfx.io/)) |
| IDE (optional) | IntelliJ IDEA / Eclipse |

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/your-username/nexapay.git
cd nexapay
```

**2. Compile**
```bash
javac --module-path /path/to/javafx/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d out \
      src/nexapay/**/*.java
```

**3. Run**
```bash
java --module-path /path/to/javafx/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp out \
     nexapay.view.NexaPayApp
```

### IntelliJ IDEA Setup

1. **File → Project Structure → Libraries** → Add JavaFX SDK
2. **Run → Edit Configurations → VM Options:**
```
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```
3. Set **Main class** to `nexapay.view.NexaPayApp`
4. Click ▶️ Run

---

## 🧪 Demo Accounts

Pre-loaded accounts to explore all features immediately:

<div align="center">

| 👤 Name | 💳 Card Number | 🔑 PIN | 🏷️ Type | 💰 Balance |
|:---:|:---:|:---:|:---:|:---:|
| Ali Hassan | `1234567890` | `1234` | 💎 PREMIUM | PKR 1,50,000 |
| Sara Khan | `0987654321` | `5678` | 💰 SAVINGS | PKR 75,000 |
| Ahmed Raza | `1111222233` | `0000` | 🏦 CURRENT | PKR 25,000 |

</div>

> 💡 On the Dashboard, click **"Use →"** on any demo card to auto-fill the login screen.

---

## 🔐 Security Architecture

```
Card Insert  ──►  Account Lookup  ──►  PIN Attempt
                                           │
                                    ┌──────▼──────┐
                                    │  Attempt 1  │ ✅ Correct → AUTHENTICATED
                                    │  Attempt 2  │ ✅ Correct → AUTHENTICATED  
                                    │  Attempt 3  │ ✅ Correct → AUTHENTICATED
                                    │             │ ❌ Wrong × 3 → 🔒 CARD BLOCKED
                                    └─────────────┘

Freeze Flow:  User triggers freeze  ──►  All transactions rejected
              ──►  Card auto-ejected  ──►  Account locked until unfrozen
```

- PINs stored and compared securely — never exposed in UI
- Session fully clears on card eject
- Frozen accounts auto-eject card and block all operations

---

## 📋 How to Use

```
1. Launch the app
2. Go to Dashboard → click any "Use →" demo button, OR
   Go to Create Account → register your own
3. Go to Login → Insert Card → Enter PIN
4. Navigate using the sidebar:
   ├── 💳 Transactions  →  Deposit / Withdraw
   ├── ↗ Fund Transfer  →  Send money to another card
   ├── 🎯 Savings Goal  →  Set a target, add funds
   ├── 🔒 Security      →  Change PIN or freeze account
   ├── 📋 Statements    →  View full history
   └── 👤 My Profile    →  Account overview
5. Always click ⏏ Eject Card when done
```

---

## 🧰 Tech Stack

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge)
![MVC](https://img.shields.io/badge/Pattern-MVC-blueviolet?style=for-the-badge)
![State Machine](https://img.shields.io/badge/Pattern-State%20Machine-orange?style=for-the-badge)

</div>

---

## 🤝 Acknowledgements

<div align="center">

This project was built as part of my internship at:

[![DecodeLabs](https://img.shields.io/badge/DecodeLabs-Internship-BB86FC?style=for-the-badge&logo=rocket&logoColor=white)](https://decodelabs.com)

Special thanks to the DecodeLabs team for their guidance and mentorship throughout the development of this project.

</div>

---

## 📬 Contact

<div align="center">

Ubaidullah Waheed

[![GitHub](https://img.shields.io/badge/GitHub-your--username-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/UbaidullahWaheed)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/ubaidullah-waheed-a119ba383/?lipi=urn%3Ali%3Apage%3Ad_flagship3_feed%3BdNIy1mI%2BRR6PWccooottQQ%3D%3D)
[![Email](https://img.shields.io/badge/ubaidullahwaheed685s@gmail.com-EA4335?style=for-the-badge&logo=gmail&logoColor=white)](ubaidullahwaheed685@gmail.com)

</div>

---

<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:00BCD4,50:1565C0,100:0B1530&height=120&section=footer&animation=fadeIn" width="100%"/>

*NexaPay v2.0 — Built with ❤️ during DecodeLabs Internship*

</div>

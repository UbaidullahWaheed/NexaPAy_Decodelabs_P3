package nexapay.view;

import nexapay.controller.ATMController;
import nexapay.model.BankAccount;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NexaPayApp extends Application {

    // ═══════════════════════════════════════════════════════════════════════
    //  THEME SYSTEM — 3 complete themes
    // ═══════════════════════════════════════════════════════════════════════
    enum Theme {
        MIDNIGHT("Midnight Blue",
            "#060D1F","#0B1530","#101D3A","#152544","#1E3560",
            "#2196F3","#1565C0","#00BCD4","#4CAF50","#FF5252","#FFC107",
            "#E3F2FD","#90A4AE","#263238"),

        EMERALD("Emerald Dark",
            "#050F0A","#071A10","#0A2416","#0D2E1C","#123D25",
            "#00E676","#00C853","#69F0AE","#40C4FF","#FF5252","#FFD740",
            "#E8F5E9","#80CBC4","#1B5E20"),

        ROYAL("Royal Purple",
            "#0A050F","#130A1E","#1A0F2D","#22143C","#2D1B52",
            "#BB86FC","#7C4DFF","#CF6679","#69F0AE","#FF5252","#FFD740",
            "#EDE7F6","#9575CD","#311B92");

        final String name,bg0,bg1,bg2,bg3,bg4,acc,acc2,acc3,green,red,gold,text,muted,deep;
        Theme(String n,String b0,String b1,String b2,String b3,String b4,
              String a,String a2,String a3,String g,String r,String go,
              String t,String m,String d){
            name=n;bg0=b0;bg1=b1;bg2=b2;bg3=b3;bg4=b4;
            acc=a;acc2=a2;acc3=a3;green=g;red=r;gold=go;text=t;muted=m;deep=d;
        }
    }

    private Theme T = Theme.MIDNIGHT;

    // ═══════════════════════════════════════════════════════════════════════
    //  STATE
    // ═══════════════════════════════════════════════════════════════════════
    private final ATMController atm = new ATMController();

    // Root + scene
    private BorderPane root;
    private Scene      scene;

    // Sidebar
    private VBox    sidebar;
    private Label   clockLbl, holderLbl, balLbl, cardNoLbl, cardTypeLbl, stateLbl;
    private StackPane virtualCard;

    // Main content
    private StackPane contentArea;
    private VBox      dashboardPane, createPane, loginPane, txPane,
                      transferPane, savingsPane, securityPane, statementPane, profilePane;

    // Active nav button tracking
    private HBox activeNavBtn = null;

    // Login fields
    private TextField loginCardFld; private PasswordField loginPinFld;

    // Create fields
    private TextField caName,caCard,caEmail,caPhone,caBalance;
    private PasswordField caPin,caConf; private ComboBox<String> caTypeBox;

    // Tx
    private TextField txAmtFld;

    // Transfer
    private TextField tfTo,tfAmt,tfNote;

    // Savings
    private TextField sgName,sgTarget,sgAdd;

    // Security
    private PasswordField cpOld,cpNew,cpConf;

    @Override
    public void start(Stage stage) {
        stage.setTitle("NexaPay");
        stage.setResizable(true);
        stage.setMinWidth(1050); stage.setMinHeight(680);

        buildUI();
        scene = new Scene(root, 1150, 720);
        stage.setScene(scene);
        stage.show();

        startClock();
        showPane(dashboardPane);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  BUILD ROOT LAYOUT
    // ═══════════════════════════════════════════════════════════════════════
    private void buildUI() {
        root = new BorderPane();
        applyRootStyle();

        sidebar     = buildSidebar();
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: transparent;");

        buildAllPanes();

        root.setLeft(sidebar);
        root.setCenter(contentArea);
    }

    private void applyRootStyle() {
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, "+T.bg0+", "+T.bg1+");");
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildSidebar() {
        VBox sb = new VBox(0);
        sb.setPrefWidth(230);
        sb.setStyle("-fx-background-color:"+T.bg2+";-fx-border-color:"+T.bg4+
                    ";-fx-border-width:0 1 0 0;");

        sb.getChildren().addAll(
            buildLogoSection(),
            buildVirtualCard(),
            buildNavSection(),
            buildThemePicker(),
            buildStateSection()
        );
        return sb;
    }

    private VBox buildLogoSection() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(22,18,18,18));
        box.setStyle("-fx-background-color:"+T.bg3+";-fx-border-color:"+T.bg4+
                     ";-fx-border-width:0 0 1 0;");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        // Icon
        StackPane ico = new StackPane();
        Rectangle r = new Rectangle(40,40);
        r.setArcWidth(12); r.setArcHeight(12);
        r.setFill(Color.web(T.acc,0.15));
        r.setStroke(Color.web(T.acc,0.7)); r.setStrokeWidth(1.5);
        Label N = new Label("N");
        N.setStyle("-fx-font-size:22px;-fx-font-weight:900;-fx-text-fill:"+T.acc+
                   ";-fx-font-family:'Segoe UI';");
        ico.getChildren().addAll(r,N);
        ico.setEffect(new DropShadow(12,Color.web(T.acc,0.5)));

        VBox txt = new VBox(1);
        Label name = new Label("NexaPay");
        name.setStyle("-fx-font-size:19px;-fx-font-weight:900;-fx-text-fill:"+T.text+
                      ";-fx-font-family:'Segoe UI';");
        Label sub = new Label("Smart Banking");
        sub.setStyle("-fx-font-size:10px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
        txt.getChildren().addAll(name,sub);
        row.getChildren().addAll(ico,txt);

        clockLbl = new Label();
        clockLbl.setStyle("-fx-font-size:10px;-fx-text-fill:"+T.muted+";-fx-font-family:Consolas;");

        box.getChildren().addAll(row,clockLbl);
        return box;
    }

    private VBox buildVirtualCard() {
        VBox wrap = new VBox(10);
        wrap.setPadding(new Insets(14,14,14,14));
        wrap.setStyle("-fx-border-color:"+T.bg4+";-fx-border-width:0 0 1 0;");

        virtualCard = new StackPane();
        virtualCard.setPrefSize(202,118);

        Rectangle bg = new Rectangle(202,118);
        bg.setArcWidth(16); bg.setArcHeight(16);
        bg.setFill(new LinearGradient(0,0,1,1,true,CycleMethod.NO_CYCLE,
            new Stop(0,Color.web(T.bg4)),
            new Stop(0.5,Color.web(T.bg3)),
            new Stop(1,Color.web(T.acc2,0.4))));
        bg.setStroke(Color.web(T.acc,0.4)); bg.setStrokeWidth(1.2);

        // Decorative glow circles
        Circle c1 = new Circle(50); c1.setFill(Color.web(T.acc,0.06));
        StackPane.setAlignment(c1,Pos.TOP_RIGHT);
        StackPane.setMargin(c1,new Insets(-25,-25,0,0));
        Circle c2 = new Circle(35); c2.setFill(Color.web(T.acc3,0.07));
        StackPane.setAlignment(c2,Pos.BOTTOM_LEFT);
        StackPane.setMargin(c2,new Insets(0,0,-18,-12));

        VBox content = new VBox(5);
        content.setPadding(new Insets(12,14,10,14));

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        Label bank = new Label("NexaPay");
        bank.setStyle("-fx-font-size:10px;-fx-font-weight:900;-fx-text-fill:rgba(255,255,255,0.7);-fx-font-family:'Segoe UI';");
        Region sp = new Region(); HBox.setHgrow(sp,Priority.ALWAYS);
        Rectangle chip = new Rectangle(22,16);
        chip.setArcWidth(4); chip.setArcHeight(4);
        chip.setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
            new Stop(0,Color.web("#B8860B")), new Stop(1,Color.web("#FFD700"))));
        top.getChildren().addAll(bank,sp,chip);

        cardNoLbl = new Label("•••• •••• •••• ????");
        cardNoLbl.setStyle("-fx-font-size:11px;-fx-text-fill:rgba(255,255,255,0.5);-fx-font-family:Consolas;");

        holderLbl = new Label("INSERT YOUR CARD");
        holderLbl.setStyle("-fx-font-size:9px;-fx-font-weight:bold;-fx-text-fill:rgba(255,255,255,0.65);-fx-font-family:'Segoe UI';");

        balLbl = new Label("PKR ————");
        balLbl.setStyle("-fx-font-size:15px;-fx-font-weight:900;-fx-text-fill:"+T.acc+";-fx-font-family:'Segoe UI';");

        cardTypeLbl = new Label("——");
        cardTypeLbl.setStyle("-fx-font-size:9px;-fx-text-fill:rgba(255,255,255,0.4);-fx-font-family:'Segoe UI';");

        content.getChildren().addAll(top,cardNoLbl,holderLbl,balLbl,cardTypeLbl);
        virtualCard.getChildren().addAll(bg,c1,c2,content);
        virtualCard.setEffect(new DropShadow(20,Color.web(T.acc,0.3)));

        wrap.getChildren().add(virtualCard);
        return wrap;
    }

    private VBox buildNavSection() {
        VBox nav = new VBox(2);
        nav.setPadding(new Insets(10,8,10,8));
        VBox.setVgrow(nav,Priority.ALWAYS);

        String[][] items = {
            {"⬛","Dashboard"},
            {"✚","Create Account"},
            {"🔑","Login"},
            {"💳","Transactions"},
            {"↗","Fund Transfer"},
            {"🎯","Savings Goal"},
            {"🔒","Security"},
            {"📋","Statements"},
            {"👤","My Profile"},
        };

        for (String[] item : items) {
            HBox btn = navBtn(item[0], item[1]);
            nav.getChildren().add(btn);
        }
        return nav;
    }

    private HBox navBtn(String icon, String label) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10,14,10,14));
        row.setCursor(javafx.scene.Cursor.HAND);

        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size:14px;-fx-min-width:20px;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size:12.5px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");

        row.getChildren().addAll(ico,lbl);
        styleNavNormal(row,lbl);

        row.setOnMouseEntered(e -> { if(row!=activeNavBtn) styleNavHover(row,lbl); });
        row.setOnMouseExited(e  -> { if(row!=activeNavBtn) styleNavNormal(row,lbl); });
        row.setOnMouseClicked(e -> {
            if(activeNavBtn!=null) {
                Label old = (Label)((HBox)activeNavBtn).getChildren().get(1);
                styleNavNormal(activeNavBtn,old);
            }
            activeNavBtn = row;
            styleNavActive(row,lbl);

            VBox pane = switch(label) {
                case "Dashboard"      -> dashboardPane;
                case "Create Account" -> createPane;
                case "Login"          -> loginPane;
                case "Transactions"   -> txPane;
                case "Fund Transfer"  -> transferPane;
                case "Savings Goal"   -> savingsPane;
                case "Security"       -> securityPane;
                case "Statements"     -> statementPane;
                case "My Profile"     -> profilePane;
                default               -> dashboardPane;
            };
            showPane(pane);
        });
        return row;
    }

    private void styleNavNormal(HBox row, Label lbl) {
        row.setStyle("-fx-background-color:transparent;-fx-background-radius:10;");
        lbl.setStyle("-fx-font-size:12.5px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
    }
    private void styleNavHover(HBox row, Label lbl) {
        row.setStyle("-fx-background-color:"+T.bg4+";-fx-background-radius:10;");
        lbl.setStyle("-fx-font-size:12.5px;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
    }
    private void styleNavActive(HBox row, Label lbl) {
        row.setStyle("-fx-background-color:"+T.acc+";-fx-background-radius:10;" +
                     "-fx-effect:dropshadow(gaussian,"+T.acc+",10,0.4,0,0);");
        lbl.setStyle("-fx-font-size:12.5px;-fx-font-weight:bold;-fx-text-fill:#000;-fx-font-family:'Segoe UI';");
    }

    private HBox buildThemePicker() {
        HBox box = new HBox(8);
        box.setPadding(new Insets(10,14,10,14));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-border-color:"+T.bg4+";-fx-border-width:1 0 1 0;");

        Label lbl = new Label("Theme:");
        lbl.setStyle("-fx-font-size:10px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");

        for (Theme th : Theme.values()) {
            Circle dot = new Circle(9);
            dot.setFill(Color.web(th.acc));
            dot.setStroke(th==T ? Color.WHITE : Color.TRANSPARENT);
            dot.setStrokeWidth(2);
            dot.setCursor(javafx.scene.Cursor.HAND);
            Tooltip.install(dot, new Tooltip(th.name));
            dot.setOnMouseClicked(e -> switchTheme(th));
            box.getChildren().add(dot);
        }
        box.getChildren().add(0,lbl);
        return box;
    }

    private VBox buildStateSection() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(12,14,16,14));
        stateLbl = new Label("● IDLE  —  Ready");
        stateLbl.setStyle("-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:"+T.gold+
                          ";-fx-font-family:Consolas;");
        Label ver = new Label("NexaPay v2.0  |  OOP Project 3");
        ver.setStyle("-fx-font-size:9px;-fx-text-fill:"+T.muted+";-fx-font-family:Consolas;");
        ver.setWrapText(true);
        box.getChildren().addAll(stateLbl,ver);
        return box;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  THEME SWITCH
    // ═══════════════════════════════════════════════════════════════════════
    private void switchTheme(Theme th) {
        T = th;
        // Rebuild sidebar and refresh content
        sidebar.getChildren().clear();
        sidebar.setStyle("-fx-background-color:"+T.bg2+";-fx-border-color:"+T.bg4+";-fx-border-width:0 1 0 0;");
        sidebar.getChildren().addAll(buildLogoSection(),buildVirtualCard(),buildNavSection(),buildThemePicker(),buildStateSection());
        applyRootStyle();
        buildAllPanes();
        showPane(dashboardPane);
        updateCard();
        startClock();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  BUILD ALL CONTENT PANES
    // ═══════════════════════════════════════════════════════════════════════
    private void buildAllPanes() {
        dashboardPane  = buildDashboardPane();
        createPane     = buildCreatePane();
        loginPane      = buildLoginPane();
        txPane         = buildTxPane();
        transferPane   = buildTransferPane();
        savingsPane    = buildSavingsPane();
        securityPane   = buildSecurityPane();
        statementPane  = buildStatementPane();
        profilePane    = buildProfilePane();
    }

    private void showPane(VBox pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), pane);
        ft.setFromValue(0); ft.setToValue(1);
        contentArea.getChildren().setAll(pane);
        ft.play();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TOP BAR  (used in every pane)
    // ═══════════════════════════════════════════════════════════════════════
    private HBox topBar(String title, String subtitle) {
        HBox bar = new HBox(10);
        bar.setPadding(new Insets(22,28,18,28));
        bar.setAlignment(Pos.CENTER_LEFT);

        VBox titles = new VBox(3);
        Label t = new Label(title);
        t.setStyle("-fx-font-size:22px;-fx-font-weight:900;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
        Label s = new Label(subtitle);
        s.setStyle("-fx-font-size:12px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
        titles.getChildren().addAll(t,s);

        Region sp = new Region(); HBox.setHgrow(sp,Priority.ALWAYS);

        // Status chips
        HBox chips = new HBox(8);
        chips.setAlignment(Pos.CENTER);
        chips.getChildren().addAll(
            chip("● Online",  T.green),
            chip("🔒 Secure", T.acc),
            chip(atm.ok()?"✓ Logged In":"○ Guest", atm.ok()?T.green:T.gold)
        );

        bar.getChildren().addAll(titles,sp,chips);
        return bar;
    }

    private HBox chip(String txt, String color) {
        HBox c = new HBox();
        c.setPadding(new Insets(4,10,4,10));
        c.setStyle("-fx-background-color:"+color+"22;-fx-border-color:"+color+"66;" +
                   "-fx-border-width:1;-fx-border-radius:20;-fx-background-radius:20;");
        Label l = new Label(txt);
        l.setStyle("-fx-font-size:10px;-fx-font-weight:bold;-fx-text-fill:"+color+";-fx-font-family:'Segoe UI';");
        c.getChildren().add(l);
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  STAT CARD  (used in dashboard)
    // ═══════════════════════════════════════════════════════════════════════
    private VBox statCard(String icon, String title, String value, String color) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(18,20,18,20));
        card.setStyle("-fx-background-color:"+T.bg2+";-fx-border-color:"+color+"44;" +
                      "-fx-border-width:1;-fx-border-radius:14;-fx-background-radius:14;");
        card.setEffect(new DropShadow(15,Color.web(color,0.15)));

        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size:22px;");
        Label ttl = new Label(title);
        ttl.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
        Label val = new Label(value);
        val.setStyle("-fx-font-size:16px;-fx-font-weight:900;-fx-text-fill:"+color+";-fx-font-family:'Segoe UI';");
        val.setWrapText(true);

        card.getChildren().addAll(ico,ttl,val);
        HBox.setHgrow(card,Priority.ALWAYS);
        return card;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  GLASS CARD  (panel wrapper)
    // ═══════════════════════════════════════════════════════════════════════
    private VBox glassCard(String... children) { return glassCard(); }
    private VBox glassCard() {
        VBox c = new VBox(14);
        c.setPadding(new Insets(22,24,22,24));
        c.setStyle("-fx-background-color:"+T.bg2+";-fx-border-color:"+T.bg4+";" +
                   "-fx-border-width:1;-fx-border-radius:16;-fx-background-radius:16;");
        c.setEffect(new DropShadow(20,Color.web(T.acc,0.08)));
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FORM HELPERS
    // ═══════════════════════════════════════════════════════════════════════
    private Label sectionTitle(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:15px;-fx-font-weight:700;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
        return l;
    }
    private Label fieldLbl(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';-fx-padding:6 0 2 0;");
        return l;
    }
    private String fldStyle() {
        return "-fx-background-color:"+T.bg3+";-fx-text-fill:"+T.text+";" +
               "-fx-prompt-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';-fx-font-size:13px;" +
               "-fx-border-color:"+T.bg4+";-fx-border-width:1.5;" +
               "-fx-border-radius:8;-fx-background-radius:8;-fx-padding:10 14;";
    }
    private TextField   tf(String p){ TextField f=new TextField(); f.setPromptText(p); f.setStyle(fldStyle()); return f; }
    private PasswordField pf(String p){ PasswordField f=new PasswordField(); f.setPromptText(p); f.setStyle(fldStyle()); return f; }

    private Button primaryBtn(String txt, String color) {
        Button b = new Button(txt);
        String bs = "-fx-font-family:'Segoe UI';-fx-font-size:13px;-fx-font-weight:700;" +
                    "-fx-cursor:hand;-fx-border-radius:10;-fx-background-radius:10;-fx-padding:11 20;";
        String n  = bs+"-fx-text-fill:"+T.bg0+";-fx-background-color:"+color+";" +
                    "-fx-border-color:"+color+";-fx-border-width:0;";
        String h  = bs+"-fx-text-fill:"+T.bg0+";-fx-background-color:"+color+"CC;" +
                    "-fx-border-color:"+color+";-fx-border-width:0;" +
                    "-fx-effect:dropshadow(gaussian,"+color+",12,0.5,0,2);";
        b.setStyle(n);
        b.setEffect(new DropShadow(10,Color.web(color,0.4)));
        b.setOnMouseEntered(e->b.setStyle(h));
        b.setOnMouseExited(e->b.setStyle(n));
        return b;
    }
    private Button outlineBtn(String txt, String color) {
        Button b = new Button(txt);
        String bs = "-fx-font-family:'Segoe UI';-fx-font-size:12px;-fx-font-weight:600;" +
                    "-fx-cursor:hand;-fx-border-radius:10;-fx-background-radius:10;-fx-padding:10 18;";
        b.setStyle(bs+"-fx-text-fill:"+color+";-fx-background-color:"+color+"15;" +
                   "-fx-border-color:"+color+";-fx-border-width:1.5;");
        b.setOnMouseEntered(e->b.setStyle(bs+"-fx-text-fill:"+T.bg0+";-fx-background-color:"+color+";" +
                   "-fx-border-color:"+color+";-fx-border-width:1.5;"));
        b.setOnMouseExited(e->b.setStyle(bs+"-fx-text-fill:"+color+";-fx-background-color:"+color+"15;" +
                   "-fx-border-color:"+color+";-fx-border-width:1.5;"));
        return b;
    }

    private VBox infoBox(String msg, String color) {
        VBox b = new VBox(4);
        b.setPadding(new Insets(12,14,12,14));
        b.setStyle("-fx-background-color:"+color+"12;-fx-border-color:"+color+"55;" +
                   "-fx-border-width:1;-fx-border-radius:10;-fx-background-radius:10;");
        for (String line : msg.split("\n")) {
            Label l = new Label(line);
            l.setStyle("-fx-font-size:11px;-fx-text-fill:"+color+";-fx-font-family:'Segoe UI';");
            b.getChildren().add(l);
        }
        return b;
    }

    private ScrollPane scrollWrap(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true); sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;" +
                    "-fx-border-width:0;");
        VBox.setVgrow(sp,Priority.ALWAYS);
        return sp;
    }

    private Separator div() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:"+T.bg4+";");
        return s;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  DASHBOARD PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildDashboardPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");

        pane.getChildren().add(topBar("Dashboard","Welcome to NexaPay — Your Smart Bank"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        // Stat cards row
        HBox stats = new HBox(16);
        BankAccount acc = atm.active();
        String bal  = acc!=null ? acc.getFmtBalance()  : "——";
        String type = acc!=null ? acc.getAccountType().name() : "——";
        String stat = acc!=null ? (acc.isFrozen()?"🔒 Frozen":"✅ Active") : "○ Guest";

        stats.getChildren().addAll(
            statCard("💰","Current Balance", bal,        T.green),
            statCard("💎","Account Type",    type,       T.acc),
            statCard("✅","Account Status",  stat,       T.gold),
            statCard("🔒","Security",        "PIN Protected", T.acc3)
        );

        // Quick actions
        VBox quickCard = glassCard();
        quickCard.getChildren().add(sectionTitle("Quick Actions"));
        HBox quickBtns = new HBox(12);

        Button[] qBtns = {
            primaryBtn("💳  Deposit",   T.green),
            primaryBtn("⬆  Withdraw",  T.gold),
            primaryBtn("↗  Transfer",  T.acc),
            primaryBtn("📋  Statement", T.acc3),
        };
        qBtns[0].setOnAction(e->showPane(txPane));
        qBtns[1].setOnAction(e->showPane(txPane));
        qBtns[2].setOnAction(e->showPane(transferPane));
        qBtns[3].setOnAction(e->showPane(statementPane));
        for (Button b : qBtns) quickBtns.getChildren().add(b);
        quickCard.getChildren().add(quickBtns);

        // Features overview
        VBox featCard = glassCard();
        featCard.getChildren().add(sectionTitle("Available Features"));
        GridPane grid = new GridPane();
        grid.setHgap(14); grid.setVgap(10);

        String[][] feats = {
            {"✚","Create Account","Register a new NexaPay account"},
            {"🔑","Secure Login","Card + PIN authentication"},
            {"💳","Transactions","Deposit & Withdraw cash"},
            {"↗","Fund Transfer","Send money instantly"},
            {"🎯","Savings Goal","Track your saving targets"},
            {"🔒","Security","Change PIN, Freeze account"},
            {"📋","Statements","Mini & Full statements"},
            {"👤","My Profile","View account details"},
            {"🎨","3 Themes","Midnight, Emerald, Royal"},
            {"📊","Daily Limits","Tier-based withdrawal limits"},
        };
        for (int i=0;i<feats.length;i++) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(8,12,8,12));
            item.setStyle("-fx-background-color:"+T.bg3+";-fx-border-radius:8;-fx-background-radius:8;");
            Label ic = new Label(feats[i][0]); ic.setStyle("-fx-font-size:14px;");
            VBox txt = new VBox(1);
            Label tl = new Label(feats[i][1]);
            tl.setStyle("-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Label sl = new Label(feats[i][2]);
            sl.setStyle("-fx-font-size:10px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
            txt.getChildren().addAll(tl,sl);
            item.getChildren().addAll(ic,txt);
            grid.add(item, i%2, i/2);
            GridPane.setHgrow(item,Priority.ALWAYS);
        }
        featCard.getChildren().add(grid);

        // Demo accounts
        VBox demoCard = glassCard();
        demoCard.getChildren().add(sectionTitle("Demo Accounts"));
        HBox demoRow = new HBox(14);
        String[][] demos = {
            {"1234567890","1234","Ali Hassan","PREMIUM","PKR 1,50,000"},
            {"0987654321","5678","Sara Khan","SAVINGS","PKR 75,000"},
            {"1111222233","0000","Ahmed Raza","CURRENT","PKR 25,000"},
        };
        for (String[] d : demos) {
            VBox dc = new VBox(6);
            dc.setPadding(new Insets(14,16,14,16));
            dc.setStyle("-fx-background-color:"+T.bg3+";-fx-border-color:"+T.acc+"44;" +
                        "-fx-border-width:1;-fx-border-radius:12;-fx-background-radius:12;");
            HBox.setHgrow(dc,Priority.ALWAYS);
            Label nm = new Label(d[2]); nm.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Label cn = new Label("Card: "+d[0]); cn.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.muted+";-fx-font-family:Consolas;");
            Label pn = new Label("PIN:  "+d[1]);  pn.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.muted+";-fx-font-family:Consolas;");
            Label tp = new Label(d[3]);            tp.setStyle("-fx-font-size:10px;-fx-text-fill:"+T.acc+";-fx-font-family:'Segoe UI';-fx-font-weight:bold;");
            Label bl = new Label(d[4]);            bl.setStyle("-fx-font-size:13px;-fx-font-weight:900;-fx-text-fill:"+T.green+";-fx-font-family:'Segoe UI';");
            Button use = outlineBtn("Use →", T.acc);
            final String card=d[0], pin=d[1];
            use.setOnAction(e->{ loginCardFld.setText(card); loginPinFld.clear(); showPane(loginPane); });
            dc.getChildren().addAll(nm,cn,pn,tp,bl,use);
            demoRow.getChildren().add(dc);
        }
        demoCard.getChildren().add(demoRow);

        body.getChildren().addAll(stats,quickCard,featCard,demoCard);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  CREATE ACCOUNT PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildCreatePane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Create Account","Register a new NexaPay account"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        // Form card
        VBox form = glassCard();
        form.getChildren().add(sectionTitle("Account Registration"));

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(4);

        caName  = tf("e.g. Ali Hassan");
        caCard  = tf("10-digit unique number");
        caEmail = tf("email@example.com");
        caPhone = tf("+92-300-0000000");
        caPin   = pf("4-digit PIN");
        caConf  = pf("Confirm PIN");
        caBalance = tf("Minimum PKR 500");
        caTypeBox = new ComboBox<>();
        caTypeBox.getItems().addAll("SAVINGS","CURRENT","PREMIUM");
        caTypeBox.setValue("SAVINGS");
        caTypeBox.setMaxWidth(Double.MAX_VALUE);
        caTypeBox.setStyle(fldStyle());

        String[][] formFields = {
            {"Full Name",""},{"Card Number",""},
            {"Email",""},{"Phone",""},
            {"PIN",""},{"Confirm PIN",""},
            {"Opening Balance",""},{"Account Type",""},
        };
        Node[] nodes = {caName,caCard,caEmail,caPhone,caPin,caConf,caBalance,caTypeBox};
        for (int i=0;i<nodes.length;i++) {
            Label fl = fieldLbl(formFields[i][0]+":");
            grid.add(fl,    (i%2)*2,   i/2*2);
            grid.add(nodes[i], (i%2)*2, i/2*2+1);
            GridPane.setHgrow(nodes[i],Priority.ALWAYS);
        }
        grid.getColumnConstraints().addAll(
            colConst(), colConst(14), colConst(), colConst()
        );
        form.getChildren().add(grid);

        HBox btnRow = new HBox(12);
        Button createBtn = primaryBtn("✚  Create Account", T.green);
        Button clearBtn  = outlineBtn("Clear Form", T.muted);
        createBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(createBtn,Priority.ALWAYS);
        HBox.setHgrow(clearBtn,Priority.ALWAYS);
        createBtn.setOnAction(e->handleCreate());
        clearBtn.setOnAction(e->{ caName.clear();caCard.clear();caEmail.clear();
            caPhone.clear();caPin.clear();caConf.clear();caBalance.clear(); });
        btnRow.getChildren().addAll(createBtn,clearBtn);
        form.getChildren().add(btnRow);

        // Info card
        VBox info = glassCard();
        info.getChildren().add(sectionTitle("Account Types & Daily Limits"));
        HBox tiers = new HBox(14);
        String[][] tierData = {
            {"💰","SAVINGS","Daily Limit: PKR 50,000","Best for personal savings",T.green},
            {"🏦","CURRENT","Daily Limit: PKR 2,00,000","Best for business use",T.acc},
            {"💎","PREMIUM","Daily Limit: PKR 5,00,000","Exclusive benefits",T.gold},
        };
        for (String[] td : tierData) {
            VBox tc = new VBox(6);
            tc.setPadding(new Insets(14,16,14,16));
            tc.setStyle("-fx-background-color:"+T.bg3+";-fx-border-color:"+td[4]+"55;" +
                        "-fx-border-width:1;-fx-border-radius:12;-fx-background-radius:12;");
            HBox.setHgrow(tc,Priority.ALWAYS);
            Label ic = new Label(td[0]); ic.setStyle("-fx-font-size:22px;");
            Label nm = new Label(td[1]); nm.setStyle("-fx-font-size:13px;-fx-font-weight:900;-fx-text-fill:"+td[4]+";-fx-font-family:'Segoe UI';");
            Label lm = new Label(td[2]); lm.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Label ds = new Label(td[3]); ds.setStyle("-fx-font-size:10px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
            tc.getChildren().addAll(ic,nm,lm,ds);
            tiers.getChildren().add(tc);
        }
        info.getChildren().add(tiers);

        body.getChildren().addAll(form,info);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  LOGIN PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildLoginPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Login","Authenticate with your card & PIN"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        HBox row = new HBox(20);

        // Login card
        VBox form = glassCard();
        form.setPrefWidth(380);
        form.getChildren().add(sectionTitle("Card Authentication"));

        loginCardFld = tf("Enter 10-digit card number");
        loginPinFld  = pf("Enter 4-digit PIN");

        Button insBtn  = primaryBtn("🔌  INSERT CARD", T.acc);
        Button pinBtn  = primaryBtn("✓  CONFIRM PIN", T.green);
        Button ejBtn   = outlineBtn("⏏  EJECT CARD",  T.red);
        insBtn.setMaxWidth(Double.MAX_VALUE);
        pinBtn.setMaxWidth(Double.MAX_VALUE);
        ejBtn.setMaxWidth(Double.MAX_VALUE);

        insBtn.setOnAction(e->handleInsert());
        pinBtn.setOnAction(e->handlePin());
        ejBtn.setOnAction(e->handleEject());
        loginCardFld.setOnAction(e->handleInsert());
        loginPinFld.setOnAction(e->handlePin());

        // PIN dots display
        HBox pinDots = new HBox(8);
        pinDots.setAlignment(Pos.CENTER);
        for (int i=0;i<4;i++) {
            Circle dot = new Circle(6);
            dot.setFill(Color.web(T.bg4));
            dot.setStroke(Color.web(T.muted)); dot.setStrokeWidth(1.5);
            pinDots.getChildren().add(dot);
        }
        loginPinFld.textProperty().addListener((obs,ov,nv)->{
            for (int i=0;i<4;i++) {
                Circle d = (Circle)pinDots.getChildren().get(i);
                d.setFill(i<nv.length() ? Color.web(T.acc) : Color.web(T.bg4));
            }
        });

        form.getChildren().addAll(
            fieldLbl("Card Number:"), loginCardFld,
            fieldLbl("PIN:"), loginPinFld,
            pinDots,
            insBtn, pinBtn, div(), ejBtn
        );

        // Security info
        VBox secInfo = glassCard();
        secInfo.setPrefWidth(300);
        secInfo.getChildren().add(sectionTitle("Security Info"));
        secInfo.getChildren().addAll(
            infoBox("🔒  3 wrong PINs = Card blocked\n" +
                    "✅  Encrypted PIN storage\n" +
                    "⏏   Always eject when done", T.acc),
            div(),
            sectionTitle("Demo Accounts"),
            infoBox("1234567890  |  PIN: 1234\nAli Hassan  |  PREMIUM\n\n" +
                    "0987654321  |  PIN: 5678\nSara Khan   |  SAVINGS\n\n" +
                    "1111222233  |  PIN: 0000\nAhmed Raza  |  CURRENT", T.muted)
        );

        HBox.setHgrow(form,Priority.ALWAYS);
        row.getChildren().addAll(form,secInfo);
        body.getChildren().add(row);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TRANSACTIONS PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildTxPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Transactions","Deposit and Withdraw cash"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        HBox row = new HBox(20);

        // Left: operations
        VBox ops = glassCard();
        ops.setPrefWidth(400);
        ops.getChildren().add(sectionTitle("Banking Operations"));

        txAmtFld = tf("Enter amount in PKR");
        txAmtFld.setOnAction(e->handleDeposit());

        Button balBtn = primaryBtn("📊  Check Balance", T.acc);
        Button depBtn = primaryBtn("⬇  Deposit",       T.green);
        Button witBtn = primaryBtn("⬆  Withdraw",      T.gold);
        for (Button b : new Button[]{balBtn,depBtn,witBtn}) b.setMaxWidth(Double.MAX_VALUE);
        balBtn.setOnAction(e->handleBalance());
        depBtn.setOnAction(e->handleDeposit());
        witBtn.setOnAction(e->handleWithdraw());

        ops.getChildren().addAll(fieldLbl("Amount (PKR):"),txAmtFld,balBtn,depBtn,witBtn);
        if (!atm.ok()) ops.getChildren().add(infoBox("🔒  Login required to make transactions.\nGo to Login → Insert card → Enter PIN.",T.gold));

        // Right: quick amounts
        VBox quick = glassCard();
        quick.getChildren().add(sectionTitle("Quick Amounts"));
        int[][] qAmts = {{500,1000},{2000,5000},{10000,20000},{50000,100000}};
        VBox qCol = new VBox(10);
        for (int[] pair : qAmts) {
            HBox qRow = new HBox(10);
            for (int amt : pair) {
                Button qb = outlineBtn("PKR "+String.format("%,d",amt), T.acc);
                qb.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(qb,Priority.ALWAYS);
                final int a = amt;
                qb.setOnAction(e->{ txAmtFld.setText(String.valueOf(a)); pulse(txAmtFld); });
                qRow.getChildren().add(qb);
            }
            qCol.getChildren().add(qRow);
        }
        quick.getChildren().add(qCol);

        HBox.setHgrow(ops,Priority.ALWAYS);
        row.getChildren().addAll(ops,quick);

        // Daily limit progress
        VBox limitCard = glassCard();
        limitCard.getChildren().add(sectionTitle("Daily Withdrawal Limit"));
        if (atm.ok() && atm.active()!=null) {
            BankAccount acc = atm.active();
            double used = acc.getDailyUsed();
            double lim  = acc.getDailyLimit();
            int pct = (int)((used/lim)*100);

            HBox limRow = new HBox(20);
            limRow.setAlignment(Pos.CENTER_LEFT);
            Label usedLbl = new Label("Used: PKR "+String.format("%,.0f",used));
            usedLbl.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Label limLbl = new Label("Limit: PKR "+String.format("%,.0f",lim));
            limLbl.setStyle("-fx-font-size:13px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
            limRow.getChildren().addAll(usedLbl,limLbl);

            StackPane bar = new StackPane();
            bar.setPrefHeight(12); bar.setMaxWidth(Double.MAX_VALUE);
            Rectangle bg = new Rectangle(); bg.setHeight(12); bg.setArcWidth(6); bg.setArcHeight(6);
            bg.setFill(Color.web(T.bg4));
            bg.widthProperty().bind(bar.widthProperty());
            Rectangle fill = new Rectangle();
            fill.setHeight(12); fill.setArcWidth(6); fill.setArcHeight(6);
            fill.setFill(pct>80?Color.web(T.red):pct>50?Color.web(T.gold):Color.web(T.green));
            fill.widthProperty().bind(bar.widthProperty().multiply(pct/100.0));
            StackPane.setAlignment(fill,Pos.CENTER_LEFT);
            bar.getChildren().addAll(bg,fill);

            limitCard.getChildren().addAll(limRow,bar);
        } else {
            limitCard.getChildren().add(infoBox("Login to view your daily limits.",T.muted));
        }

        body.getChildren().addAll(row,limitCard);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FUND TRANSFER PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildTransferPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Fund Transfer","Send money to any NexaPay account"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));
        HBox row = new HBox(20);

        VBox form = glassCard();
        form.setPrefWidth(420);
        form.getChildren().add(sectionTitle("Send Money"));

        tfTo   = tf("Recipient card number (10 digits)");
        tfAmt  = tf("Amount in PKR");
        tfNote = tf("Note / Reference (optional)");

        Button sendBtn = primaryBtn("↗  SEND MONEY", T.acc);
        sendBtn.setMaxWidth(Double.MAX_VALUE);
        sendBtn.setOnAction(e->handleTransfer());
        if (!atm.ok()) form.getChildren().add(infoBox("🔒  Login required.",T.gold));

        form.getChildren().addAll(
            fieldLbl("Recipient Card:"), tfTo,
            fieldLbl("Amount (PKR):"),   tfAmt,
            fieldLbl("Note:"),           tfNote,
            sendBtn
        );

        VBox info = glassCard();
        info.getChildren().add(sectionTitle("Transfer Info"));
        info.getChildren().addAll(
            infoBox("✅  Instant within NexaPay\n✅  Full history logged\n✅  Add a note/reference\n❌  Cannot transfer to own account",T.green),
            div(),
            sectionTitle("Transfer Flow"),
            infoBox("1. Enter recipient card number\n2. Enter amount\n3. Add optional note\n4. Click Send Money\n5. Check statement for confirmation",T.acc)
        );

        HBox.setHgrow(form,Priority.ALWAYS);
        row.getChildren().addAll(form,info);
        body.getChildren().add(row);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SAVINGS GOAL PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildSavingsPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Savings Goal","Set targets and track your progress"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        HBox row = new HBox(20);

        VBox form = glassCard();
        form.setPrefWidth(400);
        form.getChildren().add(sectionTitle("Set a Goal"));

        sgName   = tf("e.g. iPhone, Car, Vacation");
        sgTarget = tf("Target amount in PKR");
        sgAdd    = tf("Amount to add this session");

        Button setBtn  = primaryBtn("🎯  Set Goal",      T.acc);
        Button addBtn  = primaryBtn("➕  Add to Goal",   T.green);
        Button viewBtn = outlineBtn("📊  View Progress", T.gold);
        setBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setMaxWidth(Double.MAX_VALUE);
        viewBtn.setMaxWidth(Double.MAX_VALUE);

        setBtn.setOnAction(e->handleSetGoal());
        addBtn.setOnAction(e->handleAddGoal());
        viewBtn.setOnAction(e->handleViewGoal());
        if (!atm.ok()) form.getChildren().add(infoBox("🔒  Login required.",T.gold));

        form.getChildren().addAll(
            fieldLbl("Goal Name:"),    sgName,
            fieldLbl("Target (PKR):"), sgTarget,
            setBtn, div(),
            fieldLbl("Add (PKR):"),    sgAdd,
            addBtn, viewBtn
        );

        // Progress card
        VBox prog = glassCard();
        prog.getChildren().add(sectionTitle("Current Progress"));

        if (atm.ok() && atm.active()!=null && atm.active().getGoalTarget()>0) {
            BankAccount acc = atm.active();
            int pct = acc.getGoalPct();

            Label gn = new Label(acc.getGoalName());
            gn.setStyle("-fx-font-size:15px;-fx-font-weight:bold;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Label gt = new Label("Target: PKR "+String.format("%,.2f",acc.getGoalTarget()));
            gt.setStyle("-fx-font-size:12px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';");
            Label gs = new Label("Saved: PKR "+String.format("%,.2f",acc.getGoalSaved()));
            gs.setStyle("-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:"+T.green+";-fx-font-family:'Segoe UI';");

            // Progress bar
            StackPane bar = new StackPane();
            bar.setPrefHeight(18); bar.setMaxWidth(Double.MAX_VALUE);
            Rectangle bg2 = new Rectangle(); bg2.setHeight(18); bg2.setArcWidth(9); bg2.setArcHeight(9);
            bg2.setFill(Color.web(T.bg4)); bg2.widthProperty().bind(bar.widthProperty());
            Rectangle fill = new Rectangle();
            fill.setHeight(18); fill.setArcWidth(9); fill.setArcHeight(9);
            fill.setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop(0,Color.web(T.acc)),new Stop(1,Color.web(T.green))));
            fill.widthProperty().bind(bar.widthProperty().multiply(pct/100.0));
            StackPane.setAlignment(fill,Pos.CENTER_LEFT);
            Label pctLbl = new Label(pct+"%");
            pctLbl.setStyle("-fx-font-size:10px;-fx-font-weight:bold;-fx-text-fill:#000;-fx-font-family:'Segoe UI';");
            bar.getChildren().addAll(bg2,fill,pctLbl);

            // Emoji milestones
            HBox miles = new HBox(8);
            int[] milePcts = {25,50,75,100};
            for (int mp : milePcts) {
                Label m = new Label(mp+"% "+(pct>=mp?"✅":"⬜"));
                m.setStyle("-fx-font-size:11px;-fx-text-fill:"+(pct>=mp?T.green:T.muted)+";-fx-font-family:'Segoe UI';");
                miles.getChildren().add(m);
            }

            prog.getChildren().addAll(gn,gt,gs,bar,miles);
        } else {
            prog.getChildren().add(infoBox("No goal set yet.\nCreate a goal to start tracking!",T.muted));
        }

        HBox.setHgrow(form,Priority.ALWAYS);
        HBox.setHgrow(prog,Priority.ALWAYS);
        row.getChildren().addAll(form,prog);
        body.getChildren().add(row);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SECURITY PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildSecurityPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Security Center","Manage PIN & protect your account"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));
        HBox row = new HBox(20);

        VBox pinCard = glassCard();
        pinCard.setPrefWidth(380);
        pinCard.getChildren().add(sectionTitle("Change PIN"));

        cpOld  = pf("Current PIN");
        cpNew  = pf("New PIN (4 digits)");
        cpConf = pf("Confirm New PIN");

        Button chgBtn = primaryBtn("🔑  Change PIN", T.acc);
        chgBtn.setMaxWidth(Double.MAX_VALUE);
        chgBtn.setOnAction(e->handleChangePin());
        if (!atm.ok()) pinCard.getChildren().add(infoBox("🔒  Login required.",T.gold));

        pinCard.getChildren().addAll(
            fieldLbl("Current PIN:"), cpOld,
            fieldLbl("New PIN:"),     cpNew,
            fieldLbl("Confirm PIN:"), cpConf,
            chgBtn
        );

        VBox freezeCard = glassCard();
        freezeCard.getChildren().add(sectionTitle("Account Lock"));
        freezeCard.getChildren().add(infoBox("⚠  Freeze: Locks ALL transactions immediately.\n" +
                "⚠  Frozen card is auto-ejected.\n" +
                "✅  You can unfreeze anytime.",T.red));

        Button frzBtn   = primaryBtn("🔒  FREEZE Account",   T.red);
        Button unfrzBtn = outlineBtn("🔓  UNFREEZE Account", T.green);
        frzBtn.setMaxWidth(Double.MAX_VALUE);
        unfrzBtn.setMaxWidth(Double.MAX_VALUE);
        frzBtn.setOnAction(e->{ showAlert("Freeze Account",atm.freeze()); handleEject(); });
        unfrzBtn.setOnAction(e->showAlert("Unfreeze Account",atm.unfreeze()));
        freezeCard.getChildren().addAll(frzBtn,unfrzBtn);

        // Security tips
        VBox tips = glassCard();
        tips.getChildren().add(sectionTitle("Security Tips"));
        tips.getChildren().add(infoBox(
            "🔐  Never share your PIN with anyone\n" +
            "👀  Always check surroundings at ATM\n" +
            "⏏   Always eject card after session\n" +
            "🔒  Freeze account if card is lost\n" +
            "🔑  Change PIN regularly",T.acc));

        HBox.setHgrow(pinCard,Priority.ALWAYS);
        HBox.setHgrow(freezeCard,Priority.ALWAYS);
        row.getChildren().addAll(pinCard,freezeCard);
        body.getChildren().addAll(row,tips);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  STATEMENT PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildStatementPane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("Statements","View your full transaction history"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        HBox btnRow = new HBox(14);
        Button miniBtn = primaryBtn("📄  Mini Statement",   T.acc);
        Button fullBtn = primaryBtn("📋  Full Statement",   T.acc3);
        Button summBtn = primaryBtn("👤  Account Summary",  T.green);
        miniBtn.setOnAction(e->showStatementResult(atm.mini()));
        fullBtn.setOnAction(e->showStatementResult(atm.full()));
        summBtn.setOnAction(e->showStatementResult(atm.checkBalance()));
        btnRow.getChildren().addAll(miniBtn,fullBtn,summBtn);

        // Statement output area
        VBox outputCard = glassCard();
        outputCard.getChildren().add(sectionTitle("Statement Output"));
        TextArea out = new TextArea();
        out.setEditable(false); out.setPrefHeight(380); out.setWrapText(true);
        out.setStyle("-fx-control-inner-background:"+T.bg3+";-fx-text-fill:"+T.text+";" +
                     "-fx-font-family:Consolas;-fx-font-size:11.5px;" +
                     "-fx-border-color:"+T.bg4+";-fx-border-width:1;" +
                     "-fx-border-radius:8;-fx-background-radius:8;");
        out.setText(atm.ok() ? atm.mini() : "Login to view your statements.");
        outputCard.getChildren().add(out);

        miniBtn.setOnAction(e->{if(atm.ok())out.setText(atm.mini()); else out.setText("Login required.");});
        fullBtn.setOnAction(e->{if(atm.ok())out.setText(atm.full()); else out.setText("Login required.");});
        summBtn.setOnAction(e->{if(atm.ok())out.setText(atm.checkBalance()); else out.setText("Login required.");});

        // Tx table-style list
        VBox txList = glassCard();
        txList.getChildren().add(sectionTitle("Recent Transactions"));
        if (atm.ok() && atm.active()!=null) {
            java.util.List<BankAccount.Txn> list = atm.active().getTxns();
            int start = Math.max(0,list.size()-8);
            for (int i=list.size()-1;i>=start;i--) {
                BankAccount.Txn tx = list.get(i);
                HBox txRow = new HBox(12);
                txRow.setAlignment(Pos.CENTER_LEFT);
                txRow.setPadding(new Insets(10,14,10,14));
                txRow.setStyle("-fx-background-color:"+T.bg3+";-fx-border-radius:8;-fx-background-radius:8;");

                boolean isOut = tx.getType().contains("OUT")||tx.getType().equals("WITHDRAW")||tx.getType().equals("GOAL_SAVE");
                Label ic = new Label(isOut ? "⬆" : "⬇");
                ic.setStyle("-fx-font-size:14px;-fx-text-fill:"+(isOut?T.red:T.green)+";");
                Label tp = new Label(tx.getType());
                tp.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';-fx-min-width:100;");
                Region spr = new Region(); HBox.setHgrow(spr,Priority.ALWAYS);
                Label am = new Label((isOut?"-":"+")+String.format("PKR %,.2f",tx.getAmount()));
                am.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:"+(isOut?T.red:T.green)+";-fx-font-family:'Segoe UI';");
                txRow.getChildren().addAll(ic,tp,spr,am);
                txList.getChildren().add(txRow);
            }
        } else {
            txList.getChildren().add(infoBox("Login to view transactions.",T.muted));
        }

        body.getChildren().addAll(btnRow,outputCard,txList);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    private void showStatementResult(String r) { /* handled inline above */ }

    // ═══════════════════════════════════════════════════════════════════════
    //  MY PROFILE PANE
    // ═══════════════════════════════════════════════════════════════════════
    private VBox buildProfilePane() {
        VBox pane = new VBox(0);
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().add(topBar("My Profile","Your account information"));

        VBox body = new VBox(20);
        body.setPadding(new Insets(0,28,24,28));

        if (!atm.ok() || atm.active()==null) {
            VBox info = glassCard();
            info.getChildren().add(infoBox("🔒  Please login to view your profile.",T.gold));
            body.getChildren().add(info);
            pane.getChildren().add(scrollWrap(body));
            return pane;
        }

        BankAccount acc = atm.active();

        HBox row = new HBox(20);

        // Profile card
        VBox profCard = glassCard();
        profCard.setPrefWidth(360);

        // Avatar
        StackPane avatar = new StackPane();
        avatar.setMaxWidth(80); avatar.setMaxHeight(80);
        Circle av = new Circle(40);
        av.setFill(new LinearGradient(0,0,1,1,true,CycleMethod.NO_CYCLE,
            new Stop(0,Color.web(T.acc)), new Stop(1,Color.web(T.acc2))));
        av.setEffect(new DropShadow(15,Color.web(T.acc,0.5)));
        Label initials = new Label(acc.getAccountHolder().substring(0,1).toUpperCase());
        initials.setStyle("-fx-font-size:32px;-fx-font-weight:900;-fx-text-fill:#000;-fx-font-family:'Segoe UI';");
        avatar.getChildren().addAll(av,initials);
        avatar.setMaxSize(80,80);

        Label nm  = new Label(acc.getAccountHolder());
        nm.setStyle("-fx-font-size:18px;-fx-font-weight:900;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
        Label typ = new Label(acc.getAccountType().name()+" Account");
        typ.setStyle("-fx-font-size:12px;-fx-text-fill:"+T.acc+";-fx-font-family:'Segoe UI';-fx-font-weight:bold;");
        Label sts = new Label(acc.isFrozen() ? "🔒 FROZEN" : "✅ ACTIVE");
        sts.setStyle("-fx-font-size:12px;-fx-text-fill:"+(acc.isFrozen()?T.red:T.green)+";-fx-font-family:'Segoe UI';");

        profCard.getChildren().addAll(avatar,nm,typ,sts,div());

        String[][] details = {
            {"Card Number", acc.getAccountNumber()},
            {"Email",       acc.getEmail()},
            {"Phone",       acc.getPhone()},
            {"Balance",     acc.getFmtBalance()},
            {"Daily Limit", "PKR "+String.format("%,.0f",acc.getDailyLimit())},
            {"Used Today",  "PKR "+String.format("%,.0f",acc.getDailyUsed())},
        };
        for (String[] d : details) {
            HBox dr = new HBox(10);
            dr.setAlignment(Pos.CENTER_LEFT);
            dr.setPadding(new Insets(8,0,8,0));
            dr.setStyle("-fx-border-color:transparent transparent "+T.bg4+" transparent;-fx-border-width:0 0 1 0;");
            Label k = new Label(d[0]+":"); k.setStyle("-fx-font-size:11px;-fx-text-fill:"+T.muted+";-fx-font-family:'Segoe UI';-fx-min-width:100;");
            Label v = new Label(d[1]);      v.setStyle("-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Region spr = new Region(); HBox.setHgrow(spr,Priority.ALWAYS);
            dr.getChildren().addAll(k,spr,v);
            profCard.getChildren().add(dr);
        }

        // Savings goal in profile
        VBox goalCard = glassCard();
        goalCard.getChildren().add(sectionTitle("Savings Goal Status"));
        if (acc.getGoalTarget()>0) {
            int pct = acc.getGoalPct();
            Label gn = new Label(acc.getGoalName());
            gn.setStyle("-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:"+T.text+";-fx-font-family:'Segoe UI';");
            Label gt = new Label(String.format("%.0f",acc.getGoalSaved())+" / "+String.format("%.0f",acc.getGoalTarget())+" PKR  ("+pct+"%)");
            gt.setStyle("-fx-font-size:12px;-fx-text-fill:"+T.green+";-fx-font-family:'Segoe UI';");

            StackPane bar = new StackPane();
            bar.setPrefHeight(14); bar.setMaxWidth(Double.MAX_VALUE);
            Rectangle bg3 = new Rectangle(); bg3.setHeight(14); bg3.setArcWidth(7); bg3.setArcHeight(7);
            bg3.setFill(Color.web(T.bg4)); bg3.widthProperty().bind(bar.widthProperty());
            Rectangle fill = new Rectangle(); fill.setHeight(14); fill.setArcWidth(7); fill.setArcHeight(7);
            fill.setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop(0,Color.web(T.acc)),new Stop(1,Color.web(T.green))));
            fill.widthProperty().bind(bar.widthProperty().multiply(pct/100.0));
            StackPane.setAlignment(fill,Pos.CENTER_LEFT);
            bar.getChildren().addAll(bg3,fill);
            goalCard.getChildren().addAll(gn,gt,bar);
        } else {
            goalCard.getChildren().add(infoBox("No savings goal set.\nGo to Savings Goal to create one!",T.muted));
        }

        HBox.setHgrow(profCard,Priority.ALWAYS);
        HBox.setHgrow(goalCard,Priority.ALWAYS);
        row.getChildren().addAll(profCard,goalCard);
        body.getChildren().add(row);
        pane.getChildren().add(scrollWrap(body));
        return pane;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  EVENT HANDLERS
    // ═══════════════════════════════════════════════════════════════════════
    private void handleCreate() {
        double bal=0;
        try { bal=Double.parseDouble(caBalance.getText().trim()); }
        catch(Exception e){ showAlert("Error","Opening balance must be a number."); return; }
        String r = atm.createAccount(caName.getText(),caCard.getText(),caEmail.getText(),
            caPhone.getText(),caPin.getText(),caConf.getText(),bal,caTypeBox.getValue());
        showAlert(r.startsWith("SUCCESS")?"Account Created":"Error",r);
        if (r.startsWith("SUCCESS")) {
            String cn=caCard.getText().trim();
            caName.clear();caCard.clear();caEmail.clear();caPhone.clear();caPin.clear();caConf.clear();caBalance.clear();
            loginCardFld.setText(cn); showPane(loginPane);
        }
    }

    private void handleInsert() {
        String r = atm.insertCard(loginCardFld.getText());
        showAlert(r.startsWith("SUCCESS")?"Card Accepted":"Error",r);
        updateCard();
        if(r.startsWith("SUCCESS")){ loginCardFld.setDisable(true); pulse(loginPinFld); }
    }

    private void handlePin() {
        String r = atm.enterPin(loginPinFld.getText());
        loginPinFld.clear();
        showAlert(r.startsWith("SUCCESS")?"Authenticated":"Error",r);
        updateCard();
        if(r.startsWith("SUCCESS")){ loginPinFld.setDisable(true); rebuildAndShow(dashboardPane=buildDashboardPane()); }
        if(r.contains("blocked")) resetUI();
    }

    private void handleBalance()  { showAlert("Balance",atm.checkBalance()); }
    private void handleDeposit()  { double a=parseAmt(txAmtFld); if(a<0)return; showAlert("Deposit",atm.deposit(a)); updateCard(); txAmtFld.clear(); rebuildTx(); }
    private void handleWithdraw() { double a=parseAmt(txAmtFld); if(a<0)return; showAlert("Withdraw",atm.withdraw(a)); updateCard(); txAmtFld.clear(); rebuildTx(); }
    private void handleTransfer() { double a=parseAmt(tfAmt); if(a<0)return;
        String r=atm.transfer(tfTo.getText(),a,tfNote.getText());
        showAlert(r.startsWith("SUCCESS")?"Transfer Sent":"Error",r); updateCard();
        if(r.startsWith("SUCCESS")){tfTo.clear();tfAmt.clear();tfNote.clear();} }
    private void handleSetGoal()  { double a=parseAmtRaw(sgTarget); if(a<0)return; showAlert("Goal Set",atm.setGoal(sgName.getText(),a)); rebuildSavings(); }
    private void handleAddGoal()  { double a=parseAmt(sgAdd); if(a<0)return; showAlert("Goal Update",atm.addGoal(a)); updateCard(); rebuildSavings(); }
    private void handleViewGoal() {
        if(!atm.ok()||atm.active()==null){showAlert("Error","Login first.");return;}
        BankAccount acc=atm.active();
        if(acc.getGoalTarget()==0){showAlert("No Goal","Set a goal first.");return;}
        int p=acc.getGoalPct();
        StringBuilder bar=new StringBuilder("[");
        for(int i=0;i<20;i++) bar.append(i<p/5?"█":"░");
        bar.append("] ").append(p).append("%");
        showAlert("Savings Progress","Goal: "+acc.getGoalName()+"\nTarget: PKR "+String.format("%,.2f",acc.getGoalTarget())+"\nSaved: PKR "+String.format("%,.2f",acc.getGoalSaved())+"\n"+bar);
    }
    private void handleChangePin(){ showAlert("Change PIN",atm.changePin(cpOld.getText(),cpNew.getText(),cpConf.getText())); cpOld.clear();cpNew.clear();cpConf.clear(); }
    private void handleEject()    { showAlert("Goodbye",atm.ejectCard()); resetUI(); }

    private void rebuildTx()      { txPane=buildTxPane();         if(contentArea.getChildren().stream().anyMatch(n->n==txPane)) showPane(txPane); }
    private void rebuildSavings() { savingsPane=buildSavingsPane(); showPane(savingsPane); }
    private void rebuildAndShow(VBox p) { showPane(p); }

    // ═══════════════════════════════════════════════════════════════════════
    //  UI UTILS
    // ═══════════════════════════════════════════════════════════════════════
    private void updateCard() {
        String st=atm.stateName();
        if("IDLE".equals(st)){ holderLbl.setText("INSERT YOUR CARD"); balLbl.setText("PKR ————"); cardNoLbl.setText("•••• •••• •••• ????"); cardTypeLbl.setText("——");
            stateLbl.setText("● IDLE  —  Ready"); stateLbl.setStyle("-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:"+T.gold+";-fx-font-family:Consolas;"); }
        else if("HAS_CARD".equals(st)){ BankAccount a=atm.active(); if(a!=null){ holderLbl.setText(a.getAccountHolder().toUpperCase()); balLbl.setText("PKR ••••••"); String cn=a.getAccountNumber(); cardNoLbl.setText("•••• •••• •••• "+cn.substring(cn.length()-4)); cardTypeLbl.setText(a.getAccountType().name()); }
            stateLbl.setText("● CARD IN  —  Enter PIN"); stateLbl.setStyle("-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:"+T.acc+";-fx-font-family:Consolas;"); }
        else { BankAccount a=atm.active(); if(a!=null){ holderLbl.setText(a.getAccountHolder().toUpperCase()); balLbl.setText(a.getFmtBalance()); String cn=a.getAccountNumber(); cardNoLbl.setText("•••• •••• •••• "+cn.substring(cn.length()-4)); cardTypeLbl.setText(a.getAccountType().name()); }
            stateLbl.setText("● AUTHENTICATED  ✓"); stateLbl.setStyle("-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:"+T.green+";-fx-font-family:Consolas;"); }
    }

    private void resetUI() {
        loginCardFld.clear(); loginCardFld.setDisable(false);
        loginPinFld.clear();  loginPinFld.setDisable(false);
        updateCard();
        dashboardPane=buildDashboardPane(); showPane(dashboardPane);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        DialogPane dp = a.getDialogPane();
        dp.setStyle("-fx-background-color:"+T.bg2+";-fx-font-family:'Segoe UI';");
        dp.lookup(".content.label").setStyle("-fx-text-fill:"+T.text+";-fx-font-size:13px;-fx-font-family:'Segoe UI';");
        a.showAndWait();
    }

    private double parseAmt(TextField f) {
        try { double v=Double.parseDouble(f.getText().trim()); if(v<=0) throw new NumberFormatException(); return v; }
        catch(Exception e){ showAlert("Error","Enter a valid positive amount."); pulse(f); return -1; }
    }
    private double parseAmtRaw(TextField f) {
        try { double v=Double.parseDouble(f.getText().trim()); if(v<=0) throw new NumberFormatException(); return v; }
        catch(Exception e){ showAlert("Error","Enter a valid positive amount."); return -1; }
    }

    private void pulse(Node n) {
        ScaleTransition s=new ScaleTransition(Duration.millis(100),n);
        s.setFromX(1);s.setFromY(1);s.setToX(1.03);s.setToY(1.03);
        s.setCycleCount(2);s.setAutoReverse(true);s.play();
    }

    private void startClock() {
        Timeline tl=new Timeline(new KeyFrame(Duration.seconds(1),e->{
            if(clockLbl!=null) clockLbl.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE dd MMM  HH:mm:ss")));
        }));
        tl.setCycleCount(Animation.INDEFINITE); tl.play();
    }

    private ColumnConstraints colConst()       { ColumnConstraints c=new ColumnConstraints(); c.setHgrow(Priority.ALWAYS); c.setFillWidth(true); return c; }
    private ColumnConstraints colConst(int gap){ ColumnConstraints c=new ColumnConstraints(gap); return c; }

    public static void main(String[] args) { launch(args); }
}

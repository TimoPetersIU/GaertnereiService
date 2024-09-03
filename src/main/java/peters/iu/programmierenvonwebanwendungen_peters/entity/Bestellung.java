package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bestellung")
public class Bestellung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bestellnummer;

    @Column(length = 255)
    private String beschreibung;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preis;

    @Column(nullable = false)
    private LocalDate bestelldatum;

    @Column(nullable = false)
    private int lieferstatus;

    private String bestellprozessTyp;

    @ManyToOne
    @JoinColumn(name = "kundennummer", nullable = false)
    private Kunde kunde;

    @OneToMany(mappedBy = "bestellung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bestellposition> bestellpositionen;

    public Bestellung() {
        this.bestellpositionen = new ArrayList<>();
    }

    // Getter und Setter für alle Attribute

    public Long getBestellnummer() {
        return bestellnummer;
    }

    public void setBestellnummer(Long bestellnummer) {
        this.bestellnummer = bestellnummer;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public LocalDate getBestelldatum() {
        return bestelldatum;
    }

    public void setBestelldatum(LocalDate bestelldatum) {
        this.bestelldatum = bestelldatum;
    }

    public int getLieferstatus() {
        return lieferstatus;
    }

    public void setLieferstatus(int lieferstatus) {
        this.lieferstatus = lieferstatus;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public List<Bestellposition> getBestellpositionen() {
        return bestellpositionen;
    }

    public void setBestellpositionen(List<Bestellposition> bestellpositionen) {
        this.bestellpositionen = bestellpositionen;
    }

    public String getBestellprozessTyp() {
        return bestellprozessTyp;
    }

    public void setBestellprozessTyp(String bestellprozessTyp) {
        this.bestellprozessTyp = bestellprozessTyp;
    }
}
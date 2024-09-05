package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Entität repräsentiert eine Bestellung eines Kunden.
 * Eine Bestellung enthält Informationen zur Bestellung selbst sowie eine Liste von Bestellpositionen,
 * die die einzelnen Produkte und Mengen umfassen.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@Table(name = "bestellung")
public class Bestellung {

    /**
     * Die eindeutige Bestellnummer, die automatisch generiert wird.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bestellnummer;

    /**
     * Eine optionale Beschreibung der Bestellung.
     */
    @Column(length = 255)
    private String beschreibung;

    /**
     * Der Preis der gesamten Bestellung.
     * Das Feld ist nicht null und hat eine Präzision von 10 Stellen und 2 Dezimalstellen.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preis;

    /**
     * Das Datum, an dem die Bestellung aufgegeben wurde.
     * Das Feld ist nicht null.
     */
    @Column(nullable = false)
    private LocalDate bestelldatum;

    /**
     * Der Lieferstatus der Bestellung.
     * Das Feld ist nicht null und verwendet einen ganzzahligen Wert, um den Status zu repräsentieren.
     */
    @Column(nullable = false)
    private int lieferstatus;

    /**
     * Der Typ des Bestellprozesses, der für diese Bestellung verwendet wird.
     * Dies könnte z.B. "Standard", "Express" oder ein benutzerdefinierter Typ sein.
     */
    private String bestellprozessTyp;

    /**
     * Der Kunde, der die Bestellung aufgegeben hat.
     * Jede Bestellung gehört zu genau einem Kunden.
     */
    @ManyToOne
    @JoinColumn(name = "kundennummer", nullable = false)
    private Kunde kunde;

    /**
     * Die Liste der Bestellpositionen, die zu dieser Bestellung gehören.
     * Jede Bestellung kann mehrere Bestellpositionen haben.
     */
    @OneToMany(mappedBy = "bestellung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bestellposition> bestellpositionen;

    /**
     * Standardkonstruktor, der eine leere Liste von Bestellpositionen initialisiert.
     */
    public Bestellung() {
        this.bestellpositionen = new ArrayList<>();
    }

    /**
     * Gibt die Bestellnummer zurück.
     *
     * @return die Bestellnummer der Bestellung
     */
    public Long getBestellnummer() {
        return bestellnummer;
    }

    /**
     * Setzt die Bestellnummer.
     *
     * @param bestellnummer die Bestellnummer der Bestellung
     */
    public void setBestellnummer(Long bestellnummer) {
        this.bestellnummer = bestellnummer;
    }

    /**
     * Gibt die Beschreibung der Bestellung zurück.
     *
     * @return die Beschreibung der Bestellung
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * Setzt die Beschreibung der Bestellung.
     *
     * @param beschreibung die Beschreibung der Bestellung
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * Gibt den Preis der Bestellung zurück.
     *
     * @return den Preis der Bestellung
     */
    public BigDecimal getPreis() {
        return preis;
    }

    /**
     * Setzt den Preis der Bestellung.
     *
     * @param preis der Preis der Bestellung
     */
    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    /**
     * Gibt das Bestelldatum zurück.
     *
     * @return das Bestelldatum der Bestellung
     */
    public LocalDate getBestelldatum() {
        return bestelldatum;
    }

    /**
     * Setzt das Bestelldatum.
     *
     * @param bestelldatum das Bestelldatum der Bestellung
     */
    public void setBestelldatum(LocalDate bestelldatum) {
        this.bestelldatum = bestelldatum;
    }

    /**
     * Gibt den Lieferstatus der Bestellung zurück.
     *
     * @return den Lieferstatus der Bestellung
     */
    public int getLieferstatus() {
        return lieferstatus;
    }

    /**
     * Setzt den Lieferstatus der Bestellung.
     *
     * @param lieferstatus der Lieferstatus der Bestellung
     */
    public void setLieferstatus(int lieferstatus) {
        this.lieferstatus = lieferstatus;
    }

    /**
     * Gibt den Kunden zurück, der die Bestellung aufgegeben hat.
     *
     * @return der Kunde, der die Bestellung aufgegeben hat
     */
    public Kunde getKunde() {
        return kunde;
    }

    /**
     * Setzt den Kunden, der die Bestellung aufgegeben hat.
     *
     * @param kunde der Kunde, der die Bestellung aufgegeben hat
     */
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    /**
     * Gibt die Liste der Bestellpositionen zurück, die zu dieser Bestellung gehören.
     *
     * @return die Liste der Bestellpositionen
     */
    public List<Bestellposition> getBestellpositionen() {
        return bestellpositionen;
    }

    /**
     * Setzt die Liste der Bestellpositionen.
     *
     * @param bestellpositionen die Liste der Bestellpositionen
     */
    public void setBestellpositionen(List<Bestellposition> bestellpositionen) {
        this.bestellpositionen = bestellpositionen;
    }

    /**
     * Gibt den Typ des Bestellprozesses zurück.
     *
     * @return der Typ des Bestellprozesses
     */
    public String getBestellprozessTyp() {
        return bestellprozessTyp;
    }

    /**
     * Setzt den Typ des Bestellprozesses.
     *
     * @param bestellprozessTyp der Typ des Bestellprozesses
     */
    public void setBestellprozessTyp(String bestellprozessTyp) {
        this.bestellprozessTyp = bestellprozessTyp;
    }
}

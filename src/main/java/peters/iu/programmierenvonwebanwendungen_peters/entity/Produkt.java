package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Diese Entität repräsentiert ein Produkt in der Anwendung.
 * Ein Produkt hat einen Namen, eine Beschreibung, einen Preis und einen Lagerbestand.
 * Die Entität wird in der Tabelle "produkt" gespeichert und ist mit Bestellpositionen verknüpft,
 * die dieses Produkt referenzieren.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@Table(name = "produkt")
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String beschreibung;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preis;

    @Column(nullable = false)
    private int bestand;

    @OneToMany(mappedBy = "produkt")
    private List<Bestellposition> bestellpositionen;

    /**
     * Gibt die ID des Produkts zurück.
     *
     * @return die ID des Produkts
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Produkts.
     *
     * @param id die ID des Produkts
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Produkts zurück.
     *
     * @return der Name des Produkts
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Produkts.
     *
     * @param name der Name des Produkts
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Beschreibung des Produkts zurück.
     *
     * @return die Beschreibung des Produkts
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * Setzt die Beschreibung des Produkts.
     *
     * @param beschreibung die Beschreibung des Produkts
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * Gibt den Preis des Produkts zurück.
     *
     * @return der Preis des Produkts
     */
    public BigDecimal getPreis() {
        return preis;
    }

    /**
     * Setzt den Preis des Produkts.
     *
     * @param preis der Preis des Produkts
     */
    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    /**
     * Gibt den Bestand des Produkts zurück.
     *
     * @return der Bestand des Produkts
     */
    public int getBestand() {
        return bestand;
    }

    /**
     * Setzt den Bestand des Produkts.
     *
     * @param bestand der Bestand des Produkts
     */
    public void setBestand(int bestand) {
        this.bestand = bestand;
    }
}

package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.*;

/**
 * Diese Entität repräsentiert eine Bestellposition in einer Bestellung.
 * Eine Bestellposition verknüpft ein Produkt mit einer Bestellung und enthält die Menge des Produkts.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@Table(name = "bestellposition")
public class Bestellposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Die Bestellung, zu der diese Position gehört.
     * Jede Bestellposition ist mit genau einer Bestellung verknüpft.
     */
    @ManyToOne
    @JoinColumn(name = "bestellung_id", nullable = false)
    private Bestellung bestellung;

    /**
     * Das Produkt, das in dieser Bestellposition bestellt wird.
     * Jede Bestellposition ist mit genau einem Produkt verknüpft.
     */
    @ManyToOne
    @JoinColumn(name = "produkt_id", nullable = false)
    private Produkt produkt;

    /**
     * Die Menge des Produkts in dieser Bestellposition.
     * Gibt an, wie viele Einheiten des Produkts in der Bestellung enthalten sind.
     */
    @Column(nullable = false)
    private int menge;

    /**
     * Gibt die ID dieser Bestellposition zurück.
     *
     * @return die ID der Bestellposition
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID dieser Bestellposition.
     *
     * @param id die ID der Bestellposition
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt die Bestellung zurück, zu der diese Bestellposition gehört.
     *
     * @return die Bestellung der Bestellposition
     */
    public Bestellung getBestellung() {
        return bestellung;
    }

    /**
     * Setzt die Bestellung, zu der diese Bestellposition gehört.
     *
     * @param bestellung die Bestellung der Bestellposition
     */
    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }

    /**
     * Gibt das Produkt zurück, das in dieser Bestellposition bestellt wird.
     *
     * @return das Produkt der Bestellposition
     */
    public Produkt getProdukt() {
        return produkt;
    }

    /**
     * Setzt das Produkt, das in dieser Bestellposition bestellt wird.
     *
     * @param produkt das Produkt der Bestellposition
     */
    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    /**
     * Gibt die Menge des Produkts in dieser Bestellposition zurück.
     *
     * @return die Menge des Produkts
     */
    public int getMenge() {
        return menge;
    }

    /**
     * Setzt die Menge des Produkts in dieser Bestellposition.
     *
     * @param menge die Menge des Produkts
     */
    public void setMenge(int menge) {
        this.menge = menge;
    }
}

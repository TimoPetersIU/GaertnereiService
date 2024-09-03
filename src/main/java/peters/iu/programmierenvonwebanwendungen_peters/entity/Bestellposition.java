package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bestellposition")
public class Bestellposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bestellung_id", nullable = false)
    private Bestellung bestellung;

    @ManyToOne
    @JoinColumn(name = "produkt_id", nullable = false)
    private Produkt produkt;

    @Column(nullable = false)
    private int menge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bestellung getBestellung() {
        return bestellung;
    }

    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }
}
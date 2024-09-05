package peters.iu.programmierenvonwebanwendungen_peters.entity.kunde;

import jakarta.persistence.*;

/**
 * Diese Entität repräsentiert einen Kundentyp in der Anwendung.
 * Der Kundentyp definiert die Art des Kunden, wie z.B. Privatkunde oder Geschäftskunde.
 * Die Klasse wird verwendet, um verschiedene Typen von Kunden in der Anwendung zu kategorisieren.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@Table(name = "kundentyp")
public class Kundentyp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int typ;

    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Gibt die ID des Kundentyps zurück.
     *
     * @return die ID des Kundentyps
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Kundentyps.
     *
     * @param id die zu setzende ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Typ des Kundentyps zurück.
     *
     * @return der Typ des Kundentyps
     */
    public int getTyp() {
        return typ;
    }

    /**
     * Setzt den Typ des Kundentyps.
     *
     * @param typ der zu setzende Typ
     */
    public void setTyp(int typ) {
        this.typ = typ;
    }

    /**
     * Gibt den Namen des Kundentyps zurück.
     *
     * @return der Name des Kundentyps
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Kundentyps.
     *
     * @param name der zu setzende Name
     */
    public void setName(String name) {
        this.name = name;
    }
}

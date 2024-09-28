package peters.iu.programmierenvonwebanwendungen_peters.entity.kunde;

import jakarta.persistence.*;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;

import java.util.List;

/**
 * Diese Entität repräsentiert einen Kunden in der Anwendung.
 * <p>
 * Die Klasse verwendet das Vererbungsmuster Single Table, um verschiedene
 * Kundentypen in einer einzigen Tabelle zu speichern. Der Typ des Kunden wird
 * durch das Feld 'dtype' in der Tabelle unterschieden.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Table(name = "kunde")
public class Kunde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kundennummer;

    @Column(nullable = false, length = 50)
    private String nachname;

    @Column(nullable = false, length = 50)
    private String vorname;

    @Column(nullable = false, length = 50)
    private String strasse;

    @Column(nullable = false, length = 10)
    private String hausnummer;

    @Column(nullable = false, length = 5)
    private String postleitzahl;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String telefonnummer;

    @OneToMany(mappedBy = "kunde", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bestellung> bestellungen;

    @ManyToOne
    @JoinColumn(name = "kundentyp_id", nullable = false)
    private Kundentyp kundentyp;

    /**
     * Konstruktor für die Erzeugung eines Kunden mit einem spezifischen {@link Kundentyp}.
     * Dieser Konstruktor wird verwendet, um einen neuen Kunden zu erstellen, der
     * einen bestimmten Kundentyp zugewiesen bekommt.
     *
     * @param kundentyp der Kundentyp des Kunden
     */
    public Kunde(Kundentyp kundentyp) {
        this.kundentyp = kundentyp;
    }

    /**
     * Standardkonstruktor für die JPA-Entität.
     * Dieser Konstruktor wird von JPA benötigt, um Instanzen der Entität zu erstellen.
     */
    public Kunde() {
        // Kein spezifischer Code erforderlich
    }

    /**
     * Gibt die Kundennummer des Kunden zurück.
     *
     * @return die Kundennummer
     */
    public Long getKundennummer() {
        return kundennummer;
    }

    /**
     * Setzt die Kundennummer des Kunden.
     *
     * @param kundennummer die zu setzende Kundennummer
     */
    public void setKundennummer(Long kundennummer) {
        this.kundennummer = kundennummer;
    }

    /**
     * Gibt den Nachnamen des Kunden zurück.
     *
     * @return der Nachname des Kunden
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * Setzt den Nachnamen des Kunden.
     *
     * @param nachname der zu setzende Nachname
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Gibt den Vornamen des Kunden zurück.
     *
     * @return der Vorname des Kunden
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Setzt den Vornamen des Kunden.
     *
     * @param vorname der zu setzende Vorname
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Gibt die Straße des Kunden zurück.
     *
     * @return die Straße des Kunden
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Setzt die Straße des Kunden.
     *
     * @param strasse die zu setzende Straße
     */
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    /**
     * Gibt die Hausnummer des Kunden zurück.
     *
     * @return die Hausnummer des Kunden
     */
    public String getHausnummer() {
        return hausnummer;
    }

    /**
     * Setzt die Hausnummer des Kunden.
     *
     * @param hausnummer die zu setzende Hausnummer
     */
    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    /**
     * Gibt die Postleitzahl des Kunden zurück.
     *
     * @return die Postleitzahl des Kunden
     */
    public String getPostleitzahl() {
        return postleitzahl;
    }

    /**
     * Setzt die Postleitzahl des Kunden.
     *
     * @param postleitzahl die zu setzende Postleitzahl
     */
    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    /**
     * Gibt die E-Mail-Adresse des Kunden zurück.
     *
     * @return die E-Mail-Adresse des Kunden
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail-Adresse des Kunden.
     *
     * @param email die zu setzende E-Mail-Adresse
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gibt die Telefonnummer des Kunden zurück.
     *
     * @return die Telefonnummer des Kunden
     */
    public String getTelefonnummer() {
        return telefonnummer;
    }

    /**
     * Setzt die Telefonnummer des Kunden.
     *
     * @param telefonnummer die zu setzende Telefonnummer
     */
    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    /**
     * Gibt die Liste der Bestellungen des Kunden zurück.
     *
     * @return die Liste der Bestellungen
     */
    public List<Bestellung> getBestellungen() {
        return bestellungen;
    }

    /**
     * Setzt die Liste der Bestellungen des Kunden.
     *
     * @param bestellungen die zu setzende Liste der Bestellungen
     */
    public void setBestellungen(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen;
    }

    /**
     * Gibt den Kundentyp des Kunden zurück.
     *
     * @return der Kundentyp
     */
    public Kundentyp getKundentyp() {
        return kundentyp;
    }

    /**
     * Setzt den Kundentyp des Kunden.
     *
     * @param kundentyp der zu setzende Kundentyp
     */
    public void setKundentyp(Kundentyp kundentyp) {
        this.kundentyp = kundentyp;
    }
}

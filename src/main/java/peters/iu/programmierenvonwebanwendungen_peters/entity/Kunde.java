package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.*;

import java.util.List;


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


    public Kunde(Kundentyp kundentyp) {
        this.kundentyp = kundentyp;
    }

    public Kunde() {

    }
    // Getter und Setter für alle Attribute

    public Long getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(Long kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public List<Bestellung> getBestellungen() {
        return bestellungen;
    }

    public void setBestellungen(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen;
    }

    public Kundentyp getKundentyp() {
        return kundentyp;
    }

    public void setKundentyp(Kundentyp kundentyp) {
        this.kundentyp = kundentyp;
    }
}

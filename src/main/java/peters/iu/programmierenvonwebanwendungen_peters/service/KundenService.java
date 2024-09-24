package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kundentyp;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundentypRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.KundenFactory;

import java.util.List;

/**
 * Dieser Service verwaltet die CRUD-Operationen für Kunden.
 *
 * @author Timo Peters - IU Hamburg
 */
@Service
public class KundenService {

    private static final Logger log = LoggerFactory.getLogger(KundenService.class);

    @Autowired
    private KundenRepository kundenRepository; // Repository zum Zugriff auf Kunden-Daten

    @Autowired
    private KundentypRepository kundentypRepository; // Repository zum Zugriff auf Kundentypen-Daten

    @Autowired
    private KundenFactory kundenFactory; // Factory zur Erstellung von Kundenobjekten

    /**
     * Holt eine Liste aller Kunden.
     *
     * @return Liste aller Kunden
     */
    public List<Kunde> alleKunden() {
        List<Kunde> kunden = kundenRepository.findAll();
        log.info("Kundenliste abgerufen: {} Kunden gefunden", kunden.size());
        return kunden;
    }

    /**
     * Erstellt einen neuen Kunden mit den übergebenen Daten und speichert ihn in der Datenbank.
     *
     * @param kunde       die Daten des neuen Kunden
     * @param kundentypId die ID des Kundentyps
     * @return der neu erstellte Kunde
     */
    public Kunde neuerKundeErstellen(Kunde kunde, Long kundentypId) {
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        Kunde neuerKunde = kundenFactory.erstelleKunde(kundentyp);
        setzeKundenDaten(neuerKunde, kunde);
        kundenRepository.save(neuerKunde);
        log.info("Neuer Kunde erstellt: {}", neuerKunde);
        return neuerKunde;
    }

    /**
     * Bearbeitet die Daten eines bestehenden Kunden und speichert die Änderungen.
     *
     * @param id          die ID des zu bearbeitenden Kunden
     * @param kunde       die aktualisierten Daten des Kunden
     * @param kundentypId die ID des Kundentyps
     * @return der aktualisierte Kunde
     */
    public Kunde kundeBearbeiten(Long id, Kunde kunde, Long kundentypId) {
        Kunde existingKunde = kundenRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        if (!existingKunde.getKundentyp().equals(kundentyp)) {
            return neuerKundeErstellen(kunde, kundentypId);
        } else {
            setzeKundenDaten(existingKunde, kunde);
            kundenRepository.save(existingKunde);
            log.info("Kunde ID {} aktualisiert: {}", id, existingKunde);
            return existingKunde;
        }
    }

    /**
     * Löscht einen Kunden anhand der ID.
     *
     * @param id die ID des zu löschenden Kunden
     */
    public void kundeLoeschen(Long id) {
        kundenRepository.deleteById(id);
        log.info("Kunde ID {} gelöscht", id);
    }

    /**
     * Setzt die Daten des Kunden.
     *
     * @param existingKunde der bestehende Kunde
     * @param kunde         die neuen Daten des Kunden
     */
    private void setzeKundenDaten(Kunde existingKunde, Kunde kunde) {
        existingKunde.setVorname(kunde.getVorname());
        existingKunde.setNachname(kunde.getNachname());
        existingKunde.setStrasse(kunde.getStrasse());
        existingKunde.setHausnummer(kunde.getHausnummer());
        existingKunde.setPostleitzahl(kunde.getPostleitzahl());
        existingKunde.setEmail(kunde.getEmail());
        existingKunde.setTelefonnummer(kunde.getTelefonnummer());
    }
}

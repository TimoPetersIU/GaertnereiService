package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kundentyp;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundentypRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.KundenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Timo Peters - IU Hamburg
 * Dieser Controller verwaltet die CRUD-Operationen für Kunden.
 */
@Controller
public class KundenController {

    private static final Logger log = LoggerFactory.getLogger(BestellungController.class);

    @Autowired
    private KundenRepository kundenRepository; // Repository zum Zugriff auf Kunden-Daten

    @Autowired
    private KundentypRepository kundentypRepository; // Repository zum Zugriff auf Kundentypen-Daten

    @Autowired
    private KundenFactory kundenFactory; // Factory zur Erstellung von Kundenobjekten

    /**
     * Weiterleitung zur Kundenübersicht.
     *
     * @return der Name der View für die Kundenübersicht
     */
    @GetMapping("/")
    public String index() {
        // Weiterleitung zur Kundenübersicht
        return "redirect:/kunden";
    }

    /**
     * Zeigt eine Liste aller Kunden an.
     *
     * @param model das Model, um die Kundenliste und Kundentypen zur View zu übergeben
     * @return der Name der View für die Kundenliste
     */
    @GetMapping("/kunden")
    public String alleKundenAnzeigen(Model model) {
        // Alle Kunden aus der Datenbank abrufen
        List<Kunde> kunden = kundenRepository.findAll();
        // Die Kundenliste und Kundentypen dem Model hinzufügen
        model.addAttribute("kunden", kunden);
        model.addAttribute("kundentypen", kundentypRepository.findAll());
        // Loggen der Anzahl der gefundenen Kunden
        log.info("Kundenliste angezeigt: {} Kunden gefunden", kunden.size());
        // View für die Kundenliste zurückgeben
        return "kundenliste";
    }

    /**
     * Zeigt das Formular zum Erstellen eines neuen Kunden an.
     *
     * @param model das Model, um das Formular mit einem neuen Kunden und Kundentypen zu füllen
     * @return der Name der View für das Kunden-Erstellungsformular
     */
    @GetMapping("/kunden/neu")
    public String neuerKundeFormular(Model model) {
        // Erstellen eines Standard-Kundentyps
        Kundentyp standardKundentyp = new Kundentyp();
        standardKundentyp.setId(1L);
        standardKundentyp.setTyp(1);
        standardKundentyp.setName("Privatkunde");

        // Neuen Kunden mit dem Standard-Kundentyp erstellen und dem Model hinzufügen
        model.addAttribute("kunde", new Kunde(standardKundentyp));
        model.addAttribute("kundentypen", kundentypRepository.findAll());
        // Loggen, dass das Formular für einen neuen Kunden angezeigt wird
        log.info("Formular für neuen Kunden angezeigt");
        // View für das Kunden-Erstellungsformular zurückgeben
        return "kundenneu";
    }

    /**
     * Erstellt einen neuen Kunden mit den übergebenen Daten und speichert ihn in der Datenbank.
     *
     * @param kunde die Daten des neuen Kunden
     * @param kundentypId die ID des Kundentyps
     * @return eine Weiterleitung zur Kundenübersicht
     */
    @PostMapping("/kunden")
    public String neuerKundeErstellen(@ModelAttribute Kunde kunde, @RequestParam("kundentypId") Long kundentypId) {
        // Den Kundentyp anhand der übergebenen ID aus der Datenbank abrufen
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId)
                .orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        // Neuen Kunden basierend auf dem Kundentyp erstellen
        Kunde neuerKunde = kundenFactory.erstelleKunde(kundentyp);
        // Die übergebenen Daten des neuen Kunden setzen
        neuerKunde.setVorname(kunde.getVorname());
        neuerKunde.setNachname(kunde.getNachname());
        neuerKunde.setStrasse(kunde.getStrasse());
        neuerKunde.setHausnummer(kunde.getHausnummer());
        neuerKunde.setPostleitzahl(kunde.getPostleitzahl());
        neuerKunde.setEmail(kunde.getEmail());
        neuerKunde.setTelefonnummer(kunde.getTelefonnummer());
        neuerKunde.setKundentyp(kundentyp);

        // Den neuen Kunden in der Datenbank speichern
        kundenRepository.save(neuerKunde);
        // Loggen, dass der neue Kunde erstellt wurde
        log.info("Neuer Kunde erstellt: {}", neuerKunde);
        // Weiterleitung zur Kundenübersicht
        return "redirect:/kunden";
    }

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Kunden an.
     *
     * @param id die ID des zu bearbeitenden Kunden
     * @param model das Model, um den bestehenden Kunden und die Kundentypen zur View zu übergeben
     * @return der Name der View für das Kunden-Bearbeitungsformular
     */
    @GetMapping("/kunden/{id}/bearbeiten")
    public String kundeBearbeitenFormular(@PathVariable Long id, Model model) {
        // Den bestehenden Kunden anhand der ID aus der Datenbank abrufen
        Kunde kunde = kundenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        // Den Kunden und die Kundentypen dem Model hinzufügen
        model.addAttribute("kunde", kunde);
        model.addAttribute("kundentypen", kundentypRepository.findAll());
        // Loggen, dass das Bearbeitungsformular für den Kunden angezeigt wird
        log.info("Formular zum Bearbeiten des Kunden ID {} angezeigt", id);
        // View für das Kunden-Bearbeitungsformular zurückgeben
        return "kundebearbeiten";
    }

    /**
     * Bearbeitet die Daten eines bestehenden Kunden und speichert die Änderungen.
     *
     * @param id die ID des zu bearbeitenden Kunden
     * @param kunde die aktualisierten Daten des Kunden
     * @param kundentypId die ID des Kundentyps
     * @return eine Weiterleitung zur Kundenübersicht
     */
    @PostMapping("/kunden/{id}")
    public String kundeBearbeiten(@PathVariable Long id, @ModelAttribute Kunde kunde, @RequestParam("kundentypId") Long kundentypId) {
        // Den bestehenden Kunden anhand der ID aus der Datenbank abrufen
        Kunde existingKunde = kundenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        // Den Kundentyp anhand der übergebenen ID aus der Datenbank abrufen
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId)
                .orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        // Überprüfen, ob der Kundentyp geändert wurde
        if (!existingKunde.getKundentyp().equals(kundentyp)) {
            // Neuen Kunden erstellen, falls der Kundentyp geändert wurde
            Kunde neuerKunde = kundenFactory.erstelleKunde(kundentyp);
            neuerKunde.setVorname(kunde.getVorname());
            neuerKunde.setNachname(kunde.getNachname());
            neuerKunde.setStrasse(kunde.getStrasse());
            neuerKunde.setHausnummer(kunde.getHausnummer());
            neuerKunde.setPostleitzahl(kunde.getPostleitzahl());
            neuerKunde.setEmail(kunde.getEmail());
            neuerKunde.setTelefonnummer(kunde.getTelefonnummer());
            neuerKunde.setKundentyp(kundentyp);

            // Den bestehenden Kunden löschen und den neuen Kunden speichern
            kundenRepository.delete(existingKunde);
            kundenRepository.save(neuerKunde);
            // Loggen, dass der Kunde aufgrund einer Typänderung aktualisiert wurde
            log.info("Kunde ID {} geändert zu neuer Kunde: {}", id, neuerKunde);
        } else {
            // Die Daten des bestehenden Kunden aktualisieren, wenn der Kundentyp gleich bleibt
            existingKunde.setVorname(kunde.getVorname());
            existingKunde.setNachname(kunde.getNachname());
            existingKunde.setStrasse(kunde.getStrasse());
            existingKunde.setHausnummer(kunde.getHausnummer());
            existingKunde.setPostleitzahl(kunde.getPostleitzahl());
            existingKunde.setEmail(kunde.getEmail());
            existingKunde.setTelefonnummer(kunde.getTelefonnummer());
            existingKunde.setKundentyp(kundentyp);

            // Den aktualisierten Kunden speichern
            kundenRepository.save(existingKunde);
            // Loggen, dass der Kunde aktualisiert wurde
            log.info("Kunde ID {} aktualisiert: {}", id, existingKunde);
        }

        // Weiterleitung zur Kundenübersicht
        return "redirect:/kunden";
    }

    /**
     * Löscht einen Kunden anhand der ID.
     *
     * @param id die ID des zu löschenden Kunden
     * @return eine Weiterleitung zur Kundenübersicht
     */
    @PostMapping("/kunden/{id}/loeschen")
    public String kundeLoeschen(@PathVariable Long id) {
        // Den Kunden anhand der ID löschen
        kundenRepository.deleteById(id);
        // Loggen, dass der Kunde gelöscht wurde
        log.info("Kunde ID {} gelöscht", id);
        // Weiterleitung zur Kundenübersicht
        return "redirect:/kunden";
    }
}
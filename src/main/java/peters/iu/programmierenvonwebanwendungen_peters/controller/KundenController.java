package peters.iu.programmierenvonwebanwendungen_peters.controller;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kundentyp;
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
 * Dieser Controller verwaltet die CRUD-Operationen für Kunden
 */

@Controller
public class KundenController {

    @Autowired
    private KundenRepository kundenRepository;

    @Autowired
    private KundentypRepository kundentypRepository;

    @Autowired
    private KundenFactory kundenFactory;

    /**
     * Weiterleitung zur Kundenübersicht.
     *
     * @return der Name der View für die Kundenübersicht
     */
    @GetMapping("/")
    public String index() {
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
        List<Kunde> kunden = kundenRepository.findAll();
        model.addAttribute("kunden", kunden);
        model.addAttribute("kundentypen", kundentypRepository.findAll());
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
        Kundentyp standardKundentyp = new Kundentyp();
        standardKundentyp.setId(1L);
        standardKundentyp.setTyp(1);
        standardKundentyp.setName("Privatkunde");

        model.addAttribute("kunde", new Kunde(standardKundentyp));
        model.addAttribute("kundentypen", kundentypRepository.findAll());
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
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        Kunde neuerKunde = kundenFactory.erstelleKunde(kundentyp);
        neuerKunde.setVorname(kunde.getVorname());
        neuerKunde.setNachname(kunde.getNachname());
        neuerKunde.setStrasse(kunde.getStrasse());
        neuerKunde.setHausnummer(kunde.getHausnummer());
        neuerKunde.setPostleitzahl(kunde.getPostleitzahl());
        neuerKunde.setEmail(kunde.getEmail());
        neuerKunde.setTelefonnummer(kunde.getTelefonnummer());
        neuerKunde.setKundentyp(kundentyp);

        kundenRepository.save(neuerKunde);
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
        Kunde kunde = kundenRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        model.addAttribute("kunde", kunde);
        model.addAttribute("kundentypen", kundentypRepository.findAll());
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
        Kunde existingKunde = kundenRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        if (!existingKunde.getKundentyp().equals(kundentyp)) {
            Kunde neuerKunde = kundenFactory.erstelleKunde(kundentyp);
            neuerKunde.setVorname(kunde.getVorname());
            neuerKunde.setNachname(kunde.getNachname());
            neuerKunde.setStrasse(kunde.getStrasse());
            neuerKunde.setHausnummer(kunde.getHausnummer());
            neuerKunde.setPostleitzahl(kunde.getPostleitzahl());
            neuerKunde.setEmail(kunde.getEmail());
            neuerKunde.setTelefonnummer(kunde.getTelefonnummer());
            neuerKunde.setKundentyp(kundentyp);

            kundenRepository.delete(existingKunde);
            kundenRepository.save(neuerKunde);
        } else {
            existingKunde.setVorname(kunde.getVorname());
            existingKunde.setNachname(kunde.getNachname());
            existingKunde.setStrasse(kunde.getStrasse());
            existingKunde.setHausnummer(kunde.getHausnummer());
            existingKunde.setPostleitzahl(kunde.getPostleitzahl());
            existingKunde.setEmail(kunde.getEmail());
            existingKunde.setTelefonnummer(kunde.getTelefonnummer());
            existingKunde.setKundentyp(kundentyp);

            kundenRepository.save(existingKunde);
        }

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
        kundenRepository.deleteById(id);
        return "redirect:/kunden";
    }
}

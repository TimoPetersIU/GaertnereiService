package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kundentyp;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundentypRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.KundenService;

import java.util.List;

/**
 * @author Timo Peters - IU Hamburg
 * Dieser Controller verwaltet die CRUD-Operationen für Kunden.
 */
@Controller
public class KundenController {

    private static final Logger log = LoggerFactory.getLogger(KundenController.class);

    @Autowired
    private KundenService kundenService; // Service zur Verwaltung der Kunden

    @Autowired
    private KundenRepository kundenRepository;

    @Autowired
    private KundentypRepository kundentypRepository;

    /**
     * Weiterleitung zur Kundenübersicht.
     *
     * @return der Name der View für die Kundenübersicht
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Zeigt eine Liste aller Kunden an.
     *
     * @param model das Model, um die Kundenliste zur View zu übergeben
     * @return der Name der View für die Kundenliste
     */
    @GetMapping("/kunden")
    public String alleKundenAnzeigen(Model model) {
        List<Kunde> kunden = kundenService.alleKunden();
        model.addAttribute("kunden", kunden);
        return "kundenliste";
    }

    /**
     * Zeigt das Formular zum Erstellen eines neuen Kunden an.
     *
     * @param model das Model, um das Formular mit einem neuen Kunden zu füllen
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
        return "kundenneu";
    }

    /**
     * Erstellt einen neuen Kunden mit den übergebenen Daten und speichert ihn in der Datenbank.
     *
     * @param kunde       die Daten des neuen Kunden
     * @param kundentypId die ID des Kundentyps
     * @return eine Weiterleitung zur Kundenübersicht
     */
    @PostMapping("/kunden")
    public String neuerKundeErstellen(@ModelAttribute Kunde kunde, @RequestParam("kundentypId") Long kundentypId) {
        kundenService.neuerKundeErstellen(kunde, kundentypId);
        return "redirect:/kunden";
    }

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Kunden an.
     *
     * @param id    die ID des zu bearbeitenden Kunden
     * @param model das Model, um den bestehenden Kunden zur View zu übergeben
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
     * @param id          die ID des zu bearbeitenden Kunden
     * @param kunde       die aktualisierten Daten des Kunden
     * @param kundentypId die ID des Kundentyps
     * @return eine Weiterleitung zur Kundenübersicht
     */
    @PostMapping("/kunden/{id}")
    public String kundeBearbeiten(@PathVariable Long id, @ModelAttribute Kunde kunde, @RequestParam("kundentypId") Long kundentypId) {
        kundenService.kundeBearbeiten(id, kunde, kundentypId);
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
        kundenService.kundeLoeschen(id);
        return "redirect:/kunden";
    }
}

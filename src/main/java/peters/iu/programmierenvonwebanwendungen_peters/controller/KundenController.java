package peters.iu.programmierenvonwebanwendungen_peters.controller;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kundentyp;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundentypRepository;
import peters.iu.programmierenvonwebanwendungen_peters.service.KundenFactory;

import java.util.List;

@Controller
public class KundenController {

    @Autowired
    private KundenRepository kundenRepository;

    @Autowired
    private KundentypRepository kundentypRepository;

    @Autowired
    private KundenFactory kundenFactory;

    @GetMapping("/")
    public String index() {
        return "redirect:/kunden";
    }

    // Alle Kunden anzeigen
    @GetMapping("/kunden")
    public String alleKundenAnzeigen(Model model) {
        List<Kunde> kunden = kundenRepository.findAll();
        model.addAttribute("kunden", kunden);
        model.addAttribute("kundentypen", kundentypRepository.findAll()); // Kundentypen für Dropdown
        return "kundenliste";
    }

    // Formular für neuen Kunden anzeigen
    @GetMapping("/kunden/neu")
    public String neuerKundeFormular(Model model) {
        // Erstellen Sie ein temporäres oder Standard-Kundentyp-Objekt
        Kundentyp standardKundentyp = new Kundentyp();
        standardKundentyp.setId(1L); // Setzen Sie die ID auf einen gültigen Wert aus Ihrer Datenbank
        standardKundentyp.setTyp(1);  // Setzen Sie den Typ entsprechend
        standardKundentyp.setName("Privatkunde");

        model.addAttribute("kunde", new Kunde(standardKundentyp));
        model.addAttribute("kundentypen", kundentypRepository.findAll());
        return "kundenneu";
    }

    // Neuen Kunden erstellen
    @PostMapping("/kunden")
    public String neuerKundeErstellen(@ModelAttribute Kunde kunde, @RequestParam("kundentypId") Long kundentypId) {
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId)
                .orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

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

    // Formular zum Bearbeiten eines Kunden anzeigen
    @GetMapping("/kunden/{id}/bearbeiten")
    public String kundeBearbeitenFormular(@PathVariable Long id, Model model) {
        Kunde kunde = kundenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        model.addAttribute("kunde", kunde);
        model.addAttribute("kundentypen", kundentypRepository.findAll()); // Kundentypen für Dropdown
        return "kundebearbeiten";
    }

    @PostMapping("/kunden/{id}")
    public String kundeBearbeiten(@PathVariable Long id, @ModelAttribute Kunde kunde, @RequestParam("kundentypId") Long kundentypId) {
        Kunde existingKunde = kundenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        Kundentyp kundentyp = kundentypRepository.findById(kundentypId)
                .orElseThrow(() -> new IllegalArgumentException("Ungültiger Kundentyp-ID: " + kundentypId));

        // Überprüfen, ob der Kundentyp geändert wurde
        if (!existingKunde.getKundentyp().equals(kundentyp)) {
            // Neuen Kunden des richtigen Typs erstellen
            Kunde neuerKunde = kundenFactory.erstelleKunde(kundentyp);
            // Relevante Attribute kopieren
            neuerKunde.setVorname(kunde.getVorname());
            neuerKunde.setNachname(kunde.getNachname());
            neuerKunde.setStrasse(kunde.getStrasse());
            neuerKunde.setHausnummer(kunde.getHausnummer());
            neuerKunde.setPostleitzahl(kunde.getPostleitzahl());
            neuerKunde.setEmail(kunde.getEmail());
            neuerKunde.setTelefonnummer(kunde.getTelefonnummer());
            neuerKunde.setKundentyp(kundentyp);

            // Alten Kunden löschen und neuen speichern
            kundenRepository.delete(existingKunde);
            kundenRepository.save(neuerKunde);
        } else {
            // Kundentyp wurde nicht geändert, einfach aktualisieren
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

    // Kunden löschen
    @PostMapping("/kunden/{id}/loeschen")
    public String kundeLoeschen(@PathVariable Long id) {
        kundenRepository.deleteById(id);
        return "redirect:/kunden";
    }
}

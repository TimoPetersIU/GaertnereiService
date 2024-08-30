package peters.iu.programmierenvonwebanwendungen_peters.controller;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import peters.iu.programmierenvonwebanwendungen_peters.repository.KundenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class KundenController {

    @Autowired
    private KundenRepository kundenRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Alle Kunden anzeigen
    @GetMapping("/kunden")
    public String alleKundenAnzeigen(Model model) {
        List<Kunde> kunden = kundenRepository.findAll();
        model.addAttribute("kunden", kunden);
        return "kundenliste";
    }

    // Formular für neuen Kunden anzeigen
    @GetMapping("/kunden/neu")
    public String neuerKundeFormular(Model model) {
        model.addAttribute("kunde", new Kunde());
        return "kundenneu";
    }

    // Neuen Kunden erstellen
    @PostMapping("/kunden")
    public String neuerKundeErstellen(@ModelAttribute Kunde kunde) {
        kundenRepository.save(kunde);
        return "redirect:/kunden";
    }

    // Formular zum Bearbeiten eines Kunden anzeigen
    @GetMapping("/kunden/{id}/bearbeiten")
    public String kundeBearbeitenFormular(@PathVariable Long id, Model model) {
        Kunde kunde = kundenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));
        model.addAttribute("kunde", kunde);
        return "kundebearbeiten";
    }

    // Kunden bearbeiten
    @PostMapping("/kunden/{id}")
    public String kundeBearbeiten(@PathVariable Long id, @ModelAttribute Kunde kunde) {
        Kunde existingKunde = kundenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Kunden-ID: " + id));

        existingKunde.setVorname(kunde.getVorname());
        existingKunde.setNachname(kunde.getNachname());
        existingKunde.setStrasse(kunde.getStrasse());
        existingKunde.setHausnummer(kunde.getHausnummer());
        existingKunde.setPostleitzahl(kunde.getPostleitzahl());
        existingKunde.setEmail(kunde.getEmail());
        existingKunde.setTelefonnummer(kunde.getTelefonnummer());

        kundenRepository.save(existingKunde);
        return "redirect:/kunden";
    }

    // Kunden löschen
    @PostMapping("/kunden/{id}/loeschen")
    public String kundeLoeschen(@PathVariable Long id) {
        kundenRepository.deleteById(id);
        return "redirect:/kunden";
    }
}

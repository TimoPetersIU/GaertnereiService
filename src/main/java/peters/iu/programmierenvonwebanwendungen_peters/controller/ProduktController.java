package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import peters.iu.programmierenvonwebanwendungen_peters.entity.produkt.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;

import java.util.List;

@Controller
public class ProduktController {

    @Autowired
    private ProduktRepository produktRepository;

    // Alle Produkte anzeigen
    @GetMapping("/produkte")
    public String alleProdukteAnzeigen(Model model) {
        List<Produkt> produkte = produktRepository.findAll();
        model.addAttribute("produkte", produkte);
        return "produkteliste";
    }

    // Formular für neues Produkt anzeigen
    @GetMapping("/produkte/neu")
    public String neuesProduktFormular(Model model) {
        model.addAttribute("produkt", new Produkt());
        return "produktneu";
    }

    // Neues Produkt erstellen
    @PostMapping("/produkte")
    public String neuesProduktErstellen(@ModelAttribute Produkt produkt) {
        produktRepository.save(produkt);
        return "redirect:/produkte";
    }

    // Formular zum Bearbeiten eines Produkts anzeigen
    @GetMapping("/produkte/{id}/bearbeiten")
    public String produktBearbeitenFormular(@PathVariable Long id, Model model) {
        Produkt produkt = produktRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + id));
        model.addAttribute("produkt", produkt);
        return "produktbearbeiten";
    }

    // Produkt bearbeiten
    @PostMapping("/produkte/{id}")
    public String produktBearbeiten(@PathVariable Long id, @ModelAttribute Produkt produkt) {
        Produkt existingProdukt = produktRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + id));

        existingProdukt.setName(produkt.getName());
        existingProdukt.setBeschreibung(produkt.getBeschreibung());
        existingProdukt.setPreis(produkt.getPreis());
        existingProdukt.setBestand(produkt.getBestand());

        produktRepository.save(existingProdukt);
        return "redirect:/produkte";
    }

    // Produkt löschen
    @PostMapping("/produkte/{id}/loeschen")
    public String produktLoeschen(@PathVariable Long id) {
        produktRepository.deleteById(id);
        return "redirect:/produkte";
    }

}
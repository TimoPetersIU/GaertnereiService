package peters.iu.programmierenvonwebanwendungen_peters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.produkt.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.BestellpositionRepository;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;

import java.util.List;

/**
 * Controller für die Verwaltung von Produkten.
 * Dieser Controller behandelt die Anzeige, Erstellung, Bearbeitung und Löschung von Produkten.
 *
 * @author Timo Peters - IU Hamburg
 */
@Controller
public class ProduktController {

    @Autowired
    private ProduktRepository produktRepository;

    @Autowired
    private BestellpositionRepository bestellpositionRepository;

    /**
     * Zeigt alle Produkte an.
     * Fügt die Liste der Produkte dem Model hinzu, damit sie in der View angezeigt werden kann.
     *
     * @param model Das Model, das der View übergeben wird.
     * @return Der Name der View, die die Produktliste anzeigt.
     */
    @GetMapping("/produkte")
    public String alleProdukteAnzeigen(Model model) {
        List<Produkt> produkte = produktRepository.findAll();
        model.addAttribute("produkte", produkte); // Produkte der Model-Attribute hinzufügen
        return "produkteliste"; // Rückgabe der View "produkteliste"
    }

    /**
     * Zeigt das Formular zur Erstellung eines neuen Produkts.
     * Ein leeres Produkt-Objekt wird dem Model hinzugefügt, damit es in der View gefüllt werden kann.
     *
     * @param model Das Model, das der View übergeben wird.
     * @return Der Name der View, die das Formular für ein neues Produkt anzeigt.
     */
    @GetMapping("/produkte/neu")
    public String neuesProduktFormular(Model model) {
        model.addAttribute("produkt", new Produkt()); // Ein leeres Produkt-Objekt dem Model hinzufügen
        return "produktneu"; // Rückgabe der View "produktneu"
    }

    /**
     * Speichert ein neues Produkt in der Datenbank.
     *
     * @param produkt Das Produkt-Objekt, das im Formular ausgefüllt wurde.
     * @return Eine Weiterleitung zur Produktliste.
     */
    @PostMapping("/produkte")
    public String neuesProduktErstellen(@ModelAttribute Produkt produkt) {
        produktRepository.save(produkt); // Neues Produkt speichern
        return "redirect:/produkte"; // Weiterleitung zur Produktliste
    }

    /**
     * Zeigt das Formular zur Bearbeitung eines Produkts.
     * Sucht ein bestehendes Produkt anhand der ID und fügt es dem Model hinzu.
     *
     * @param id    Die ID des zu bearbeitenden Produkts.
     * @param model Das Model, das der View übergeben wird.
     * @return Der Name der View, die das Bearbeitungsformular anzeigt.
     * @throws IllegalArgumentException Wenn das Produkt mit der angegebenen ID nicht gefunden wird.
     */
    @GetMapping("/produkte/{id}/bearbeiten")
    public String produktBearbeitenFormular(@PathVariable Long id, Model model) {
        Produkt produkt = produktRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + id)); // Produkt anhand der ID suchen
        model.addAttribute("produkt", produkt); // Gefundenes Produkt dem Model hinzufügen
        return "produktbearbeiten"; // Rückgabe der View "produktbearbeiten"
    }

    /**
     * Speichert die Änderungen an einem bestehenden Produkt.
     *
     * @param id      Die ID des zu bearbeitenden Produkts.
     * @param produkt Das bearbeitete Produkt-Objekt.
     * @return Eine Weiterleitung zur Produktliste.
     * @throws IllegalArgumentException Wenn das Produkt mit der angegebenen ID nicht gefunden wird.
     */
    @PostMapping("/produkte/{id}")
    public String produktBearbeiten(@PathVariable Long id, @ModelAttribute Produkt produkt) {
        Produkt existingProdukt = produktRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + id)); // Existierendes Produkt suchen

        // Bestehendes Produkt mit neuen Werten aktualisieren
        existingProdukt.setName(produkt.getName());
        existingProdukt.setBeschreibung(produkt.getBeschreibung());
        existingProdukt.setPreis(produkt.getPreis());
        existingProdukt.setBestand(produkt.getBestand());

        produktRepository.save(existingProdukt); // Aktualisiertes Produkt speichern
        return "redirect:/produkte"; // Weiterleitung zur Produktliste
    }

    /**
     * Löscht ein Produkt anhand der ID.
     *
     * @param id Die ID des zu löschenden Produkts.
     * @return Eine Weiterleitung zur Produktliste.
     */
    @PostMapping("/produkte/{id}/loeschen")
    public String produktLoeschen(@PathVariable Long id, Model model) {
        Produkt produkt = produktRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + id));

        List<Bestellposition> bestellpositionen = bestellpositionRepository.findByProdukt(produkt);
        if (!bestellpositionen.isEmpty()) {
            model.addAttribute("errorMessage", "Das Produkt kann nicht gelöscht werden, da es in Bestellungen verwendet wird.");
            model.addAttribute("produkte", produktRepository.findAll()); // Produkte erneut laden
            return "produkteliste";
        }

        produktRepository.delete(produkt);
        return "redirect:/produkte";
    }

}

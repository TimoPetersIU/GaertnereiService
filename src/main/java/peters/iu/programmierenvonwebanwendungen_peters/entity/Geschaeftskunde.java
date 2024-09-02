package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Geschaeftskunde")
public class Geschaeftskunde extends Kunde {
    public Geschaeftskunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    public Geschaeftskunde() {

    }
}

package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Privatkunde")
public class Privatkunde extends Kunde {
    public Privatkunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    public Privatkunde() {

    }
}

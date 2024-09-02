package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Stammkunde")
public class Stammkunde extends Kunde {
    public Stammkunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    public Stammkunde() {

    }
}

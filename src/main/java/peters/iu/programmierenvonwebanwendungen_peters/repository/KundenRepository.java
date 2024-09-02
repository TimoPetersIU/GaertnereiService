package peters.iu.programmierenvonwebanwendungen_peters.repository;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kundentyp;

import java.util.List;

@Repository
public interface KundenRepository extends JpaRepository<Kunde, Long> {

    List<Kunde> findByKundentyp(Kundentyp kundentyp);
}

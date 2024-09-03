package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellposition;

@Repository
public interface BestellpositionRepository extends JpaRepository<Bestellposition, Long> {
}
package repository;

import entity.Screening;
import java.time.LocalDate;
import java.util.List;

public interface ScreeningRepository extends Repository<Screening> {
    List<Screening> findByDate(LocalDate date);
}

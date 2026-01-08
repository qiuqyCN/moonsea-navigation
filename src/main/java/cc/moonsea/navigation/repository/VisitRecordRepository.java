package cc.moonsea.navigation.repository;

import cc.moonsea.navigation.entity.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {

    @Query("SELECT COUNT(DISTINCT vr.ip) FROM VisitRecord vr WHERE vr.date = ?1")
    Long countUniqueVisitorsByDate(LocalDate date);

    @Query("SELECT COUNT(vr) FROM VisitRecord vr WHERE vr.date = ?1")
    Long countTotalVisitsByDate(LocalDate date);

    @Query("SELECT COUNT(DISTINCT vr.ip) FROM VisitRecord vr WHERE vr.date BETWEEN ?1 AND ?2")
    Long countUniqueVisitorsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(vr) FROM VisitRecord vr WHERE vr.date BETWEEN ?1 AND ?2")
    Long countTotalVisitsBetweenDates(LocalDate startDate, LocalDate endDate);

    List<VisitRecord> findByDateOrderByCreatedAtAsc(LocalDate date);
}
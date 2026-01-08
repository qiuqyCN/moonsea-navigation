package cc.moonsea.navigation.service;

import cc.moonsea.navigation.entity.VisitRecord;
import cc.moonsea.navigation.repository.VisitRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitRecordService {

    private final VisitRecordRepository visitRecordRepository;

    public void saveVisitRecord(String ip, String userAgent, String uri) {
        VisitRecord record = new VisitRecord();
        record.setIp(ip);
        record.setUserAgent(userAgent);
        record.setUri(uri);
        record.setDate(LocalDate.now());
        visitRecordRepository.save(record);
    }

    public Long getTodayUniqueVisitors() {
        return visitRecordRepository.countUniqueVisitorsByDate(LocalDate.now());
    }

    public Long getTodayTotalVisits() {
        return visitRecordRepository.countTotalVisitsByDate(LocalDate.now());
    }

    public Long getTotalUniqueVisitors() {
        return visitRecordRepository.countUniqueVisitorsBetweenDates(
                LocalDate.of(2024, 1, 1), LocalDate.now());
    }

    public Long getTotalVisits() {
        return visitRecordRepository.countTotalVisitsBetweenDates(
                LocalDate.of(2024, 1, 1), LocalDate.now());
    }

    public Long getUniqueVisitorsByDate(LocalDate date) {
        return visitRecordRepository.countUniqueVisitorsByDate(date);
    }

    public Long getTotalVisitsByDate(LocalDate date) {
        return visitRecordRepository.countTotalVisitsByDate(date);
    }

    public List<VisitRecord> getVisitRecordsByDate(LocalDate date) {
        return visitRecordRepository.findByDateOrderByCreatedAtAsc(date);
    }
}
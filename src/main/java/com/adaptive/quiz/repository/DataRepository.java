package com.adaptive.quiz.repository;

import com.adaptive.quiz.data.ReportAccumulator;
import com.adaptive.quiz.data.ReportData;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataRepository {

    ConcurrentHashMap<UUID, ReportAccumulator> reports = new ConcurrentHashMap<>();

    public UUID register(UUID key) {
        return key;
    }

    public ReportAccumulator add(UUID key, ReportData data) {
        ReportAccumulator reportAccumulator = reports.get(key);
        if (reportAccumulator == null) {
            reportAccumulator = new ReportAccumulator();
            reports.put(key, reportAccumulator);
        }
        reportAccumulator.add(data);
        return reportAccumulator;
    }
}

package org.university.zoomanagementsystem.feeding_record;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.feeding_schedule.FeedingScheduleMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedingRecordMapper {
    private FeedingRecordMapper() {}

    @SuppressWarnings("java:S1172")
    public static FeedingRecord mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new FeedingRecord(rs.getInt("record_id"),
                FeedingScheduleMapper.mapToPojo(rs, ignoredRowNum),
                rs.getDate("date").toLocalDate());
    }
}

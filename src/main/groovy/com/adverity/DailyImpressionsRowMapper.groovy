package com.adverity

import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

class DailyImpressionsRowMapper implements RowMapper<DailyImpressions>{

    @Override
    DailyImpressions mapRow(ResultSet rs, int rowNum) throws SQLException {
        DailyImpressions dailyImpressions = new DailyImpressions()
        dailyImpressions.campaign = rs.getString("campaign")
        dailyImpressions.datasource = rs.getString("datasource")
        dailyImpressions.impressions = rs.getInt("impressions")
        dailyImpressions.setDaily(rs.getDate("daily"))
        return dailyImpressions
    }
}

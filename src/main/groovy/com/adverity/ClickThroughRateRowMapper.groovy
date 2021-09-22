package com.adverity

import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

class ClickThroughRateRowMapper implements RowMapper<ClickThroughRate> {
    @Override
    ClickThroughRate mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClickThroughRate clickThroughRate = new ClickThroughRate()
        clickThroughRate.campaign = rs.getString("campaign")
        clickThroughRate.datasource = rs.getString("datasource")
        clickThroughRate.clickThroughRate = rs.getDouble("clickThroughRate")
        return clickThroughRate
    }
}



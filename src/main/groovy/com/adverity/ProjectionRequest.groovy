package com.adverity

import com.adverity.exceptions.DateParsingException

import java.text.ParseException
import java.text.SimpleDateFormat

class ProjectionRequest {
    Map<String,List<String>> projections
    Map<String, String> filters =[:]
    Map<String, List<String>> groupBy =[:]

    Optional<DateRange> getDateRange() {
        String key = "\$between"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy")
        if(filters.containsKey(key) ) {
            try {
                Map<String, String> model = filters["\$between"]
                Date from = dateFormat.parse(model['from'])
                Date to = dateFormat.parse(model['to'])
                return Optional.of(new DateRange(to: to, from: from))
            }
            catch (ParseException ex) {
                throw new DateParsingException("Please enter a valid date of format dd/MM/yyyy")
            }
        }
        return Optional.empty()
    }
}

class DateRange {
    Date to
    Date from
}
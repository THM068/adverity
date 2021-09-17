package com.adverity

class DataSource {
    String name
    static constraints = {
        name(blank: false, unique: true)
    }
}

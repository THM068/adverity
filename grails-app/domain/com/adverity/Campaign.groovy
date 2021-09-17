package com.adverity

class Campaign {
    String name

    static constraints = {
        name(blank: false, unique: true)
    }
}

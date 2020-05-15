package com.audiospotapplication.data.model

class PaypalArguments {
    var dollarPrice: Double = 0.0
    var key: String = ""
    var invoiceNumber: String = ""

    constructor() {

    }

    constructor(dollarPrice: Double, key: String) {
        this.dollarPrice = dollarPrice
        this.key = key
    }
}
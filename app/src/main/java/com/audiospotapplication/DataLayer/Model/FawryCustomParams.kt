package com.audiospotapplication.DataLayer.Model

class FawryCustomParams(var voucher: String = "", var to: String, var promo_code: String) {

    override fun toString(): String {
        return "{" +
                "voucher='" + voucher + '\''.toString() +
                ", to='" + to + '\''.toString() +
                ", promo_code='" + promo_code + '\''.toString() +
                '}'.toString()
    }
}

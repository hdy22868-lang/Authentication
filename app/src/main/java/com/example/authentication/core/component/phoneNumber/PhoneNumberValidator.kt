package com.example.authentication.core.component.phoneNumber

import android.content.Context
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.util.Locale

data class CountryItem(
    val name: String,
    val callingCode: String,
    val isoCode: String,
    val flagEmoji: String
)

class PhoneNumberValidator(
    context: Context
) {
    private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)

    fun isValidPhoneNumber(phoneNumber: String, countryIsoCode: String): Boolean {
        if (phoneNumber.isBlank() || countryIsoCode.isBlank()) return false
        return try {
            val numberProto = phoneUtil.parse(phoneNumber, countryIsoCode.uppercase())
            phoneUtil.isValidNumberForRegion(numberProto, countryIsoCode.uppercase())
        } catch (e: NumberParseException) {
            false
        }
    }

    fun formatToE164(phoneNumber: String, countryIsoCode: String): String? {
        return try {
            val numberProto = phoneUtil.parse(phoneNumber, countryIsoCode.uppercase())
            if (phoneUtil.isValidNumberForRegion(numberProto, countryIsoCode.uppercase())) {
                phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164)
            } else {
                null
            }
        } catch (e: NumberParseException) {
            null
        }
    }

    fun getAllCountries(): List<CountryItem> {
        val regions = phoneUtil.supportedRegions
        return regions.mapNotNull { isoCode ->
            val countryCode = phoneUtil.getCountryCodeForRegion(isoCode)
            if (countryCode > 0) {
                val countryName = Locale("", isoCode).displayCountry

                // الطريقة الصحيحة والأكيدة لتوليد علم الدولة (Emoji Flag) من الـ ISO Code
                val flag = if (isoCode.length == 2) {
                    val firstChar = Character.codePointAt(isoCode.uppercase(), 0) - 0x41 + 0x1F1E6
                    val secondChar = Character.codePointAt(isoCode.uppercase(), 1) - 0x41 + 0x1F1E6
                    String(intArrayOf(firstChar, secondChar), 0, 2)
                } else {
                    ""
                }

                if (countryName.isNotBlank()) {
                    CountryItem(
                        name = countryName,
                        callingCode = "+$countryCode",
                        isoCode = isoCode,
                        flagEmoji = flag
                    )
                } else null
            } else {
                null
            }
        }.sortedBy { it.name }
    }
}
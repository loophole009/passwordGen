package com.example.passwordgen.util

import android.R
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.math.abs


class Helper {
    companion object {
        @SuppressLint("SimpleDateFormat")
        @Throws(ParseException::class)
        fun getProgress(dateInString: String?): Int {
            val d1: Date
            val d2: Date
            val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz")
            d1 = sdf.parse(sdf.format(Date())) as Date
            d2 = dateInString?.let { sdf.parse(it) } as Date
            val difference = abs(d1.time - d2.time)
            val differenceDates = difference / (24 * 60 * 60 * 1000)
            return Math.toIntExact(differenceDates)
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun decrypt(algorithm: String, cipherText: String, key: SecretKeySpec, iv: IvParameterSpec): String {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.DECRYPT_MODE, key, iv)
            val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))
            return String(plainText)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun encrypt(algorithm: String, inputText: String, key: SecretKeySpec, iv: IvParameterSpec): String {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, key, iv)
            val cipherText = cipher.doFinal(inputText.toByteArray())
            return Base64.getEncoder().encodeToString(cipherText)
        }
    }
}
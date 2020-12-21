package com.example.myapplication.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class EncryptionHandler {


    companion object {
        val salt = "1234567812345678"

        private val ALGORITHM = "AES"

        @RequiresApi(Build.VERSION_CODES.O)
        fun encrypt(input: String): String {

            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = SecretKeySpec(salt.toByteArray(), ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec)

            val encrypt = cipher.doFinal(input.toByteArray());

            return Base64.getEncoder().encodeToString(encrypt)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun decrypt(input: String): String {

            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = SecretKeySpec(salt.toByteArray(),ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, keySpec)

            val decrypt = cipher.doFinal(Base64.getDecoder().decode(input))

            return String(decrypt)
        }

    }

}
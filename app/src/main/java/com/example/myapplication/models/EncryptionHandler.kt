package com.example.myapplication.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHandler {


    companion object {
        val password = "password".toCharArray()
        val salt = "1234567812345678"

        private val ALGORITHM = "AES"

        @RequiresApi(Build.VERSION_CODES.O)
        fun encrypt(input: String): String {

            val cipher = Cipher.getInstance(ALGORITHM)
            val keyBytes = getKeyBytes()

            val keySpec = SecretKeySpec(keyBytes, ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec)

            val encrypt = cipher.doFinal(input.toByteArray());

            return Base64.getEncoder().encodeToString(encrypt)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun decrypt(input: String): String {

            val cipher = Cipher.getInstance(ALGORITHM)
            val keyBytes = getKeyBytes()

            val keySpec = SecretKeySpec(keyBytes, ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, keySpec)

            val decrypt = cipher.doFinal(Base64.getDecoder().decode(input))

            return String(decrypt)
        }

        fun getKeyBytes() : ByteArray {
            val pbKeySpec = PBEKeySpec(password, salt.toByteArray(), 1324, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded

            return keyBytes
        }

    }

}
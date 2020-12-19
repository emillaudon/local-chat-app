package com.example.myapplication.models

import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class EncryptionHandler {

    companion object {

        private val ALGORITHM = "AES"
        private var key: SecretKey? = null

        private var salt = "A8768CC5BEAA6093"

        fun generateKey() {
            key = getKey()
        }

        fun encrypt(string: String): ByteArray {

            val aes = Cipher.getInstance(ALGORITHM)
            aes.init(Cipher.ENCRYPT_MODE, key)

            val l = aes.doFinal(string.toByteArray())

            return l
        }

        fun decrypt(item: ByteArray): String {

            val aes = Cipher.getInstance(ALGORITHM)
            aes.init(Cipher.DECRYPT_MODE, key)
            val decrypted = aes.doFinal(item)

            return decrypted.toString()
        }

        private fun getKey(): SecretKey? {
            var secretKey: SecretKey? = null

            try {
                secretKey = SecretKeySpec(salt.toByteArray(), ALGORITHM)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            println("key" + secretKey)
            return secretKey
        }
    }

}
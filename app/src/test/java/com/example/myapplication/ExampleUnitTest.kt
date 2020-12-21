package com.example.myapplication

import com.example.myapplication.models.EncryptionHandler
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun encryption_isCorrect() {

        val testString = "This is a test string to encrypt"

        val encryptedString = EncryptionHandler.encrypt(testString)

        assertNotEquals(testString, encryptedString)

        val decryptedString = EncryptionHandler.decrypt(encryptedString)

        assertEquals(testString, decryptedString)
    }


}

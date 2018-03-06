package com.baoda.mypos

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.baoda.pos.PosPrinter

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var posPrinter: PosPrinter

    @Before
    fun setup() {
        posPrinter = PosPrinter()
        posPrinter.connect("192.168.2.150")
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.baoda.mypos", appContext.packageName)
    }


    @Test
    fun usePosPrinter() {
        posPrinter.initPosPrinter()
        posPrinter.printText("hello!")
    }

}

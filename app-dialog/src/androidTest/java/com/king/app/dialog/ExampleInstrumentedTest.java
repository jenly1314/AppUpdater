package com.king.app.dialog;

import android.content.Context;

import org.junit.Test;

import androidx.test.InstrumentationRegistry;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.king.app.dialog.test", appContext.getPackageName());
    }
}

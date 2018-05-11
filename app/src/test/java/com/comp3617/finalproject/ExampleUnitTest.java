package com.comp3617.finalproject;

import com.comp3617.bitcoinCasino.RestClient_GET_CryptoCurrencyNews;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


        String s =
        RestClient_GET_CryptoCurrencyNews.getInstanceWithUrl("")
                .processDescription("      <p>hello frinds</p>");

        System.out.println(s);
    }
}
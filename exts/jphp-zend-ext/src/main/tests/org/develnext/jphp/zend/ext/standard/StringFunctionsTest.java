package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ext.standard.StringFunctions;
import org.junit.Assert;
import org.junit.Test;

public class StringFunctionsTest {

  @Test
  public void testToUUChar() {
    Assert.assertEquals('`', StringFunctions.toUUChar(0));
    Assert.assertEquals(' ', StringFunctions.toUUChar(64));
  }

  @Test
  public void testIsWhitespace() {
    Assert.assertFalse(StringFunctions.isWhitespace('\u0000'));
    Assert.assertTrue(StringFunctions.isWhitespace('\t'));
    Assert.assertTrue(StringFunctions.isWhitespace('\r'));
    Assert.assertTrue(StringFunctions.isWhitespace('\n'));
    Assert.assertTrue(StringFunctions.isWhitespace(' '));
  }

  @Test
  public void testToUpperCase() {
    Assert.assertEquals('\u0000', StringFunctions.toUpperCase('\u0000'));
    Assert.assertEquals('{', StringFunctions.toUpperCase('{'));
    Assert.assertEquals('Z', StringFunctions.toUpperCase('z'));
  }

  @Test
  public void testToHexChar() {
    Assert.assertEquals('c', StringFunctions.toHexChar(12));
    Assert.assertEquals('0', StringFunctions.toHexChar(0));
  }

  @Test
  public void testToUpperHexChar() {
    Assert.assertEquals('8', StringFunctions.toUpperHexChar(8));
    Assert.assertEquals('A', StringFunctions.toUpperHexChar(10));
  }

  @Test
  public void testHexToDigit() {
    Assert.assertEquals(0, StringFunctions.hexToDigit('0'));
    Assert.assertEquals(15, StringFunctions.hexToDigit('F'));
    Assert.assertEquals(15, StringFunctions.hexToDigit('f'));
    Assert.assertEquals(-1, StringFunctions.hexToDigit('`'));
    Assert.assertEquals(-1, StringFunctions.hexToDigit('@'));
    Assert.assertEquals(-1, StringFunctions.hexToDigit(' '));
    Assert.assertEquals(-1, StringFunctions.hexToDigit('r'));
  }

  @Test
  public void testOctToDigit() {
    Assert.assertEquals(0, StringFunctions.octToDigit('0'));
    Assert.assertEquals(-1, StringFunctions.octToDigit('8'));
    Assert.assertEquals(-1, StringFunctions.octToDigit(' '));
  }

  @Test
  public void testLtrim() {
    Assert.assertEquals("1a 2b 3c", StringFunctions.ltrim(" 1a 2b 3c"));
    Assert.assertEquals("3", StringFunctions.ltrim("3", "2"));
    Assert.assertEquals("", StringFunctions.ltrim("3", "A1B2C3"));
  }

  @Test
  public void testRtrim() {
    Assert.assertEquals("", StringFunctions.rtrim("1"));
    Assert.assertEquals("1234", StringFunctions.rtrim("1234"));
    Assert.assertEquals("", StringFunctions.rtrim("\'", "2"));
    Assert.assertEquals("1234", StringFunctions.rtrim("1234", "2"));
    Assert.assertEquals("a\'b\'", StringFunctions.rtrim("a\'b\'c", "1a 2b 3c"));
  }

  @Test
  public void testStrcoll() {
    Assert.assertEquals(-1, StringFunctions.strcoll(",", "bar"));
    Assert.assertEquals(1, StringFunctions.strcoll("foo", "bar")); 
    Assert.assertEquals(0, StringFunctions.strcoll("foo", "foo"));
  }

  @Test
  public void test_substr_replace() {
    Assert.assertEquals("barfoobar",
        StringFunctions._substr_replace("foobar", "bar", 0, 0));
    Assert.assertEquals("foobarbar",
        StringFunctions._substr_replace("foobar", "bar", 7, 0));
    Assert.assertEquals("foobabarr",
        StringFunctions._substr_replace("foobar", "bar", -1, 0));
    Assert.assertEquals("bar",
        StringFunctions._substr_replace("", "bar", -1, 0));
    Assert.assertEquals("barr",
        StringFunctions._substr_replace("foobar", "bar", 0, -1));
    Assert.assertEquals("bar",
        StringFunctions._substr_replace("foobar", "bar", 0, 7));
  }
}

package si.urbas.chrony.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.util.Ordinals.toNumericOrdinal;

public class OrdinalsTest {

  @Test(expected = IllegalArgumentException.class)
  public void toNumericOrdinal_MUST_throw_an_exception_WHEN_given_non_positive_numbers() {
    toNumericOrdinal(0);
  }

  @Test
  public void toNumericOrdinal_MUST_return_1st() {
    assertEquals("1st", toNumericOrdinal(1));
  }

  @Test
  public void toNumericOrdinal_MUST_return_2nd() {
    assertEquals("2nd", toNumericOrdinal(2));
  }

  @Test
  public void toNumericOrdinal_MUST_return_3rd() {
    assertEquals("3rd", toNumericOrdinal(3));
  }

  @Test
  public void toNumericOrdinal_MUST_return_th_WHEN_given_numbers_smaller_than_20_and_greater_than_3() {
    assertEquals("4th", toNumericOrdinal(4));
    assertEquals("5th", toNumericOrdinal(5));
    assertEquals("6th", toNumericOrdinal(6));
    assertEquals("7th", toNumericOrdinal(7));
    assertEquals("8th", toNumericOrdinal(8));
    assertEquals("9th", toNumericOrdinal(9));
    assertEquals("10th", toNumericOrdinal(10));
    assertEquals("11th", toNumericOrdinal(11));
    assertEquals("12th", toNumericOrdinal(12));
    assertEquals("13th", toNumericOrdinal(13));
    assertEquals("15th", toNumericOrdinal(15));
    assertEquals("20th", toNumericOrdinal(20));
  }

  @Test
  public void toNumericOrdinal_MUST_return_31st() {
    assertEquals("31st", toNumericOrdinal(31));
  }

  @Test
  public void toNumericOrdinal_MUST_return_22nd() {
    assertEquals("22nd", toNumericOrdinal(22));
  }

  @Test
  public void toNumericOrdinal_MUST_return_43rd() {
    assertEquals("43rd", toNumericOrdinal(43));
  }

  @Test
  public void toNumericOrdinal_MUST_return_111th() {
    assertEquals("111th", toNumericOrdinal(111));
  }

  @Test
  public void toNumericOrdinal_MUST_return_112th() {
    assertEquals("112th", toNumericOrdinal(112));
  }

}
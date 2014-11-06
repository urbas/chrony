package si.urbas.chrony.recurrence;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ConstantPeriodOccurrencesTest {

  private ConstantPeriodOccurrences occurrencesStart150Period400End9001;
  private ConstantPeriodOccurrences occurrencesWithNegativeInterval;
  private ConstantPeriodOccurrences occurrencesWithZeroInterval;

  @Before
  public void setUp() throws Exception {
    occurrencesStart150Period400End9001 = new ConstantPeriodOccurrences(150, 400, 9001);
    occurrencesWithNegativeInterval = new ConstantPeriodOccurrences(123, 150, 42);
    occurrencesWithZeroInterval = new ConstantPeriodOccurrences(123, 150, 123);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_periodInMillis_is_non_positive() {
    new ConstantPeriodOccurrences(123, 0, 42);
  }

  @Test
  public void size_MUST_return_0_WHEN_untilTime_is_smaller_than_first_occurrence_time() {
    assertEquals(0, occurrencesWithNegativeInterval.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_MUST_throw_an_exception_WHEN_accessing_the_first_element_of_an_empty_recurrence_list() {
    occurrencesWithNegativeInterval.get(0);
  }

  @Test
  public void size_MUST_return_1_WHEN_the_untilTime_is_equal_to_the_first_occurrence_time() {
    assertEquals(1, occurrencesWithZeroInterval.size());
  }

  @Test
  public void get_MUST_return_the_first_occurrence_time_for_index_0() {
    assertEquals(123L, occurrencesWithZeroInterval.getOccurrenceAt(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_MUST_throw_an_exception_WHEN_trying_to_access_a_negative_index() {
    occurrencesWithZeroInterval.getOccurrenceAt(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_MUST_throw_an_exception_WHEN_trying_to_access_an_occurrence_at_too_high_an_index() {
    occurrencesStart150Period400End9001.getOccurrenceAt(23);
  }

  @Test
  public void get_MUST_must_return_the_last_element() {
    assertEquals(8950, occurrencesStart150Period400End9001.getOccurrenceAt(22));
  }

  @Test
  public void get_MUST_return_a_correctly_calculated_occurrence() {
    assertEquals(1350L, occurrencesStart150Period400End9001.getOccurrenceAt(3));
  }

  @Test
  public void indexOf_MUST_return_minus_1_WHEN_asking_for_an_occurrence_before_the_first_occurrence() {
    assertEquals(-1L, occurrencesStart150Period400End9001.indexOf(100));
  }

  @Test
  public void indexOf_MUST_return_0_WHEN_asking_for_the_first_occurrence() {
    assertEquals(0, occurrencesStart150Period400End9001.indexOf(150));
  }

  @Test
  public void indexOf_MUST_return_minus_1_WHEN_asking_for_an_occurrence_not_in_the_list() {
    assertEquals(-1, occurrencesStart150Period400End9001.indexOf(701));
  }

  @Test
  public void indexOf_MUST_return_size_minus_1_WHEN_asking_for_the_last_occurrence() {
    assertEquals(occurrencesStart150Period400End9001.size() - 1, occurrencesStart150Period400End9001.indexOf(8950));
  }

  @Test
  public void indexOf_MUST_return_minus_1_WHEN_asking_for_an_occurrence_beyond_the_upper_bound() {
    assertEquals(-1, occurrencesStart150Period400End9001.indexOf(8951));
  }

  @Test
  public void indexOf_MUST_return_an_intermediate_index() {
    assertEquals(5, occurrencesStart150Period400End9001.indexOf(2150));
  }

  @Test
  public void indexOfClosest_MUST_return_0_WHEN_asking_for_an_occurrence_before_the_first() {
    assertEquals(0, occurrencesStart150Period400End9001.indexOfClosest(-120));
  }

  @Test
  public void indexOfClosest_MUST_return_size_minus_1_WHEN_asking_for_an_occurrence_beyond_the_last() {
    assertEquals(occurrencesStart150Period400End9001.size() - 1, occurrencesStart150Period400End9001.indexOfClosest(8951));
  }

  @Test
  public void indexOfClosest_MUST_return_size_minus_1_WHEN_asking_for_an_occurrence_just_before_untilTime() {
    assertEquals(occurrencesStart150Period400End9001.size() - 1, occurrencesStart150Period400End9001.indexOfClosest(8949));
  }

  @Test
  public void indexOfClosest_MUST_return_the_lower_index_WHEN_lower_one_is_closer() {
    assertEquals(1, occurrencesStart150Period400End9001.indexOfClosest(740));
  }

  @Test
  public void indexOfClosest_MUST_return_the_upper_index_WHEN_lower_one_is_closer() {
    assertEquals(2, occurrencesStart150Period400End9001.indexOfClosest(760));
  }

  @Test
  public void distanceTo_MUST_return_negative_1150_WHEN_the_given_time_is_1150_millis_before_the_first_occurrence() {
    assertEquals(-1150, occurrencesStart150Period400End9001.distanceTo(-1000));
  }

  @Test
  public void distanceTo_MUST_return_positive_50_WHEN_the_given_time_is_50_millis_after_the_closest_occurrence() {
    assertEquals(50, occurrencesStart150Period400End9001.distanceTo(200));
  }

  @Test
  public void distanceTo_MUST_return_negative_50_WHEN_the_given_time_is_50_millis_before_the_closest_occurrence() {
    assertEquals(-50, occurrencesStart150Period400End9001.distanceTo(500));
  }

  @Test
  public void distanceTo_MUST_return_1000_WHEN_the_given_time_is_1000_millis_after_the_last_occurrence() {
    assertEquals(1000, occurrencesStart150Period400End9001.distanceTo(9950));
  }

  @Test
  public void contains_MUST_return_false_WHEN_the_given_time_misses_an_occurrence() {
    assertFalse(occurrencesStart150Period400End9001.contains(551));
  }

  @Test
  public void contains_MUST_return_true_WHEN_the_given_time_hits_an_occurrence() {
    assertTrue(occurrencesStart150Period400End9001.contains(550));
  }

  @Test
  public void getSubOccurrences_MUST_return_a_constant_period_occurrences_list_with_the_old_period() {
    ConstantPeriodOccurrences subOccurrences = (ConstantPeriodOccurrences) occurrencesStart150Period400End9001.getSubOccurrences(-150, 7400);
    assertEquals(400, subOccurrences.getPeriodInMillis());
  }

  @Test
  public void getSubOccurrences_MUST_return_a_constant_period_occurrences_list_with_the_old_lower_bound_WHEN_the_given_one_is_lower() {
    ConstantPeriodOccurrences subOccurrences = (ConstantPeriodOccurrences) occurrencesStart150Period400End9001.getSubOccurrences(-150, 7400);
    assertEquals(150, subOccurrences.getFromTimeInMillis());
  }

  @Test
  public void getSubOccurrences_MUST_return_a_constant_period_occurrences_list_with_the_new_lower_bound_WHEN_the_given_one_is_higher() {
    ConstantPeriodOccurrences subOccurrences = (ConstantPeriodOccurrences) occurrencesStart150Period400End9001.getSubOccurrences(200, 7400);
    assertEquals(200, subOccurrences.getFromTimeInMillis());
  }

  @Test
  public void getSubOccurrences_MUST_return_a_constant_period_occurrences_list_with_the_new_upper_bound_WHEN_the_given_one_is_lower() {
    ConstantPeriodOccurrences subOccurrences = (ConstantPeriodOccurrences) occurrencesStart150Period400End9001.getSubOccurrences(200, 7400);
    assertEquals(7400, subOccurrences.getUntilTimeInMillis());
  }

  @Test
  public void getSubOccurrences_MUST_return_a_constant_period_occurrences_list_with_the_old_upper_bound_WHEN_the_given_one_is_higher() {
    ConstantPeriodOccurrences subOccurrences = (ConstantPeriodOccurrences) occurrencesStart150Period400End9001.getSubOccurrences(200, 9002);
    assertEquals(9001, subOccurrences.getUntilTimeInMillis());
  }

}
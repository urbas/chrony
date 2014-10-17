package si.urbas.chrony.recurrence;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class OccurrenceListTest {

  @Test
  public void size_MUST_return_0_WHEN_toTime_is_smaller_than_first_occurrence_time() {
    OccurrenceList occurrences = new OccurrenceList(123, 42, 150);
    assertEquals(0, occurrences.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_periodInMillis_is_non_positive() {
    new OccurrenceList(123, 42, 0);
  }

  @Test
  public void size_MUST_return_1_WHEN_the_toTime_is_equal_to_the_first_occurrence_time() {
    assertEquals(1, new OccurrenceList(123, 123, 150).size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_MUST_throw_an_exception_WHEN_accessing_the_first_element_of_an_empty_recurrence_list() {
    new OccurrenceList(123, 42, 150).getOccurrenceAt(0);
  }

  @Test
  public void get_MUST_return_the_first_occurrence_time_for_index_0() {
    assertEquals(123L, new OccurrenceList(123, 123, 150).getOccurrenceAt(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_MUST_throw_an_exception_WHEN_trying_to_access_a_negative_index() {
    new OccurrenceList(123, 123, 150).getOccurrenceAt(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_MUST_throw_an_exception_WHEN_trying_to_access_an_occurrence_at_too_high_an_index() {
    new OccurrenceList(123, 273, 150).getOccurrenceAt(2);
  }

  @Test
  public void get_MUST_return_a_correctly_calculated_occurrence() {
    assertEquals(423L, new OccurrenceList(123, 423, 150).getOccurrenceAt(2));
  }

  @Test
  public void indexOf_MUST_return_minus_1_WHEN_asking_for_an_occurrence_before_the_first_occurrence() {
    assertEquals(-1L, new OccurrenceList(300, 9001, 400).indexOf(250));
  }

  @Test
  public void indexOf_MUST_return_0_WHEN_asking_for_the_first_occurrence() {
    assertEquals(0, new OccurrenceList(300, 9001, 400).indexOf(300));
  }

  @Test
  public void indexOf_MUST_return_minus_1_WHEN_asking_for_an_occurrence_not_in_the_list() {
    assertEquals(-1, new OccurrenceList(300, 9001, 400).indexOf(701));
  }

  @Test
  public void indexOf_MUST_return_minus_1_WHEN_asking_for_an_occurrence_beyond_the_upper_bound() {
    assertEquals(-1, new OccurrenceList(300, 3900, 400).indexOf(3901));
  }

  @Test
  public void indexOf_MUST_return_size_minus_1_WHEN_asking_for_the_last_occurrence() {
    assertEquals(9, new OccurrenceList(300, 3900, 400).indexOf(3900));
  }

}
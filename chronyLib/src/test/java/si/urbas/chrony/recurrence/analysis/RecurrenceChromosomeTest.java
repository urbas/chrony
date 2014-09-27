package si.urbas.chrony.recurrence.analysis;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static si.urbas.chrony.recurrence.RecurrencesTestUtils.toList;

public class RecurrenceChromosomeTest {

  private RecurrenceFitnessPolicy recurrenceFitnessPolicy;
  private RecurrenceChromosome recurrenceChromosomeSize1_Binary1;
  private RecurrenceChromosome recurrenceChromosomeSize3_Binary101;

  @Before
  public void setUp() throws Exception {
    recurrenceFitnessPolicy = mock(RecurrenceFitnessPolicy.class);
    recurrenceChromosomeSize1_Binary1 = new RecurrenceChromosome(singleAvailableRecurrence(), Arrays.asList(1), recurrenceFitnessPolicy);
    recurrenceChromosomeSize3_Binary101 = new RecurrenceChromosome(threeAvailableRecurrences(), Arrays.asList(1, 0, 1), recurrenceFitnessPolicy);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_sizes_of_available_recurrences_and_included_recurrences_do_not_match() {
    new RecurrenceChromosome(singleAvailableRecurrence(), emptyIncludedRecurrences(), recurrenceFitnessPolicy);
  }

  @Test
  public void fitness_MUST_delegate_to_the_fitness_policy() {
    recurrenceChromosomeSize1_Binary1.fitness();
    verify(recurrenceFitnessPolicy).fitness(recurrenceChromosomeSize1_Binary1);
  }

  @Test
  public void fitness_MUST_return_the_answer_from_the_fitness_policy() {
    double expectedFitness = Math.random();
    when(recurrenceFitnessPolicy.fitness(recurrenceChromosomeSize1_Binary1)).thenReturn(expectedFitness);
    assertThat(recurrenceChromosomeSize1_Binary1.fitness(), equalTo(expectedFitness));
  }

  @Test
  public void size_MUST_return_the_number_of_non_zero_elements_in_the_binary_list() {
    assertEquals(2, recurrenceChromosomeSize3_Binary101.getRecurrencesCount());
  }

  @Test
  public void getRecurrences_MUST_return_only_those_recurrences_provided_in_the_binary_list() {
    List<Recurrence> availableRecurrences = threeAvailableRecurrences();
    assertThat(
      recurrenceChromosomeSize3_Binary101.getRecurrences(),
      contains(availableRecurrences.get(0), availableRecurrences.get(2))
    );
  }

  private List<Recurrence> singleAvailableRecurrence() {return toList(new DailyPeriodRecurrence(7, 0, 0, 0, 0, 0));}

  private List<Recurrence> threeAvailableRecurrences() {
    return toList(
      new DailyPeriodRecurrence(7, 0, 0, 0, 0, 0),
      new DailyPeriodRecurrence(1, 0, 0, 0, 2, 3),
      new DailyPeriodRecurrence(4, 0, 0, 0, 5, 6)
    );
  }

  private ArrayList<Integer> emptyIncludedRecurrences() {return new ArrayList<Integer>();}

}
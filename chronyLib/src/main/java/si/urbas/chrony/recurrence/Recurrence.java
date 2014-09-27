package si.urbas.chrony.recurrence;

public interface Recurrence {
  /**
   * @return milliseconds from the closest recurrence to the given time. A positive value is returned if the closest
   * recurrence happens before the given time, otherwise a negative value is returned.
   */
  long differenceTo(long timeInMilliseconds);
}

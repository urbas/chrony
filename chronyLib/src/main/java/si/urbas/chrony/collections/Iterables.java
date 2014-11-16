package si.urbas.chrony.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Iterables {

  public static <T> ArrayList<T> merge(Iterable<T> elementsA, Iterable<T> elementsB, Comparator<T> comparator) {
    ArrayList<T> mergedElements = new ArrayList<T>();
    Iterator<T> elementsAIterator = elementsA.iterator();
    Iterator<T> elementsBIterator = elementsB.iterator();
    addInterleavedInOrder(mergedElements, elementsAIterator, elementsBIterator, comparator);
    addRest(mergedElements, elementsAIterator);
    addRest(mergedElements, elementsBIterator);
    return mergedElements;
  }

  private static <T> void addInterleavedInOrder(ArrayList<T> mergedElements, Iterator<T> elementsAIterator, Iterator<T> elementsBIterator, Comparator<T> comparator) {
    T elementA = nextOrNull(elementsAIterator);
    T elementB = nextOrNull(elementsBIterator);
    while (elementA != null && elementB != null) {
      if (comparator.compare(elementA, elementB) <= 0) {
        mergedElements.add(elementA);
        elementA = nextOrNull(elementsAIterator);
      } else {
        mergedElements.add(elementB);
        elementB = nextOrNull(elementsBIterator);
      }
    }
    addIfNonNull(mergedElements, elementA);
    addIfNonNull(mergedElements, elementB);
  }

  private static <T> void addIfNonNull(ArrayList<T> mergedElements, T element) {
    if (element != null) {
      mergedElements.add(element);
    }
  }

  private static <T> T nextOrNull(Iterator<T> iterator) {
    return iterator.hasNext() ? iterator.next() : null;
  }

  private static <T> void addRest(ArrayList<T> mergedElements, Iterator<T> elementsAIterator) {
    while (elementsAIterator.hasNext()) {
      T elementA = elementsAIterator.next();
      mergedElements.add(elementA);
    }
  }
}

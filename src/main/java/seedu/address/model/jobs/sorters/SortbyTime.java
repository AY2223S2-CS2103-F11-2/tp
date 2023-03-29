package seedu.address.model.jobs.sorters;

import java.util.Comparator;
import java.util.NoSuchElementException;

import seedu.address.model.jobs.DeliveryJob;

/**
 * Helper class implementing Comparator
 * Sort by job's scheduled timing
 */
public class SortbyTime implements Comparator<DeliveryJob> {

    /**
     * Method sort by time (increasing)
     * @param a the first job to be compared.
     * @param b the second job to be compared.
     * @return difference between jobs' timing
     */
    public int compare(DeliveryJob a, DeliveryJob b) {
        try {
            return a.getDate().compareTo(b.getDate());
        } catch (NoSuchElementException e) {
            if (b.isScheduled()) {
                return -1;
            } else if (a.isScheduled()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

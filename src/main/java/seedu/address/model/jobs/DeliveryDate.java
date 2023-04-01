package seedu.address.model.jobs;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a job's job date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class DeliveryDate {

    public static final String MESSAGE_CONSTRAINTS = "Dates should only contain numeric characters and spaces, "
            + "and it should not be blank\n"
            + "with format like this: YYYY-mm-DD";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final DateTimeFormatter VALID_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-dd");

    private static final String placeholder = "0000-00-00";

    public final String date;

    /**
     * Constructs a {@code JobDate}.
     *
     * @param date A valid date.
     */
    public DeliveryDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        this.date = date;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidDate(String test) {
        try {
            if (test.equals(placeholder)) {
                return true;
            }
            LocalDate.parse(test);
            return test.matches(VALIDATION_REGEX);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns date in LocalDate format
     */
    public LocalDate getDate() {
        return LocalDate.parse(this.date);
    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeliveryDate // instanceof handles nulls
                        && date.equals(((DeliveryDate) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    public static DeliveryDate placeholder() {
        return new DeliveryDate(placeholder);
    }

}

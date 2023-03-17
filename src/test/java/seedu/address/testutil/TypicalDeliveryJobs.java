package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.DeliveryJobSystem;
import seedu.address.model.jobs.DeliveryJob;

/**
 * A set of TypicalDeliveryJobs
 */
public class TypicalDeliveryJobs {

    public static final DeliveryJob JOBA = new DeliveryJob(
        TypicalPersons.ALICE.getPersonId(),
        TypicalPersons.BOB.getPersonId(),
        "1.0");

    public static final DeliveryJob JOBB = new DeliveryJob(
        TypicalPersons.BOB.getPersonId(),
        TypicalPersons.CARL.getPersonId(),
        "2.0");
    public static final DeliveryJob JOBC = new DeliveryJob(
        TypicalPersons.CARL.getPersonId(),
        TypicalPersons.DANIEL.getPersonId(),
        "3.0");
    public static final DeliveryJob JOBD = new DeliveryJob(
        TypicalPersons.DANIEL.getPersonId(),
        TypicalPersons.ELLE.getPersonId(),
        "4.0");
    public static final DeliveryJob JOBE = new DeliveryJob(
        TypicalPersons.ELLE.getPersonId(),
        TypicalPersons.FIONA.getPersonId(),
        "5.0");
    public static final DeliveryJob JOBF = new DeliveryJob(
        TypicalPersons.FIONA.getPersonId(),
        TypicalPersons.GEORGE.getPersonId(),
        "6.0");

    public static DeliveryJobSystem getTypicalDeliveryJobSystem() {
        DeliveryJobSystem sampleDS = new DeliveryJobSystem();
        for (DeliveryJob sampleJob : getTypicalDeliveryJobs()) {
            sampleDS.addDeliveryJob(sampleJob);
        }
        return sampleDS;
    }

    public static List<DeliveryJob> getTypicalDeliveryJobs() {
        return new ArrayList<>(Arrays.asList(JOBA, JOBB, JOBC, JOBD, JOBE, JOBF));
    }
}

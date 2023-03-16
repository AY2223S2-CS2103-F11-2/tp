package seedu.address.model.jobs;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents delivery jobs entities.
 */
public class DeliveryJob {
    // Identity fields
    private final String jobId;

    // Delivery informations
    private final String recepient;
    private final String sender; // aka customer
    private final String deliverSlot; // TODO: Update data type when confirmed
    private final Earning earning;
    private final boolean isDelivered;

    /**
     * Constructs a job entity.
     *
     * @param recepient
     * @param sender
     * @param deliverSlot
     * @param earning
     */
    public DeliveryJob(String recepient, String sender, String deliverSlot, String earning) {
        this(genJobId(recepient, sender), recepient, sender, deliverSlot, earning, false);
    }

    /**
     * DeliveryJob
     *
     * @param jobId
     * @param recepient
     * @param deliverSlot
     * @param sender
     * @param earning
     * @param isDelivered
     */
    public DeliveryJob(String jobId, String recepient, String sender, String deliverSlot, String earning,
            boolean isDelivered) {
        this.jobId = jobId;
        this.recepient = recepient;
        this.sender = sender;
        this.deliverSlot = deliverSlot;
        this.earning = new Earning(earning);
        this.isDelivered = isDelivered;
    }

    private static String genJobId(String recepient, String sender) {
        requireNonNull(recepient, sender);
        return recepient.substring(0, 2)
                .concat(sender.substring(0, 2))
                .concat(UUID.randomUUID().toString().substring(0, 6))
                .toUpperCase();
    }

    public String getJobId() {
        return jobId;
    }

    public String getRecepientId() {
        return recepient;
    }

    public String getSenderId() {
        return sender;
    }

    public String getDeliverSlot() {
        return deliverSlot;
    }

    public Earning getEarning() {
        return earning;
    }

    public boolean getDeliveredStatus() {
        return isDelivered;
    }
    public LocalDate getDeliverDate() {
        return LocalDate.now();
    }


    /**
     * isSameDeliveryJob.
     *
     * @param otherJob
     * @return
     */
    public boolean isSameDeliveryJob(DeliveryJob otherJob) {
        if (otherJob == this) {
            return true;
        }

        return otherJob != null && otherJob.getJobId().equals(getJobId());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        String outString = "Job [%s]\n"
                + "receipent: %s\n"
                + "sender: %s\n"
                + "slot: %s\n"
                + "earn: $%s\n"
                + "status: $%s\n";

        builder.append(
                String.format(outString,
                        jobId,
                        getRecepientId(),
                        getSenderId(),
                        getDeliverSlot(),
                        getEarning(),
                        getDeliveredStatus()));

        return builder.toString();
    }
}

package org.firstinspires.ftc.teamcode.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * InterpolationTable - Table with linear interpolation
 *
 * Three-column table structure:
 * - Column 1: Key (x-axis, distance from robot to the goal)
 * - Column 2: Data 1 (y1-axis, velocity of the flywheel)
 * - Column 3: Data 2 (y2-axis, hood position)
 *
 * Features:
 * - Linear interpolation between points
 * - Sorted by key for efficient lookup
 * - Handles edge cases (out of bounds)
 * - Multiple interpolation modes
 */
public class InterpolationTable {

    /**
     * Table entry with key and two data values
     */
    public static class Entry {
        public final double key;
        public final double data1;
        public final double data2;

        public Entry(double key, double data1, double data2) {
            this.key = key;
            this.data1 = data1;
            this.data2 = data2;
        }

        @Override
        public String toString() {
            return String.format("(%.2f → %.2f, %.2f)", key, data1, data2);
        }
    }

    /**
     * Interpolation result
     */
    public static class Result {
        public final double data1;
        public final double data2;
        public final boolean extrapolated;  // True if outside table range

        public Result(double data1, double data2, boolean extrapolated) {
            this.data1 = data1;
            this.data2 = data2;
            this.extrapolated = extrapolated;
        }

        @Override
        public String toString() {
            String flag = extrapolated ? " (EXTRAPOLATED)" : "";
            return String.format("(%.2f, %.2f)%s", data1, data2, flag);
        }
    }

    /**
     * Extrapolation mode (what to do outside table range)
     */
    public enum ExtrapolationMode {
        CLAMP,      // Use nearest table value
        LINEAR,     // Continue linear trend
        ZERO        // Return zero
    }

    private List<Entry> entries;
    private ExtrapolationMode extrapolationMode;
    private boolean sorted;

    /**
     * Create empty table with CLAMP extrapolation
     */
    public InterpolationTable() {
        this(ExtrapolationMode.CLAMP);
    }

    /**
     * Create empty table with specified extrapolation mode
     */
    public InterpolationTable(ExtrapolationMode mode) {
        this.entries = new ArrayList<>();
        this.extrapolationMode = mode;
        this.sorted = true;
    }

    /**
     * Add entry to table
     */
    public void add(double key, double data1, double data2) {
        entries.add(new Entry(key, data1, data2));
        sorted = false;
    }

    /**
     * Add entry to table
     */
    public void add(Entry entry) {
        entries.add(entry);
        sorted = false;
    }

    /**
     * Build table from arrays
     */
    public void build(double[] keys, double[] data1Array, double[] data2Array) {
        if (keys.length != data1Array.length || keys.length != data2Array.length) {
            throw new IllegalArgumentException("Array lengths must match");
        }

        entries.clear();
        for (int i = 0; i < keys.length; i++) {
            entries.add(new Entry(keys[i], data1Array[i], data2Array[i]));
        }
        sorted = false;
    }

    /**
     * Sort table by key (required before interpolation)
     */
    private void ensureSorted() {
        if (!sorted) {
            Collections.sort(entries, Comparator.comparingDouble(e -> e.key));
            sorted = true;
        }
    }

    /**
     * Get interpolated values for given key
     */
    public Result get(double key) {
        ensureSorted();

        if (entries.isEmpty()) {
            return new Result(0, 0, true);
        }

        if (entries.size() == 1) {
            Entry e = entries.get(0);
            return new Result(e.data1, e.data2, true);
        }

        // Check if key is below minimum
        if (key <= entries.get(0).key) {
            return handleBelowMin(key);
        }

        // Check if key is above maximum
        if (key >= entries.get(entries.size() - 1).key) {
            return handleAboveMax(key);
        }

        // Find surrounding entries for interpolation
        for (int i = 0; i < entries.size() - 1; i++) {
            Entry lower = entries.get(i);
            Entry upper = entries.get(i + 1);

            if (key >= lower.key && key <= upper.key) {
                return interpolate(key, lower, upper);
            }
        }

        // Should never reach here
        Entry last = entries.get(entries.size() - 1);
        return new Result(last.data1, last.data2, true);
    }

    /**
     * Handle key below minimum
     */
    private Result handleBelowMin(double key) {
        Entry first = entries.get(0);

        switch (extrapolationMode) {
            case CLAMP:
                return new Result(first.data1, first.data2, true);

            case LINEAR:
                if (entries.size() >= 2) {
                    Entry second = entries.get(1);
                    return interpolate(key, first, second);
                }
                return new Result(first.data1, first.data2, true);

            case ZERO:
                return new Result(0, 0, true);

            default:
                return new Result(first.data1, first.data2, true);
        }
    }

    /**
     * Handle key above maximum
     */
    private Result handleAboveMax(double key) {
        Entry last = entries.get(entries.size() - 1);

        switch (extrapolationMode) {
            case CLAMP:
                return new Result(last.data1, last.data2, true);

            case LINEAR:
                if (entries.size() >= 2) {
                    Entry secondLast = entries.get(entries.size() - 2);
                    return interpolate(key, secondLast, last);
                }
                return new Result(last.data1, last.data2, true);

            case ZERO:
                return new Result(0, 0, true);

            default:
                return new Result(last.data1, last.data2, true);
        }
    }

    /**
     * Perform linear interpolation between two entries
     */
    private Result interpolate(double key, Entry lower, Entry upper) {
        // Calculate interpolation factor (0.0 to 1.0)
        double t = (key - lower.key) / (upper.key - lower.key);

        // Linear interpolation formula: y = y1 + t * (y2 - y1)
        double data1 = lower.data1 + t * (upper.data1 - lower.data1);
        double data2 = lower.data2 + t * (upper.data2 - lower.data2);

        boolean extrapolated = (t < 0 || t > 1);
        return new Result(data1, data2, extrapolated);
    }

    /**
     * Get data1 only
     */
    public double getData1(double key) {
        return get(key).data1;
    }

    /**
     * Get data2 only
     */
    public double getData2(double key) {
        return get(key).data2;
    }

    /**
     * Get number of entries
     */
    public int size() {
        return entries.size();
    }

    /**
     * Check if table is empty
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Clear all entries
     */
    public void clear() {
        entries.clear();
        sorted = true;
    }

    /**
     * Get all entries (sorted)
     */
    public List<Entry> getEntries() {
        ensureSorted();
        return new ArrayList<>(entries);
    }

    /**
     * Get key range
     */
    public double getMinKey() {
        ensureSorted();
        return entries.isEmpty() ? 0 : entries.get(0).key;
    }

    public double getMaxKey() {
        ensureSorted();
        return entries.isEmpty() ? 0 : entries.get(entries.size() - 1).key;
    }

    /**
     * Set extrapolation mode
     */
    public void setExtrapolationMode(ExtrapolationMode mode) {
        this.extrapolationMode = mode;
    }

    /**
     * Get table summary
     */
    @Override
    public String toString() {
        ensureSorted();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("InterpolationTable [%d entries, mode=%s]\n",
            entries.size(), extrapolationMode));
        sb.append("Key      → Data1     Data2\n");
        sb.append("--------------------------------\n");
        for (Entry e : entries) {
            sb.append(String.format("%-8.2f → %-9.2f %-9.2f\n",
                e.key, e.data1, e.data2));
        }
        return sb.toString();
    }
}


/**
 * Example Usage: Shooting Table
 * Distance → Arm Angle, Shooter Speed
 */
class ShootingTable {

    private InterpolationTable table;

    public ShootingTable() {
        table = new InterpolationTable(InterpolationTable.ExtrapolationMode.CLAMP);

        // Distance (inches) → Arm Angle (degrees), Shooter Speed (RPM)
        table.add(12, 30, 2000);   // Close range
        table.add(24, 35, 2200);
        table.add(36, 40, 2400);
        table.add(48, 45, 2600);
        table.add(60, 50, 2800);   // Far range
    }

    public double getArmAngle(double distance) {
        return table.getData1(distance);
    }

    public double getShooterSpeed(double distance) {
        return table.getData2(distance);
    }

    public InterpolationTable.Result getBoth(double distance) {
        return table.get(distance);
    }
}


/**
 * Example Usage: Slide Compensation Table
 * Slide Extension → Arm Angle Adjustment, Power Multiplier
 */
class SlideCompensationTable {

    private InterpolationTable table;

    public SlideCompensationTable() {
        table = new InterpolationTable(InterpolationTable.ExtrapolationMode.LINEAR);

        // Slide Position → Arm Angle Offset, Power Multiplier
        table.add(0,    0,    1.0);   // Fully retracted
        table.add(500,  5,    0.95);  // Partial extension
        table.add(1000, 10,   0.9);   // Half extension
        table.add(1500, 15,   0.85);  // Most extended
        table.add(2000, 20,   0.8);   // Fully extended
    }

    public double getArmOffset(double slidePosition) {
        return table.getData1(slidePosition);
    }

    public double getPowerMultiplier(double slidePosition) {
        return table.getData2(slidePosition);
    }
}


/**
 * Example Usage: Battery Compensation Table
 * Voltage → Speed Correction, Servo Offset
 */
class BatteryCompensationTable {

    private InterpolationTable table;

    public BatteryCompensationTable() {
        table = new InterpolationTable(InterpolationTable.ExtrapolationMode.CLAMP);

        // Voltage → Speed Multiplier, Servo Position Offset
        table.add(11.0, 1.15, 0.05);  // Low voltage - increase power
        table.add(12.0, 1.05, 0.02);
        table.add(13.0, 1.0,  0.0);   // Nominal voltage
        table.add(13.5, 0.98, -0.01);
        table.add(14.0, 0.95, -0.02); // High voltage - decrease power
    }

    public double getSpeedCorrection(double voltage) {
        return table.getData1(voltage);
    }

    public double getServoOffset(double voltage) {
        return table.getData2(voltage);
    }
}


/**
 * Example Usage: Vision Targeting Table
 * AprilTag Distance → Forward Power, Strafe Power
 */
class VisionTargetingTable {

    private InterpolationTable table;

    public VisionTargetingTable() {
        table = new InterpolationTable(InterpolationTable.ExtrapolationMode.ZERO);

        // Distance (inches) → Forward Power, Strafe Correction
        table.add(6,  0.0,  0.0);   // At target - stop
        table.add(12, 0.2,  0.05);  // Very close - slow
        table.add(24, 0.4,  0.1);   // Close
        table.add(36, 0.6,  0.15);  // Medium
        table.add(48, 0.8,  0.2);   // Far - faster approach
    }

    public double getForwardPower(double distance) {
        return table.getData1(distance);
    }

    public double getStrafeCorrection(double distance) {
        return table.getData2(distance);
    }
}


/**
 * Example Usage: Encoder to Real World Conversion
 * Encoder Counts → Degrees, Inches
 */
class EncoderConversionTable {

    private InterpolationTable table;

    public EncoderConversionTable() {
        table = new InterpolationTable(InterpolationTable.ExtrapolationMode.LINEAR);

        // Encoder → Degrees, Inches
        // (Useful if relationship is non-linear due to gearing, etc.)
        table.add(0,    0,    0);
        table.add(100,  10,   2.5);
        table.add(200,  20,   5.0);
        table.add(300,  30,   7.5);
        table.add(400,  40,   10.0);
        table.add(500,  50,   12.5);
    }

    public double getDegrees(int encoderCounts) {
        return table.getData1(encoderCounts);
    }

    public double getInches(int encoderCounts) {
        return table.getData2(encoderCounts);
    }
}
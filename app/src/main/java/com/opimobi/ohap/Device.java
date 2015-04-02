package com.opimobi.ohap;

/**
 * A real device in an OHAP application. Inherits all common properties from the
 * {@link com.opimobi.ohap.Item} base class.
 *
 * A device may be either a sensor or an actuator. The actual value of a sensor may be only
 * queried. For an actuator, it is also possible to change the value.
 *
 * The actual value may be either binary or decimal. For the case of a binary value, the device
 * provides {@link #getBinaryValue()} and {@link #setBinaryValue(boolean)} method. For decimal
 * values, the {@link #getDecimalValue()} and {@link #setDecimalValue(double)} methods as well a
 * {@link #getMinValue()}, {@link #getMaxValue()}, {@link #setMinMaxValues(double, double)} methods
 * are used. In addition, a device provides interface to get and set the unit and unit abbreviation
 * of the decimal value.
 *
 * @see com.opimobi.ohap.Item
 *
 * @author Henrik hedberg &lt;henrik.hedberg@iki.fi>
 * @version 1.0
 */
public class Device extends Item {
    /**
     * The possible types of a device.
     */
    public enum Type {
        /**
         * Actuator device. The actual value may be both queried and changed.
         */
        ACTUATOR,

        /**
         * Sensor device. The actual value of the device may be only queried from the central unit.
         */
        SENSOR
    }

    /**
     * The possible types of the value of a device.
     */
    public enum ValueType {
        /**
         * Binary value. The actual value is boolean.
         */
        BINARY,

        /**
         * Decimal value. The actual value is double.
         */
        DECIMAL
    }

    /**
     * The type of the device.
     */
    private Type type;

    /**
     * The type of the value of the device.
     */
    private ValueType valueType;

    /**
     * The value of the device if the type of the value is binary. Otherwise, false.
     */
    private boolean binaryValue;

    /**
     * The value of the device if the type of the value is decimal. Otherwise, NaN.
     */
    private double decimalValue = Double.NaN;

    /**
     * The minimum value of the value of the device if the type of the value is decimal.
     * Otherwise, Nan.
     */
    private double minValue = Double.NaN;

    /**
     * The maximum value of the value of the device if the type of the value is decimal.
     * Otherwise, NaN.
     */
    private double maxValue = Double.NaN;

    /**
     * The unit of the value of the device as a text if the type of the value is decimal.
     * Otherwise, null.
     */
    private String unit;

    /**
     * The unit of the value of the device as an abbreviated form if the type of the value is
     * decimal. Otherwise, null.
     */
    private String unitAbbreviation;

    /**
     * The event source for value changed events. The listeners of this source will be called
     * after the value of the device has changed. The event argument (Object) will be null.
     */
    public final EventSource<Device, Object> valueChangedEventSource = new EventSource<>(this);

    /**
     * Constructs a new device with the parent and identifier properties.
     *
     * @see com.opimobi.ohap.Item#Item(Container, long)
     *@param parent The parent container of the item.
     * @param id The unique identifier of the item. Must be positive integer.
     */
    public Device(Container parent, long id) {
        super(parent, id);

        this.type = type;
        this.valueType = valueType;
    }

    /**
     * Returns the binary value of the device, if the type of the value is binary.
     * If the type of the value is not binary, returns false.
     *
     * @return The binary value of the device.
     */
    public boolean getBinaryValue() {
        return binaryValue;
    }

    /**
     * Returns the decimal value of the device, if the type of the value is decimal.
     * If the type of the value is not decimal, returns Nan.
     *
     * @return The decimal value of the device or NaN.
     */
    public double getDecimalValue() {
        return decimalValue;
    }

    /**
     * Returns the maximum value of the value of the device, if the type of the value is decimal.
     * If the type of the value is not decimal, returns Nan.
     *
     * @return The maximum value of the decimal value of the device or NaN.
     */
    public double getMaxValue() {
            return maxValue;
    }

    /**
     * Returns the minimum value of the value of the device, if the type of the value is decimal.
     * If the type of the value is not decimal, returns Nan.
     *
     * @return The minimum value of the decimal value of the device or NaN.
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Returns the type of the device.
     *
     * @return The type of the device.
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the unit of the value of the device, if the type of the value is decimal.
     * If the type of the value is not decimal, returns null.
     *
     * @return The unit of the decimal value or null.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Returns the abbreviation of the unit of the value of the device, if the type of the value is
     * decimal. If the type of the value is not decimal, returns null.
     *
     * @return The unit abbreviation of the decimal value or null.
     */
    public String getUnitAbbreviation() {
        return unitAbbreviation;
    }

    /**
     * Returns the type of the value of the device.
     *
     * @return The type of the value of the device.
     */
    public ValueType getValueType() {
        return valueType;
    }

    /**
     * Sets the binary value of the device, if the type of the value is binary. If the type of
     * the value is not binary, throws an {@link java.lang.IllegalStateException}.
     *
     * Calls the listeners of the {@link #valueChangedEventSource} after the value has been changed.
     *
     * @param binaryValue The binary value of the device.
     * @exception java.lang.IllegalStateException If the type of the value is not binary.
     */
    public void setBinaryValue(boolean binaryValue) {
        if (valueType != ValueType.BINARY)
            throw new IllegalStateException("Value type is not binary.");

        if (this.binaryValue == binaryValue)
            return;

        this.binaryValue = binaryValue;
        valueChangedEventSource.fireEvent(null);
    }

    /**
     * Sets the decimal value of the device, if the type of the value is decimal. If the type of
     * the value is not decimal, throws an {@link java.lang.IllegalStateException}.
     *
     * Calls the listeners of the {@link #valueChangedEventSource} after the value has been changed.
     *
     * @param decimalValue The decimal value of the device.
     * @exception java.lang.IllegalStateException If the type of the value is not decimal.
     */
    public void setDecimalValue(double decimalValue) {
        if (valueType != ValueType.DECIMAL)
            throw new IllegalStateException("Value type is not decimal.");

        if (this.decimalValue == decimalValue)
            return;

        this.decimalValue = decimalValue;
        valueChangedEventSource.fireEvent(null);
    }

    /**
     * Sets the minimum and the maximum values of the decimal value of the device, if the type of
     * the value is decimal. If the type of the value is not decimal, throws an
     * {@link java.lang.IllegalStateException}.
     *
     * @param minValue The minimum value of the decimal value of the device.
     * @param maxValue The minimum value of the decimal value of the device.
     * @exception java.lang.IllegalStateException If the type of the value is not decimal.
     */
    public void setMinMaxValues(double minValue, double maxValue) {
        if (valueType != ValueType.DECIMAL)
            throw new IllegalStateException("Value type is not decimal.");

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Sets the unit and the abreviation of the unit of the decimal value of the device, if the
     * type of the value is decimal. If the type of the value is not decimal, throws an
     * {@link java.lang.IllegalStateException}.
     *
     * @param unit The unit of the decimal value of the device.
     * @param unitAbbreviation The unit abbreviation of the decimal value of the device.
     * @exception java.lang.IllegalStateException If the type of the value is not decimal.
     */
    public void setUnit(String unit, String unitAbbreviation) {
        if (valueType != ValueType.DECIMAL)
            throw new IllegalStateException("Value type is not decimal.");

        this.unit = unit;
        this.unitAbbreviation = unitAbbreviation;
    }
}

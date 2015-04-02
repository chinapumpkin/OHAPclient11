package com.opimobi.ohap;

/**
 * A base object holding all common properties of an item in an OHAP application.
 *
 * Each item must have an unique identifier. Most items have also a parent. If the parent property
 * is null, the item is a top-level container, that is, a central unit. Other properties are null
 * or zero by default.
 *
 * The item registers itself into the central unit of its parent container and adds itself into
 * the specified parent container when created. It also unregister and removes itself from those
 * when destroyed using the {@link Item#destroy()} method.
 *
 * @author Henrik hedberg &lt;henrik.hedberg@iki.fi>
 * @version 1.0
 */
public abstract class Item {
    /**
     * The event source for destroyed events. The listeners of this source will be called
     * before the item is removed from the parent container, unregistered from the central and
     * invalidated. The event (Object) argument will be null.
     */
    public final EventSource<Item, Object> destroyedEventSource = new EventSource<>(this);

    /**
     * The central unit this item belongs to.
     */
    private CentralUnit centralUnit;

    /**
     * The user visible description of the item.
     */
    private String description;

    /**
     * The unique identifier of the item. In practice, this is unique within a central unit.
     * An item must always have this value. The value must be non-negative integer.
     * The value -1 is used internally to represent a destroyed, invalidated item.
     */
    private long id;

    /**
     * Whether the item is internal for the system or displayable to the user by default.
     */
    private boolean internal;

    /**
     * The x component of the location of the item.
     */
    private int x;

    /**
     * The y component of the location of the item.
     */
    private int y;

    /**
     * The z component of the location of the item.
     */
    private int z;

    /**
     * The user visible name of the item.
     */
    private String name;

    /**
     * The container of which the item belongs. May be null, if this is a top-level container,
     * that is, a central unit.
     */
    private Container parent;

    /**
     * An implementation-only constructor that is used to initialise a central unit.
     */
    protected Item() {
        centralUnit = (CentralUnit)this;
    }

    /**
     * Constructs a new item with the parent and identifier properties. Other properties are left
     * null or zero.
     *
     * The item adds itself into the central unit of the parent container and into the parent
     * container.
     *
     * @param parent The parent container of the item. May be null, if this the root container of a central unit.
     * @param id The unique identifier of the item. Must be positive integer.
     * @exception java.lang.IllegalArgumentException If the parent argument is null.
     * @exception java.lang.IllegalArgumentException If the id is not positive integer.
     * @exception java.lang.IllegalArgumentException If an item with the same id exists within the same central unit.
     */
    public Item(Container parent, long id) {
        if (parent == null)
            throw new IllegalArgumentException("The item must have a parent.");
        if (id < 1)
            throw new IllegalArgumentException("The id must be positive integer.");

        this.parent = parent;
        this.id = id;

        centralUnit = parent.getCentralUnit();
        centralUnit.register(this);
        parent.add(this);
    }

    /**
     * Destroys the item. The item is invalid after this operation and must not be used.
     *
     * The item fires a destroyed event first. Then it removes itself from the parent container
     * as well as from the central unit and invalidates itself.
     */
    public void destroy() {
        destroyedEventSource.fireEvent(null);

        if (parent != null) {
            parent.remove(this);
            parent = null;
            centralUnit.unregister(this);
            centralUnit = null;
        }

        id = -1;
    }

    /**
     * Returns the central unit the item belongs to.
     * @return The central unit of the item.
     */
    public CentralUnit getCentralUnit() {
        return centralUnit;
    }

    /**
     * Returns the user visible description of the item.
     *
     * @return The user visible description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the unique identifier of the item. In practice, this is unique within a central unit.
     *
     * @return The unique identifier of the item.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the user visible name of the item.
     *
     * @return The user visible name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the parent container of the item.
     *
     * @return The parent container of the item.
     */
    public Container getParent() {
        return parent;
    }

    /**
     * Returns the x component of the location of the item.
     *
     * @return The x component of the location of the item.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y component of the location of the item.
     *
     * @return The y component of the location of the item.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the z component of the location of the item.
     *
     * @return The z component of the location of the item.
     */
    public int getZ() {
        return z;
    }

    /**
     * Returns whether the item is internal for the system or displayable to the user by default.
     *
     * @return Whether the item is internal for the system.
     */
    public boolean isInternal() {
        return internal;
    }

    /**
     * Sets the user visible description of the item.
     *
     * @param description The user visible description of the item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets whether the item is internal for the system or displayable to the user
     *
     * @param internal Whether the item is internal for the system.
     */

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    /**
     * Sets the location of the item.
     *
     * @param x The x component of the location of the item.
     * @param y The y component of the location of the item.
     * @param z The z component of the location of the item.
     */
    public void setLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sets the user visible name of the item.
     *
     * @param name The user visible name of the item.
     */
    public void setName(String name) {
        this.name = name;
    }
}

package com.opimobi.ohap;

import java.util.ArrayList;

/**
 * A container holding items in an OHAP application. Inherits all common properties from the Item
 * base class.
 *
 * The container is not updating itself from the server before it is asked to start listening
 * updates by calling the {@link #startListening()} method. Thus, there are no children in
 * the container initially. The implementation of the {@link com.opimobi.ohap.CentralUnit}
 * may be asynchronous, meaning that the items are not immediately available after a
 * {@link #startListening()} call but will be added into the container later.
 *
 * Items add themselves automatically into and remove themselves from the container. Thus, the API
 * to modify the container is not visible outside the package.
 *
 * @see com.opimobi.ohap.Item
 *
 * @author Henrik Hedberg &lt;henrik.hedberg@iki.fi>
 * @version 1.1
 */
public class Container extends Item {
    /**
     * The event source for item added events. The listeners of this source will be called
     * after an item has been added into the container. The event argument (Item) is the added item.
     */
    public final EventSource<Container, Item> itemAddedEventSource = new EventSource<>(this);

    /**
     * The event source for item removed events. The listeners of this source will be called
     * after an item has been removed from the container. The event argument (Item) is the
     * removed item.
     */
    public final EventSource<Container, Item> itemRemovedEventSource = new EventSource<>(this);

    /**
     * An ordered map containing the items of the container. The unique identifier of the items
     * are used as keys.
     */
    private ArrayList<Item> items = new ArrayList<>();

    /**
     * Specifies how many listeners have been registered with {@link #startListening()}
     * but not unregistered with {@link #stopListening()}.
     */
    private int listeners;

    /**
     * Constructs a new container with the parent and identifier properties.
     *
     * @see com.opimobi.ohap.Item#Item(Container, long)
     *
     * @param parent The parent container of the item. May be null, if this the root container of a central unit.
     * @param id The unique identifier of the item. Must be positive integer.
     */
    public Container(Container parent, long id) {
        super(parent, id);
    }

    /**
     * An implantation-only constructor that is used to initialise a central unit.
     */
    protected Container() {
    }

    /**
     * Destroys the container and its children.
     *
     * @see Item#destroy()
     */
    @Override
    public void destroy() {
        while (items.size() > 0)
            items.get(items.size() - 1).destroy();

        super.destroy();
    }

    /**
     * Returns the number of items in the container.
     *
     * @return The number of items in the container.
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Returns a child item specified by index.
     *
     * @param index The index of a child item.
     * @return The child item.
     */
    public Item getItemByIndex(int index) {
        return items.get(index);
    }

    /**
     * Returns whether the container is listening updates from the server.
     *
     * @return Whether the container is listening updates.
     */
    public boolean isListening() {
        return listeners != 0;
    }

    /**
     * Asks the container to start listening updates from the server.
     *
     * If this method is called multiple times, the {@link #stopListening()} method must be called
     * the same amount of times to really stop listening
     */
    public void startListening() {
        listeners++;
        if (listeners == 1)
            getCentralUnit().listeningStateChanged(this, true);
    }

    /**
     * Asks the container to stop listening updated from the server.
     *
     * This method must be called exactly the same amount of times than the
     * {@link #startListening()} method to really stop listening.
     */
    public void stopListening() {
        listeners--;
        if (listeners == 0)
            getCentralUnit().listeningStateChanged(this, false);
    }

    /**
     * Adds an item into the container.
     *
     * Used only by the item adding itself into the container.
     *
     * @param item The item to be added into the container.
     */
    void add(Item item) {
        items.add(item);
        itemAddedEventSource.fireEvent(item);
    }

    /**
     * Removes an item from the container.
     *
     * Used only by the item removing itself from the container.
     *
     * @param item The item to be removed from the container.
     */
    void remove(Item item) {
        items.remove(item);
        itemRemovedEventSource.fireEvent(item);
    }
}

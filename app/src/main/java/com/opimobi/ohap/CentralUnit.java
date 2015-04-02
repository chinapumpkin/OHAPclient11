package com.opimobi.ohap;

import java.net.URL;
import java.util.HashMap;

/**
 * A central unit in an OHAP application. Acts also as a top-level
 * {@link com.opimobi.ohap.Container}. The identifier of the central unit is 0. It does not have
 * a parent container.
 *
 * The central unit has an URL that is used when connecting to the OHAP server.
 *
 * All items will register themselves automatically to the central unit they belong to. The
 * central unit provides {@link #getItemById(long)} method to retrieve an item from any
 * level of the container hierarchy by specifying the identifier of the item.
 *
 * @see com.opimobi.ohap.Container
 *
 * @author Henrik hedberg &lt;henrik.hedberg@iki.fi>
 * @version 1.1
 */
public abstract class CentralUnit extends Container {

    /**
     * A map containing the items of the container. The unique identifiers of the items
     * are used as keys.
     */
    private HashMap<Long, Item> items = new HashMap<>();

    /**
     * The URL of the central unit.
     */
    private URL url;

    /**
     * The event source for item registenered events. The listeners of this source will be called
     * after an item has registered itself into the container. The event argument (Item) is the
     * registered item.
     */
    public final EventSource<CentralUnit, Item> itemRegisteredEventSource = new EventSource<>(this);

    /**
     * The event source for item unregistered events. The listeners of this source will be called
     * after an item has unregistered itself from the container. The event argument (Item) is the
     * unregistered item.
     */
    public final EventSource<CentralUnit, Item> itemUnregisteredEventSource = new EventSource<>(this);

    /**
     * Constructs a new central unit with the specified URL.
     *
     * @param url The URL of the central unit.
     */
    public CentralUnit(URL url) {
        this.url = url;
    }

    /**
     * Returns an item belonging to the central unit and specified by the identifier.
     *
     * @param id The identifier specifying an item.
     * @return The item belonging to the the central unit.
     */
    public Item getItemById(long id) {
        return items.get(Long.valueOf(id));
    }

    /**
     * Returns the URL of the central unit.
     *
     * @return The URL of the central unit.
     */
    public URL getURL() {
        return url;
    }

    /**
     * Notifies the central unit that the specified container has started or stopped listening.
     * When a client is listening a container, the server sends updates when items are added
     * or removed from it as well as when a value of some of its child items has changed.
     *
     * This method is abstract and must be provided by the implementation.
     *
     * @param container The container of which listening state has changed.
     * @param listening The new listening state of the container.
     */
    protected abstract void listeningStateChanged(Container container, boolean listening);

    /**
     * Registers an item into the central unit.
     *
     * Used only by the item when it is created.
     *
     * @param item The item to be registered into the central unit.
     * @exception java.lang.IllegalArgumentException If an item with the same id already exists.
     */
    void register(Item item) {
        Item previousItem = items.put(item.getId(), item);
        if (previousItem != null) {
            items.put(previousItem.getId(), previousItem);
            throw new IllegalArgumentException("An item with the same id already exists.");
        }
        itemRegisteredEventSource.fireEvent(item);
    }

    /**
     * Unregister an item from the central unit.
     *
     * Used only by the item when it is destroyed.
     *
     * @param item The item to be unregistered from the central unit.
     */
    void unregister(Item item) {
        items.remove(item.getId());
        itemUnregisteredEventSource.fireEvent(item);
    }
}

package es.wolfteam.filters;

import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.exceptions.FilterException;

/**
 * The interface Filter message.
 * <p/>
 * > Add here the new implementation of FilterMessage to filter a message from discord user
 */
public interface FilterMessage<T>
{
    /**
     * Filter message.
     *
     * @param messages the messages to filter
     * @return the function data with information of Action and event discord
     * @throws FilterException the filter exception if found any problem
     */
    T filterMessages(String ... messages) throws FilterException;
}
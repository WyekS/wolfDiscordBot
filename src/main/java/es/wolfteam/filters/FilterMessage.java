package es.wolfteam.filters;

import es.wolfteam.data.FunctionData;
import es.wolfteam.exceptions.FilterException;

public interface FilterMessage
{
    FunctionData filterMessage(String message) throws FilterException;
}
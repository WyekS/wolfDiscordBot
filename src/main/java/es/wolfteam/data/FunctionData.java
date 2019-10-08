package es.wolfteam.data;

import es.wolfteam.data.types.ActionType;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Function data.
 */
public class FunctionData
{
    private ActionType action;
    private String base;
    private Set<String> params;

    public FunctionData()
    {
    }

    public FunctionData(final String base, final Set<String> params)
    {
        this.base = base;
        this.params = params;
    }

    public ActionType getAction()
    {
        return action;
    }

    public void setAction(ActionType action)
    {
        this.action = action;
    }

    /**
     * Instantiates a new Functions data.
     *
     * @param base the base
     */
    public FunctionData(final String base)
    {
        this.base = base;
        params = new HashSet<>();
    }

    /**
     * Gets base.
     *
     * @return the base
     */
    public String getBase()
    {
        return base;
    }

    /**
     * Sets base.
     *
     * @param base the base
     */
    public void setBase(String base)
    {
        this.base = base;
    }

    /**
     * Gets params.
     *
     * @return params list
     */
    public Set<String> getParams()
    {
        return this.params;
    }

    /**
     * Add param.
     *
     * @param newParam add new param to list
     */
    public void addParam(final String newParam)
    {
        params.add(newParam);
    }
}
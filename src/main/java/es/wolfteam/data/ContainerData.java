package es.wolfteam.data;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * The type Container data.
 */
public class ContainerData
{
    private Message sourceMessage;
    private MessageEmbed resultMessage;
    private FunctionData functionData;

    /**
     * Gets message event.
     *
     * @return the message event
     */
    public Message getSourceMessage()
    {
        return sourceMessage;
    }

    /**
     * Sets message event.
     *
     * @param sourceMessage the message event
     */
    public void setSourceMessage(Message sourceMessage)
    {
        this.sourceMessage = sourceMessage;
    }

    /**
     * Gets result message.
     *
     * @return the result message
     */
    public MessageEmbed getResultMessage()
    {
        return resultMessage;
    }

    /**
     * Sets result message.
     *
     * @param resultMessage the result message
     */
    public void setResultMessage(MessageEmbed resultMessage)
    {
        this.resultMessage = resultMessage;
    }

    /**
     * Gets function data.
     *
     * @return the function data
     */
    public FunctionData getFunctionData()
    {
        return functionData;
    }

    /**
     * Sets function data.
     *
     * @param functionData the function data
     */
    public void setFunctionData(FunctionData functionData)
    {
        this.functionData = functionData;
    }
}

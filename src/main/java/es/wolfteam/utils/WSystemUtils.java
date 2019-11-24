package es.wolfteam.utils;

import es.wolfteam.data.types.UserType;

import java.io.InputStream;

public class WSystemUtils
{
    /**
     * Execute a command in all user from UserEnum
     * <p/>
     *
     * @param command {@link String} command to execute
     * @return <code>true<code/> if all commands were executed correctly
     */
    public static boolean allServerExecution(final String command)
    {
        boolean result = true;
        for (UserType userType : UserType.values())
        {
            if (!executeCommand(command, userType.getName()))
            {
                result = false;
            }
        }
        return result;
    }

    /**
     * A lower level method than before <strong>allServerExecution</strong
     * Execute a command in the system. Control I/O exception
     * <p/>
     *
     * @param arguments to generate command(0) + arguments(1-X) ProcessBuilder
     * @return <code>true<code/> if all commands were executed correctly
     */
    public static boolean executeCommand(final String... arguments)
    {
        Process process;
        try
        {
            // LOG.info("Command to execute and arguments: " + Arrays.toString(arguments));
            process = new ProcessBuilder(arguments).start();
        }
        catch (final Exception ex) // catch generic Exception because we want to show error for any unusual situation
        {
            // LOG.error("Error to execute this command in the system. Info command:\n" + Arrays.toString(arguments) + "; ");
            return false;
        }
        return processErrorHandler(process);
    }

    /**
     * Handle the process to show an error when the result not be expected
     * <p/>
     *
     * @param process to calculate
     * @return <code>true</code> it's finalized or not <code>false</code>
     */
    public static boolean processErrorHandler(Process process)
    {
        try
        {
            int result = process.waitFor();
            InputStream inputStream = result == 0 ? process.getInputStream() : process.getErrorStream();
            if (result != 0)
            {
                // LOG.error(new BufferedReader(new InputStreamReader(inputStream))
                //         .lines().parallel().collect(Collectors.joining("\n")));
                return false;
            }
            return true;
        }
        catch (final InterruptedException e)
        {
            // LOG.error("InterruptedException to execute command");
            return false;
        }
    }

}

package server;

/**
 * Initialise un serveur
 */
public class ServerLauncher {
    public final static int PORT = 1337;

    /**
     * methode generale qui lance le server
     * @param args Argument inutilise
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package server;

/**
 * Assure la gestion des evènements pour toutes les classes qui l'implementent
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Methode de gestion de commande
     * @param cmd La commande à gerer
     * @param arg L'argument de la commande
     */
    void handle(String cmd, String arg);
}

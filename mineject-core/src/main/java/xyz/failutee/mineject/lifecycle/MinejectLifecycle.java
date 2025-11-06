package xyz.failutee.mineject.lifecycle;

/**
 * Interface for managing Mineject framework lifecycle.
 * Provides methods for proper cleanup and resource management
 * to prevent memory leaks.
 */
public interface MinejectLifecycle {

    /**
     * Shuts down the Mineject framework and cleans up all resources.
     * This method should be called when the plugin is being disabled
     * to prevent memory leaks.
     */
    void shutdown();

    /**
     * Checks if the framework has been shut down.
     * 
     * @return true if shutdown() has been called, false otherwise
     */
    boolean isShutdown();
}

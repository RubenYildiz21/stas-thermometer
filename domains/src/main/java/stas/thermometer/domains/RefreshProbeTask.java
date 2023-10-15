package stas.thermometer.domains;

/**
 * Tâche destinée à demander un rafraîchissement périodique de la sonde de température.
 *
 * <p>Cette classe implémente l'interface {@code Runnable} et est prévue pour être exécutée
 * par un service d'exécution (comme un ExecutorService) à intervalles réguliers.
 * Son rôle est de déclencher une nouvelle mesure de la température par la sonde
 * et de notifier les observateurs du résultat.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public class RefreshProbeTask implements Runnable {

    private final TemperatureProbe probe;

    /**
     * Construit une nouvelle tâche de rafraîchissement pour la sonde spécifiée.
     *
     * @param probe la sonde de température à rafraîchir.
     */
    public RefreshProbeTask(TemperatureProbe probe) {
        this.probe = probe;
    }

    /**
     * Déclenche une mesure de la température par la sonde et notifie les observateurs.
     *
     * <p>Cette méthode est appelée directement par le service d'exécution
     * à intervalles réguliers.</p>
     */
    @Override
    public void run() {
        probe.measureAndNotify();
    }
}


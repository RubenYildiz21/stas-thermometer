package stas.thermometer.domains;

/**
 * Interface définissant les opérations que doit implémenter un observateur
 * qui souhaite recevoir des notifications sur les mesures moyennes de température.
 *
 * <p>Cette interface fait partie du modèle d'observation et est utilisée pour
 * permettre à des objets de s'abonner et de recevoir des notifications
 * lorsque la mesure moyenne de température est mise à jour.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public interface AverageMeasurementObserver {

    /**
     * Assure de notifier l'observateur qu'une nouvelle moyenne de value a
     * été calculée.
     *
     * @param averageMeasurement la nouvelle mesure moyenne de température.
     *                           Il ne doit pas être {@code null}.
     */
    void updateAverageMeasurement(Measurement averageMeasurement);

    void updateAverageHumidity(Humidity averageHumidity);

}

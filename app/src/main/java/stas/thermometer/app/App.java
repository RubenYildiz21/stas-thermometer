/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package stas.thermometer.app;

// Dépendances tierces
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Dépendances propres
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.*;

import stas.thermometer.domains.*;
import stas.thermometer.infrastructures.IniConfigurationReader;
import stas.thermometer.presentations.ThermometerPresenter;
import stas.thermometer.presentations.ThermometerView;
import stas.thermometer.views.ConsoleThermometerView;

public class App {
    private static final Logger LOG = LogManager.getLogger("stas");


    public static void main(String[] args) throws FileNotFoundException {

        // Vérification qu'un fichier de configuration est passé en parametres
        int configFileIndex = Arrays.asList(args).indexOf("--config-file");
        if (configFileIndex == -1 || configFileIndex == args.length - 1) {
            LOG.error(" missing configuration file argument");
            return;
        }
        String resource = args[1];
        // Lecture du ficher de configuration
        InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resource);

        if (inputStream == null) {
            LOG.error("Configuration file not found");
            return;
        }

        // Récuperer les info du thermometre
        ConfigurationReader iniFileReader = new IniConfigurationReader(inputStream);
        String thermometerName = iniFileReader.getThermometerName();
        if (thermometerName == null) {
            LOG.error("missing required property name");
            return;
        }
        // Initialisation du thermometre avec ses informations (nom, foramt de température et format de dateTime)
        ConfigurationInterface config = new Configuration(iniFileReader.getDateTimeFormat(), iniFileReader.getTemperatureFormat(), thermometerName);

        // Construction de mes objets

        TemperatureProfile profile = iniFileReader.getTemperatureProfile();
        TemperatureProbe probe = new TemperatureProbe(profile);
        MeasurementAggregator aggregator = new MeasurementAggregator();

        // Ajouter l'agrégateur comme observateur de la sonde
        probe.addObserver(aggregator);

        // Créer le présentateur et la vue
        ThermometerView view = new ConsoleThermometerView();
        ThermometerPresenter presenter = new ThermometerPresenter(view, aggregator, config, probe);

        // Associer le présentateur à la sonde et à l'agrégateur
        probe.addObserver(presenter);
        aggregator.addAverageObserver(presenter);


        // Configurer et démarrer le rafraîchissement périodique de la sonde
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        RefreshProbeTask refreshTask = new RefreshProbeTask(probe);
        presenter.displayThermometerName();
        scheduledExecutor.scheduleAtFixedRate(refreshTask, 0, 100, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(aggregator::calculateAndNotifyAverage, 2, 2, TimeUnit.SECONDS);  // calcule la moyenne toutes les 2 secondes



        // Démarrer la boucle principale
        presenter.runMainLoop();

        // Arrêter l'exécuteur planifié
        scheduledExecutor.shutdown();


        //TODO: faire collaborer la sonde avec la tâche de rafraichissement


    }



    public Object getGreeting() {
        return null;
    }
}


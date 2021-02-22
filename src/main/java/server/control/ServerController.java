package server.control;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ServerController {
    // mock class so my code doesn't throw 20 gazillion errors.
    // fix in merge conflict.

    PropertyChangeSupport loggerPropertyChange = new PropertyChangeSupport(this);

    public void addLoggerListener(PropertyChangeListener listener) {
        loggerPropertyChange.addPropertyChangeListener(listener);
    }
}

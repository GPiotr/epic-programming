/**
 * 
 */
package pl.gieldon.epic.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import pl.gieldon.epic.plugin.Activator;

/**
 * @author Piotr Gie³don
 *
 */
public class EpicPreferencesInitializer extends AbstractPreferenceInitializer {

    public EpicPreferencesInitializer() {
    }

    @Override
    public void initializeDefaultPreferences() {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            store.setDefault(WorkbenchPreferencePage1.IS_MUTED, false);
            store.setDefault(WorkbenchPreferencePage1.IS_SHAKED, true);
            store.setDefault(WorkbenchPreferencePage1.IS_UPWARD_DRAWN, true);
            store.setDefault(WorkbenchPreferencePage1.IS_DOWNWARD_DRAWN, true);
            store.setDefault(WorkbenchPreferencePage1.RECT_SIZE, 2);
            store.setDefault(WorkbenchPreferencePage1.SHAKE_POWER, 10);
            store.setDefault(WorkbenchPreferencePage1.TIMER_INTERVAL, 1);
            store.setDefault(WorkbenchPreferencePage1.PARTICLE_AMOUNT, 20);
    }
}

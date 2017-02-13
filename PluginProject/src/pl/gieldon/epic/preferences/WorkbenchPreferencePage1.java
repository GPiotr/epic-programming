/**
 * 
 */
package pl.gieldon.epic.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import pl.gieldon.epic.plugin.Activator;

/**
 * @author Piotr Gie³don
 *
 */
public class WorkbenchPreferencePage1 extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String IS_MUTED = "IS_MUTED";
	public static final String IS_SHAKED = "IS_SHAKED";
	public static final String IS_UPWARD_DRAWN = "IS_UPWARD_DRAWN";
	public static final String IS_DOWNWARD_DRAWN = "IS_DOWNWARD_DRAWN";
	public static final String RECT_SIZE = "RECT_SIZE";
	public static final String SHAKE_POWER = "SHAKE_POWER";
	public static final String TIMER_INTERVAL = "TIMER_INTERVAL";
	public static final String PARTICLE_AMOUNT = "PARTICLE_AMOUNT";

	public WorkbenchPreferencePage1() {
		super(GRID);

	}

	public void createFieldEditors() {

		addField(new BooleanFieldEditor(IS_MUTED, "Mute", getFieldEditorParent()));

		addField(new IntegerFieldEditor(RECT_SIZE, "Particle size", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(IS_SHAKED, "Shake", getFieldEditorParent()));

		addField(new IntegerFieldEditor(SHAKE_POWER, "Shake power (1-*)", getFieldEditorParent()));

		addField(new IntegerFieldEditor(TIMER_INTERVAL, "Animation timer interval", getFieldEditorParent()));

		addField(new IntegerFieldEditor(PARTICLE_AMOUNT, "Particle amount", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(IS_UPWARD_DRAWN, "Draw upward particles", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(IS_DOWNWARD_DRAWN, "Drawn downward particles", getFieldEditorParent()));

	}

	public void init(IWorkbench workbench) {

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for Epic Programming Editor");
	}

}

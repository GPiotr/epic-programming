
package pl.gieldon.epic.editor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;

import pl.gieldon.epic.model.Frame;
import pl.gieldon.epic.model.Particles;
import pl.gieldon.epic.plugin.Activator;
import pl.gieldon.epic.preferences.WorkbenchPreferencePage1;
import pl.gieldon.epic.util.ParticleGenerator;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * This is the main class, where all magic happens.
 * We've got here all Listeners and redrawing loop.
 * 
 * @author Piotr Gie³don
 *
 */
public class EpicEditor extends TextEditor {

	// VARIABLES
	private int topMargin;
	private int leftMargin;
	private List<Particles> particleList;
	private boolean isRedrawing = false;
	private Runnable runnable;
	private PaintListener myPaintListener;
	private KeyListener keyListener;

	// CONSTANTS
	private final static String SOUND_FILENAME_KEY_1_PRESS = "/resources/sounds/key_1_press.wav";
	private final static String SOUND_FILENAME_SPACE_PRESS_RELEASE = "/resources/sounds/space_press_release.wav";
	private final static String SOUND_FILENAME_DING = "/resources/sounds/ding.wav";
	private final static String SOUND_FILENAME_NEW_LINE_1 = "/resources/sounds/new_line_1.wav";
	private final static String SOUND_FILENAME_LEVER_PRESS = "/resources/sounds/lever_press.wav";

	// PREFERENCES
	private int prefShakePower = 10;
	private int prefRectSize = 2;
	private int prefTimerInterval = 1;
	private int prefParticleAmount = 20;
	private boolean prefIsMuted = false;
	private boolean prefIsShaked = true;
	private boolean prefIsUpwardDrawn = true;
	private boolean prefIsDownwardDrawn = true;

	
	/**
	 * CONSTRUCTOR
	 */
	public EpicEditor() {
		super();
		
		particleList = new ArrayList<Particles>();
		
		JavaTextTools textTools = JavaPlugin.getDefault().getJavaTextTools();
		setSourceViewerConfiguration(new JavaSourceViewerConfiguration(textTools.getColorManager(),
				getPreferenceStore(), this, IJavaPartitions.JAVA_PARTITIONING));
		// IPreferenceStore store =
		// JavaPlugin.getDefault().getCombinedPreferenceStore();
		// setPreferenceStore(store);
		setDocumentProvider(JavaPlugin.getDefault().getPropertiesFileDocumentProvider());

		// Load Preferences
		loadPreferences();

		// Add Properties change Listener
		addPropertyChangeListener();

		System.out.println("_EPIC_");
	}

	/**
	 * When we go to Window=>Preferenes=>Epic Preference Page and change any
	 * preference Listener below will be called and set proper value.
	 *
	 */
	private void addPropertyChangeListener() {

		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(WorkbenchPreferencePage1.IS_MUTED)) {
					prefIsMuted = Boolean.parseBoolean(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.IS_SHAKED)) {
					prefIsShaked = Boolean.parseBoolean(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.IS_UPWARD_DRAWN)) {
					prefIsUpwardDrawn = Boolean.parseBoolean(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.IS_DOWNWARD_DRAWN)) {
					prefIsDownwardDrawn = Boolean.parseBoolean(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.RECT_SIZE)) {
					prefRectSize = Integer.parseInt(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.SHAKE_POWER)) {
					prefShakePower = Integer.parseInt(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.TIMER_INTERVAL)) {
					prefTimerInterval = Integer.parseInt(event.getNewValue().toString());
				} else if (event.getProperty().equals(WorkbenchPreferencePage1.PARTICLE_AMOUNT)) {
					prefParticleAmount = Integer.parseInt(event.getNewValue().toString());
				}
			}
		});
	}

	/**
	 * This method loads Preferences from Preferences =)
	 * Window=>Preferenes=>Epic Preference Page
	 */
	private void loadPreferences() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);

		prefIsMuted = preferences.getBoolean(WorkbenchPreferencePage1.IS_MUTED, false);
		prefIsShaked = preferences.getBoolean(WorkbenchPreferencePage1.IS_SHAKED, true);
		prefIsUpwardDrawn = preferences.getBoolean(WorkbenchPreferencePage1.IS_UPWARD_DRAWN, true);
		prefIsDownwardDrawn = preferences.getBoolean(WorkbenchPreferencePage1.IS_DOWNWARD_DRAWN, true);
		prefRectSize = preferences.getInt(WorkbenchPreferencePage1.RECT_SIZE, 2);
		prefShakePower = preferences.getInt(WorkbenchPreferencePage1.SHAKE_POWER, 10);
		prefTimerInterval = preferences.getInt(WorkbenchPreferencePage1.TIMER_INTERVAL, 1);
		prefParticleAmount = preferences.getInt(WorkbenchPreferencePage1.PARTICLE_AMOUNT, 20);
		
		/*
		 * Load preferences is called only once, so we do not need StringBuilder etc. ;)
		 */
		System.out.println(WorkbenchPreferencePage1.IS_MUTED + ": " + prefIsMuted);
		System.out.println(WorkbenchPreferencePage1.IS_SHAKED + ": " + prefIsShaked);
		System.out.println(WorkbenchPreferencePage1.IS_UPWARD_DRAWN + ": " + prefIsUpwardDrawn);
		System.out.println(WorkbenchPreferencePage1.IS_DOWNWARD_DRAWN + ": " + prefIsDownwardDrawn);
		System.out.println(WorkbenchPreferencePage1.RECT_SIZE + ": " + prefRectSize);
		System.out.println(WorkbenchPreferencePage1.SHAKE_POWER + ": " + prefShakePower);
		System.out.println(WorkbenchPreferencePage1.TIMER_INTERVAL + ": " + prefTimerInterval);
		System.out.println(WorkbenchPreferencePage1.PARTICLE_AMOUNT + ": " + prefParticleAmount);

	}

	@Override
	public void dispose() {
		super.dispose();
		System.out.println("_DISPOSE_");
		// getSourceViewer().getTextWidget().getDisplay().timerExec(-1,
		// runnable);
	}

	@Override
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		// getSourceViewer().invalidateTextPresentation();

		super.handlePreferenceStoreChanged(event);
		System.out.println("_PREFERENCE_");
	}

	@Override
	public void setFocus() {

		StyledText styledText = getTextWidget();
		if (styledText != null) {

			styledText.setFocus();

			// Store configured margin in global variables
			leftMargin = styledText.getLeftMargin();
			topMargin = styledText.getTopMargin();

			final Display display = styledText.getDisplay();

			// Create redraw loop
			createThreadLoop(display);

			// Add Paint Listener
			attachPaintListener();

			// Add key Listener
			attachKeyListener();

			// Add timer for another redraw
			display.timerExec(prefTimerInterval, runnable);

		}
		System.out.println("_FOCUS_");

	}

	/**
	 * This is main loop method which is going to animate all Particles. First
	 * of all it push animation frames forward, and then request redraw() which
	 * calls paintControl() method of PaintController
	 * 
	 * @param display
	 */
	private void createThreadLoop(final Display display) {

		runnable = new Runnable() {
			public void run() {

				// Check if we do not redrawing in different Thread to not stack
				// redraw() request and to do not lost any frames.
				if (!isRedrawing) {

					// For all particles perform frame increase
					for (int i = 0; i < particleList.size(); i++) {
						animate(particleList.get(i), i);
					}

					// this run() method will be called by Display in
					// TIMER_INTERVAL
					display.timerExec(prefTimerInterval, this);

					// call redraw()
					if (!particleList.isEmpty()) {
						isRedrawing = true;

						if (getSourceViewer() != null) {
							getSourceViewer().getTextWidget().redraw();
						}
					}
				}
			}
		};
	}

	/**
	 * Attach PaintListener to the TextWidget, so we can redraw our particles.
	 * At this moment I see no other way than store PaintListener in global
	 * variable and check if it is initialized. If so it means that
	 * PaintListener is attached, so we do not attach another one to the
	 * TextWidget after second and next call of the setFocus() method.
	 */
	private void attachPaintListener() {

		if (myPaintListener == null) {

			// Store paint listener in global variable
			myPaintListener = new PaintListener() {
				public void paintControl(PaintEvent event) {

					// This flag determines if we need redraw or not. Stop us
					// from stacked redraws and pause redraw when there is no
					// particles to draw
					if (isRedrawing) {

						for (Particles particles : particleList) {
							// Paint only if FrameNumber is positive (0
							// included).
							if (particles.getFrame().getFrameNumber() >= 0) {

								// Alpha for particles. We want them to vanish
								// smoothly so, the alpha must be calculated for
								// every frame.
								int alpha = computeAlphaChannel(particles);

								// Yes we set Background here, but it is BG of
								// small particle. We want particles filled with
								// color, so that is OK.
								event.gc.setBackground(particles.getMyColor());

								// Loop through prefParticleAmount particles (default 20)
								for (int i = 0; i < prefParticleAmount; i++) {

									// Set out Alpha for transparency
									event.gc.setAlpha(alpha);

									// Let's draw our upward particle
									// <getTopParticles()>
									if (prefIsUpwardDrawn) {
										event.gc.fillRectangle(
												particles.getFrame().getPoint().x
														+ particles.getTopParticles()[i][particles.getFrame()
																.getFrameNumber()].x,
												particles.getFrame().getPoint().y
														- particles.getTopParticles()[i][particles.getFrame()
																.getFrameNumber()].y,
												prefRectSize, prefRectSize);
									}
									// Let's draw our downward particle
									// <getBottomParticles()>
									if (prefIsDownwardDrawn) {
										event.gc.fillRectangle(
												particles.getFrame().getPoint().x
														+ particles.getBottomParticles()[i][particles.getFrame()
																.getFrameNumber()].x,
												particles.getFrame().getPoint().y
														+ particles.getBottomParticles()[i][particles.getFrame()
																.getFrameNumber()].y, // +15,
												prefRectSize, prefRectSize);
									}
								}
							}

						}

						System.out.println("_REDRAW_");

						// Drawing is done, can negate the flag
						isRedrawing = false;
					}
				}

			};

			getSourceViewer().getTextWidget().addPaintListener(myPaintListener);
		}
	}

	/**
	 * As I found universal and valid values of Alpha are between 1 and 254
	 * Frames at index 0-29 are drawn with full alpha. I want them visible
	 * without transparency. After index 30 until the end (89) I want 60 frames
	 * that will slowly lost their visibility until they disappear. Mutiplier
	 * 4.3 is perfect to get full transparency at the last 90th frame (index
	 * 89).
	 * 
	 * @param particle
	 * @return
	 */
	private int computeAlphaChannel(Particles particle) {
		// For first frames keep Alpha at 254 level
		int alpha = 254;

		// For frames at index 30-89 increase transparency (alpha going down
		// till 1)
		if (particle.getFrame().getFrameNumber() >= 30) {

			// Substracted 30 because we start at 30th frame. Thanks to this
			// formula transition is smooth.
			alpha = (int) (254 - ((particle.getFrame().getFrameNumber() - 30) * 4.3));

			// Stay always positive! =)
			if (alpha <= 0) {
				alpha = 1;
			}
		}
		return alpha;
	}

	/**
	 * Attach KeyListener to the TextWidget, so we can read every key pressed
	 * and key released. At this moment I see no other way than store
	 * KeyListener in global variable and check if it is initialized. If so it
	 * means that KeyListener is attached, so we do not attach another one to
	 * the TextWidget after second and next call of the setFocus() method.
	 */
	private void attachKeyListener() {

		if (keyListener == null) {

			// Store KeyListener in global variable
			keyListener = new KeyListener() {

				public void keyPressed(KeyEvent event) {

					if (!prefIsMuted) {
						playSound(event);
					}
					if (prefIsShaked) {
						shakeBackground();
					}
					createParticlesFrames();

				}

				public void keyReleased(KeyEvent e) {
					if (prefIsShaked) {
						resetBackground();
					}
				}

			};

			getSourceViewer().getTextWidget().addKeyListener(keyListener);
		}
	}

	/**
	 * Let's play sound depends on which key was pressed.
	 * 
	 * @param event
	 */
	private void playSound(KeyEvent event) {
		AudioStream audioStream = null;
		InputStream inputStream = null;
		try {
			switch (event.keyCode) {
			// SPACE
			case SWT.SPACE:
				inputStream = getClass().getResourceAsStream(SOUND_FILENAME_SPACE_PRESS_RELEASE);
				break;
			// ENTER
			case '\n':
			case '\r':
				inputStream = getClass().getResourceAsStream(SOUND_FILENAME_NEW_LINE_1);
				break;
			// SEMICOLON
			case ';':
				inputStream = getClass().getResourceAsStream(SOUND_FILENAME_DING);
				break;
			// BACKSPACE
			case 8:
				inputStream = getClass().getResourceAsStream(SOUND_FILENAME_LEVER_PRESS);
				break;
			// ANY KEY
			default:
				inputStream = getClass().getResourceAsStream(SOUND_FILENAME_KEY_1_PRESS);
				break;
			}

			audioStream = new AudioStream(inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		AudioPlayer.player.start(audioStream);

	}

	/**
	 * It is time to prepare all animation frames for particles at exactly
	 * coordinates when the key was pressed. We get Caret location to find
	 * coordinates, set present FrameNumber to 0, get font color, and finally
	 * use particle generator to compute position of every particle in every one
	 * of 90 frames for this keypress.
	 */
	private void createParticlesFrames() {

		final Frame frame = new Frame();

		// Let's set frameNumber to 0 so we can run to 89
		frame.setFrameNumber(0);

		// Get Caret location so we know where to draw our particles.
		frame.setPoint(getSourceViewer().getTextWidget().getCaret().getLocation());

		final Particles particles = new Particles();
		particles.setFrame(frame);

		// Generate particles which will move upwards
		particles.setTopParticles(ParticleGenerator.generateParticles(prefParticleAmount));

		// Generate particles which will move downwards
		particles.setBottomParticles(ParticleGenerator.generateParticles(prefParticleAmount));

		// Get the font color
		particles.setMyColor(getSourceViewer().getTextWidget()
				.getStyleRangeAtOffset(getSourceViewer().getTextWidget().getCaretOffset()).foreground);

		// Add particles to global list
		particleList.add(particles);

	}

	/**
	 * On every tick(redraw) we want to draw next frame of the animation. This
	 * method increments frames for every Particles object until it is 89 index
	 * (90th frame). Then set frame to -1 so it will not be redraw in paint
	 * method, and remove it from the global list because its animation is over.
	 * 
	 * @param particles
	 * @param index
	 */
	public void animate(Particles particles, int index) {

		// Increment frame number (max is 89)
		if (particles.getFrame().getFrameNumber() < 89 && particles.getFrame().getFrameNumber() >= 0) {
			particles.getFrame().setFrameNumber(particles.getFrame().getFrameNumber() + 1);
		} else {
			// Set frame to -1 so paint method will omit this particles
			particles.getFrame().setFrameNumber(-1);

			// Remove particles from list. It was fully animated and vanished.
			particleList.remove(index);
		}

	}

	/**
	 * This method will randomly increase left and top margin. Thanks to this
	 * change we will get shake effect.
	 */
	private void shakeBackground() {

		Random r = new Random();
		getSourceViewer().getTextWidget().setLeftMargin(r.nextInt(prefShakePower));
		getSourceViewer().getTextWidget().setTopMargin(r.nextInt(prefShakePower));
	}

	/**
	 * Back to initial values of margins after shake.
	 */
	private void resetBackground() {

		getSourceViewer().getTextWidget().setLeftMargin(leftMargin);
		getSourceViewer().getTextWidget().setTopMargin(topMargin);

		getSourceViewer().invalidateTextPresentation(); // Last change for
														// invalidation - not
														// sure if necessary.

	}

	/**
	 * @return
	 */
	private StyledText getTextWidget() {
		SourceViewer viewer = (SourceViewer) getSourceViewer();
		if (viewer != null) {
			return viewer.getTextWidget();
		}
		return null;
	}

}

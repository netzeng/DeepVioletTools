package com.mps.deepviolet.bin;

import java.awt.Image;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mps.deepviolet.ui.MainFrm;
import com.mps.deepviolet.util.FileUtils;
import com.mps.deepviolet.util.LogUtils;

/**
 * Entry point to start DeepViolet and display a user interface.
 * 
 * @author Milton Smith
 *
 */
public class StartUI {

	// Initialize logging before we do anything
	static {
		LogUtils.logInit();
	}

	private static final Logger logger = LoggerFactory.getLogger("com.mps.deepviolet.bin.StartUI");

	/**
	 * Main entry point
	 * 
	 * @param args
	 *            Command line arguments (not used for now)
	 */
	public static void main(String[] args) {

		new StartUI().init(args);

	}

	/**
	 * Initialization
	 */
	private void init(String[] args) {

		logger.info("Starting UI via dvUI");

		// Create ~/DeepViolet/ working directory on OS
		FileUtils.createWorkingDirectory();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					logger.debug("Look and feel assigned.  Class=" + UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					logger.error("Error setting lookandfeel, msg=" + e.getMessage());
				}

				try {
					// Add a dock icon for OS X
					String os_type = System.getProperty("os.name");
					if (os_type != null && os_type.toUpperCase().indexOf("MAC") > -1) {
						Class util = Class.forName("com.apple.eawt.Application");
						Method getApplication = util.getMethod("getApplication", new Class[0]);
						Object application = getApplication.invoke(util);
						Class params[] = new Class[1];
						params[0] = Image.class;
						Method setDockIconImage = util.getMethod("setDockIconImage", params);
						URL url = this.getClass().getClassLoader().getResource("deepviolet-logo.png");
						Image image = Toolkit.getDefaultToolkit().getImage(url);
						setDockIconImage.invoke(application, image);
						logger.debug("Dock icon assigned, url=" + url.toString());
					}
				} catch (Exception e) {
					logger.error("Error setting dockicon image, msg=" + e.getMessage());
				}

				MainFrm main = new MainFrm();
				main.initComponents();
				main.setVisible(true);
			}
		});

	}

}

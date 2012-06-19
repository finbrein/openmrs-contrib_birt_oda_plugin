package org.eclipse.birt.report.data.oda.openmrs.ui;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * It contains logic for actions to be performed during the loading
 * and unloading of the plugin.
 * 
 * It also provides mechanism of internationalization, by helping
 * to load strings from the properties file 
 */
public class UiPlugin extends AbstractUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.birt.report.data.oda.openmrs.ui";
	
	//The shared instance.
	private static UiPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public UiPlugin( )
	{
		super( );
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle( "org.eclipse.birt.report.data.oda.openmrs.ui.i18n.messages" ); //$NON-NLS-1$
		}
		catch ( MissingResourceException x )
		{
			resourceBundle = null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static UiPlugin getDefault( )
	{
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString( String key )
	{
		ResourceBundle bundle = UiPlugin.getDefault( ).getResourceBundle( );
		try
		{
			return ( bundle != null ) ? bundle.getString( key ) : key;
		}
		catch ( MissingResourceException e )
		{
			return key;
		}
	}
	
	/**
	 * Returns the string from the Resource bundle, formatted according 
	 * to the arguments specified
	 */
	public static String getFormattedString(String key, Object[] arguments) {
		return MessageFormat.format(getResourceString(key), arguments);
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle( )
	{
		return resourceBundle;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
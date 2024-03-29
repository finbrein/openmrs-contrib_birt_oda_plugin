package org.eclipse.birt.report.data.oda.openmrs.ui.wizards;

import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

public class CustomDataSourceWizardPage extends DataSourceWizardPage
{
	
	private CustomDataSourcePageHelper m_pageHelper;
	private Properties m_folderProperties;
	
	public CustomDataSourceWizardPage( String pageName )
	{
		super( pageName );
        setMessage( CustomDataSourcePageHelper.DEFAULT_MESSAGE );
		// page title is specified in extension manifest
	}
	
	public void createPageCustomControl( Composite parent )
	{
		if ( m_pageHelper == null )
			m_pageHelper = new CustomDataSourcePageHelper( this );
		
		m_pageHelper.setResourceIdentifiers( this.getHostResourceIdentifiers( ) );
		m_pageHelper.createCustomControl( parent );
		m_pageHelper.initCustomControl( m_folderProperties ); // in case init was called before create 

	}
	
	public void setInitialProperties( Properties dataSourceProps )
	{
		m_folderProperties = dataSourceProps;
		if ( m_pageHelper == null )
			return; // ignore, wait till createPageCustomControl to initialize
		m_pageHelper.initCustomControl( m_folderProperties );
	}

	public Properties collectCustomProperties( )
	{
		if ( m_pageHelper != null )
			return m_pageHelper.collectCustomProperties( m_folderProperties );

		return ( m_folderProperties != null ) ? m_folderProperties : new Properties( );
	}
	
	   /*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
		 */
		public void setVisible( boolean visible )
		{
			super.setVisible( visible );
			getControl( ).setFocus( );
		}
		
	    /*
	     * (non-Javadoc)
	     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage#refresh()
	     */
	    public void refresh()
	    {
	        // enable/disable all controls on page in respect of the editable session state
	        enableAllControls( getControl(), isSessionEditable() );
	    }

		/* (non-Javadoc)
		 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSourceWizardPageCore#createTestConnectionRunnable(org.eclipse.datatools.connectivity.IConnectionProfile)
		 */
		protected Runnable createTestConnectionRunnable( IConnectionProfile profile )
		{
			return m_pageHelper.createTestConnectionRunnable( profile );
			
		}
	    
}

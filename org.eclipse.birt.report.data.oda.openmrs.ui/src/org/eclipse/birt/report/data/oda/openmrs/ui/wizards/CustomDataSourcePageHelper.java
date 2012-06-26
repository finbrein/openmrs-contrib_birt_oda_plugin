package org.eclipse.birt.report.data.oda.openmrs.ui.wizards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.openmrs.ui.control.FileSelectionButton;
import org.eclipse.birt.report.data.oda.openmrs.ui.control.IMenuActionHandler;
import org.eclipse.birt.report.data.oda.openmrs.ui.i18n.Messages;
import org.eclipse.birt.report.data.oda.openmrs.ui.utils.IHelpConstants;
import org.eclipse.birt.report.data.oda.openmrs.ui.utils.XMLRelationInfoUtil;
import org.eclipse.birt.report.data.oda.openmrs.xml.Constants;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.ResourceLocatorUtil;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.XMLSourceFromPath;
import org.eclipse.datatools.connectivity.IConnection;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.nls.TextProcessorWrapper;
import org.eclipse.datatools.connectivity.ui.PingJob;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CustomDataSourcePageHelper 
{
    private static final String AUTO_ENCODING = Messages.getString( "wizard.autoEncoding" ); //$NON-NLS-1$
	private WizardPage m_wizardPage;
    private PreferencePage m_propertyPage;

    private transient Text m_folderLocation = null;
    private transient Text m_schemaLocation = null;
    
 // Text of url, name and password
 	private Text userName, password;

	private transient FileSelectionButton browseFolderButton = null;
	private transient Combo encodingCombo = null;
	private transient Composite parent = null;
	private transient org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers ri = null;
	
    static final String DEFAULT_MESSAGE = Messages.getString( "wizard.defaultMessage.selectFolder" ); //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private final String[] XML_FILTER = new String[]{"*.xml", "*.*"} ;  //$NON-NLS-1$ //$NON-NLS-2$
    private final String[] XSD_FILTER = new String[]{"*.xsd", "*.*"} ;  //$NON-NLS-1$ //$NON-NLS-2$
	
	CustomDataSourcePageHelper( WizardPage page )
    {
        m_wizardPage = page;
    }

	CustomDataSourcePageHelper( PreferencePage page )
    {
        m_propertyPage = page;
    }
	
    void createCustomControl( Composite parent )
    {
    	this.parent = parent;
    	this.setMessage( DEFAULT_MESSAGE );
    	
    	// create the composite to hold the widgets
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 4;
		layout.verticalSpacing = 10;
		layout.marginBottom = 10;
		composite.setLayout( layout );
		
		GridData data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL );
		
		data.horizontalSpan = 3;
		final Label label1 = new Label( composite, SWT.NONE );
		label1.setText( Messages.getString( "label.serverOpenMRS" ) ); //$NON-NLS-1$
		label1.setLayoutData( data );

		// GridData data;
		setupXMLFolderLocation( composite );
		
		GridData gridData;
		
		// User Name
		new Label( composite, SWT.RIGHT ).setText( Messages.getString( "label.user" ) );//$NON-NLS-1$
		userName = new Text( composite, SWT.BORDER );
		gridData = new GridData( );
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		userName.setLayoutData( gridData );

		// Password
		new Label( composite, SWT.RIGHT ).setText( Messages.getString( "label.Password" ) );//$NON-NLS-1$
		password = new Text( composite, SWT.BORDER | SWT.PASSWORD );
		gridData = new GridData( );
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		password.setLayoutData( gridData );
		
		XMLRelationInfoUtil.setSystemHelp( getControl(), IHelpConstants.CONEXT_ID_DATASOURCE_XML );
    }
    
    String getFolderLocation()
    {
        if( m_folderLocation == null )
            return EMPTY_STRING;
        return getFolderLocationString( );
    }

    String getSchemaFileLocation()
    {
        if( m_schemaLocation == null )
            return EMPTY_STRING;
        return getSchemaLocationString( );
    }

    String getEncoding( )
	{
		if ( encodingCombo == null || encodingCombo.getText( ).equals( AUTO_ENCODING ) )
			return EMPTY_STRING;
		else
			return encodingCombo.getText( );
	}
    
    Properties collectCustomProperties( Properties props )
    {
        if( props == null )
            props = new Properties();
        
        // set custom driver specific properties
		props.setProperty( Constants.CONST_PROP_FILELIST, getFolderLocation( ) );
		props.setProperty( Constants.CONST_PROP_SCHEMA_FILELIST, getSchemaFileLocation( ) );
		props.setProperty( Constants.CONST_PROP_ENCODINGLIST, getEncoding( ) );
		props.setProperty( Constants.CONST_PROP_USERNAME, userName.getText().trim() );
		props.setProperty( Constants.CONST_PROP_PASSWORD, password.getText().trim() );
		return props;
    }
    
    void initCustomControl( Properties profileProps )
    {
        if( profileProps == null || profileProps.isEmpty() || 
            m_folderLocation == null )
            return;     // nothing to initialize
        
        String folderPath = profileProps.getProperty( Constants.CONST_PROP_FILELIST );
        if( folderPath == null )
            folderPath = EMPTY_STRING;
        setFolderLocation( folderPath );
        
        String encoding = profileProps.getProperty( Constants.CONST_PROP_ENCODINGLIST );
        if ( encoding == null )
		{//use auto encoding
			encodingCombo.select( 0 );
		}
		else
		{
			encodingCombo.select( getIndex( encoding ) );
		}
       
        String schemaPath = profileProps.getProperty( Constants.CONST_PROP_SCHEMA_FILELIST );
        if( schemaPath == null )
        	schemaPath = EMPTY_STRING;
        setSchemaLocation( schemaPath );

    }

    private int getIndex( String encoding )
	{
		return Arrays.binarySearch( encodingCombo.getItems( ), encoding );
	}

	/**
     * @param composite
     */
    private void setupEncodingControl( Composite composite )
    {
    	GridData data = new GridData( GridData.FILL_HORIZONTAL );
    	encodingCombo = new Combo(composite, SWT.READ_ONLY);
    	encodingCombo.setLayoutData( data );
    	encodingCombo.add( AUTO_ENCODING );
    	for ( Iterator i = Charset.availableCharsets( ).keySet( ).iterator( ); i.hasNext( ); )
		{
			String	encoding = (String) i.next( );
			encodingCombo.add( encoding );			
		}
    	encodingCombo.select( 0 );
    }
    
	/**
	 * @param composite
	 */
	private void setupXMLFolderLocation( Composite composite )
	{
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		m_folderLocation = new Text( composite, SWT.BORDER );
		m_folderLocation.setLayoutData( data );
	}
	
	private String getResourceFolder( )
	{
		if ( ri != null )
		{
			if ( ri.getApplResourceBaseURI( ) != null )
			{
				return new File(ri.getApplResourceBaseURI( )).getAbsolutePath( );
			}
		}
		return null;
	}

	/**
	 * @param composite
	 */
	private void setupSchemaFolderLocation( Composite composite )
	{

		GridData data = new GridData( GridData.FILL_HORIZONTAL );

		m_schemaLocation = new Text( composite, SWT.BORDER );
		m_schemaLocation.setLayoutData( data );

		browseFolderButton = new FileSelectionButton( composite, SWT.NONE );
		browseFolderButton.setText( Messages.getString( "schema.choose" ) ); //$NON-NLS-1$
		browseFolderButton.setActionHandler( new IMenuActionHandler( ){

			public String getBaseFolder( )
			{
				return getResourceFolder( );
			}

			public String[] getExtensionsFilter( )
			{
				return XSD_FILTER;
			}

			public void setPath( String path )
			{
				m_schemaLocation.setText( path );
			}

			public String getFilePath( )
			{
				return getSchemaFileLocation( );
			}
			
		});
	}
	
	/**
	 * 
	 * @param text
	 */
	private void setFolderLocation( String text )
	{
		m_folderLocation.setText( ResourceLocatorUtil.processPath( TextProcessorWrapper.process( text ) ) );
	}
	
	/**
	 * 
	 * @return
	 */
	private String getFolderLocationString( )
	{
        return TextProcessorWrapper.deprocess( ResourceLocatorUtil.processPath( m_folderLocation.getText( ) ) );
	}
	
	/**
	 * 
	 * @param text
	 */
	private void setSchemaLocation( String text )
	{
		m_schemaLocation.setText( ResourceLocatorUtil.processPath( TextProcessorWrapper.process( text ) ) );
	}
	
	/**
	 * 
	 * @return
	 */
	private String getSchemaLocationString( )
	{
        return TextProcessorWrapper.deprocess( ResourceLocatorUtil.processPath( m_schemaLocation.getText( ) ) );
	}
	
	/**
	 * set message
	 * @param message
	 */
	private void setMessage( String message )
	{
		if ( m_wizardPage != null )
			m_wizardPage.setMessage( message );
		else if ( m_propertyPage != null )
			m_propertyPage.setMessage( message );
	}
    
    private Control getControl()
    {
        if ( m_wizardPage != null )
            return m_wizardPage.getControl();
        assert( m_propertyPage != null );
        return m_propertyPage.getControl();
    }

	public Runnable createTestConnectionRunnable( final IConnectionProfile profile )
	{
		return new Runnable() 
        {
			public void run() 
            {
                IConnection conn = PingJob.createTestConnection( profile );

                Throwable exception = PingJob.getTestConnectionException( conn );
                
                if ( exception == null ) //succeed in creating connection
                {
					try
					{
						testConnection( );
					}
					catch( Exception ex )
					{
						exception = ex;
					}
                }
                
                PingJob.PingUIJob.showTestConnectionMessage( parent.getShell( ), exception );
                if( conn != null )
                {
                    conn.close();
                }
            }
		};
	}
	
	private void testConnection(  ) throws Exception
	{
		String schema = getSchemaFileLocation();
		String encoding = getEncoding( );
		if ( schema != null && schema.length( ) > 0 )
		{
			//if XML schema is provided, check whether it's valid
			InputStream is = new XMLSourceFromPath( schema, encoding, ri ).openInputStream( );
			try
			{
				is.close( );
			}
			catch ( IOException e )
			{
			}
			//schemaFile provided is valid, this connection at least can be used to fetch meta data 
		}
		
		String xmlFile = getFolderLocation( );
		if ( xmlFile == null || xmlFile.length( ) <= 0)
			throw new OdaException( Messages.getString( "error.invalidSource" )); //$NON-NLS-1$
		
		InputStream is = new XMLSourceFromPath( xmlFile, encoding, ri ).openInputStream( );
		try
		{
			is.close( );
		}
		catch ( IOException e )
		{
		}
	}
 
	protected void setResourceIdentifiers( ResourceIdentifiers ri )
	{
		if ( ri == null )
			return;
		
		this.ri = DesignSessionUtil.createRuntimeResourceIdentifiers( ri );
	}

}

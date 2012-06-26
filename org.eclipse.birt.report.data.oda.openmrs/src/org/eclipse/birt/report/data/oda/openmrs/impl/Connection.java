/*
 *************************************************************************
 * Copyright (c) 2012 <<Your Company Name here>>
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.openmrs.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.openmrs.xml.Constants;
import org.eclipse.birt.report.data.oda.openmrs.xml.i18n.Messages;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.IXMLSource;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.XMLSourceFromInputStream;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.XMLSourceFromPath;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection
{
    //private boolean m_isOpen = false;
    private static final String TRUE_LITERAL = "true";	//$NON-NLS-1$

    private IXMLSource xmlSource;
    
    private String path, user, pass;

	//The boolean indicate whether the connection is open.
	private boolean isOpen;
	
	private Map<String, Object> appContext;
	
	Properties connProperties;
	
	private Object ri;
    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		if( isOpen == true )
		{
			return;
		}
		this.connProperties = connProperties;
		
		isOpen = true;
		
		// Set global path, domain, username, and password
        path = connProperties.getProperty( Constants.CONST_PROP_FILELIST );
        user = connProperties.getProperty( Constants.CONST_PROP_USERNAME );
        pass = connProperties.getProperty( Constants.CONST_PROP_PASSWORD );
        String domain = "http://localhost:8086/openmrs";
        try {
			getAuthenticated(path, domain, user, pass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	private static void getAuthenticated(final String urlStr, final String domain, final String userName, final String password) throws IOException 
	{

	    /*StringBuilder response = new StringBuilder();*/

		Authenticator.setDefault(new Authenticator() {
	        @Override
	        public PasswordAuthentication getPasswordAuthentication() {
	        	return new PasswordAuthentication(userName, password.toCharArray());
	        }
	    });
		
		URL urlRequest = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) urlRequest.openConnection();
	    conn.setRequestProperty("Accept", "text/xml");		
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
		if ( !( context instanceof Map ) )
			throw new OdaException( Messages.getString( "Connection.InvalidAppContext" ) );//$NON-NLS-1$
		this.appContext = (Map) context;

		// The following code are for backward compatibility only.Once we
		// nolonger
		// support original BIRT ODA XML Driver this code block should be
		// removed.
		// ///////////////////////////////////////////////////////////////////
		String legacyInputStreamKey = "org.eclipse.birt.report.data.oda.xml.inputStream"; //$NON-NLS-1$
		if ( this.appContext.get( legacyInputStreamKey ) != null )
		{
			this.appContext.put( Constants.APPCONTEXT_INPUTSTREAM,
					this.appContext.get( legacyInputStreamKey ) );
			this.appContext.remove( legacyInputStreamKey );
		}

		String legacyCloseInputStreamKey = "org.eclipse.birt.report.data.oda.xml.closeInputStream"; //$NON-NLS-1$
		if ( this.appContext.get( legacyCloseInputStreamKey ) != null )
		{
			this.appContext.put( Constants.APPCONTEXT_CLOSEINPUTSTREAM,
					this.appContext.get( legacyCloseInputStreamKey ) );
			this.appContext.remove( legacyCloseInputStreamKey );
		}
		/////////////////////////////////////////////////////////////////////
		
		ri = appContext.get( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
		
		isOpen = false;
		if ( xmlSource != null )
		{
			xmlSource.release( );
		}
		this.appContext = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
		return isOpen;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType ) throws OdaException
	{
	    // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new DataSetMetaData( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSetType ) throws OdaException
	{
        // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new Query( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException
	{
		//return 0;	// no limit
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException
	{
	    // do nothing; assumes no transaction support needed
		throw new UnsupportedOperationException( );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException
	{
        // do nothing; assumes no transaction support needed
		throw new UnsupportedOperationException( );
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu.util.ULocale)
     */
    public void setLocale( ULocale locale ) throws OdaException
    {
        // do nothing; assumes no locale support
    	throw new UnsupportedOperationException( );
    }
    
    public IXMLSource getXMLSource( ) throws OdaException
	{
		if ( xmlSource == null )
		{
			initXMLSource( );
		}
		return xmlSource;
	}
    
	private void initXMLSource( ) throws OdaException
	{
		String encoding = connProperties == null ? null :(String) connProperties.get( Constants.CONST_PROP_ENCODINGLIST);
		String file = connProperties == null ? null :(String) connProperties.get( Constants.CONST_PROP_FILELIST );
		if ( appContext != null
				&& appContext.get( Constants.APPCONTEXT_INPUTSTREAM ) instanceof InputStream )
		{
			boolean closeOriginalInputStream = false;
			Object closeInputStream = appContext.get( Constants.APPCONTEXT_CLOSEINPUTSTREAM );
			if( TRUE_LITERAL.equalsIgnoreCase( closeInputStream == null ? null : closeInputStream.toString( ) ) )
			{
				closeOriginalInputStream = true;
			}
			xmlSource = new XMLSourceFromInputStream( 
					(InputStream) appContext.get( Constants.APPCONTEXT_INPUTSTREAM ), 
					encoding,
					closeOriginalInputStream );
		}
		else
		{
			xmlSource = new XMLSourceFromPath( file, encoding, ri );
		}
	}
    
}

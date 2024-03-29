/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.openmrs.xml.util;

import java.io.InputStream;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.openmrs.xml.i18n.Messages;


public class XMLSourceFromPath implements IXMLSource
{
	private String path;
	private String encoding;
	private Object ri;
	
	public XMLSourceFromPath( String path, Object resourceIdentifiers ) throws OdaException
	{	
		if ( path == null || path.equals( "" ) ) //$NON-NLS-1$
		{
			throw new OdaException( Messages.getString( "Connection.PropertiesMissing" ) ); //$NON-NLS-1$
		}
		this.path = path;
		this.ri = resourceIdentifiers;
	}
	
	public XMLSourceFromPath( String path, String encoding, Object resourceIdentifiers ) throws OdaException
	{
		this( path, resourceIdentifiers );
		this.encoding = encoding;
	}

	public InputStream openInputStream( ) throws OdaException
	{
		return ResourceLocatorUtil.getInputStream( ri, path );
	}
	
	public void release( ) throws OdaException
	{
	}

	public String getEncoding( )
	{
		return encoding;
	}

}

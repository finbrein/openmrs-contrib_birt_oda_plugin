/*******************************************************************************
 * Copyright (c) 2004, 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.openmrs.xml.util;

/**
 * This interface defined the methods that would be used by classes that use sax parser.
 */
public interface ISaxParserConsumer
{
	/**
	 * The method is used to populate one column that is specified by path in current row.
	 * @param path
	 * @param value
	 */
	public void manipulateData(XMLPath path, String value);
	

	public void startElement( XMLPath path );
	
	public void endElement( XMLPath path );
	
	public void finish( );
}

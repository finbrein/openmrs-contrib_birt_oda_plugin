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
package org.eclipse.birt.report.data.oda.openmrs.impl;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.MappedTables;

/**
 * This class describe the information of certain ResultSet.
 */
public class ResultSetMetaData implements IResultSetMetaData
{
	
	private MappedTables mt;
	
	//Table Name.
	private String tableName;
	
	/**
	 * 
	 * @param ri
	 * @param tableName
	 */
	public ResultSetMetaData(MappedTables mt, String tableName)
	{
		this.mt = mt;
		this.tableName = tableName;

	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount( ) throws OdaException
	{
		return mt.getColumnCount( tableName );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName( int index ) throws OdaException
	{
		return mt.getColumnName( tableName, index );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel( int index ) throws OdaException
	{
		return getColumnName( index );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType( int index ) throws OdaException
	{
		return DataTypes.getType( mt.getColumnType( tableName, index ));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName( int index ) throws OdaException
	{
		return mt.getColumnType( tableName, index );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnDisplayLength(int)
	 */
	public int getColumnDisplayLength( int index ) throws OdaException
	{
		throw new UnsupportedOperationException();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision( int index ) throws OdaException
	{
		return -1;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getScale(int)
	 */
	public int getScale( int index ) throws OdaException
	{
		return -1;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable( int index ) throws OdaException
	{
		return columnNullableUnknown;
	}
}



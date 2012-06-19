/*
 *************************************************************************
 * Copyright (c) 2012 <<Your Company Name here>>
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.openmrs.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

import org.eclipse.birt.report.data.oda.openmrs.xml.Constants;
import org.eclipse.birt.report.data.oda.openmrs.xml.i18n.Messages;
import org.eclipse.birt.report.data.oda.openmrs.xml.util.MappedTables;

/**
 * Implementation class of IQuery for an ODA runtime driver.
 * <br>
 * For demo purpose, the auto-generated method stubs have
 * hard-coded implementation that returns a pre-defined set
 * of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place. 
 */
public class Query implements IQuery
{
	//private int m_maxRows;
    //private String m_preparedText;
    
	//The name of the XML table that will be prepared
	private String tableName;

	//The max rows of result set created by this query that might returned
	private int maxRows;
	
	//	indicate whether the result set has been closed.
	private boolean isClosed;
	
	private Connection connection;
	
	private MappedTables mt;
	/**
	 * 
	 * @param file
	 * @param ri
	 */
	public Query( Connection connection )
	{
		tableName = null;
		maxRows = 0;
		isClosed = false;
		this.connection = connection;
	}
		
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	public void prepare( String queryText ) throws OdaException
	{
        // TODO Auto-generated method stub
        //m_preparedText = queryText;
		testClosed();
		if ( queryText == null )
			throw new org.eclipse.datatools.connectivity.oda.OdaException( Messages.getString("Query.InvalidQueryText") ); //$NON-NLS-1$
	
		String[] temp = queryText.trim().split(Constants.QUERYTEXT_TABLE_NAME_DEFN_DELIMITER);
		
		if ( temp.length != 2 )
			throw new org.eclipse.datatools.connectivity.oda.OdaException( Messages.getString("Query.InvalidQueryText") ); //$NON-NLS-1$
	
		this.tableName = temp[0];
		
		this.mt = new MappedTables( temp[1] );
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
		throw new UnsupportedOperationException ();	
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException
	{
        // TODO Auto-generated method stub
        //m_preparedText = null;
        this.isClosed = true;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this prepared query.
         */
		//return new ResultSetMetaData();
		testClosed();
		return new ResultSetMetaData( mt, tableName);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this prepared query.
         */
/*		IResultSet resultSet = new ResultSet();
		resultSet.setMaxRows( getMaxRows() );
		return resultSet;*/
		
		testClosed( );
		if ( this.tableName == null || this.tableName.trim( ).length( ) == 0 )
			throw new OdaException( Messages.getString( "Query.QueryHasNotBeenPrepared" ) ); //$NON-NLS-1$
		
		ResultSet result = new ResultSet( connection, mt, tableName, this.getMaxRows( ));
	
		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		// do nothing; assumes no data set query property
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
	    //m_maxRows = max;
	    testClosed();
		this.maxRows = max > 0 ? max:0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException
	{
		//return m_maxRows;
		testClosed();
		return this.maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		throw new UnsupportedOperationException ();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		setString( parameterName, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		setString( parameterId, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
	 */
	public void setDouble( String parameterName, double value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		setString( parameterName, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		setString( parameterId, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		setString( parameterName, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		setString( parameterId, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
	 */
	public void setString( String parameterName, String value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		if ( mt != null )
		{
			mt.setParameterValue( parameterName, value );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		if ( mt != null )
		{
			if ( parameterId >= 1 && parameterId <= mt.getParameters( ).length )
			{
				mt.setParameterValue( mt.getParameters( )[parameterId-1], value );
			}
		}
		
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		setString( parameterName, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		setString( parameterId, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		setString( parameterName, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		setString( parameterId, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		setString( parameterName, String.valueOf( value ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
		setString( parameterId, String.valueOf( value ) );
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean( String parameterName, boolean value )
            throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    	setString( parameterName, String.valueOf( value ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
     */
    public void setBoolean( int parameterId, boolean value )
            throws OdaException
    {
        // TODO Auto-generated method stub       
        // only applies to input parameter
    	setString( parameterId, String.valueOf( value ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String, java.lang.Object)
     */
    public void setObject( String parameterName, Object value )
            throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    	setString( parameterName, String.valueOf( value ) );
    	
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int, java.lang.Object)
     */
    public void setObject( int parameterId, Object value ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to input parameter
    	setString( parameterId, String.valueOf( value ) );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
     */
    public void setNull( String parameterName ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    	setString( parameterName, "" ); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
     */
    public void setNull( int parameterId ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to input parameter
    	setString( parameterId, "" ); //$NON-NLS-1$
    }

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
	 */
	public int findInParameter( String parameterName ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		//return 0;
		
		if ( mt == null )
		{
			return -1;
		}
		for ( int i=0; i<mt.getParameters( ).length; i++ )
		{
			if ( mt.getParameters( )[i].equals( parameterName ))
			{
				return i+1;
			}
		}
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this prepared query.
         */
		//return new ParameterMetaData();
		if ( mt == null )
		{
			return null;
		}
		return new IParameterMetaData( )
		{

			public int getParameterCount( ) throws OdaException
			{
				return mt.getParameters( ).length;
			}

			public int getParameterMode( int param ) throws OdaException
			{
				return IParameterMetaData.parameterModeIn;
			}

			public String getParameterName( int param ) throws OdaException
			{
				return mt.getParameters( )[param - 1];
			}

			public int getParameterType( int param ) throws OdaException
			{
				return DataTypes.STRING;
			}

			public String getParameterTypeName( int param ) throws OdaException
			{
				return DataTypes.getTypeString( DataTypes.STRING );
			}

			public int getPrecision( int param ) throws OdaException
			{
				return -1;
			}

			public int getScale( int param ) throws OdaException
			{
				return -1;
			}

			public int isNullable( int param ) throws OdaException
			{
				return IParameterMetaData.parameterNullableUnknown;
			}
			
		};
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		// only applies to sorting, assumes not supported
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException
	{
		// only applies to sorting
		//return null;
		throw new UnsupportedOperationException ();
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse.datatools.connectivity.oda.spec.QuerySpecification)
     */
    public void setSpecification( QuerySpecification querySpec )
            throws OdaException, UnsupportedOperationException
    {
        // assumes no support
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
     */
    public QuerySpecification getSpecification()
    {
        // assumes no support
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
     */
    public String getEffectiveQueryText()
    {
        // TODO Auto-generated method stub
        //return m_preparedText;
    	throw new UnsupportedOperationException ();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
     */
    public void cancel() throws OdaException, UnsupportedOperationException
    {
        // assumes unable to cancel while executing a query
        throw new UnsupportedOperationException();
    }

	/**
	 * If the result set is closed then throw an OdaException. This method is invoked
	 * before an method defined in IResultSet is called.
	 * 
	 * @throws OdaException
	 */
	private void testClosed() throws OdaException
	{
		if( isClosed )
			throw new OdaException( Messages.getString("Query.ResultSetClosed")); //$NON-NLS-1$
	}
    
}

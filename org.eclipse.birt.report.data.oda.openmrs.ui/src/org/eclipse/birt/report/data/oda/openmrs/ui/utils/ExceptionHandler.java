/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.openmrs.ui.utils;

import org.eclipse.swt.widgets.Shell;

/**
 * Exception handler that displays an exception in a message box, and prints a
 * stack trace.
 *
 */
public class ExceptionHandler
{

	public static void showException( Shell parentShell, String title,
			String msg, Throwable ex )
	{
		new ExceptionDialog( parentShell, title, msg, ex ).open( );
	}
}

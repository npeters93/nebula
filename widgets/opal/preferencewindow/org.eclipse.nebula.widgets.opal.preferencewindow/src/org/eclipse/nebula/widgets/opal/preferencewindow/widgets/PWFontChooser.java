/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Laurent CARON (laurent.caron at gmail dot com) - Initial
 * implementation and API
 *******************************************************************************/
package org.eclipse.nebula.widgets.opal.preferencewindow.widgets;

import org.eclipse.nebula.widgets.opal.commons.ResourceManager;
import org.eclipse.nebula.widgets.opal.preferencewindow.PreferenceWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Instances of this class are used to select a font
 */
public class PWFontChooser extends PWChooser {
	private FontData fontData;

	/**
	 * Constructor
	 *
	 * @param label associated label
	 * @param propertyKey associated key
	 */
	public PWFontChooser(final String label, final String propertyKey) {
		super(label, propertyKey);
	}

	/**
	 * @see org.eclipse.nebula.widgets.opal.preferencewindow.widgets.PWWidget#check()
	 */
	@Override
	public void check() {
		final Object value = PreferenceWindow.getInstance().getValueFor(getPropertyKey());
		if (value == null) {
			PreferenceWindow.getInstance().setValue(getPropertyKey(), null);
		} else {
			if (!(value instanceof FontData)) {
				throw new UnsupportedOperationException("The property '" + getPropertyKey()
						+ "' has to be a FontData because it is associated to a font chooser");
			}
		}
	}

	/**
	 * @see org.eclipse.nebula.widgets.opal.preferencewindow.widgets.PWChooser#setButtonAction(org.eclipse.swt.widgets.Text,
	 *      org.eclipse.swt.widgets.Button)
	 */
	@Override
	protected void setButtonAction(final Text text, final Button button) {
		fontData = (FontData) PreferenceWindow.getInstance().getValueFor(getPropertyKey());

		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				final FontDialog dialog = new FontDialog(text.getShell());
				final FontData result = dialog.open();
				if (result != null && result.getName() != null && !"".equals(result.getName().trim())) {
					fontData = result;
					PreferenceWindow.getInstance().setValue(getPropertyKey(), result);
					text.setText(buildFontInformation());
				}

			}
		});

	}

	/**
	 * @return a string that contains data about the choosen font
	 */
	protected String buildFontInformation() {
		final StringBuilder sb = new StringBuilder();
		if (fontData != null) {
			sb.append(fontData.getName()).append(",").append(fontData.getHeight()).append(" pt");
			if ((fontData.getStyle() & SWT.BOLD) == SWT.BOLD) {
				sb.append(", ").append(ResourceManager.getLabel(ResourceManager.BOLD));
			}
			if ((fontData.getStyle() & SWT.ITALIC) == SWT.ITALIC) {
				sb.append(", ").append(ResourceManager.getLabel(ResourceManager.ITALIC));
			}
		}
		return sb.toString();
	}
}

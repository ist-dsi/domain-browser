/*
 * @(#)TesseractContextAction.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Artur Ventura
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Tesseract Shell Module.
 *
 *   The Tesseract Shell Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Tesseract Shell Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Tesseract Shell Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package modules.tesseract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.domain.groups.Role;
import pt.ist.bennu.vaadin.actions.VaadinContextAction;
import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/tesseract")
/**
 * 
 * @author Artur Ventura
 * 
 */
public class TesseractContextAction extends VaadinContextAction {

	@CreateNodeAction(bundle = "TESSERACT_SHELL_RESOURCES", key = "tesseract.name", groupKey = "tesseract.group")
	public final ActionForward createTesseractNode(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");

		final Node parent = getDomainObject(request, "parentOfNodesToManageId");

		String bundle = "resources.TesseractShellResources";
		VaadinNode.createVaadinNode(virtualHost, parent, bundle, "tesseract.name", "tesseract", Role.getRole(RoleType.MANAGER));

		return forwardToMuneConfiguration(request, virtualHost, parent);
	}
}

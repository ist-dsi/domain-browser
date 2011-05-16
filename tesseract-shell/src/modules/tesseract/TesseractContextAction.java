package modules.tesseract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.Node;
import myorg.domain.groups.Role;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.vaadin.actions.VaadinContextAction;
import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/tesseract")
public class TesseractContextAction extends VaadinContextAction{
    
    @CreateNodeAction(bundle = "TESSERACT_SHELL_RESOURCES", key = "tesseract.name", groupKey = "tesseract.group")
    public final ActionForward createTesseractNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");

	final Node parent = getDomainObject(request, "parentOfNodesToManageId");

	String bundle = "resources.TesseractShellResources";
	VaadinNode.createVaadinNode(virtualHost, parent, bundle, "tesseract.name",
		"tesseract", Role.getRole(RoleType.MANAGER));

	return forwardToMuneConfiguration(request, virtualHost, parent);
    }
}

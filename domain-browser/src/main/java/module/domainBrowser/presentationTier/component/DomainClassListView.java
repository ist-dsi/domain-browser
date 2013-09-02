package module.domainBrowser.presentationTier.component;

import java.util.ArrayList;
import java.util.Collection;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.domain.DomainUtils.DomainClassLink;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainFenixFrameworkRoot;
import pt.ist.fenixframework.DomainMetaClass;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DomainClassListView extends VerticalLayout {

    private static final String CLASS_NAME_COLUMN = "className";
    private static final String OBJECTS_COLUMN = "objects";
    private static final String PREDICATES_COLUMN = "predicates";
    private static final String INCONSISTENCIES_COLUMN = "inconsistencies";

    public static class DomainMetaClassBean {

        public DomainMetaClassBean(DomainMetaClass metaClass) {
            this.metaClass = metaClass;
        }

        private final DomainMetaClass metaClass;

        private DomainMetaClass getMetaClass() {
            return metaClass;
        }

        public Link getClassName() {
            return new DomainClassLink(getMetaClass());
        }

        public Integer getObjects() {
            return DomainUtils.getObjectCountIncludingSubclasses(getMetaClass());
        }

        public Integer getPredicates() {
            return getMetaClass().getAllConsistencyPredicates().size();
        }

        public Integer getInconsistencies() {
            return DomainUtils.getInconsistencyCount(getMetaClass());
        }

        private static Collection<DomainMetaClassBean> readAllDomainMetaClasses() {
            Collection<DomainMetaClassBean> allMetaClassBeans = new ArrayList<DomainMetaClassBean>();
            for (DomainMetaClass metaClass : DomainFenixFrameworkRoot.getInstance().getDomainMetaClassSet()) {
                allMetaClassBeans.add(new DomainMetaClassBean(metaClass));
            }
            return allMetaClassBeans;
        }

        private static Collection<DomainMetaClassBean> searchDomainMetaClasses(String classSearch) {
            //add a wildcard to separate packages
            classSearch = classSearch.replaceAll("\\.", ".*");

            //add a wildcard before each capitalized letter to search for camel-cased class names
            //Start at index 1, because the first letter is already going to be preceeded by a wildcard
            for (int index = 1; index < classSearch.length(); index++) {
                if (Character.isUpperCase(classSearch.charAt(index))) {
                    classSearch = classSearch.substring(0, index) + ".*" + classSearch.substring(index);
                    index += 2;
                }
            }

            Collection<DomainMetaClassBean> matchingMetaClassBeans = new ArrayList<DomainMetaClassBean>();
            for (DomainMetaClass metaClass : DomainFenixFrameworkRoot.getInstance().getDomainMetaClassSet()) {
                if (metaClass.getDomainClass().getName().matches(".*" + classSearch + ".*")) {
                    matchingMetaClassBeans.add(new DomainMetaClassBean(metaClass));
                }
            }
            return matchingMetaClassBeans;
        }
    }

    public DomainClassListView() {
        this("");
    }

    public DomainClassListView(String classSearch) {
        super();
        setSpacing(true);

        Label header;
        Collection<DomainMetaClassBean> metaClassBeans;
        if (StringUtils.isEmpty(classSearch)) {
            header = new Label("All Domain Classes");
            metaClassBeans = DomainMetaClassBean.readAllDomainMetaClasses();
        } else {
            header = new Label("Domain Classes matching search results for: <b>" + classSearch + "</b>", Label.CONTENT_XHTML);
            metaClassBeans = DomainMetaClassBean.searchDomainMetaClasses(classSearch);
        }

        addComponent(header);

        //The BeanItemContainer discovers the properties by using reflection to search for public getters
        //Methods discovered by reflection are in an arbitrary order
        final BeanItemContainer<DomainMetaClassBean> container =
                new BeanItemContainer<DomainMetaClassBean>(DomainMetaClassBean.class, metaClassBeans);
        container.getContainerPropertyIds().clear();
        //This code orders the columns manually (by the order they are added)
        container.addNestedContainerProperty(CLASS_NAME_COLUMN);
        container.addNestedContainerProperty(OBJECTS_COLUMN);
        container.addNestedContainerProperty(PREDICATES_COLUMN);
        container.addNestedContainerProperty(INCONSISTENCIES_COLUMN);

        Table classListTable = new Table();
        classListTable.setSizeFull();
        classListTable.setHeight("500px");
        classListTable.setContainerDataSource(container);
        classListTable.setColumnAlignment(CLASS_NAME_COLUMN, Table.ALIGN_RIGHT);
        addComponent(classListTable);

        addComponent(new Label("Label:"));
        addComponent(new Label("<b><span style='font-size:11px'>" + CLASS_NAME_COLUMN.toUpperCase()
                + "</span></b> - Full package name of the class", Label.CONTENT_XHTML));

        addComponent(new Label("<b><span style='font-size:11px'>" + OBJECTS_COLUMN.toUpperCase()
                + "</span></b> - Number of existing objects of the class (and subclasses)", Label.CONTENT_XHTML));

        addComponent(new Label("<b><span style='font-size:11px'>" + PREDICATES_COLUMN.toUpperCase()
                + "</span></b> - Number of predicates declared by the class", Label.CONTENT_XHTML));

        addComponent(new Label("<b><span style='font-size:11px'>" + INCONSISTENCIES_COLUMN.toUpperCase()
                + "</span></b> - Objects that are inconsistent according to the declared predicates, "
                + "among the existing objects of the class (and subclasses)", Label.CONTENT_XHTML));
    }
}

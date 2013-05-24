package module.domainBrowser.presentationTier.component;

import java.util.ArrayList;
import java.util.Collection;

import pt.ist.fenixframework.DomainFenixFrameworkRoot;
import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class DomainClassListView extends Table {

    public static class DomainMetaClassBean {

        public DomainMetaClassBean(DomainMetaClass metaClass) {
            this.metaClass = metaClass;
        }

        private final DomainMetaClass metaClass;

        private DomainMetaClass getMetaClass() {
            return metaClass;
        }

        public Label getClassName() {
            Class<? extends DomainObject> domainClass = getMetaClass().getDomainClass();
            Label label =
                    new Label(domainClass.getPackage().getName() + ".<b>" + domainClass.getSimpleName() + "</b>",
                            Label.CONTENT_XHTML);
            label.setSizeUndefined();
            return label;
        }

        public Integer getObjects() {
            return getObjectCountIncludingSubclasses(getMetaClass());
        }

        public Integer getPredicates() {
            return getMetaClass().getDeclaredConsistencyPredicateSet().size();
        }

        public Integer getInconsistencies() {
            int inconsistencies = 0;
            for (DomainConsistencyPredicate predicate : getMetaClass().getDeclaredConsistencyPredicateSet()) {
                inconsistencies += predicate.getInconsistentDependenceRecordSet().size();
            }
            return inconsistencies;
        }

        private static Collection<DomainMetaClassBean> readAllDomainMetaClasses() {
            Collection<DomainMetaClassBean> allMetaClassBeans = new ArrayList<DomainMetaClassBean>();
            for (DomainMetaClass metaClass : DomainFenixFrameworkRoot.getInstance().getDomainMetaClassSet()) {
                allMetaClassBeans.add(new DomainMetaClassBean(metaClass));
            }
            return allMetaClassBeans;
        }

        private static int getObjectCountIncludingSubclasses(DomainMetaClass metaClass) {
            int totalCount = metaClass.getExistingDomainMetaObjectsCount();
            for (DomainMetaClass metaSubclass : metaClass.getDomainMetaSubclassSet()) {
                totalCount += getObjectCountIncludingSubclasses(metaSubclass);
            }
            return totalCount;
        }
    }

    public DomainClassListView() {
        super();
        setSizeFull();
        setHeight("500px");

        BeanItemContainer<DomainMetaClassBean> container =
                new BeanItemContainer<DomainMetaClassBean>(DomainMetaClassBean.class,
                        DomainMetaClassBean.readAllDomainMetaClasses());

        //The BeanItemContainer discovers the properties by using reflection to search for public getters
        //Methods discovered by reflection are in an arbitrary order
        //This code order the columns manually (by the order they are added)
        container.removeContainerProperty("className");
        container.removeContainerProperty("objects");
        container.removeContainerProperty("predicates");
        container.removeContainerProperty("inconsistencies");
        container.addNestedContainerProperty("className");
        container.addNestedContainerProperty("objects");
        container.addNestedContainerProperty("predicates");
        container.addNestedContainerProperty("inconsistencies");

        setContainerDataSource(container);

        setColumnAlignment("className", Table.ALIGN_RIGHT);
    }
}

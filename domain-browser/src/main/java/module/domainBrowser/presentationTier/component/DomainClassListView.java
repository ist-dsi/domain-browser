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
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DomainClassListView extends VerticalLayout {

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
        setSpacing(true);

        addComponent(new Label("All Domain Classes"));

        //The BeanItemContainer discovers the properties by using reflection to search for public getters
        //Methods discovered by reflection are in an arbitrary order
        //This code orders the columns manually (by the order they are added)
        final BeanItemContainer<DomainMetaClassBean> container =
                new BeanItemContainer<DomainMetaClassBean>(DomainMetaClassBean.class,
                        DomainMetaClassBean.readAllDomainMetaClasses()) {
                    {
                        removeContainerProperty("className");
                        removeContainerProperty("objects");
                        removeContainerProperty("predicates");
                        removeContainerProperty("inconsistencies");
                        addNestedContainerProperty("className");
                        addNestedContainerProperty("objects");
                        addNestedContainerProperty("predicates");
                        addNestedContainerProperty("inconsistencies");
                    }
                };
        Table classListTable = new Table() {
            {
                setSizeFull();
                setHeight("500px");
                setContainerDataSource(container);
                setColumnAlignment("className", Table.ALIGN_RIGHT);
            }
        };
        addComponent(classListTable);

        addComponent(new Label("Label:"));
        addComponent(new Label("Objects - Number of existing objects of the class (and subclasses)"));
        addComponent(new Label("Predicates - Number of predicates declared by the class"));
        addComponent(new Label("Inconsistencies - Objects that are inconsistent according to the declared predicates, "
                + "among the existing objects of the class (and subclasses)"));

    }
}

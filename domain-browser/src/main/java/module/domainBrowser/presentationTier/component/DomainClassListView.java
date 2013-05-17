package module.domainBrowser.presentationTier.component;

import pt.ist.fenixframework.DomainFenixFrameworkRoot;
import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class DomainClassListView extends TransactionalTable {

    public DomainClassListView() {
        super("resources/DomainBrowserResources");
        setSizeFull();
        setHeight("500px");

        DomainContainer<DomainMetaClass> container =
                new DomainContainer<DomainMetaClass>(DomainFenixFrameworkRoot.getInstance().getDomainMetaClassSet(),
                        DomainMetaClass.class);
        container.setContainerProperties("domainClass");

        addGeneratedColumn("Objects", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                DomainMetaClass metaClass = (DomainMetaClass) itemId;
                return String.valueOf(metaClass.getExistingDomainMetaObjectsCount());
            }
        });

        addGeneratedColumn("Predicates", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                DomainMetaClass metaClass = (DomainMetaClass) itemId;
                return String.valueOf(metaClass.getAllConsistencyPredicates().size());
            }
        });

        addGeneratedColumn("Inconsistencies", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                DomainMetaClass metaClass = (DomainMetaClass) itemId;
                int inconsistencies = 0;
                for (DomainConsistencyPredicate predicate : metaClass.getAllConsistencyPredicates()) {
                    inconsistencies += predicate.getInconsistentDependenceRecordSet().size();
                }
                return String.valueOf(inconsistencies);
            }
        });

        setContainerDataSource(container);
    }
}

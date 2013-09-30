package module.domainBrowser.presentationTier.component;

import java.util.Collection;
import java.util.HashSet;

import module.domainBrowser.domain.DomainUtils.ConsistencyPredicateLink;
import module.domainBrowser.domain.DomainUtils.DomainClassLink;
import module.domainBrowser.domain.DomainUtils.DomainObjectLink;
import module.domainBrowser.presentationTier.component.DomainClassView.MetaObjectCollectionWrapper;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.DomainMetaObject;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;
import pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord;
import pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ConsistencyPredicateView extends GridLayout {

    private final DomainMetaClass metaClass;
    private final DomainConsistencyPredicate consistencyPredicate;

    public ConsistencyPredicateView(DomainConsistencyPredicate consistencyPredicate) {
        super(6, 100);
        setSpacing(true);
        setSizeFull();

        this.consistencyPredicate = consistencyPredicate;
        metaClass = consistencyPredicate.getDomainMetaClass();
    }

    @Override
    public void attach() {
        super.attach();

        HorizontalLayout classLayout = new HorizontalLayout();
        classLayout.setSpacing(true);
        classLayout.addComponent(new Label("from class"));
        classLayout.addComponent(new DomainClassLink(metaClass));
        addComponent(classLayout, 0, 0, 2, 0);

        if (consistencyPredicate.isPublic()) {
            PublicConsistencyPredicate overriddenPredicate =
                    ((PublicConsistencyPredicate) consistencyPredicate).getPublicConsistencyPredicateOverridden();
            if (overriddenPredicate != null) {
                HorizontalLayout overridesLayout = new HorizontalLayout();
                overridesLayout.setSpacing(true);
                overridesLayout.addComponent(new Label("overrides predicate"));
                overridesLayout.addComponent(new ConsistencyPredicateLink(overriddenPredicate));
                addComponent(overridesLayout, 3, 0, 5, 0);
                setComponentAlignment(overridesLayout, Alignment.MIDDLE_LEFT);
            }
        }

        String modifier = "";
        if (consistencyPredicate.isFinal()) {
            modifier = "final ";
        } else if (consistencyPredicate.isPrivate()) {
            modifier = "private ";
        } else if (consistencyPredicate.isPublic()) {
            modifier = "public ";
        }
        addComponent(new Label("<h2>" + modifier + "consistency predicate " + consistencyPredicate.getPredicate().getName()
                + "()</h2>", Label.CONTENT_XHTML), 0, 1, 5, 1);

        if (consistencyPredicate.isPublic() && !consistencyPredicate.isFinal()) {
            PublicConsistencyPredicate publicPredicate = (PublicConsistencyPredicate) consistencyPredicate;
            if (!publicPredicate.getPublicConsistencyPredicateOverridingSet().isEmpty()) {
                addComponent(new Label("Overriding Predicates:"), 0, 2, 5, 2);
                Panel panel = new Panel();
                for (PublicConsistencyPredicate overridingPredicate : publicPredicate
                        .getPublicConsistencyPredicateOverridingSet()) {
                    panel.addComponent(new ConsistencyPredicateLink(overridingPredicate));
                }
                addComponent(panel, 0, 3, 5, 3);
            }
        }

        addComponent(new Label("<h3>Objects</h3>", Label.CONTENT_XHTML), 0, 4, 5, 4);
        final TabSheet tabs = new TabSheet();
        MetaObjectCollectionWrapper affectedObjects = new MetaObjectCollectionWrapper(findAffectedClasses(consistencyPredicate));
        Collection<DomainMetaObject> inconsistentMetaObjects = new HashSet<DomainMetaObject>();
        for (DomainDependenceRecord dependenceRecord : consistencyPredicate.getInconsistentDependenceRecordSet()) {
            inconsistentMetaObjects.add(dependenceRecord.getDependentDomainMetaObject());
        }
        tabs.addTab(createObjectsLayout(affectedObjects), affectedObjects.size() + " affected objects");
        tabs.addTab(createObjectsLayout(inconsistentMetaObjects), inconsistentMetaObjects.size() + " inconsistent objects");
        addComponent(tabs, 0, 5, 5, 5);
    }

    private VerticalLayout createObjectsLayout(final Collection<DomainMetaObject> objects) {
        final BeanItemContainer<DomainMetaObject> container = new BeanItemContainer<DomainMetaObject>(DomainMetaObject.class);
        //Clear all properties; use only the table's generated columns
        container.getContainerPropertyIds().clear();
        container.addAll(first(objects, 25));

        final VerticalLayout objectsLayout = new VerticalLayout();
        final PagedTable objectsTable = new PagedTable();
        objectsTable.setSizeFull();
        objectsTable.setPageLength(first(objects, 25).size());
        objectsTable.setContainerDataSource(container);
        objectsTable.addGeneratedColumn("Object", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return new DomainObjectLink(((DomainMetaObject) itemId).getDomainObject());
            }
        });
        objectsTable.addGeneratedColumn("Type", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return new DomainClassLink(((DomainMetaObject) itemId).getDomainMetaClass());
            }
        });
        objectsLayout.addComponent(objectsTable);
        final ProgressIndicator progress = new ProgressIndicator();
        progress.setIndeterminate(true);
        progress.setEnabled(true);
        objectsLayout.addComponent(progress);
        objectsLayout.setComponentAlignment(progress, Alignment.MIDDLE_CENTER);

        Thread thread = new Thread() {
            @Override
            @Atomic(mode = Atomic.TxMode.READ)
            public void run() {
                // force the iteration through all the existingObjects to pre-load them in memory
                // to reduce the contention time inside the syncronized block
                for (DomainMetaObject metaObject : objects) {
                    ;
                }
                synchronized (getApplication()) {
                    if (objects.size() > 25) {
                        container.addAll(objects);
                        HorizontalLayout controls = objectsTable.createControls();
                        controls.removeComponent(controls.getComponent(0)); //Remove page length control
                        objectsLayout.addComponent(controls);
                    }
                    objectsLayout.removeComponent(progress);
                }
            }
        };
        thread.start();

        return objectsLayout;
    }

    public static Collection<DomainMetaObject> first(Collection<DomainMetaObject> objects, int size) {
        Collection<DomainMetaObject> firstObjs = new HashSet<DomainMetaObject>();
        for (DomainMetaObject metaObject : objects) {
            if (firstObjs.size() >= size) {
                break;
            }
            firstObjs.add(metaObject);
        }

        return firstObjs;
    }

    private DomainMetaClass[] findAffectedClasses(DomainConsistencyPredicate domainPredicate) {
        Collection<DomainMetaClass> affectedClasses = new HashSet<DomainMetaClass>();
        DomainMetaClass predicateClass = domainPredicate.getDomainMetaClass();
        affectedClasses.add(predicateClass);
        for (DomainMetaClass metaSubclass : predicateClass.getDomainMetaSubclassSet()) {
            affectedClasses.addAll(findAffectedClasses(domainPredicate, metaSubclass));
        }
        return affectedClasses.toArray(new DomainMetaClass[affectedClasses.size()]);
    }

    private Collection<DomainMetaClass> findAffectedClasses(DomainConsistencyPredicate domainPredicate, DomainMetaClass metaClass) {
        Collection<DomainMetaClass> affectedClasses = new HashSet<DomainMetaClass>();
        try {
            metaClass.getDomainClass().getDeclaredMethod(domainPredicate.getPredicate().getName());
            // If no exception was thrown, the method is being overridden from this class downwards,
            // so stop and don't search in subclasses.
            return affectedClasses;
        } catch (NoSuchMethodException e) {
            // The method is not being overridden here, so add this subclass.
            affectedClasses.add(metaClass);
        }

        for (DomainMetaClass metaSubclass : metaClass.getDomainMetaSubclassSet()) {
            affectedClasses.addAll(findAffectedClasses(domainPredicate, metaSubclass));
        }
        return affectedClasses;
    }
}

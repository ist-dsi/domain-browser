package module.domainBrowser.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import dml.DomainClass;
import dml.Role;
import dml.Slot;

public class DomainUtils {

    public static final Comparator<Slot> SLOT_COMPARATOR = new Comparator<Slot>() {
        @Override
        public int compare(final Slot s1, final Slot s2) {
            return s1.getName().compareTo(s2.getName());
        }
    };

    public static final Comparator<Role> RELATION_SLOT_COMPARATOR = new Comparator<Role>() {
        @Override
        public int compare(final Role r1, final Role r2) {
            return compareRoleNames(r1, r2);
        }
    };

    private static int compareRoleNames(Role r1, Role r2) {
        if ((r1.getName() == null) && (r2.getName() != null)) {
            return -1;
        }
        if ((r1.getName() != null) && (r2.getName() == null)) {
            return 1;
        }
        if ((r1.getName() == null) && (r2.getName() == null)) {
            if (r1.getOtherRole().getName() == null || r2.getOtherRole().getName() == null) {
                throw new RuntimeException("Found a domain relation without any playsRole?!");
            }
            return compareRoleNames(r1.getOtherRole(), r2.getOtherRole());
        }
        return r1.getName().compareTo(r2.getName());
    }

    public static DomainObject readDomainObject(final String externalId) {
        return externalId != null && !externalId.isEmpty() && StringUtils.isNumeric(externalId) ? AbstractDomainObject
                .fromExternalId(externalId) : null;
    }

    public static Object getSlot(final DomainObject domainObject, final Slot slot) {
        final Method method = getMethod(domainObject, slot);
        if (method != null) {
            try {
                return method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    public static DomainObject getRelationSlot(final DomainObject domainObject, final Role role) {
        final Method method = getMethod(domainObject, role);
        if (method != null) {
            try {
                return (DomainObject) method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    public static Set<DomainObject> getRelationSet(final DomainObject domainObject, final String slotName) {
        final Method method = getMethod(domainObject, getterMethod(slotName));
        if (method != null) {
            try {
                return (Set<DomainObject>) method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Set<DomainObject> getRelationSet(final DomainObject domainObject, final Role role) {
        final Method method = getMethod(domainObject, role);
        if (method != null) {
            try {
                return (Set<DomainObject>) method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    protected static Method getMethod(final DomainObject domainObject, final Slot slot) {
        return getMethod(domainObject, getterMethod(slot));
    }

    protected static Method getMethod(final DomainObject domainObject, final Role role) {
        return getMethod(domainObject, getterMethod(role));
    }

    protected static Method getMethod(final DomainObject domainObject, final String slotName) {
        if (domainObject != null && slotName != null && !slotName.isEmpty()) {
            final Class<? extends DomainObject> clazz = domainObject.getClass();
            try {
                return clazz.getMethod(slotName);
            } catch (final NoSuchMethodException e) {
                throw new Error(e);
            } catch (final SecurityException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    protected static String getterMethod(final String slotName) {
        return slotName == null || slotName.isEmpty() ? null : "get" + StringUtils.capitalize(slotName);
    }

    protected static String getterMethod(final Slot slot) {
        return getterMethod(slot.getName());
    }

    protected static String getterMethod(final Role role) {
        return getterMethod(role.getName());
    }

    public static SortedSet<Slot> getSlots(final DomainClass domainClass) {
        final SortedSet<Slot> result = new TreeSet<Slot>(DomainUtils.SLOT_COMPARATOR);
        for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
            for (final Slot slot : dc.getSlotsList()) {
                result.add(slot);
            }
        }
        return result;
    }

    public static SortedSet<Role> getRelationSlots(final DomainClass domainClass) {
        final SortedSet<Role> result = new TreeSet<Role>(RELATION_SLOT_COMPARATOR);
        for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
            for (final Role role : dc.getRoleSlotsList()) {
                if (role.getMultiplicityUpper() == 1) {
                    result.add(role);
                }
            }
        }
        return result;
    }

    public static SortedSet<Role> getRelationSets(final DomainClass domainClass) {
        final SortedSet<Role> result = new TreeSet<Role>(RELATION_SLOT_COMPARATOR);
        for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
            for (final Role role : dc.getRoleSlotsList()) {
                if (role.getMultiplicityUpper() != 0 && role.getMultiplicityUpper() != 1) {
                    result.add(role);
                }
            }
        }
        return result;
    }

}

/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.rule.properties;

import java.util.Map;

import net.sourceforge.pmd.PropertyDescriptorFactory;
import net.sourceforge.pmd.PropertyDescriptorField;
import net.sourceforge.pmd.util.ClassUtil;
import net.sourceforge.pmd.util.StringUtil;

/**
 * Defines a property that supports single class types, even for primitive
 * values!
 *
 * TODO - untested for array types
 *
 * @author Brian Remedios
 */
public final class TypeProperty extends AbstractPackagedProperty<Class> {

    /** Factory. */
    public static final PropertyDescriptorFactory<Class> FACTORY // @formatter:off
        = new SingleValuePropertyDescriptorFactory<Class>(Class.class, PACKAGED_FIELD_TYPES_BY_KEY) {
            @Override
            public TypeProperty createWith(Map<PropertyDescriptorField, String> valuesById) {
                char delimiter = delimiterIn(valuesById);
                return new TypeProperty(nameIn(valuesById),
                                        descriptionIn(valuesById),
                                        defaultValueIn(valuesById),
                                        legalPackageNamesIn(valuesById, delimiter),
                                        0f);
            }
        }; // @formatter:on


    /**
     * Constructor for TypeProperty using a string as default value.
     *
     * @param theName           String
     * @param theDescription    String
     * @param defaultTypeStr    String
     * @param legalPackageNames String[]
     * @param theUIOrder        float
     *
     * @throws IllegalArgumentException if the default string could not be parsed into a Class
     */
    public TypeProperty(String theName, String theDescription, String defaultTypeStr, String[] legalPackageNames,
                        float theUIOrder) {
        this(theName, theDescription, classFrom(defaultTypeStr), legalPackageNames, theUIOrder);
    }


    /**
     * Constructor for TypeProperty.
     *
     * @param theName           String
     * @param theDescription    String
     * @param theDefault        Class
     * @param legalPackageNames String[]
     * @param theUIOrder        float
     *
     * @throws IllegalArgumentException
     */
    public TypeProperty(String theName, String theDescription, Class<?> theDefault, String[] legalPackageNames,
                        float theUIOrder) {
        super(theName, theDescription, theDefault, legalPackageNames, theUIOrder);
    }


    static Class<?> classFrom(String className) {
        if (StringUtil.isEmpty(className)) {
            return null;
        }

        Class<?> cls = ClassUtil.getTypeFor(className);
        if (cls != null) {
            return cls;
        }

        try {
            return Class.forName(className);
        } catch (Exception ex) {
            throw new IllegalArgumentException(className);
        }
    }


    // TODO:cf deprecate this
    public TypeProperty(String theName, String theDescription, String defaultTypeStr, Map<PropertyDescriptorField, String> otherParams,
                        float theUIOrder) {
        this(theName, theDescription, classFrom(defaultTypeStr), packageNamesIn(otherParams), theUIOrder);
    }


    @Override
    protected String packageNameOf(Class item) {
        return item.getName();
    }


    @Override
    public Class<Class> type() {
        return Class.class;
    }


    @Override
    protected String itemTypeName() {
        return "type";
    }


    @Override
    protected String asString(Class value) {
        return value == null ? "" : value.getName();
    }


    @Override
    public Class<?> createFrom(String valueString) {
        return classFrom(valueString);
    }
}

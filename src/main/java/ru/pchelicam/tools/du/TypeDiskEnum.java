package ru.pchelicam.tools.du;

/**
 * Enumeration for type of the disk
 */
public enum TypeDiskEnum {
    YA,
    DB;

    /**
     * Gets String value of the type
     * and returns the enum constant
     */
    public static TypeDiskEnum getType(String type) {
        for (TypeDiskEnum tde : TypeDiskEnum.values()) {
            if (tde.YA.toString().equals(type)) {
                return TypeDiskEnum.YA;
            } else if (tde.DB.toString().equals(type)) {
                return TypeDiskEnum.DB;
            }
        }
        return null;
    }
}

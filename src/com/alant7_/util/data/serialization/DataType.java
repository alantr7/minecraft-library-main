package com.alant7_.util.data.serialization;

public enum DataType {

    AUTO(null),
    ENUM(null),
    INTEGER(Serializers.INTEGER),
    LONG(Serializers.LONG),
    DOUBLE(Serializers.DOUBLE),
    BOOLEAN(Serializers.BOOLEAN),
    STRING(Serializers.STRING),
    UUID(Serializers.UUID),
    LOCATION(Serializers.LOCATION),
    ITEMSTACK(Serializers.ITEMSTACK),
    SERIALIZABLE(null),
    CUSTOM(null);

    private final FieldSerializer<?> serializer;

    DataType(FieldSerializer<?> serializer) {
        this.serializer = serializer;
    }

    @SuppressWarnings("unchecked")
    public FieldSerializer<Object> getSerializer() {
        return (FieldSerializer<Object>) serializer;
    }

}

package com.alant7_.util.data.serialization;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SerializableField {

    String name() default "";

    DataType type() default DataType.AUTO;

    Class<? extends FieldSerializer> serializer() default FieldSerializer.NULL.class;

}

package xyz.failutee.mineject.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.stream.Stream;

public final class AnnotationUtil {

    private AnnotationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be initialized");
    }

    // Use annotationType to bypass google proxy mechanism
    public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Annotation[] annotations = element.getDeclaredAnnotations();

        return Stream.of(annotations)
                .anyMatch(currentAnnotation -> currentAnnotation.annotationType() == annotationType);
    }
}

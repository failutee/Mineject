package xyz.failutee.mineject.util;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ClassScannerUtil {

    private ClassScannerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be initialized");
    }

    public static List<Class<?>> scanClasses(String packageName, ClassLoader classLoader) {
        try {
            ClassPath classPath = ClassPath.from(classLoader);

            Set<ClassPath.ClassInfo> classesInfo = classPath.getTopLevelClassesRecursive(packageName);

            List<Class<?>> classes = new ArrayList<>();

            for (ClassPath.ClassInfo info : classesInfo) {

                if (!info.getPackageName().startsWith(packageName)) {
                    continue;
                }

                Class<?> clazz = Class.forName(info.getName(), false, classLoader);

                classes.add(clazz);
            }

            return classes;

        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException("There was a problem while scanning classes.");
        }
    }
}

package com.tohant.om2d.di;

import com.google.common.reflect.ClassPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassScannerUtil {

    public static Set<Class<?>> getClasses(String packageName) throws IOException {
        Set<Class<?>> classes = new HashSet<>();
        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
        for (ClassPath.ClassInfo info : cp.getTopLevelClassesRecursive(packageName)) {
            classes.add(info.load());
        }
        return classes;
    }

}
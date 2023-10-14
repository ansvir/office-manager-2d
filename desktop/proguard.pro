# Specify input and output files.
-injars build/libs/desktop-1.0.jar
-outjars build/libs/desktop-1.0-minified.jar

# Don't optimize or shrink classes.
-dontobfuscate
-dontshrink
-dontoptimize

# Keep your main class (entry point)
-keep class com.tohant.om2d.DesktopLauncher {
    public static void main(java.lang.String[]);
}

# Strip debug information.
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
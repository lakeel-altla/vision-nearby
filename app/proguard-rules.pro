
# Reflection (Firebase)
-keep class com.lakeel.altla.vision.nearby.data.entity.**{ *; }

# Android Classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.view.View
-keep public class * extends android.view.ActionProvider {
  <init>(...);
}

# Enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# R
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Serializable
-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Atribute
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes Signature
-keepattributes SourceFile
-keepattributes LineNumberTable
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Deprecated

# Preference
-keep class android.support.v7.preference.PreferenceCategoryFix

# AltBeacon
-dontwarn org.altbeacon.**
-keep class org.altbeacon.** { *; }
-keep interface org.altbeacon.** { *; }

# Dagger
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

# Retrolamda
-dontwarn java.lang.invoke.*

# Rx
-keep class java.lang.invoke**
-dontwarn sun.misc.**
-dontnote rx.internal.util.PlatformDependent

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# logback
-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-dontwarn ch.qos.logback.core.net.*
-dontwarn javax.mail.**
-dontwarn javax.naming.Context
-dontwarn javax.naming.InitialContext

# google play (See http://librastudio.hatenablog.com/entry/2014/10/29/145321)
-keep class com.google.android.gms.** { *; }
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-dontwarn com.google.android.gms.**

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# apache commons
-dontwarn org.apache.commons.**
-dontwarn org.apache.commons.beanutils.**
-dontwarn org.apache.commons.collections.**
-dontwarn org.apache.commons.beanutils.locale.**

# Other
-dontwarn java.awt.**,javax.activation.**,java.beans.**
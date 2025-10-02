pro
# Recommended rules for Jetpack Compose
-keep class androidx.compose.runtime.snapshots.SnapshotKt {
    <fields>;
    <methods>;
}
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class * implements androidx.compose.ui.tooling.preview.PreviewParameterProvider

# Recommended rules for Kotlinx Serialization
# This is critical if you use reflection-based serializers, which is common.
-keepclassmembers class **.* {
    @kotlinx.serialization.Serializable <fields>;
}
-keep class **$$serializer { *; }
-keepclassmembers class * {
    *** Companion;
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializer fun serializer(...);
}

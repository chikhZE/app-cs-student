package com.zineeddine.cs_student.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DarkColorScheme = darkColorScheme(
    primary = TechBlue,          // الأزرق للعناصر الرئيسية والأزرار
    secondary = HackerGreen,     // الأخضر للتمييز والإشعارات أو الشارات
    background = DarkBackground, // خلفية التطبيق العامة
    surface = CardSurface,       // خلفية البطاقات (Cards) والـ TopBar
    onPrimary = Color.Black,
    onBackground = Color(0xFF142216) ,// لون النص فوق الزر الأزرق
    //onBackground = Color(0xFFECEFF4), // لون النصوص العامة ليكون واضحاً ومريحاً
    onSurface = Color(0xFFECEFF4)    // لون النصوص داخل البطاقات
)

private val LightColorScheme = lightColorScheme(
   // primary = Purple40,
    //secondary = PurpleGrey40,
    //tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

)

// Light Schemes


val PinkColorScheme = darkColorScheme(
    primary = Color(0xFFb46a72),
    secondary = Color(0xFFb46a72),
    background = Color(0xFF2d3a47),
    onBackground = Color(0xFF1D2734),
    surface = Color(0xFF1D2734),
    onPrimary = Color(0xFFD1DCB3),
)

val OceanLightColorScheme = darkColorScheme(
    primary = Color(0xFF0288D1),
    secondary = Color(0xFF4FC3F7),
    background = DarkBackground
)

// Dark Schemes (يمكنك إضافة الألوان الداكنة لكل مظهر)
val GreenDarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    secondary = Color(0xFF2E7D32),
    background = Color(0xFF121212)
)

@Composable
fun CSstudentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    selectedTheme: AppTheme = AppTheme.GREEN,
    content: @Composable () -> Unit
) {
    val colorSchemee = when (selectedTheme) {
        AppTheme.GREEN -> DarkColorScheme
        AppTheme.PINK -> PinkColorScheme

    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> colorSchemee
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

enum class AppTheme {
    GREEN,

    PINK,
}

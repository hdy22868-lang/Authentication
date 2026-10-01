package com.example.authentication.core.component

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * Nav Safety: يمنع تكرار فتح نفس الشاشة عند النقر المزدوج السريع
 * باستخدام launchSingleTop = true كقيمة افتراضية
 */
fun <T : Any> NavController.navigateSafe(
    route: T,
    builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }
) {
    navigate(route, builder)
}

/**
 * Pop Safety: يمنع توقف التطبيق (Crash) إذا حاول المستخدم الرجوع للخلف
 * بينما لا توجد شاشات سابقة في الـ BackStack
 */
fun NavController.popBackStackSafe(): Boolean {
    return if (previousBackStackEntry != null) {
        popBackStack()
    } else {
        false // لا تفعل شيئاً أو يمكنك إغلاق التطبيق هنا
    }
}

/**
 * دالة مساعدة للانتقال لشاشة جديدة وحذف شاشات سابقة بأمان (Type-Safe)
 */
inline fun <reified PopUpToRoute : Any> NavController.navigateAndClearSafe(
    route: Any,
    inclusive: Boolean = true
) {
    navigateSafe(route) {
        popUpTo<PopUpToRoute> {
            this.inclusive = inclusive
        }
    }
}
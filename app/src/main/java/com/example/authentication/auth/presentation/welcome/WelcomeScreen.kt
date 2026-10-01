package com.example.authentication.auth.presentation.welcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.authentication.R
import com.example.authentication.auth.presentation.component.AuthBackground
import com.example.authentication.auth.presentation.component.AuthFooter
import com.example.authentication.core.component.language.Language
import com.example.authentication.core.component.language.LanguageManager

@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    avatarImageRes: Int = R.drawable.hello_sign,
    modifier: Modifier = Modifier
) {

    var showLanguageMenu by remember { mutableStateOf(false) }

    val currentLanguage = remember { LanguageManager.currentLanguage }
    val currentLanguageText = if (currentLanguage == Language.ARABIC) "العربية" else "English"

    AuthBackground {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF3F1F5),
                    modifier = Modifier.clickable { showLanguageMenu = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentLanguageText,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF1E293B))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Language",
                            tint = Color(0xFF1E293B)
                        )
                    }
                }

                // القائمة المنسدلة للغات
                DropdownMenu(
                    expanded = showLanguageMenu,
                    onDismissRequest = { showLanguageMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("English") },
                        onClick = {
                            showLanguageMenu = false
                            LanguageManager.switchLanguage("en")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("العربية") },
                        onClick = {
                            showLanguageMenu = false
                            LanguageManager.switchLanguage("ar")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(120.dp),
                shadowElevation = 2.dp
            ) {
                Image(
                    painter = painterResource(id = avatarImageRes),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF161616)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_up), // استخدمت نص إنشاء حساب ليكون منطقياً
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AuthFooter(
                promptText = stringResource(R.string.already_have_account),
                actionText = stringResource(R.string.sign_in),
                onActionClick = onSignInClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
}
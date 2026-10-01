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
import com.example.authentication.core.component.language.LanguageSelector
import com.example.authentication.ui.theme.LocalAuthAssets

@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    avatarImageRes: Int? = LocalAuthAssets.current.welcomeImage,
    modifier: Modifier = Modifier
) {
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

            LanguageSelector(modifier = Modifier.padding(top = 16.dp))

            Spacer(modifier = Modifier.weight(1f))

            if (avatarImageRes != null) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(120.dp),
                    shadowElevation = 2.dp
                ) {
                    Image(
                        painter = painterResource(id = avatarImageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,

                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
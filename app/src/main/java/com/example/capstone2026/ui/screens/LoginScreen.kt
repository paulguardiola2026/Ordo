package com.example.capstone2026.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import com.example.capstone2026.R

/**
 * Simple login screen with basic credential validation.
 * Used to gate access to the main application.
 */
@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Welcome to Ordo",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Sign in to manage your academic schedule.",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val credentialManager = CredentialManager.create(context)

                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId(context.getString(R.string.default_web_client_id))
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                val result = credentialManager.getCredential(
                                    context = context,
                                    request = request
                                )

                                val credential = result.credential
                                val googleIdTokenCredential = GoogleIdTokenCredential
                                    .createFrom(credential.data)

                                val idToken = googleIdTokenCredential.idToken

                                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                                FirebaseAuth.getInstance()
                                    .signInWithCredential(firebaseCredential)
                                    .addOnSuccessListener {
                                        onGoogleSignInClick()
                                    }
                                    .addOnFailureListener { e ->
                                        errorMessage = e.message ?: "Google sign-in failed"
                                    }

                            } catch (e: Exception) {
                                Log.e("LoginScreen", "Google sign-in failed", e)
                                errorMessage = e.message ?: "Google sign-in failed"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign in with Google")
                }
            }
        }
    }
}
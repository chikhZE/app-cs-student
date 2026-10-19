package com.zineeddine.cs_student

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun signInWithGoogle(
    context: Context,
    scope: CoroutineScope,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val credentialManager = CredentialManager.create(context)

    val serverClientId = "611141881788-99li8f4drqnjcsjt6h9kn8gf9kut47sf.apps.googleusercontent.com"

    val googleIdOption = com.google.android.libraries.identity.googleid.GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientId)
        .setAutoSelectEnabled(true)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    scope.launch {
        try {
            val result = credentialManager.getCredential(context, request)
            handleSignIn(result, onSuccess, onError)
        } catch (e: Exception) {
            onError(e.localizedMessage ?: "Sign-in failed")
        }
    }
}

private fun handleSignIn(
    result: GetCredentialResponse,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val credential = result.credential
    if (credential is androidx.credentials.CustomCredential &&
        credential.type == com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleToken = googleIdTokenCredential.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(googleToken, null)
            FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(task.exception?.localizedMessage ?: "Firebase auth failed")
                    }
                }
        } catch (e: GoogleIdTokenParsingException) {
            onError("Invalid Google ID token")
        }
    } else {
        onError("Unexpected credential type")
    }
}

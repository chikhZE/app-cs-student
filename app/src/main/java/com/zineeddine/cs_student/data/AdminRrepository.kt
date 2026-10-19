package com.zineeddine.cs_student.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun checkIfCurrentUserIsAdmin(): Boolean {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: return false

        return try {
            val documentSnapshot = firestore.collection("settings")
                .document("app_config")
                .get()
                .await()
            if (!documentSnapshot.exists()) return false
            @Suppress("UNCHECKED_CAST")
            val adminsList = documentSnapshot.get("admins") as? List<String> ?: emptyList()

            adminsList.contains(currentUserEmail)

        } catch (e: Exception) {
            false
        }
    }
}

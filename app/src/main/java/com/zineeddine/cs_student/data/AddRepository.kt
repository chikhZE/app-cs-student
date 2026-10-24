package com.zineeddine.cs_student.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminContentRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addNews(
        titleEn: String, titleFr: String,
        descEn: String, descFr: String,
        tagEn: String, tagFr: String,
        date: String
    ): Boolean {
        val currentUser = auth.currentUser ?: return false

        val newsData = hashMapOf(
            "date" to date,
            "title" to mapOf("en" to titleEn, "fr" to titleFr),
            "description" to mapOf("en" to descEn, "fr" to descFr),
            "tag" to mapOf("en" to tagEn, "fr" to tagFr),
            "authorName" to (currentUser.displayName ?: "Admin"),
            "authorPhotoUrl" to (currentUser.photoUrl?.toString() ?: ""),
            "authorEmail" to (currentUser.email ?: ""),
            "createdAt" to FieldValue.serverTimestamp()
        )

        return try {
            firestore.collection("news").add(newsData).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addResource(
        moduleCode: String,
        semester: String,
        titleEn: String,
        titleFr: String,
        url: String,
        type: String
    ): Boolean {
        val currentUser = auth.currentUser ?: return false

        val newResourceMap = hashMapOf(
            "semester" to semester,
            "title" to mapOf("en" to titleEn, "fr" to titleFr),
            "type" to type,
            "url" to url,
            "authorName" to (currentUser.displayName ?: "Admin"),
            "authorPhotoUrl" to (currentUser.photoUrl?.toString() ?: ""),
            "authorEmail" to (currentUser.email ?: "")
        )

        return try {
            firestore.collection("resources")
                .document(moduleCode)
                .update("res", FieldValue.arrayUnion(newResourceMap))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addSchedule(
        subjectRes: String,
        dayRes: String,
        time: String,
        typeRes: String
    ): Boolean {
        val newScheduleMap = hashMapOf(
            "subjectRes" to subjectRes,
            "dayRes" to dayRes,
            "time" to time,
            "typeRes" to typeRes
        )

        return try {
            firestore.collection("schedule")
                .document("schedule")
                .update("scheduleArr", FieldValue.arrayUnion(newScheduleMap))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

}

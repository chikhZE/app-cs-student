package com.zineeddine.cs_student.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ResourceRepository{
    val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getResources(moduleCode: String, contentType: String): List<ResourceItem> {
        return try {
            val documentSnapshot = firestore.collection("resources")
                .document(moduleCode)
                .get()
                .await()

            if (!documentSnapshot.exists()) return emptyList()

            @Suppress("UNCHECKED_CAST")
            val rawList = documentSnapshot.get("res") as? List<Map<String, Any>> ?: return emptyList()

            rawList.mapNotNull { map ->
                val semester = map["semester"] as? String ?: ""
                @Suppress("UNCHECKED_CAST")
                val titleMap = map["title"] as? Map<String, String> ?: emptyMap()
                val type = map["type"] as? String ?: ""
                val url = map["url"] as? String ?: ""
                val authorName = map["authorName"] as? String ?: ""
                val authorPhotoUrl = map["authorPhotoUrl"] as? String ?: ""

                ResourceItem(
                    semester = semester,
                    title = titleMap,
                    type = type,
                    url = url,
                    authorName = authorName,
                    authorPhotoUrl = authorPhotoUrl
                )
            }.filter { it.type.equals(contentType, ignoreCase = true) } // تصفية حسب النوع (pdf, video, link)

        } catch (e: Exception) {
            emptyList()
        }
    }
}

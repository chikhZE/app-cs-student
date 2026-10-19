package com.zineeddine.cs_student.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class NewsRepository(
    private val newsDao: NewsDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val allNews: Flow<List<NewsEntity>> = newsDao.getAllNews()

    suspend fun refreshNews() {
        try {
            val snapshot = firestore.collection("news").get().await()
            val newsList = snapshot.documents.mapNotNull { doc ->
                val title = doc.get("title") as? Map<String, String> ?: emptyMap()
                val desc = doc.get("description") as? Map<String, String> ?: emptyMap()
                val tag = doc.get("tag") as? Map<String, String> ?: emptyMap()
                NewsEntity(
                    id = doc.id,
                    color = doc.getString("color") ?: "",
                    date = doc.getString("date") ?: "",
                    titleEn = title["en"] ?: "",
                    titleFr = title["fr"] ?: "",
                    descriptionEn = desc["en"] ?: "",
                    descriptionFr = desc["fr"] ?: "",
                    tagEn = tag["en"] ?: "",
                    tagFr = tag["fr"] ?: ""
                )

            }
            if(newsList.isNotEmpty()) {
                newsDao.clearAll()
                newsDao.insertAll(newsList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

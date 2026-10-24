package com.zineeddine.cs_student.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val allSchedule: Flow<List<ScheduleEntity>> = scheduleDao.getAllSchedule()

    suspend fun refreshSchedule() {
        try {
            val snapshot = firestore.collection("schedule").document("schedule").get().await()

            val rawList = snapshot.get("scheduleArr") as? List<Map<String, Any>> ?: emptyList()

            val scheduleList = rawList.mapNotNull { doc ->
                val dayRes = doc.get("dayRes") as? String ?: "Sunday"
                val subjectRes = doc.get("subjectRes") as? String ?: "ALGO"
                val time = doc.get("time") as? String ?: "00:00 - 00:00"
                val typeRes = doc.get("typeRes") as? String ?: "Cours"

                ScheduleEntity(
                    subjectRes = subjectRes,
                    typeRes = typeRes,
                    dayRes = dayRes,
                    time = time
                )
            }

            if (scheduleList.isNotEmpty()) {
                scheduleDao.clearAll()
                scheduleDao.insertAll(scheduleList)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteSchedule(itemToDelete: ScheduleEntity): Boolean {
        return try {
            val docRef = firestore.collection("schedule").document("schedule")
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) return false

            @Suppress("UNCHECKED_CAST")
            val rawList = snapshot.get("scheduleArr") as? List<Map<String, Any>> ?: return false

            val updatedList = rawList.filterNot { map ->
                val dayRes = map["dayRes"] as? String ?: ""
                val subjectRes = map["subjectRes"] as? String ?: ""
                val time = map["time"] as? String ?: ""
                val typeRes = map["typeRes"] as? String ?: ""

                dayRes.equals(itemToDelete.dayRes, ignoreCase = true) &&
                subjectRes.equals(itemToDelete.subjectRes, ignoreCase = true) &&
                time == itemToDelete.time &&
                typeRes.equals(itemToDelete.typeRes, ignoreCase = true)
            }

            docRef.update("scheduleArr", updatedList).await()
            refreshSchedule()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
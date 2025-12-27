package com.niaw.launchnea.database


import androidx.room.TypeConverter
import com.niaw.launchnea.Data_Classes.Location
import com.niaw.launchnea.Data_Classes.Reusables
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    private val zonedFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    private val localFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // ZonedDateTime ↔ String
    @TypeConverter fun zonedDateTimeToString(dt: ZonedDateTime?): String? = dt?.format(zonedFormatter)
    @TypeConverter fun stringToZonedDateTime(value: String?): ZonedDateTime? = value?.let { ZonedDateTime.parse(it, zonedFormatter) }

    // LocalDateTime ↔ String
    @TypeConverter fun localDateTimeToString(dt: LocalDateTime?): String? = dt?.format(localFormatter)
    @TypeConverter fun stringToLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it, localFormatter) }
   //  List<Reusables> ↔ JSON
    @TypeConverter fun reusablesToString(list: List<Reusables>?): String? = list?.let { json.encodeToString(it) }
    @TypeConverter fun stringToReusables(value: String?): List<Reusables>? = value?.let { json.decodeFromString(it) }

    // Location ↔ JSON
    @TypeConverter fun locationToString(loc: Location?): String? = loc?.let { json.encodeToString(it) }
    @TypeConverter fun stringToLocation(value: String?): Location? = value?.let { json.decodeFromString(it) }
}
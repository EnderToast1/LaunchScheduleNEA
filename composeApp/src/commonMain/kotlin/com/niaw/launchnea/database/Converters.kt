package com.niaw.launchnea.database


import androidx.room.TypeConverter
import com.niaw.launchnea.Location
import com.niaw.launchnea.Reusable
import com.niaw.launchnea.TimelineEvent
import kotlinx.serialization.json.Json
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Date

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

    //Date ↔ Long
    @TypeConverter fun dateToTimeStamp (date: Date): Long = date.time
    @TypeConverter fun timeStampToDateTime(timeStamp: Long): Date = Date.from(Instant.ofEpochMilli(timeStamp))
    //  List<Reusable> ↔ JSON
    @TypeConverter fun reusableToString(list: List<Reusable>?): String? = list?.let { json.encodeToString(it) }
    @TypeConverter fun stringToReusable(value: String?): List<Reusable>? = value?.let { json.decodeFromString(it) }

    // Location ↔ JSON
    @TypeConverter fun locationToString(loc: Location?): String? = loc?.let { json.encodeToString(it) }
    @TypeConverter fun stringToLocation(value: String?): Location? = value?.let { json.decodeFromString(it) }

    @TypeConverter fun timelineEventListToString(list: List<TimelineEvent>?): String? = list?.let { json.encodeToString(it) }
    @TypeConverter fun stringToTimelineEventList(value: String?): List<TimelineEvent>? = value?.let { json.decodeFromString(it) }
}

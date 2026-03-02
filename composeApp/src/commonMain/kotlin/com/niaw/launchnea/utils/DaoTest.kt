package com.niaw.launchnea.utils

import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.StarredLaunch
import kotlinx.coroutines.flow.first

//TODO TEST Check
    suspend fun daoTest(scheduleDao: ScheduleDao){
        val lastupdated=scheduleDao.getMetaData("lastUpdated")
        println("Last Updated: $lastupdated")
        val searchKey="CREW"
        println("\n\nSearch: $searchKey:")
        val launchesLIKE= scheduleDao.getLaunchLikeName(searchKey).first()
        launchesLIKE.forEach { launch ->
            println(launch.missionName)
        }
        println("\n\nAll launches")
        val launchLIST=scheduleDao.getLaunches().first()
        launchLIST.forEach { launch ->
            println("${launch.missionName}  :  ${launch.launchURL}")
        }
        println("\n\nOn This Day")
        val today=scheduleDao.getOnThisDayLaunches().first()
        today.forEach { launch ->
            println(launch.missionName)
        }
        println("\n\n ${launchesLIKE.size} of name $searchKey , ${launchLIST.size} Total launches\n")




        println("Stared Launches TEST to see if deletion works Expect 2 of the same Text if 1 or 3 it is broken")
        scheduleDao.starLaunch(StarredLaunch("862b54e3-7bf8-48e5-8cd1-d77257d5fde9"))
        var stared=scheduleDao.getStarredLaunches().first()
        stared.forEach { launch ->
            println(launch.missionName)
        }
        scheduleDao.unstarLaunch("862b54e3-7bf8-48e5-8cd1-d77257d5fde9")
        println(scheduleDao.getLaunchById("862b54e3-7bf8-48e5-8cd1-d77257d5fde9")?.missionName)
        stared=scheduleDao.getStarredLaunches().first()
        stared.forEach { launch ->
            println(launch.missionName)
        }



        println("\nEND OF PROGRAM")
    }

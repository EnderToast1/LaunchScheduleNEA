package com.niaw.launchnea.database

/**
 * Projection for Launch list queries.
 *
 * Omits heavy columns (description, timeline, weatherConcerns) so Android's ~2MB
 * CursorWindow can load large result sets without crashing. Full rows remain
 * available via getLaunchById for detail screens.
 *
 * Must stay a compile-time const for use inside Room @Query annotations.
 */
const val LAUNCH_LIST_COLUMNS =
    "launchID, launchURL, missionName, padName, agencyID, rocketID, rocketVersionID, " +
    "launchTimeUTC, launchTimePadLocal, reusableParts, success, windowOpen, windowClose, " +
    "imageUrl, imageCredit, licenseType, weatherProbability, agencyName, rocketName, " +
    "rocketVariantName, NULL AS description, orbit, padCords, padLocation, padCountry, " +
    "padTimeZone, liveStreamUrl, netPrecision, NULL AS weatherConcerns, '[]' AS timeline, " +
    "precisionAbbrev, programType"

/** Same projection with Launch. table qualifier for JOIN queries. */
const val LAUNCH_LIST_COLUMNS_QUALIFIED =
    "Launch.launchID, Launch.launchURL, Launch.missionName, Launch.padName, Launch.agencyID, " +
    "Launch.rocketID, Launch.rocketVersionID, Launch.launchTimeUTC, Launch.launchTimePadLocal, " +
    "Launch.reusableParts, Launch.success, Launch.windowOpen, Launch.windowClose, " +
    "Launch.imageUrl, Launch.imageCredit, Launch.licenseType, Launch.weatherProbability, " +
    "Launch.agencyName, Launch.rocketName, Launch.rocketVariantName, NULL AS description, " +
    "Launch.orbit, Launch.padCords, Launch.padLocation, Launch.padCountry, Launch.padTimeZone, " +
    "Launch.liveStreamUrl, Launch.netPrecision, NULL AS weatherConcerns, '[]' AS timeline, " +
    "Launch.precisionAbbrev, Launch.programType"

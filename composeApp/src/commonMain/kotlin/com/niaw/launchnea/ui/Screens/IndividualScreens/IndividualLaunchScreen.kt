package com.niaw.launchnea.ui.Screens.IndividualScreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition.Center.position
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.navigation.setBackHandler
import com.niaw.launchnea.ui.UiElements.IconTextRow
import com.niaw.launchnea.utils.LaunchTimesFormated
import com.niaw.launchnea.utils.Location
import com.niaw.launchnea.utils.abbreviate
import com.niaw.launchnea.utils.getLaunchTimesAsNeededFormat
import launchnea.composeapp.generated.resources.FilledRocket
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.search_book
import org.jetbrains.compose.resources.painterResource
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.collections.listOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.niaw.launchnea.Platform
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.getPlatform
import com.niaw.launchnea.ui.UiElements.cardShape
import kotlinx.coroutines.launch
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.Position


@Composable
fun IndividualLaunchScreen(scheduleDao: ScheduleDao,launch: Launch,  onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) } // track selected Rocket
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) } // track selected Reuse
    var selectedAgency by remember { mutableStateOf<Agency?>(null) } // track selected Agency
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(launch) {
                setBackHandler(enabled = true) {
                        onBack()
                }
        }
    if (selectedRocket != null) {
        println("Clicked on ${selectedRocket!!.name},  \n$selectedRocket")
        IndividualRocketScreen(selectedRocket!!) {
            println("Backed out of ${selectedRocket!!.name}")
            selectedRocket = null
        }
    }else if (selectedReuse != null) {
        println("Clicked on ${selectedReuse!!.name}\n $selectedReuse")
        IndividualReuseScreen(selectedReuse!!) {
            println("Backed out of ${selectedReuse!!.name}")
            selectedReuse = null
        }
    }else if (selectedAgency != null) {
        println("Clicked on ${selectedAgency!!.name},  \n$selectedAgency")
        IndividualAgencyScreen(selectedAgency!!) {
            println("Backed out of${selectedAgency!!.name}")
            selectedAgency = null
        }
    } else {

        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            HeaderRow(launch, onBack) { agencyID ->         //Back and Agency boxes
                coroutineScope.launch {
                    selectedAgency = scheduleDao.getAgencyById(id = agencyID)
                }
            }

            MissionMainBox(launch)
            Spacer(Modifier.height(5.dp))
            Column(Modifier.fillMaxWidth().padding(10.dp)) {
                LiveStreamBox()
                Spacer(Modifier.height(15.dp))

                MissionDetails(launch)
                Spacer(Modifier.height(10.dp))

                var vehicle by remember { mutableStateOf<Rocket?>(null) }
                LaunchedEffect(launch) {
                    vehicle= launch.rocketID?.let { scheduleDao.getRocketById(it) }
                }

                RocketSection(launch, vehicle){ rocket -> selectedRocket=rocket }
                Spacer(Modifier.height(5.dp))

                AgencyBox(launch) { agencyID ->
                    coroutineScope.launch {
                        selectedAgency = scheduleDao.getAgencyById(id = agencyID)
                    }
                }
                Spacer(Modifier.height(10.dp))

                ReuseDetails(launch){ reuseID ->
                    coroutineScope.launch {
                        selectedReuse = scheduleDao.getReuseById(id = reuseID)
                    }
                }//TODO Reuse
                Spacer(Modifier.height(10.dp))

                PadLocation(launch.padCords)
                Spacer(Modifier.height(10.dp))

                //TODO Stats maybe cut
            }
        }
    }
}

@Composable
fun HeaderRow(launch: Launch,onBack: () -> Unit,onClick: (String) -> Unit){
    val id = launch.agencyID
    if (id != null) {
    val agencyName=abbreviate(launch.agencyName?: "Unknown")

    val calculatedWeight = remember(agencyName) {
            val minWeight = 1f
            val maxWeight = 2f
            val normalized = (agencyName.length / 50f).coerceIn(0f, 1f) // 50 chars = max
            minWeight + normalized * (maxWeight - minWeight)
    }
    val animatedWeight by animateFloatAsState(calculatedWeight)


    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp, 12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .clickable { onBack() }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.weight(1.5f))

        Box(
            modifier = Modifier
                .weight(animatedWeight)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp, 12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .clickable { onClick(id) }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(Icons.Filled.Business, contentDescription = "Agency")
                Spacer(modifier = Modifier.width(4.dp))
                Text(agencyName, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    }
}

@Composable
fun MissionMainBox(launch:Launch){
        var selectedZone by remember {mutableStateOf(LiftoffTimeZone.DEVICE_LOCAL)}

        Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer).fillMaxWidth().padding(10.dp)) {
                Column(Modifier.fillMaxWidth()) {
                        launch.missionName?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(Modifier.height(5.dp))
                        TimeDropDown(selected = selectedZone, onSelected = { selectedZone = it }, modifier = Modifier.fillMaxWidth())

                        val dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy")
                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                        val launchTimes=getLaunchTimesAsNeededFormat(launch, dateFormatter, timeFormatter)

                        Text(
                                text=launchTimes.liftoffTime,
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        IconTextRow(launchTimes.liftoffDate,"Launch Date",MaterialTheme.typography.bodyMedium,rememberVectorPainter(Icons.Filled.CalendarMonth),Modifier,MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(Modifier.height(4.dp))
                    launch.padName?.let { IconTextRow(it,"pad name",MaterialTheme.typography.bodyMedium,rememberVectorPainter(Icons.Filled.Place),Modifier,MaterialTheme.colorScheme.onPrimaryContainer) }

                        Spacer(Modifier.height(15.dp))
                        LaunchWindowTimeline(launchTimes)
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDropDown(selected: LiftoffTimeZone, onSelected: (LiftoffTimeZone) -> Unit, modifier: Modifier = Modifier) {
        var expanded by remember { mutableStateOf(false) }

        Box(modifier.clip(RoundedCornerShape(12.dp))) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                                .clickable { expanded = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                        Text(
                                text = "Lift-off Time (${selected.label})",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.width(0.dp))

                        Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Select time zone"
                        )
                }

                DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                ) {
                        LiftoffTimeZone.entries.forEach { zone ->
                                DropdownMenuItem(
                                        text = { Text(zone.label) },
                                        onClick = {
                                                onSelected(zone)
                                                expanded = false
                                        }
                                )
                        }
                }
        }
}

enum class LiftoffTimeZone(val label: String) {
        PAD_LOCAL("Pad local"),
        UTC("UTC"),
        DEVICE_LOCAL("Device local")
}

@Composable
fun LiveStreamBox() {//TODO add click functionality
        Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(percent = 50)) // pill shape
                        .background(Color(0xFFC40000))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
        ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                ) {
                        Text(
                                text = "Watch Livestream",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge
                        )

                        Icon(
                                imageVector = Icons.Outlined.Link,
                                contentDescription = "Livestream link",
                                tint = Color.White
                        )
                }
        }
}

@Composable
fun MissionDetails(launch:Launch){

                IconTextRow(
                        "Mission Details",
                        "Mission Details",
                        MaterialTheme.typography.headlineLarge,
                        painterResource(Res.drawable.search_book),
                        Modifier,
                        MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))

        MissionDetailsBody(launch)

}

@Composable
fun MissionDetailsBody(launch:Launch){
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(0.5.dp, Color.Gray,
            RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer)) {
                Box(Modifier.padding(10.dp)) {
                        launch.missionName?.let {
                                Text(
                                        it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                        }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box(Modifier.padding(10.dp)) {
                        launch.description?.let {
                                Text(
                                        it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                        }
                }
        }

}

@Composable
fun LaunchWindowTimeline(launchTimes: LaunchTimesFormated) {
        val windowOpen = launchTimes.windowOpen
        val windowClose = launchTimes.windowClose
        val liftoff = launchTimes.liftoff

        if (windowClose == windowOpen) return//If There is no window then do not display anything

        // Total seconds relative to window open
        val totalWindowSeconds = windowClose.toEpochSecond(ZoneOffset.UTC) - windowOpen.toEpochSecond(ZoneOffset.UTC)
        val progressSeconds = liftoff.toEpochSecond(ZoneOffset.UTC) - windowOpen.toEpochSecond(ZoneOffset.UTC)

        val progress = (progressSeconds.toFloat() / totalWindowSeconds).coerceIn(0f, 1f)

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm") // only show time

        Column {
                BoxWithConstraints(
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp) // space for label + timeline
                ) {
                        val dotSize = 10.dp
                        val usableWidth = maxWidth - dotSize
                        val markerX = usableWidth * progress

                        // === Timeline base ===
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                        ) {
                                TimelineDot(color = Color.Gray, Modifier)
                            HorizontalDivider(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp), thickness = 3.dp, color = Color.DarkGray
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp), thickness = 3.dp, color = Color.DarkGray
                            )
                            TimelineDot(color = Color.Gray, Modifier)
                        }


                        Box(
                                modifier = Modifier
                                        .offset(x = markerX - 4.5.dp, y = (-5).dp)
                                        .align(Alignment.TopStart)
                        ) {
                                Icon(
                                        imageVector = Icons.Filled.Rocket,
                                        contentDescription = "Planned Launch",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                )

                        }

                        // === Green launch dot (on the line) ===
                        TimelineDot(
                                color = Color(0xFF067D00),
                                modifier = Modifier
                                        .offset(x = markerX)
                                        .align(Alignment.CenterStart)
                        )
                }

                Spacer(Modifier.height(4.dp))

                // === Window labels ===
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        Text(
                                text = "Window Open\n${windowOpen.format(timeFormatter)}",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Start
                        )
                        Text(
                                text = "Window Close\n${windowClose.format(timeFormatter)}",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.End
                        )
                }
        }
}

@Composable
fun TimelineDot(color: Color, modifier: Modifier) {
        Box(
                modifier = modifier.size(16.dp).background(color, CircleShape)
        )
}

@Composable
fun RocketSection(launch:Launch, vehicle:Rocket?,onClick: (Rocket) -> Unit){
    if (vehicle != null) {
        IconTextRow(
            "Rocket",
            "Rocket",
            MaterialTheme.typography.displayMedium,
            painterResource(Res.drawable.FilledRocket),
            Modifier,
            MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth().border(0.5.dp, Color.Gray,
            RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer).clickable(onClick = { onClick(vehicle) })) {
            //TODO image and row
            Box(Modifier.fillMaxWidth().padding(5.dp)) {
                Text("TEMP", style = MaterialTheme.typography.headlineSmall)
            }
            Box(modifier = Modifier.border(0.5.dp, Color.Gray, RoundedCornerShape(12.dp)).padding(10.dp)) {
               RocketInformationSection(vehicle)
            }
        }
    }
}

@Composable
fun RocketInformationSection(vehicle:Rocket){
        //TODO get data
    val pricePerLaunch=1000
    val liftoffThrust=20000
    val rocketDiameter = 3.1
    val rocketHeight = 3.2
    val fairingDiameter = 4.1
    val fairingHeight = 4.2
    val payloadToOrbitLEO = 51
    val payloadToOrbitGTO = 52
    val stages = 2



    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Price Per Launch:\n $$pricePerLaunch million", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Liftoff Thrust:\n $liftoffThrust Kilonewton", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Rocket:\n Diameter: ${rocketDiameter}m\n Height: ${rocketHeight}m", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Fairing:\n Diameter: ${fairingDiameter}m\n Height: ${fairingHeight}m", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Payload to orbit:\n LEO: ${payloadToOrbitLEO}Kg\n GTO: ${payloadToOrbitGTO}Kg", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Stages:\n #$stages", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun AgencyBox(launch:Launch,onClick: (String) -> Unit) {
    val id=launch.agencyID
    val name= launch.agencyName
    if (id != null && name != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(percent = 50)) // pill shape
                .background(MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick(id) },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = MaterialTheme.typography.labelLarge
                    )


                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "Agency Link",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        }
    }
}

@Composable
fun ReuseDetails(launch:Launch,onClick: (String) -> Unit){
        Box(modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 5.dp, vertical = 1.dp)){
        IconTextRow("Vehicle Reuse", "Reusable Parts", MaterialTheme.typography.headlineMedium,rememberVectorPainter(Icons.Filled.Recycling),Modifier,MaterialTheme.colorScheme.primary)
        }
        launch.reusableParts.forEach{reusable ->

                //TODO with onClick
        }
}

@Composable
fun PadLocation(location: Location) {
    val lat = location.latitude
    val lon = location.longitude

    if (lat != null && lon != null) {
        val siteCoordinates = remember(lon, lat) { Position(lon, lat) }

        val camera = rememberCameraState(firstPosition = CameraPosition(target = siteCoordinates, zoom = 11.5))


        if (getPlatform().name == "Android") {
            val markerGeoJson = remember(lon, lat) {
                """
            {
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "geometry": {
                            "type": "Point",
                            "coordinates": [$lon, $lat]
                        },
                        "properties": {}
                    }
                ]
            }
            """.trimIndent()
            }


            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).height(220.dp)) {
                MaplibreMap(
                    baseStyle = BaseStyle.Uri("https://api.maptiler.com/maps/hybrid/style.json?key=nCtZa4uILboOm2evI9Dy"),
                    cameraState = camera
                ) {
                    val source = rememberGeoJsonSource(
                        data = GeoJsonData.JsonString(markerGeoJson)
                    )
                    SymbolLayer(
                        id = "marker-layer",
                        source = source,
                        iconImage = image(
                            rememberVectorPainter(Icons.Filled.Place),
                            drawAsSdf = true
                        ),          // ← Use the vector icon directly!
                        iconColor = const(Color.Red),                   // Works great because vector icons are tintable
                        iconSize = const(1.8f),                         // Bigger (adjust 1.5f–2.5f as needed)
                        iconAnchor = const(SymbolAnchor.Bottom),        // Pinpoint at the bottom
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true)
                    )

                }
            }
        }else{
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                MaplibreMap(
                    baseStyle = BaseStyle.Uri("https://api.maptiler.com/maps/hybrid/style.json?key=nCtZa4uILboOm2evI9Dy"),
                    cameraState = camera
                )
            }
        }
    }
}
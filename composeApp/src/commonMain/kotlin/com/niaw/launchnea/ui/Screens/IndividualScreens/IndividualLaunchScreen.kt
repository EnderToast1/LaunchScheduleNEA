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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.niaw.launchnea.Platform
import com.niaw.launchnea.database.Converters
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.getPlatform
import com.niaw.launchnea.navigation.ViewModels.ReusableViewModel
import com.niaw.launchnea.navigation.ViewModels.ReuseLaunchesViewModel
import com.niaw.launchnea.ui.UiElements.BoxChip
import com.niaw.launchnea.ui.UiElements.BoxLeftImage
import com.niaw.launchnea.ui.UiElements.Chip
import com.niaw.launchnea.ui.UiElements.ImageGroup
import com.niaw.launchnea.ui.UiElements.cardShape
import com.niaw.launchnea.utils.Reusable
import kotlinx.coroutines.launch
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.Position
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.io.println


@Composable
fun IndividualLaunchScreen(scheduleDao: ScheduleDao,launch: Launch,  onBackLaunch: () -> Unit) {
    val zonedFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    println(launch.launchTimeUTC?.let { "Is in past: ${((ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES)).format(zonedFormatter) > (it).format(zonedFormatter))}" })
    println((launch.launchTimeUTC)!!.format(zonedFormatter))
    println(ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).format(zonedFormatter))
    val scrollState = rememberScrollState()
    var selectedRocketID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Rocket
    var selectedReuseID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Reuse
    var selectedAgencyID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Agency
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) }
    var selectedAgency by remember { mutableStateOf<Agency?>(null) }

    var agencyLogo by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(launch) {
        agencyLogo = launch.agencyID?.let { scheduleDao.getAgencyById(it) }?.logoURL
    }
    // THIS HANDLES SUB-SCREEN BACK PRESS
    // Active only when you are looking at an Agency, Rocket, or Launch
    setBackHandler(enabled = selectedAgencyID != null || selectedReuseID != null || selectedRocketID != null) {
        println("Internal Back: Closing sub-screen")
        selectedAgencyID = null
        selectedReuseID = null
        selectedRocketID = null
        selectedAgency = null
        selectedReuse = null
        selectedRocket = null
    }

    // THIS HANDLES MAIN SCREEN BACK PRESS
    // Active only when the main list is visible. It calls the parent's back function.
    setBackHandler(enabled = selectedAgencyID == null && selectedReuseID == null && selectedRocketID == null) {
        println("Main Back: Exiting IndividualReuseScreen")
        onBackLaunch()
    }

    if (selectedRocketID != null) {
        LaunchedEffect(selectedRocketID){
            selectedRocket = scheduleDao.getRocketById(id = selectedRocketID!!)
        }
        selectedRocket?.let { rocket ->
            println("Clicked on ${rocket.name},  \n$rocket")
            IndividualRocketScreen(scheduleDao,rocket) {
                println("Backed out of ${rocket.name}")
                selectedRocket = null
                selectedRocketID = null
            }
        }
    }
    else if (selectedReuseID != null) {
        LaunchedEffect(selectedReuseID){
            selectedReuse = scheduleDao.getReuseById(id = selectedReuseID!!)
        }
        selectedReuse?.let { reuse ->
            println("Clicked on ${reuse.name},  \n$reuse")
            IndividualReuseScreen(scheduleDao, reuse) {
                println("Backed out of ${reuse.name}")
                selectedReuse = null
                selectedReuseID = null
            }
        }
    }
    else if (selectedAgencyID != null) {
        LaunchedEffect(selectedAgencyID){
            selectedAgency = scheduleDao.getAgencyById(id = selectedAgencyID!!)
        }
        selectedAgency?.let { agency ->
            println("Clicked on ${agency.name},  \n$agency")
            IndividualAgencyScreen(agency) {
                println("Backed out of ${agency.name}")
                selectedAgency = null
                selectedAgencyID = null
            }
        }
    }
    else {

        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            HeaderRow(launch, onBackLaunch) { agencyID -> selectedAgencyID = agencyID }


            MissionMainBox(launch)
            Spacer(Modifier.height(5.dp))
            Column(Modifier.fillMaxWidth().padding(10.dp)) {
                LiveStreamBox()
                Spacer(Modifier.height(15.dp))

                MissionDetails(launch)
                Spacer(Modifier.height(10.dp))

                var vehicle by remember { mutableStateOf<RocketVersion?>(null) }
                LaunchedEffect(launch) {
                    vehicle = launch.rocketVersionID?.let { scheduleDao.getRocketVersionById(it) }
                }
                RocketSection(launch, vehicle) { launch -> selectedRocketID = launch.rocketID }
                Spacer(Modifier.height(5.dp))
            }
            AgencyBox(launch.agencyID, launch.agencyName,agencyLogo) { agencyID -> selectedAgencyID = agencyID }
            Spacer(Modifier.height(10.dp))

            ReuseDetails(scheduleDao,launch) { reuseID -> selectedReuseID = reuseID }//TODO Reuse
            Spacer(Modifier.height(10.dp))
            Column(Modifier.fillMaxWidth().padding(10.dp)) {
                PadLocation(launch.padCords)
                Spacer(Modifier.height(10.dp))
            }
            //TODO Stats maybe cut

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
                        IconTextRow(launchTimes.liftoffDate,"Launch Date",MaterialTheme.typography.bodyMedium,rememberVectorPainter(Icons.Filled.CalendarMonth),Modifier.fillMaxWidth(),MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(Modifier.height(4.dp))
                    launch.padName?.let { IconTextRow(it,"pad name",MaterialTheme.typography.bodyMedium,rememberVectorPainter(Icons.Filled.Place),Modifier.fillMaxWidth(),MaterialTheme.colorScheme.onSecondaryContainer) }

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
                        Modifier.fillMaxWidth(),
                        MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))

        MissionDetailsBody(launch)

}

@Composable
fun MissionDetailsBody(launch:Launch){
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(0.5.dp, Color.Gray,
            RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer)) {
                Box(Modifier.padding(10.dp)) {
                        launch.missionName?.let {
                                Text(
                                        it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                        }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box(Modifier.padding(10.dp)) {
                        launch.description?.let {
                                Text(
                                        it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
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
fun RocketSection(launch:Launch, vehicle: RocketVersion?, onClick: (Launch) -> Unit){
    if (vehicle != null) {
        IconTextRow(
            "Rocket",
            "Rocket",
            MaterialTheme.typography.displayMedium,
            painterResource(Res.drawable.FilledRocket),
            Modifier.fillMaxWidth(),
            MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth().border(0.5.dp, Color.Gray,
            RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer).clickable(onClick = { onClick(launch) })) {
            //TODO image and row
            Row(Modifier.fillMaxWidth().padding(5.dp)) {
                BoxLeftImage(vehicle.listBoxText().imageGroup)
                Spacer(Modifier.width(5.dp))
                Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {

                    Text(if(vehicle.active == true) "Active" else "retired", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    vehicle.name?.let { Text(it, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSecondaryContainer) }
                    Text("Active Since: ${vehicle.activeSince}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }

            }
            Box(modifier = Modifier.border(0.5.dp, Color.Gray, RoundedCornerShape(12.dp)).padding(10.dp)) {
               RocketInformationSection(vehicle)
            }
        }
    }
}

@Composable
fun RocketInformationSection(vehicle: RocketVersion){
        //TODO get data
    val pricePerLaunch=vehicle.launchCost
    val liftoffThrust=vehicle.liftoffThrust
    val rocketDiameter = vehicle.diameterMe
    val rocketHeight = vehicle.lengthMe
    val stages = vehicle.stages
    val payloadToOrbitLEO = vehicle.leoCapacity
    val payloadToOrbitGTO = vehicle.gtoCapacity
    val liftoffMass = vehicle.launchMass



    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Price Per Launch:\n $${pricePerLaunch?.div(1_000_000)} million", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))

            Text("Stages:\n #$stages", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Rocket:\n Diameter: ${rocketDiameter}m\n Height: ${rocketHeight}m", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Payload to orbit:\n LEO: ${payloadToOrbitLEO?.div(1000)} tonnes\n GTO: ${payloadToOrbitGTO?.div(1000)} tonnes", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Mass at liftOff:\n $liftoffMass tonnes", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Liftoff Thrust:\n $liftoffThrust Kilonewton", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun AgencyBox(id:String?, name:String?,logo:String?, onClick: (String) -> Unit) {

    if (id != null && name != null) {
        Surface(
            onClick = { onClick(id) },
            modifier = Modifier.fillMaxWidth().padding(10.dp,3.dp),
            shape = CircleShape, //Pill Shape
            color = MaterialTheme.colorScheme.tertiaryContainer,
            tonalElevation = 2.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Start) {
                    if (name != "SpaceX") {
                        Box(modifier = Modifier.height(45.dp).background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(10.dp)).padding(5.dp)) {
                            LogoLeft(logo, name)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }



                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Agency Link",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}


@Composable
fun LogoLeft(logoUrl:String?, name:String?) {
    SubcomposeAsyncImage(
        model = logoUrl,
        contentDescription = "Logo of $name",
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        contentScale = ContentScale.FillHeight,
        loading = {//While image loading
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .fillMaxHeight()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(15.dp),
                    color = Color.White,
                    strokeWidth = 1.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        },
        // This catches 'NoSuchKey' / 404 errors
        error = {
            LogoPlaceholder()
        }
    )
}


@Composable
fun LogoPlaceholder() {
    Box(
        modifier = Modifier
            .width(25.dp)
            .fillMaxHeight()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ImageNotSupported,
            contentDescription = "Logo not found",
            tint = Color.LightGray,
            modifier = Modifier.size(15.dp)
        )
    }
}




@Composable
fun ReuseDetails(scheduleDao: ScheduleDao, launch:Launch,onClick: (String) -> Unit){
        Column(modifier = Modifier.padding(5.dp,0.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 5.dp, vertical = 1.dp)) {
            IconTextRow(
                "Vehicle Reuse",
                "Reusable Parts",
                MaterialTheme.typography.headlineLarge,
                rememberVectorPainter(Icons.Filled.Recycling),
                Modifier.fillMaxWidth(),
                MaterialTheme.colorScheme.primary
            )

            launch.reusableParts.forEach { reusable ->
                ReusableBox(scheduleDao, launch, reusable)

            }
        }
}

@Composable
fun ReusableBox(scheduleDao: ScheduleDao, launch:Launch,reusable: Reusable) {
    val viewModel = remember(reusable.reuseID) {
        reusable.reuseID?.let {
            launch.launchTimeUTC?.let { launchTime ->
                ReusableViewModel(
                    scheduleDao,
                    it,
                    launchTime
                )
            }
        }
    }
    if (viewModel != null) {
        val reuses by viewModel.reuses.collectAsState()
        val dayTurnAround by viewModel.dayTurnAround.collectAsState()

        Column(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
            BoxChip(Chip(reusable.landingLocationName))
            reusable.reuseName?.let { Text(it,style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer) }
            Text("Flights #$reuses",style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text("$dayTurnAround day turnaround",style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
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

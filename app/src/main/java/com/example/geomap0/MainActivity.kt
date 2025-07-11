package com.example.geomap0


import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.Image
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

import androidx.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.NotificationUtil
import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.geomap0.ui.theme.GeoMap0Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.model.circleOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.io.path.ExperimentalPathApi
import kotlin.random.Random

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.jvm.internal.Intrinsics.Kotlin


//geofencing functions//////////////////////////////
fun createGeofence(id: String, lat: Double, lng: Double, radius: Float): Geofence {
    return Geofence.Builder()
        .setRequestId(id)
        .setCircularRegion(lat, lng, radius)
        .setLoiteringDelay(3000)
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .build()
}
fun createGeofencingRequest(geofence: Geofence): GeofencingRequest {
    return GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL or GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
}
fun getGeofencePendingIntent(context: Context): PendingIntent {
    val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
    return PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )
}
fun  addGeofence(
    context: Context,
    geofencingClient: GeofencingClient,
    geoAlarm: GeoAlarm
) {
//    -0.3947315, 36.9636633
    Log.d("info", "${geoAlarm.points.first().first()}:${geoAlarm.points.first().last()}")
    val geofence = createGeofence(geoAlarm.id, geoAlarm.points.first().first(), geoAlarm.points.first().last(), geoAlarm.radius.toFloat())
    val request = createGeofencingRequest(geofence)
    val pendingIntent = getGeofencePendingIntent(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.e("Geofence", "Permissions not granted")
        return
    }
    geofencingClient.addGeofences(request, pendingIntent)
        .addOnSuccessListener { Log.d("Geofence", "Geofence added!") }
        .addOnFailureListener { Log.e("Geofence", "Failed: alot") }
}

//fun GeofenceScreen(
//    lat: Double,
//    lng: Double,
//    context: Context,
//    geofencingClient: GeofencingClient) {
////    val geofencingClient = remember { LocationServices.getGeofencingClient(context) }
//            addGeofence(
//                context = context,
//                geofencingClient = geofencingClient,
//                lat = lat, // your coordinates
//                lng = lng,
//                radius = 100f
//            )
//
//}
/////////////////////////////////////////////////////

/////Notification channel regestration/////////
private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = R.string.CHANNEL_NAME.toString()
        val descriptionText = R.string.CHANNEL_DESC.toString()
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(R.string.CHANNEL_ID.toString(), name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

class Converters {
    @TypeConverter
    fun fromPointsList(points: MutableList<MutableList<Double>>): String {
        return Gson().toJson(points)
    }

    @TypeConverter
    fun toPointsList(json: String): MutableList<MutableList<Double>> {
        val type = object : TypeToken<MutableList<MutableList<Double>>>() {}.type
        return Gson().fromJson(json, type)
    }
}

@Entity
data class GeoAlarm  (
    @PrimaryKey(autoGenerate = true)val id0: Int =0,
    var id: String = "",
    var iscircle: Boolean = false,
    var note: String = "",
    var points: MutableList<MutableList<Double>> = mutableListOf<MutableList<Double>>(),
    var isActive: Boolean = false,
    var isDebugging: Boolean = false,
//    @ColumnInfo(defaultValue = "0.0")
    var radius: Double = 0.0,
)
@Dao
interface GeoAlarmDao {
    @Insert
    fun insertAll(vararg geoAlarm: GeoAlarm)

    @Delete
    fun delete(geoAlarm: GeoAlarm)

    @Query("SELECT * FROM geoAlarm")
    fun getAll(): List<GeoAlarm>

    @Query("SELECT * FROM geoAlarm WHERE id == :id")
    fun findid(id: String):GeoAlarm?

    @Update
    fun updategeoAlarm(vararg geoAlarm: GeoAlarm)
}

@DeleteTable.Entries(

    DeleteTable(tableName = "GeoAlarm")
)
class GeoAlarmMigrationSpec : AutoMigrationSpec
@Database(entities = [GeoAlarm::class], version = 2
//    , autoMigrations = [AutoMigration(from = 1, to = 2, )]
//    spec = GeoAlarmMigrationSpec::class)]
    ,exportSchema= true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun geoAlarmDao(): GeoAlarmDao
}

class backEndStuff(application: Context):ViewModel(){
    val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration(true).build()
     fun getStuff():List<GeoAlarm>{
        val geoalarmdao = db.geoAlarmDao()
        val stuff = geoalarmdao.getAll()
        println(stuff)
        return stuff;
    }
    suspend fun updateStuff():Boolean{
        val dao0 = db.geoAlarmDao()
        val answer = dao0.updategeoAlarm()
        return true
    }
    fun deleteStuff(geoAlarm: GeoAlarm):Boolean{
        val dao1 = db.geoAlarmDao()
        dao1.delete(geoAlarm)
        return  true
    }
    fun addStuff(geoAlarm: GeoAlarm):Boolean{
        val dao2 = db.geoAlarmDao()
        dao2.insertAll(geoAlarm)
        println(geoAlarm.id0)
        return true
    }
    fun getstufById(string: String):GeoAlarm?{
        val da03 = db.geoAlarmDao()
        val data = da03.findid(string)
//        Log.i("room","${data}")
        return  data
    }
}
var incompleteGeoAlarm =  GeoAlarm()
var debuggingCircle = 0

fun addAndReturnList(mutableList: MutableList<MutableList<Double>>, new: MutableList<Double>): MutableList<MutableList<Double>> {
    mutableList.add(new)
    return mutableList
}
class MapViewModel ():ViewModel(){
    private val _circles = mutableStateListOf<GeoAlarm>(incompleteGeoAlarm)

    val circles: List<GeoAlarm> = _circles
    private val _polygons = mutableStateListOf<GeoAlarm>(incompleteGeoAlarm)
    val polygons :List<GeoAlarm> = _polygons
    fun activategeoalarm(boolean: Boolean){

        if (boolean){
            _circles.first().isDebugging = true
            _circles.first().iscircle = true
            _circles.first().radius = 100.0;

            incompleteGeoAlarm = _circles.first()

        }else{
            _polygons.first().isDebugging = true
            _polygons.first().iscircle = false
            incompleteGeoAlarm = _circles.first()
        }
        println(_circles.size)

    }
    fun update(newCenter: LatLng) {
//        println(_circles.size)
        try {
//            println(_circles.first())
            if (_circles.first().isDebugging && _circles.first().iscircle){
                println("Hello mf")
//                _circles.first().id = newCenter.toString()
                _circles[0] = _circles.first().copy(points = mutableListOf(mutableListOf(newCenter.latitude,newCenter.longitude)))
                incompleteGeoAlarm = _circles.first()
            }
            if (_polygons.first().isDebugging && !_polygons.first().iscircle)
            {
                println("Ok now")

                _polygons[0] = _polygons.first().copy(
                    id = newCenter.toString(),
                    points = addAndReturnList(incompleteGeoAlarm.points,
                    mutableListOf(newCenter.latitude,newCenter.longitude),

                ))
                println(_polygons.first())
                incompleteGeoAlarm = _polygons.first()
            }
        }catch (e: Error){
            println(e.message)

        }

    }
    fun cancel(){
        _circles.clear()
        incompleteGeoAlarm = GeoAlarm()
    }
}


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityResultContracts.RequestPermission()
        super.onCreate(savedInstanceState)
        var testt = listOf<GeoAlarm>()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = 0,
                darkScrim = 1
            )
        )
//        applicationContext.deleteDatabase("database-name")
        setContent {
            GeoMap0Theme {
                var sliderPosition by remember { mutableFloatStateOf(100f) }
                var showBottomSheet by remember { mutableStateOf(false) }
                var isdibugging by remember { mutableStateOf(false) }
                var createdAlarm = mutableListOf<GeoAlarm>()
                CoroutineScope(Dispatchers.IO).launch {
                    createdAlarm = backEndStuff(application = application).getStuff().toMutableList()
                }
//                var activeGeoalarms by remember { mutableListOf(List<GeoAlarm>) }
                var addnote by remember { mutableStateOf(false) }
                fun showbottomDrawer(){
                    showBottomSheet = true
                }

                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                CoroutineScope(Dispatchers.IO).launch {
                    createNotificationChannel(context = applicationContext)
                }
                Scaffold(
                    containerColor = Color.White,
                    floatingActionButtonPosition = FabPosition.Start,
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },

                    floatingActionButton =
                        {
                            if (
                                !isdibugging
                            ){
                                FloatingActionButton(

                                    containerColor = Color.Black,
                                    contentColor = Color.White,
                                    onClick = {
                                        run {
                                                kotlin.run {
                                                    showbottomDrawer()
                                                }
                                        }
                                    },
                                ) {
                                    Icon(Icons.Filled.Add, "Floating action button.")
                                }
                            }else{
                                FloatingActionButton(
                                    modifier = Modifier.padding(end = 30.dp),
                                    onClick = {}
                                ) {
                                    Column (
                                        verticalArrangement = Arrangement.Bottom

                                    ){
                                        Slider(
                                            valueRange = 50f..200f,
                                            enabled = true,
                                            value = sliderPosition,
                                            onValueChange = { sliderPosition = it},
                                            colors = SliderColors(
                                                thumbColor = MaterialTheme.colorScheme.secondary,
                                                activeTrackColor = MaterialTheme.colorScheme.secondary,
                                                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                                                activeTickColor = MaterialTheme.colorScheme.secondary,
                                                inactiveTickColor = MaterialTheme.colorScheme.secondaryContainer,
                                                disabledThumbColor = MaterialTheme.colorScheme.secondaryContainer,
                                                disabledActiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                                                disabledActiveTickColor = MaterialTheme.colorScheme.secondaryContainer,
                                                disabledInactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                                                disabledInactiveTickColor = MaterialTheme.colorScheme.secondaryContainer,
                                            ),
                                        )
                                        Row (
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 20.dp),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ){

                                            TextButton(
                                                onClick = {
                                                    kotlin.run {
                                                        isdibugging = false
                                                        MapViewModel().cancel()
                                                    }
                                                }
                                            ) {
                                                Text(text = "Cancel", color = Color.White)
                                            }
                                            TextButton(
                                                onClick = {
                                                    run {
                                                        if (incompleteGeoAlarm.points.isNotEmpty()){
                                                            addnote = true
                                                        }
                                                    }

                                                }
                                            ) {
                                                Text(text = "Next", color = Color.White)
                                            }
                                        }
//                                        Row (
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .padding(end = 20.dp),
//                                            horizontalArrangement = Arrangement.SpaceAround
//                                        ){
//
//                                            TextButton(
//                                                onClick = {
//                                                    kotlin.run {
//                                                        isdibugging = false
//                                                        MapViewModel().cancel()
//                                                    }
//                                                }
//                                            ) {
//                                                Text(text = "Cancel", color = Color.White)
//                                            }
//                                            TextButton(
//                                                onClick = {
//                                                    run {
//                                                        if (incompleteGeoAlarm.points.isNotEmpty()){
//                                                            addnote = true
//                                                        }
//                                                    }
//
//                                                }
//                                            ) {
//                                                Text(text = "Next", color = Color.White)
//                                            }
//                                        }
                                    }

                                }
                            }
                    },

                    topBar = {
                        TopAppBar(
                            actions = {
//                                IconButton(
//                                    onClick = {
//                                        kotlin.run {
////                                                    addGeofence(context = applicationContext, geofencingClient = LocationServices.getGeofencingClient(applicationContext), geoAlarm = incompleteGeoAlarm)
////                                                sendNotification(title = "Test01", body = "NIce", context = applicationContext)
//
//                                        }
//                                    }
//                                ) {
//                                    Icon(
//                                        painter = painterResource(com.google.maps.android.compose.R.drawable.googleg_standard_color_18),
//                                        contentDescription = "nice"
//
//                                    )
//                                }
                                Icon(
                                    modifier = Modifier
                                        .padding(all = 15.dp)
                                        .clickable {
                                            kotlin.run {
//                                                CoroutineScope(Dispatchers.IO).launch {
//                                                    backEndStuff(application = application).addStuff(
//                                                        geoAlarm = GeoAlarm(
//                                                            id = "12345678",
//                                                            isDebugging = false,
//                                                            isActive = true,
//                                                            iscircle = true,
//                                                            note = "Test note 1.0"
//                                                        )
//                                                    )
//                                                }

                                            }
                                        },
                                    tint = Color.Black,
                                    contentDescription = "Menu",
                                    imageVector = Icons.Rounded.Search)
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White
                            )
                            ,
                            modifier = Modifier.background(color = Color.White),
                            navigationIcon = {
                                Image(
                                    modifier = Modifier.padding(all = 15.dp),
                                    contentDescription = "LOGO",
                                    painter = painterResource(R.drawable.logo_tr)
                                )
                            },
                            title = {
                                Text(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = TextUnit.Unspecified,
                                    text = "GoZone")
                            }

                        )
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Column {
//                        Greeting(name = "JAMES", modifier = Modifier.padding(innerPadding))
//                    }

                    GetPermissions(
                        application = application,
                        coroutineScope =scope,
                        snackbarHostState = snackbarHostState,
                        circleR = sliderPosition,
                        createdAlarm = createdAlarm,
                        innerPaddingValues = innerPadding
                    )
                    if (addnote){
                        var text by
                        rememberSaveable(stateSaver = TextFieldValue.Saver) {
                            mutableStateOf(TextFieldValue("", TextRange(0, 7)))
                        }
                        BasicAlertDialog(
                            properties = DialogProperties(
                                dismissOnClickOutside = false
                            ),
//        modifier = Modifier.background(Color.Black),
                            onDismissRequest = { }
                        ) {
                            Card() {
                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Icon(
                                        modifier = Modifier.padding(all = 20.dp),
                                        contentDescription = "Logo",
                                        painter = painterResource(R.drawable.logo_tr))
                                    Text("Add a note ")

                                    OutlinedTextField(
                                        modifier = Modifier.padding(all = 10.dp),
                                        value = text,

                                        onValueChange = {
                                                newval-> text = newval
                                        },
                                        label = { Text("Note") }
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        TextButton(
                                            onClick = {
                                                run {
                                                    addnote = false
                                                }
                                            }
                                        ) {
                                            Text("Cancel")
                                        }
                                        TextButton(
                                            modifier = Modifier.padding(start = 80.dp),
                                            onClick = {
                                                var rd = 10.0
                                                if (incompleteGeoAlarm.iscircle){
                                                    rd = sliderPosition.toDouble()
                                                }
                                                incompleteGeoAlarm.id = Random.nextInt().toString()
                                                CoroutineScope(Dispatchers.IO).launch{
                                                    addGeofence(context = applicationContext, geofencingClient = LocationServices.getGeofencingClient(applicationContext), geoAlarm = incompleteGeoAlarm)
                                                    backEndStuff(application = application).addStuff(
                                                        GeoAlarm(
                                                            id = incompleteGeoAlarm.id,
                                                            iscircle = incompleteGeoAlarm.iscircle,
                                                            note = text.text,
                                                            points = incompleteGeoAlarm.points,
                                                            isActive = true,
                                                            isDebugging = false,
                                                            radius = rd
                                                        )
                                                    )
                                                }
                                                addnote = false;
                                                isdibugging = false;

                                            }
                                        ) {
                                            Text("Finish")
                                        }
                                    }

                                }
                            }


                        }
                    }


                    if (showBottomSheet){
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            }
                        ) {
                            Column (
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ){
                                Card (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 20.dp)
                                        .clickable {
                                            kotlin.run {
                                                MapViewModel().activategeoalarm(true)
//                                                debuggingCircle = "true"
                                                isdibugging = true
                                                showBottomSheet = false
                                            }
                                        }
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Image(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .padding(all = 10.dp),
                                            contentDescription = "Cirlce",
                                            painter = painterResource(R.drawable.circle)
                                        )
                                        Text(
                                            text = "Add a Circular Geofence"
                                        )
                                    }
                                }
//                                Card (
//                                    modifier = Modifier
//                                        .padding(all = 20.dp)
//                                        .fillMaxWidth()
//                                        .clickable {
//                                            run {
//                                                MapViewModel().activategeoalarm(false)
////                                                debuggingCircle = "false"
//                                                showBottomSheet = false
//                                            }
//                                        }
//                                ) {
//                                    Row(
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Image(
//                                            modifier = Modifier
//                                                .size(80.dp)
//                                                .padding(all = 10.dp),
//                                            contentDescription = "Polygon",
//                                            painter = painterResource(R.drawable.polygon)
//                                        )
//                                        Text(
//                                            text = "Add a iregular Geofence"
//                                        )
//
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeoMap0Theme {
        Greeting("Android")
    }
}
suspend fun getAlarms(application: Application): List<GeoAlarm> {
    var data = listOf<GeoAlarm>()
     withContext(Dispatchers.IO) {
       data = backEndStuff(application).getStuff()
    }
    println("..................${data}")
    return data
}

@Composable
fun getCurrentLoc(
    boolean: Boolean,application: Application
    ,snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    circleR: Float,
    createdAlarm: List<GeoAlarm>,
    innerPaddingValues: PaddingValues

){
    val context = LocalContext.current

    var location by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {
        location = getCurrentLocationSuspend(context)
        withContext(Dispatchers.IO){
//            activeGeoalarms = backEndStuff(application =application ).getStuff()
        }
    }
//    println("WTFFFFFFFFFFFFFFFFFFFFFFFF")
    location?.let { loc ->
        MapDraw(
            initialLat = loc.latitude,
            initialLng = loc.longitude,
            boolean = boolean,
            application = application,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            circleR = circleR,
            createdAlarm = createdAlarm,
            innerPaddingValues = innerPaddingValues
            )
    } ?: run {
        Text("Waiting for location...")
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapDraw(modifier: Modifier = Modifier,
            boolean: Boolean,
            initialLat: Double,
            initialLng:Double,
            application: Application,
            viewModel: MapViewModel = MapViewModel(),
            snackbarHostState: SnackbarHostState,
            coroutineScope: CoroutineScope,
            circleR:Float,
            createdAlarm: List<GeoAlarm>,
            innerPaddingValues: PaddingValues
){

    val home = LatLng(initialLat, initialLng)
    val singaporeMarkerState = rememberMarkerState(position = home)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(home, 15f)
    }



    Log.e("data","$createdAlarm")
    var createdCircles = createdAlarm.filter { it.iscircle }
    var createdPoly = createdAlarm.filter { !it.iscircle }
    val circles = viewModel.circles
    val polygons = viewModel.polygons
    println("mapppppppppppppppppppppppppppppp")
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled =boolean,
                isBuildingEnabled = true,
                maxZoomPreference = 22f, minZoomPreference = 5f)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                mapToolbarEnabled = false)
        )
    }
    GoogleMap(
        properties = mapProperties,
        uiSettings = mapUiSettings,
        onMapClick = {
            ltng->
            run {
                println(incompleteGeoAlarm)
                viewModel.update(newCenter = ltng)

            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        cameraPositionState = cameraPositionState,

    )

    {
        if (polygons.first().isDebugging && polygons.first().points.isNotEmpty()){
            println("What the actual fuck")
            Polyline(

            points = List(polygons.first().points.size){
                LatLng(polygons.first().points[it].first(),polygons.first().points[it].last())
            }
//                points = arrayListOf(LatLng(-0.3979564870698612, 36.97258930653334),
//                    LatLng(-0.40210073426380427, 36.95871591567993)
//                ) ,

                )
        }

        polygons.forEach{
            polygon ->
            if (polygon.points.isNotEmpty()&& !polygon.isDebugging ){
                println("This happens")
                Polygon(
                    points = List(polygon.points.size){LatLng(polygon.points[it].first(),polygon.points[it].last())},
                    clickable = false,
                    fillColor = Color.Transparent,
                    geodesic = false,
                    holes = emptyList(),
                    strokeColor = Color.Blue,
                    strokeJointType = JointType.DEFAULT,
                    strokePattern = null,
                    strokeWidth = 10f,
                    tag = "Test01",
                    visible = true,
                    zIndex = 0f,
                    onClick = {  }
                )
            }

        }
        circles.forEach{
            circle ->
            if (circle.points.isNotEmpty()&& circle.iscircle){
                Circle(
                    center = LatLng(circle.points.first().first(),circle.points.first().last()),
                    clickable = false,
                    fillColor =Color.Transparent,
                    radius = circleR.toDouble(),
                    strokeColor = Color.Blue,
                    strokePattern = null,
                    strokeWidth = 10f,
                    tag = "Test0.0",
                    visible = true,
                    zIndex = 0f,
                    onClick = {  }
                )
            }

        }
        createdCircles.forEach{
            crcl-> Circle(
                center = LatLng(crcl.points.first().first(),crcl.points.first().last()),
                clickable = true,
                fillColor = Color.Transparent,
                radius = crcl.radius,
                strokeColor = Color.Black,
                strokeWidth = 10f,
                tag = crcl.id0,
                visible = true,
                zIndex = 5f,
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = crcl.note
                        )
                    }

                }
            )
        }
        createdPoly.forEach{
            ply -> Polygon(
                points = List(ply.points.size){LatLng(ply.points[it].first(),ply.points[it].last())},
                clickable = true,
                fillColor = Color.Transparent,
                geodesic = false,
                strokeColor = Color.Black,
                tag = ply.id0,
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = ply.note
                        )
                    }
                }
            )
        }
//        Marker(
//            state = singaporeMarkerState,
//            title = "Singapore",
//            snippet = "Marker in Singapore"
//        )
    }
}
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun  GetPermissions(
    application: Application,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    circleR: Float,
    createdAlarm: List<GeoAlarm>,
    innerPaddingValues: PaddingValues

){
    val requiredPermissions = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )
    if (!requiredPermissions.allPermissionsGranted){

        BasicAlertDialog(
            onDismissRequest = {

            }
        ) {
            Surface {
                Column {
                    if (!requiredPermissions.permissions[0].status.isGranted){
                        Column {
                            Icon(
                                Icons.Rounded.Place,
                                contentDescription = "locationIcon",
                            )
                            Text(text = "The app Needs access to your location to funtion properly")
                            Button(
                                onClick = { requiredPermissions.permissions[0].launchPermissionRequest() }
                            ) {
                                Text(text = "Grant Permission")
                            }
                        }
                    }
                    if (!requiredPermissions.permissions[1].status.isGranted){
                        Column {
                            Icon(
                                Icons.Rounded.Place,
                                contentDescription = "locationIcon",
                            )
                            Text(text = "The app Needs access to your location in the background to funtion when the app is closed")
                            Button(
                                onClick = { requiredPermissions.permissions[1].launchPermissionRequest() }
                            ) {
                                Text(text = "Grant Permission")
                            }
                        }
                    }
                    if (!requiredPermissions.permissions[2].status.isGranted){
                        Column {
                            Icon(
                                Icons.Rounded.Email,
                                contentDescription = "locationIcon",
                            )
                            Text(text = "The app Needs to send you prompt notifications!")
                            Button(
                                onClick = { requiredPermissions.permissions[2].launchPermissionRequest() }
                            ) {
                                Text(text = "Grant Permission")
                            }
                        }
                    }

                }
            }
        }

    }else
         getCurrentLoc(
             boolean = true,
             application,
             coroutineScope = coroutineScope,
             snackbarHostState = snackbarHostState,
             circleR = circleR,
             createdAlarm = createdAlarm,
             innerPaddingValues = innerPaddingValues
             )
}

suspend fun getCurrentLocationSuspend(context: Context): Location? =
    suspendCancellableCoroutine { continuation ->
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            continuation.resume(null) { cause, _, _ -> }
            return@suspendCancellableCoroutine
        }

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                continuation.resume(location) {}
            }
            .addOnFailureListener {
                continuation.resume(null) { cause, _, _ -> }
            }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addNoteDialog(unit:Unit){
    var text by
    rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    BasicAlertDialog(
        properties = DialogProperties(
            dismissOnClickOutside = false
        ),
//        modifier = Modifier.background(Color.Black),
        onDismissRequest = { }
    ) {
        Card() {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(
                    modifier = Modifier.padding(all = 20.dp),
                    contentDescription = "Logo",
                    painter = painterResource(R.drawable.logo_tr))
                Text("Add the note ")

                OutlinedTextField(
                    modifier = Modifier.padding(all = 10.dp),
                    value = text,

                    onValueChange = {
                        newval-> text = newval
                    },
                    label = { Text("Note") }
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            unit.run {}
                        }
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        modifier = Modifier.padding(start = 80.dp),
                        onClick = {}
                    ) {
                        Text("Finish")
                    }
                }

            }
        }


    }
}
///To show the message of the lockalarm
@Composable
fun ShowAlertDialog(
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Alert") },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
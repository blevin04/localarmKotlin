package com.example.geomap0

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.util.trace
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.geomap0.MainActivity
import com.example.geomap0.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "onReceive: damn")
        if (context == null ){
            Log.e("Geofence","Context  is null")
            return
        }
        if ( intent == null){
            Log.e("Geofence","intent is null")
            return
        }
        val event = GeofencingEvent.fromIntent(intent)
        if (event != null) {
            if (event.hasError()) {
//                Log.e("GeofenceReceiver", "Error: ${event.errorCode}")
                return
            }
        }

        Thread{
            val transition = event?.geofenceTransition
            val ids = event?.triggeringGeofences?.map { it.requestId }
            Log.e("Id","${event?.triggeringGeofences?.first()}:${intent.extras}:${event?.triggeringGeofences?.size}:${event?.triggeringGeofences?.last()?.radius}")
            val dd = ids?.let { backEndStuff(application = context).getstufById(it.first()) }
            if (dd == null){
                Log.e("Database","The id is not contained in the database")
                return@Thread
            }
            val note = dd.note
            when (transition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
//                Log.d("Geofence", "Entered: $ids")
                    sendNotification(
                        context = context,
                        title = "Entered Location",
                        body = note.toString()
                    )
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
//                Log.d("Geofence", "Exited: $ids")
                    sendNotification(
                        context = context,
                        title = "Exited Location",
                        body = note.toString(),
                    )
                }
            }
        }.start()


    }
}

fun sendNotification(title:String,body:String,context: Context){
    var builder = NotificationCompat.Builder(context, R.string.CHANNEL_ID.toString())
        .setSmallIcon(R.drawable.logo_tr)
        .setContentTitle(title)
        .setContentText(body)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    if (ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) != PackageManager.PERMISSION_GRANTED
        ){
        return
    }
    with(NotificationManagerCompat.from(context)){
        notify(
            1101,
            builder.build()
        )
    }
}


package com.ui.currentlocation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.CameraAnimatorOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager

class MapBoxActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading_map_box)
        mapView = findViewById(R.id.mapView)

        Handler(Looper.getMainLooper()).postDelayed({
            Location.location_fun(this@MapBoxActivity)


        }, 500)
        Handler(Looper.getMainLooper()).postDelayed({
            Location.location_fun(this@MapBoxActivity)
            onMapReady()

        }, 2000)

        onMapReady()

    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(Location.long, Location.let))
                .zoom(15.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(Style.SATELLITE) {
            animateCameraDelayed()

        }
        // Create an instance of the Annotation API and get the CircleAnnotationManager.
        val annotationApi = mapView.annotations
        val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
        // Set options for the resulting circle layer.
        val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
            // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(Location.long, Location.let))
            // Style the circle that will be added to the map.
            .withCircleRadius(8.0)
            .withCircleColor("#0C8EF1")
            .withCircleStrokeWidth(2.0)
            .withCircleStrokeColor("#ffffff")
            // Add the resulting circle to the map.
        circleAnnotationManager.create(circleAnnotationOptions)


    }

    //Custom animation
    private fun animateCameraDelayed() {
        mapView.camera.apply {

            val bearing =
                createBearingAnimator(CameraAnimatorOptions.cameraAnimatorOptions(-40.0)) {
                    duration = 4000
                    interpolator = AccelerateDecelerateInterpolator()
                }
            val zoom = createZoomAnimator(CameraAnimatorOptions.cameraAnimatorOptions(15.0) {
                startValue(30.0)
            }) {
                duration = 4000
                interpolator = AccelerateDecelerateInterpolator()
            }
            val pitch = createPitchAnimator(
                CameraAnimatorOptions.cameraAnimatorOptions(15.0) {
                    startValue(0.0)
                }

            ) {
                duration = 4000
                interpolator = AccelerateDecelerateInterpolator()
            }
            playAnimatorsSequentially(zoom, pitch, bearing)
        }
    }
}
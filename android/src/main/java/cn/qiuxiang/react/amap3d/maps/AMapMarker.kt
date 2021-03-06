package cn.qiuxiang.react.amap3d.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.facebook.react.views.view.ReactViewGroup

class AMapMarker(context: Context) : ReactViewGroup(context), AMapOverlay {
    companion object {
        private val COLORS = mapOf(
                "AZURE" to BitmapDescriptorFactory.HUE_AZURE,
                "BLUE" to BitmapDescriptorFactory.HUE_BLUE,
                "CYAN" to BitmapDescriptorFactory.HUE_CYAN,
                "GREEN" to BitmapDescriptorFactory.HUE_GREEN,
                "MAGENTA" to BitmapDescriptorFactory.HUE_MAGENTA,
                "ORANGE" to BitmapDescriptorFactory.HUE_ORANGE,
                "RED" to BitmapDescriptorFactory.HUE_RED,
                "ROSE" to BitmapDescriptorFactory.HUE_ROSE,
                "VIOLET" to BitmapDescriptorFactory.HUE_VIOLET,
                "YELLOW" to BitmapDescriptorFactory.HUE_YELLOW
        )
    }

    private var bitmapDescriptor: BitmapDescriptor? = null
    var infoWindow: AMapInfoWindow? = null

    var infoWindowEnabled: Boolean = true
        set(value) {
            field = value
            marker?.isInfoWindowEnable = value
        }

    var marker: Marker? = null
        private set

    var position: LatLng? = null
        set(value) {
            field = value
            marker?.position = value
        }

    var zIndex: Float = 0.0f
        set(value) {
            field = value
            marker?.zIndex = value
        }

    var title = ""
        set(value) {
            field = value
            marker?.title = value
        }

    var snippet = ""
        set(value) {
            field = value
            marker?.snippet = value
        }

    var flat: Boolean = false
        set(value) {
            field = value
            marker?.isFlat = value
        }

    var opacity: Float = 1f
        set(value) {
            field = value
            marker?.alpha = value
        }

    var draggable: Boolean = false
        set(value) {
            field = value
            marker?.isDraggable = value
        }

    var clickable_: Boolean = true
        set(value) {
            field = value
            marker?.isClickable = value
        }

    var active: Boolean = false
        set(value) {
            field = value
            if (value) {
                marker?.showInfoWindow()
            } else {
                marker?.hideInfoWindow()
            }
        }

    var icon: AMapMarkerIcon? = null
        set(value) {
            field = value
            value?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateIcon() }
        }

    override fun add(map: AMap) {
        marker = map.addMarker(MarkerOptions()
                .setFlat(flat)
                .icon(bitmapDescriptor)
                .alpha(opacity)
                .draggable(draggable)
                .position(position)
                .title(title)
                .infoWindowEnable(infoWindowEnabled)
                .snippet(snippet)
                .zIndex(zIndex))

        if (active) {
            marker?.showInfoWindow()
        } else {
            marker?.hideInfoWindow()
        }

        marker?.isClickable = this.clickable_
    }

    override fun remove() {
        marker?.destroy()
    }

    fun setIconColor(icon: String) {
        bitmapDescriptor = COLORS[icon.toUpperCase()]?.let {
            BitmapDescriptorFactory.defaultMarker(it)
        }
        marker?.setIcon(bitmapDescriptor)
    }

    fun updateIcon() {
        icon?.let {
            val bitmap = Bitmap.createBitmap(
                    it.width, it.height, Bitmap.Config.ARGB_8888)
            it.draw(Canvas(bitmap))
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
            marker?.setIcon(bitmapDescriptor)
        }
    }

    fun setImage(name: String) {
        val drawable = context.resources.getIdentifier(name, "drawable", context.packageName)
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(drawable)
        marker?.setIcon(bitmapDescriptor)
    }
}

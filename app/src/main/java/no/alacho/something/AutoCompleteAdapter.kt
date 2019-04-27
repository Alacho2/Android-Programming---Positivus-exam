package no.alacho.something

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.predict_item.view.*

class AutoCompleteAdapter(var placesClient: PlacesClient, var mMap: GoogleMap, var callback: () -> Unit) : RecyclerView.Adapter<AutoCompleteAdapter.AutoCompleteHolder>() {

    var predictionList: List<AutocompletePrediction> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteHolder {
        return AutoCompleteHolder(
          LayoutInflater.from(parent.context)
            .inflate(R.layout.predict_item, parent, false), placesClient, mMap, callback
        )
    }

    override fun getItemCount(): Int {
        return predictionList.count()
    }

    override fun onBindViewHolder(holder: AutoCompleteHolder, position: Int) {
        holder.bindItem(predictionList[position])
    }


    class AutoCompleteHolder(var view: View, var placesClient: PlacesClient, var mMap: GoogleMap, var callback: () -> Unit) : RecyclerView.ViewHolder(view) {
      var placesId: String = ""
        init {
          view.setOnClickListener {
            fetchPlace(placesId)
            callback()
          }
        }

        fun bindItem(prediction: AutocompletePrediction) {
          view.descriptionTitle.text = prediction.getPrimaryText(null)
          placesId = prediction.placeId
        }

        fun fetchPlace(placeId: String): Place? {
          val list = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES)
          var place: Place? = null
          val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, list)
                .build()

            placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { resp ->
                place = resp.place
                  when(place!!.types!![0]) {
                    Place.Type.COUNTRY -> {
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place?.latLng, 5f))
                    }
                    Place.Type.LOCALITY -> {
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place?.latLng, 12f))
                    }
                    else -> {
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place?.latLng, 17f))
                    }
                  }
                mMap.addMarker(MarkerOptions().position(place?.latLng!!))
            }.addOnFailureListener { err ->
                Log.d("Error", err.toString())
            }
            return place
        }
    }
}
package no.alacho.something.fragments

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_maps.*
import no.alacho.something.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MapFragment : Fragment(), OnMapReadyCallback {

  private lateinit var mMap: GoogleMap
  private lateinit var search: EditText
  private lateinit var placesClient: PlacesClient
  private lateinit var layoutManager: LinearLayoutManager
  private lateinit var adapter: AutoCompleteAdapter
  private lateinit var mapFragment: SupportMapFragment

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.activity_maps, container, false)

    search = rootView.findViewById(R.id.search)
    search.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {}

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(activity!!.findViewById<RecyclerView>(R.id.autoCompleteRecycler).visibility == View.GONE){
          activity!!.findViewById<RecyclerView>(R.id.autoCompleteRecycler).visibility = View.VISIBLE
        }
        findPrediction(search.text.toString())
      }
    })

    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    mapFragment = childFragmentManager
      .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    autoCompleteRecycler.layoutManager = layoutManager
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    Places.initialize(context, getString(R.string.google_maps_key))
    placesClient = Places.createClient(context)

    layoutManager = LinearLayoutManager(context)
  }

  /* We just need to ready the map, not do anything */
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    adapter = AutoCompleteAdapter(placesClient, mMap, callback)
    autoCompleteRecycler.adapter = adapter
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
  }

  fun findPrediction(searchQuery: String){
    val request = FindAutocompletePredictionsRequest.builder()
      .setQuery(searchQuery)
      .build()

    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
      if(response.autocompletePredictions.size >= 1){
        adapter.predictionList = response.autocompletePredictions
      } else {
        adapter.predictionList = listOf()
      }
      adapter.notifyDataSetChanged()
    }.addOnFailureListener{
      Log.d("Exception", it.toString())
    }
  }

  val callback: () -> Unit = {
    activity!!.findViewById<RecyclerView>(R.id.autoCompleteRecycler).visibility = View.GONE
  }

  @AfterPermissionGranted(PermissionRequests.REQUEST_FINE_LOCATION_CODE)
  fun runCode(){
    if(EasyPermissions.hasPermissions(activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)){
      findPrediction("Oslo")
    } else {
      EasyPermissions.requestPermissions(this,
        "We wanna get your location",
        PermissionRequests.REQUEST_FINE_LOCATION_CODE,
        Manifest.permission.ACCESS_FINE_LOCATION)
    }
  }

  fun fetchPlace(placesClient: PlacesClient, mMap: GoogleMap, placeId: String): Place? {
    val list = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    var place: Place? = null
    val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, list)
      .build()

    placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { resp ->
      place = resp.place
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place?.latLng, 12f))
    }.addOnFailureListener { err ->
      Log.d("Error", err.toString())
    }
    return place
  }
}
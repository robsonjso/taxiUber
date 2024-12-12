package com.example.taxi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

private var originalTrips: List<Trip> = listOf() // Popule com os dados iniciais

class TripHistoryAdapter(private var trips: List<Trip>) : RecyclerView.Adapter<TripHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDate: TextView = view.findViewById(R.id.tv_date)
        private val tvDriverName: TextView = view.findViewById(R.id.tv_driver_name)
        private val tvOrigin: TextView = view.findViewById(R.id.tv_origin)
        private val tvDestination: TextView = view.findViewById(R.id.tv_destination)
        private val tvDistance: TextView = view.findViewById(R.id.tv_distance)
        private val tvDuration: TextView = view.findViewById(R.id.tv_duration)
        private val tvValue: TextView = view.findViewById(R.id.tv_value)


        fun bind(trip: Trip) {
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

            tvDate.text = trip.date.ifEmpty { "Data não disponível" }
            tvDriverName.text = "Motorista: ${trip.driverName.ifEmpty { "Não informado" }}"
            tvOrigin.text = "Origem: ${trip.origin.ifEmpty { "Não informada" }}"
            tvDestination.text = "Destino: ${trip.destination.ifEmpty { "Não informado" }}"
            tvDistance.text = if (trip.distance > 0) {
                "Distância: %.2f km".format(trip.distance)
            } else {
                "Distância não informada"
            }
            tvDuration.text = trip.duration.ifEmpty { "Duração não informada" }
            tvValue.text = "Valor: ${currencyFormat.format(trip.value)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount(): Int = trips.size

    fun updateTrips(newTrips: List<Trip>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}
data class Trip(
    val date: String,
    val driverName: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val value: Double,

    )
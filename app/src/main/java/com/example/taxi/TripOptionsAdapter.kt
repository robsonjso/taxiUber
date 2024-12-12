package com.example.taxi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TripOptionsAdapter(
    private var options: List<MainActivity.TripOption>,
    private val onOptionSelected: (MainActivity.TripOption) -> Unit
) : RecyclerView.Adapter<TripOptionsAdapter.ViewHolder>() {

    private var selectedDriverId: Int? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnChoose: Button = view.findViewById(R.id.btn_choose)
        val tvName: TextView = view.findViewById(R.id.tv_driver_name)
        val tvDescription: TextView = view.findViewById(R.id.tv_driver_description)
        val tvVehicle: TextView = view.findViewById(R.id.tv_vehicle)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)
        val tvPrice: TextView = view.findViewById(R.id.tv_price)


        fun bind(option: MainActivity.TripOption) {
            tvName.text = option.name
            tvDescription.text = option.description
            tvVehicle.text = "Veículo: ${option.vehicle}"
            tvRating.text = "Nota: ${String.format("%.1f", option.rating)}"
            tvPrice.text = "Preço: R$ ${String.format("%.2f", option.value)}"

            // Verifica se o motorista foi selecionado
            if (selectedDriverId == option.id) {
                btnChoose.text = "Processando..."
                btnChoose.isEnabled = false
            } else {
                btnChoose.text = "Escolher"
                btnChoose.isEnabled = selectedDriverId == null // Desabilita se outro foi selecionado
            }

            btnChoose.setOnClickListener {
                // Atualiza o estado e notifica mudanças no RecyclerView
                selectedDriverId = option.id
                notifyDataSetChanged() // Atualiza a lista para refletir o estado
                onOptionSelected(option)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_options, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

    fun updateOptions(newOptions: List<MainActivity.TripOption>) {
        options = newOptions
        notifyDataSetChanged()
    }
    fun resetSelection() {
        selectedDriverId = null
        notifyDataSetChanged()
    }
}
package com.example.taxi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class HistoryScreen : AppCompatActivity() {

    private lateinit var etUserId: EditText
    private lateinit var spDriverSelector: Spinner
    private lateinit var btnApplyFilter: Button
    private lateinit var rvTripHistory: RecyclerView
    private lateinit var tripHistoryAdapter: TripHistoryAdapter
    private var drivers: List<String> = listOf("Todos") // Padrão inicial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_screen)

        // Receber lista de motoristas do Intent
        drivers = intent.getStringArrayListExtra("drivers")?.toList() ?: listOf("Todos")

        // Inicializar componentes
        etUserId = findViewById(R.id.et_user_id)
        spDriverSelector = findViewById(R.id.sp_driver_selector)
        btnApplyFilter = findViewById(R.id.btn_apply_filter)
        rvTripHistory = findViewById(R.id.rv_trip_history)

        // Configurar Spinner dinamicamente
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drivers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDriverSelector.adapter = adapter

        // Configurar RecyclerView
        rvTripHistory.layoutManager = LinearLayoutManager(this)
        tripHistoryAdapter = TripHistoryAdapter(emptyList())
        rvTripHistory.adapter = tripHistoryAdapter

        // Carregar histórico salvo no SharedPreferences
        loadLocalHistory()


        btnApplyFilter.setOnClickListener {
            applyFilter()
        }
    }


    private fun applyFilter() {
        val userId = etUserId.text.toString().trim()
        val driver = spDriverSelector.selectedItem.toString()

        if (userId.isEmpty()) {
            Toast.makeText(this, "Por favor, informe o ID do usuário.", Toast.LENGTH_SHORT).show()
            return
        }
        // Filtrar viagens baseadas no motorista selecionado
        if (driver == "Todos") {
            fetchTripHistory(userId, "")
        } else {
            fetchTripHistory(userId, driver)
        }
    }

    private fun fetchTripHistory(userId: String, driver: String) {
        // Define a URL com base no motorista selecionado
        val url = if (driver == "Todos") {
            "https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws/ride/$userId"
        } else {
            "https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws/ride/$userId?driver_id=$driver"
        }

        val request = Request.Builder()
            .url(url)
            .get() // Define que é uma requisição GET
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Tratamento para erros de conexão
                runOnUiThread {
                    Toast.makeText(this@HistoryScreen, "Erro ao conectar com a API: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("FETCH_HISTORY_ERROR", "Erro na conexão com a API: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    try {
                        // Processa as viagens retornadas pela API
                        val trips = parseTrips(responseBody)

                        runOnUiThread {
                            if (trips.isEmpty()) {
                                // Mensagem caso nenhuma viagem seja encontrada
                                Toast.makeText(this@HistoryScreen, "Nenhuma viagem encontrada para o filtro.", Toast.LENGTH_SHORT).show()
                            } else {
                                // Atualiza o RecyclerView com as viagens filtradas
                                tripHistoryAdapter.updateTrips(trips)
                                Toast.makeText(this@HistoryScreen, "Viagens carregadas com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        // Tratamento para erros ao processar a resposta
                        runOnUiThread {
                            Toast.makeText(this@HistoryScreen, "Erro ao processar os dados da API.", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("FETCH_HISTORY_ERROR", "Erro ao processar os dados: ${e.message}")
                    }
                } else {
                    // Tratamento para erros na resposta da API
                    runOnUiThread {
                        Toast.makeText(this@HistoryScreen, "Erro ao buscar histórico: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("FETCH_HISTORY_ERROR", "Erro na API: ${response.code}, ${response.message}")
                }
            }
        })
    }


    private fun extractDriversFromTrips(trips: List<Trip>): List<String> {
        // Extrai nomes únicos dos motoristas
        val drivers = trips.map { it.driverName }.distinct()
        return listOf("Todos") + drivers // Adiciona a opção "Todos" no início
    }

    private fun updateDriverSelector(drivers: List<String>) {
        // Atualiza os motoristas no Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drivers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDriverSelector.adapter = adapter

        // Configura o listener para o Spinner
        spDriverSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDriver = drivers[position]
                val userId = etUserId.text.toString().trim()

                if (userId.isNotEmpty()) {
                    // Filtra os dados quando o motorista é selecionado
                    fetchTripHistory(userId, selectedDriver)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nenhuma ação necessária
            }
        }
    }

    private fun loadLocalHistory() {
        val sharedPrefs = getSharedPreferences("trip_history", MODE_PRIVATE)
        val tripsJson = sharedPrefs.getString("trips", "[]") // Padrão: lista vazia
        Log.d("TRIPS_SAVED", tripsJson ?: "Nenhuma viagem salva.")
        try {
            val tripArray = JSONArray(tripsJson)
            val trips = mutableListOf<Trip>()

            for (i in 0 until tripArray.length()) {
                val tripJson = tripArray.getJSONObject(i)
                trips.add(
                    Trip(
                        date = tripJson.getString("date"),
                        driverName = tripJson.getString("driverName"),
                        origin = tripJson.getString("origin"),
                        destination = tripJson.getString("destination"),
                        distance = tripJson.getDouble("distance"),
                        duration = tripJson.getString("duration"),
                        value = tripJson.getDouble("value")
                    )
                )
            }

            tripHistoryAdapter.updateTrips(trips)

            // Atualiza o Spinner com motoristas disponíveis
            val availableDrivers = extractDriversFromTrips(trips)
            updateDriverSelector(availableDrivers)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao carregar histórico local.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseTrips(responseBody: String): List<Trip> {
        val trips = mutableListOf<Trip>()

        try {
            val jsonResponse = JSONObject(responseBody)
            val rides = jsonResponse.optJSONArray("rides") ?: JSONArray() // Garante que nunca seja nulo

            for (i in 0 until rides.length()) {
                val ride = rides.optJSONObject(i) ?: continue // Ignora objetos nulos

                val driver = ride.optJSONObject("driver") // Lida com dados do motorista
                val trip = Trip(
                    date = ride.optString("date", "Data não disponível"),
                    driverName = driver?.optString("name", "Motorista desconhecido") ?: "Motorista desconhecido",
                    origin = ride.optString("origin", "Origem não informada"),
                    destination = ride.optString("destination", "Destino não informado"),
                    distance = ride.optDouble("distance", 0.0), // Distância padrão 0.0
                    duration = ride.optString("duration", "Duração desconhecida"),
                    value = ride.optDouble("value", 0.0) // Valor padrão 0.0
                )

                // Ignorar viagens com dados críticos ausentes
                if (trip.driverName == "Motorista desconhecido" ||
                    trip.origin == "Origem não informada" ||
                    trip.destination == "Destino não informado") {
                    Log.w("PARSE_TRIPS_WARNING", "Viagem ignorada devido a dados ausentes: $ride")
                    continue
                }

                trips.add(trip) // Adiciona a viagem à lista
            }
        } catch (e: Exception) {
            Log.e("PARSE_TRIPS_ERROR", "Erro ao processar JSON: ${e.message}")
        }

        return trips
    }

}

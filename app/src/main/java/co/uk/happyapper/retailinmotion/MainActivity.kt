package co.uk.happyapper.retailinmotion

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.data.observe(this, { data ->
            when (data) {
                is LuasUI.LuasError -> {
                    Toast.makeText(applicationContext, data.message, Toast.LENGTH_SHORT).show()
                }
                is LuasUI.LuasLoading -> {
                    findViewById<ProgressBar>(R.id.progress).isVisible = true
                }
                is LuasUI.LuasData -> {
                    showData(data.data)
                }
            }
        })

        findViewById<Button>(R.id.refresh).setOnClickListener {
            it.isVisible = false
            viewModel.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun showData(ui: UIData) {
        findViewById<Button>(R.id.refresh).isVisible = true
        findViewById<ProgressBar>(R.id.progress).isVisible = false
        findViewById<TextView>(R.id.station).text = ui.station
        ui.destinations?.let {
            findViewById<RecyclerView>(R.id.list_recycler).apply {
                adapter = TramItemAdapter(ui.destinations, this@MainActivity)
            }
        }
    }
}
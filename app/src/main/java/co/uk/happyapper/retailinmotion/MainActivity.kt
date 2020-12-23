package co.uk.happyapper.retailinmotion

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import co.uk.happyapper.retailinmotion.repo.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.data.observe(this, { status ->
            when (status.status) {
                Resource.Status.ERROR -> {
                    Toast.makeText(applicationContext, "There was an error", Toast.LENGTH_SHORT)
                        .show()
                }
                Resource.Status.LOADING -> {
                    findViewById<ProgressBar>(R.id.progress).isVisible = true
                }
                Resource.Status.SUCCESS -> {
                    status.data?.let { data ->
                        viewModel.gotData(data)
                    }
                }
            }
        })

        viewModel.info.observe(this, { data ->
            showData(data)
        })

        findViewById<Button>(R.id.refresh).setOnClickListener {
            it.isVisible = false
            viewModel.refresh()
        }
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
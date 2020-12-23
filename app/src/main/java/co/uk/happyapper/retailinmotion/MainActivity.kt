package co.uk.happyapper.retailinmotion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import co.uk.happyapper.retailinmotion.repo.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.info.observe(this, { destinations ->
            showData(destinations)
        })

        findViewById<Button>(R.id.refresh).setOnClickListener {
            viewModel.refresh()
        }
        viewModel.refresh()
    }

    private fun showData(destinations: List<TramDestination>){
        Log.i("destinations",destinations.toString())
    }
}
package com.example.lemonade.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.lemonade.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        // Update UI elements
                        binding.progress.isVisible = it.loading
                        studentAdapter = StudentAdapter(this@MainActivity, it.students)
                        binding.listView.adapter = studentAdapter

                        Toast.makeText(this@MainActivity, "${it.students.size}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                launch {
                    viewModel.uiEvent.collect {
                        Snackbar.make(binding.listView, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
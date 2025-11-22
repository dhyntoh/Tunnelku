package com.example.netmodapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.netmodapp.adapters.HeaderAdapter
import com.example.netmodapp.models.Header
import com.example.netmodapp.databinding.ActivityMainBinding
import com.example.netmodapp.utils.NetworkUtils
import org.json.JSONObject
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerAdapter: HeaderAdapter
    private val headers = mutableListOf<Header>()
    private var requestStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        setupInitialHeader()
    }

    private fun setupRecyclerView() {
        headerAdapter = HeaderAdapter(headers) { position ->
            // Callback when header is removed
        }
        binding.recyclerHeaders.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = headerAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnAddHeader.setOnClickListener {
            headerAdapter.addHeader()
        }

        binding.btnSend.setOnClickListener {
            makeHttpRequest()
        }
    }

    private fun setupInitialHeader() {
        // Add an empty header by default
        headerAdapter.addHeader()
    }

    private fun makeHttpRequest() {
        val url = binding.etUrl.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate URL format
        if (!isValidUrl(url)) {
            Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show()
            return
        }

        // Get selected method
        val method = when {
            binding.radioGet.isChecked -> "GET"
            binding.radioPost.isChecked -> "POST"
            binding.radioPut.isChecked -> "PUT"
            binding.radioDelete.isChecked -> "DELETE"
            else -> "GET"
        }

        val body = if (method in listOf("POST", "PUT")) {
            binding.etBody.text.toString().trim()
        } else {
            null
        }

        // Show loading state
        setLoadingState(true)
        
        // Record start time for response time calculation
        requestStartTime = System.currentTimeMillis()

        NetworkUtils.makeRequest(
            url = url,
            method = method,
            headers = headers.filter { it.key.isNotEmpty() && it.value.isNotEmpty() },
            body = body
        ) { responseData ->
            runOnUiThread {
                setLoadingState(false)
                
                // Calculate response time
                val responseTime = System.currentTimeMillis() - requestStartTime
                
                // Update UI with response
                updateResponseUI(responseData, responseTime)
            }
        }
    }

    private fun updateResponseUI(responseData: NetworkUtils.ResponseData, responseTime: Long) {
        // Update status
        val statusText = if (responseData.success) {
            "Status: ${responseData.status} (Success)"
        } else {
            "Status: ${responseData.status} (Error)"
        }
        binding.tvStatus.text = statusText

        // Update time
        val timeText = "Time: ${formatTime(responseTime)}"
        binding.tvTime.text = timeText

        // Update size
        val sizeText = "Size: ${formatSize(responseData.size)}"
        binding.tvSize.text = sizeText

        // Update response body
        binding.tvResponse.text = try {
            // Pretty print JSON if possible
            val json = JSONObject(responseData.data)
            json.toString(2)
        } catch (e: Exception) {
            // If not JSON, display as is
            responseData.data
        }
    }

    private fun setLoadingState(loading: Boolean) {
        if (loading) {
            binding.btnSend.text = "Loading..."
            binding.btnSend.isEnabled = false
            binding.tvResponse.text = "Sending request..."
        } else {
            binding.btnSend.text = getString(R.string.send_request)
            binding.btnSend.isEnabled = true
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            val uri = java.net.URI(url)
            uri.scheme != null && uri.host != null
        } catch (e: Exception) {
            false
        }
    }

    private fun formatTime(ms: Long): String {
        return if (ms < 1000) {
            "${ms}ms"
        } else {
            val seconds = ms / 1000.0
            String.format("%.2f", seconds) + "s"
        }
    }

    private fun formatSize(bytes: Int): String {
        val decimalFormat = DecimalFormat("#.##")
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${decimalFormat.format(bytes / 1024.0)} KB"
            else -> "${decimalFormat.format(bytes / (1024.0 * 1024.0))} MB"
        }
    }
}
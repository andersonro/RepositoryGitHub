package com.example.basicproject.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.basicproject.R
import com.example.basicproject.core.*
import com.example.basicproject.databinding.ActivityMainBinding
import com.example.basicproject.presentation.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MainViewModel>()
    private val adapter by lazy { RepositoryListAdapter() }
    private val dialog by lazy { createProgressDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvRepoItem.adapter = adapter
        setListernes()
        if (getSharedPrefUser().isNotEmpty()) {
            setCacheUser(getSharedPrefUser())
        }
    }

    private fun setCacheUser(user: String) {
        binding.tilUser.apply {
            visibility = View.INVISIBLE
            text = ""
        }
        binding.ivSearch.visibility = View.INVISIBLE
        binding.ivUser.visibility = View.VISIBLE
        binding.tvUser.apply {
            visibility = View.VISIBLE
            text = user
        }
        binding.ivGoto.visibility = View.VISIBLE

        setObserve(user)
    }


    private fun setListernes() {

        binding.ivSearch.setOnClickListener {

            if (hasInternet(this)) {
                val user = binding.tilUser.text.trim()
                if (user.isNotEmpty() || user.isNotBlank()) {
                    setObserve(user.toString())
                    binding.root.hideSoftKeyboard()
                } else {
                    Toast.makeText(this, getString(R.string.msg_user_empty), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Você está sem conexão com a internet", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.ivGoto.setOnClickListener {
            if (getSharedPrefUser().isNotEmpty()) {
                saveSharedPrefUser("")
            }
            fieldsDefault()
        }

        adapter.repositoryShare = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, it.name)
                putExtra(Intent.EXTRA_TEXT, it.htmlUrl)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


        adapter.repositoryCardView = {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it.htmlUrl)
                )
            )
        }

    }

    private fun fieldsDefault() {
        adapter.submitList(emptyList())
        binding.tilUser.visibility = View.VISIBLE
        binding.ivSearch.visibility = View.VISIBLE
        binding.ivUser.visibility = View.INVISIBLE
        binding.tvUser.apply {
            visibility = View.INVISIBLE
            text = ""
        }
        binding.ivGoto.visibility = View.INVISIBLE

    }

    private fun setObserve(user: String) {

        viewModel.getRepositoriesList(user)
        viewModel.repos.observe(this) {
            when (it) {
                MainViewModel.State.Loading -> {
                    adapter.submitList(emptyList())
                    dialog.show()
                }
                is MainViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.error.message)
                    }.show()
                }
                is MainViewModel.State.Success -> {
                    dialog.dismiss()
                    if (it.list.isNotEmpty()) {
                        if (getSharedPrefUser().isEmpty()) {
                            saveSharedPrefUser(user)
                        }
                        binding.rvRepoItem.visibility = View.VISIBLE
                        binding.tvEmptyRepository.visibility = View.INVISIBLE
                        adapter.submitList(it.list)
                    } else {
                        binding.rvRepoItem.visibility = View.INVISIBLE
                        binding.tvEmptyRepository.visibility = View.VISIBLE
                    }

                }
            }
        }

    }
}
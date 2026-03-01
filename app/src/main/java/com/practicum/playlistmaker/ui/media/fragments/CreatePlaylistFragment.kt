package com.practicum.playlistmaker.ui.media.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.ui.media.view_model.CreatePlaylistFragmentViewModel
import com.practicum.playlistmaker.ui.utils.Helper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

open class CreatePlaylistFragment: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    open val binding get() = _binding!!

    open val viewModel: CreatePlaylistFragmentViewModel by viewModel()

    private var textWatcher: TextWatcher? = null

    var uri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { showAlertDialog() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showAlertDialog()
            }
        } )

        binding.btnCreatePlaylist.setOnClickListener {
            val namePlaylist = binding.edtTitle.text.trim().toString()
            val description = binding.edtDescription.text.trim().toString()

            Toast.makeText(context, getString(R.string.playlist_created, namePlaylist), Toast.LENGTH_LONG).show()
            savePlaylist(namePlaylist, description);
            findNavController().navigateUp()
        }

        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int,p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                binding.btnCreatePlaylist.isEnabled = binding.edtTitle.length() > 0
            }
        }
        textWatcher?.let { binding.edtTitle.addTextChangedListener(it) }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
            if (uri != null) {
                Glide.with(requireContext())
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_album_image_placeholder_312)
                    .into(binding.imageView)
                this.uri = uri
            }
        }

        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showAlertDialog(){
        if(binding.edtTitle.text.isNotEmpty() || binding.edtDescription.text.isNotEmpty() || uri != null){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.alert_dialog_title))
                .setMessage(resources.getString(R.string.alert_dialog_message))
                .setNeutralButton(resources.getString(R.string.alert_dialog_btn_cancel)) { dialog, which -> }
                .setPositiveButton(resources.getString(R.string.alert_dialog_btn_positive)) { dialog, which -> findNavController().navigateUp() }
                .show()
        }
        else{
            findNavController().navigateUp()
        }
    }

    private fun savePlaylist(namePlaylist: String, description: String){
        var path: String? = saveImage(namePlaylist)
        viewModel.createPlaylist(namePlaylist, description, path)
    }

    fun saveImage(namePlaylist: String): String?{
        var path: String? = null
        if(uri != null){
            val filename = Helper.getFileNameFromUri(requireContext(), uri!!) ?: ""
            path = Helper.saveImage(requireActivity(), uri!!, namePlaylist, filename)
        }
        return path
    }

    override fun onDestroyView() {
        super.onDestroyView()

        textWatcher?.let { binding.edtTitle.removeTextChangedListener(it) }
        _binding = null;
    }
}
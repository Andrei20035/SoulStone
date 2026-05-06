package com.example.soulstone.data.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Copies an image from a temporary URI (e.g., from the photo picker)
     * to a permanent, private file in the app's internal storage.
     *
     * @param tempUri The temporary "content://" URI of the file to copy.
     * @return The permanent, stable "file://" URI string of the new file, or null on failure.
     */
    fun saveImageToInternalStorage(tempUri: Uri): String? {
        return try {
            // 1. Get an input stream from the temporary URI
            val inputStream = context.contentResolver.openInputStream(tempUri) ?: return null

            // 2. Create a unique file name
            val fileName = "${UUID.randomUUID()}.jpg"

            // 3. Get a path to your app's private "files" directory
            val file = File(context.filesDir, fileName)

            // 4. Copy the file
            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // 5. Return the permanent URI string for the new file
            Uri.fromFile(file).toString()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Deletes a file from internal storage.
     *
     * @param fileUriString The "file://" URI string of the file to delete.
     */
    fun deleteImageFromInternalStorage(fileUriString: String?) {
        if (fileUriString == null) return
        try {
            val file = File(Uri.parse(fileUriString).path ?: return)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
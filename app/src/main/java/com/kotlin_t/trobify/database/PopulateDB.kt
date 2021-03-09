package com.kotlin_t.trobify.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.logica.Usuario
import com.kotlin_t.trobify.presentacion.Constantes
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class PopulateDB(val database: AppDatabase, val context: Context) {
    fun populate() {
        Thread {
            //         Crear Usuarios
            database.usuarioDAO().insertAll(
                Usuario(
                    "-1",
                    "-1",
                    "-1",
                    "-1",
                    "-1",
                    "-1",
                    "-1",
                    null
                )
            )
            for (i in 0..5) {
                database.usuarioDAO()
                    .insertAll(
                        Usuario(
                            "${i}2345678E",
                            "username${i}",
                            "contraseña${i}",
                            "nombre${i}",
                            "appellido${i}",
                            "999888777",
                            "iban${i}",
                            null
                        )
                    )
            }

            // Crear Inmuebles
            for (i in 0..10) {
                createInmueble(i)
            }

            val listaInmuebles = database.inmuebleDAO().getAll()
            // Crear Favoritos
            for (i in 0..6) {
                database.favoritoDAO().insertAll(Favorito(listaInmuebles[i].inmuebleId, null))
            }

            for (i in 0 until listaInmuebles.size - 1) {
                processImageUris(
                    "https://source.unsplash.com/300x300/?building",
                    listaInmuebles[i].inmuebleId
                )
            }
        }.start()


    }

    private fun processImageUris(url: String, inmuebleId: Int) {
        var posterBitmap: Bitmap? = null
        posterBitmap = getBitmapFromURL(url)
        posterBitmap = processImage(posterBitmap!!)
        Log.e("AAAA", posterBitmap.toString())
        saveImage(posterBitmap!!, inmuebleId)
    }

    private fun createInmueble(i: Int) {
        var posterBitmap: Bitmap? = null
        posterBitmap = getBitmapFromURL("https://source.unsplash.com/300x300/?building")
        posterBitmap = processImage(posterBitmap!!)


        database.inmuebleDAO().insertAll(
            Inmueble(
                "12345678E",
                "direccion ${i}",
                i % 2 == 0,
                posterBitmap,
                null,
                i + 1,
                i * 100000,
                if (i % 2 == 0) Constantes.PISO else Constantes.CASA_CHALET,
                if (i % 2 == 0) Constantes.ALQUILER else Constantes.VENTA,
                (i + 1) * 100,
                i % 2 == 0,
                i + 1,
                i + 1,
                "Valencia",
                "Valencia",
                "Benimaclet",
                "España",
                39.489658 + i / 200,
                -0.422140 + i / 100,
                Constantes.BUEN_ESTADO,
                i % 2 == 0,
                10,
                "Direcion nº${i}, Municipio",
                "Subtitulo",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at venenatis diam, vitae tincidunt dolor. Vestibulum cursus tortor justo, vel commodo lacus porta nec. Vivamus in maximus risus. Mauris quis augue a quam euismod pharetra vitae non urna. Nunc varius enim pretium mauris placerat, at elementum felis sodales. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec et condimentum urna. Curabitur in congue eros.\n" +
                        "\n" +
                        "Cras accumsan nisi non bibendum suscipit. Integer vehicula hendrerit feugiat. Nunc tristique rhoncus erat, sollicitudin ornare quam sollicitudin sed. Nunc feugiat erat consequat luctus sollicitudin. Vestibulum a tempus nulla. Phasellus eu pulvinar diam, vitae dignissim mi. Aliquam ut tortor eleifend, consequat arcu quis, condimentum ex. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam interdum purus vel finibus tempor. Sed consectetur lacinia lacinia. Etiam nec dui ac mauris rutrum blandit.\n" +
                        "\n" +
                        "Curabitur cursus, diam vitae placerat accumsan, sapien lectus eleifend urna, at tristique velit erat eu odio. Curabitur non laoreet enim. Pellentesque eget semper ligula. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Ut et dignissim ante, eu blandit dolor. Cras tempus est nec ex volutpat accumsan. Suspendisse potenti. Pellentesque et nisl quis lacus rhoncus vehicula. Maecenas dignissim, ligula vitae suscipit maximus, tortor arcu sodales orci, quis ullamcorper mi odio in velit. Integer accumsan feugiat enim, eget consequat ante commodo et. Donec eu sapien mattis, mollis neque sit amet, dapibus quam. Vivamus in dictum eros, ac ultrices odio. Ut ut nunc ornare, volutpat nunc auctor, feugiat magna. Duis vulputate sem dolor, et consequat massa mollis nec.\n" +
                        "\n" +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac nisl auctor neque ornare rhoncus. Ut metus turpis, dictum elementum congue a, rutrum ac nisl. Curabitur posuere dolor eget ornare ornare. Duis consectetur, dui non dictum aliquet, nulla tortor elementum velit, et molestie diam arcu in mi. Phasellus commodo sem a diam sollicitudin luctus. Pellentesque volutpat neque et magna iaculis, et tempus odio faucibus. Aliquam eget convallis massa. Donec ligula ante, finibus iaculis bibendum ut, sagittis non tortor. Aliquam tellus sapien, tincidunt ut leo a, ornare lacinia quam. Vestibulum interdum accumsan lectus. Sed euismod tortor tellus, porta tempor dolor molestie nec. Mauris hendrerit lacus vitae velit bibendum, eu viverra dui malesuada. Proin aliquet mauris vel dolor efficitur, nec aliquet leo molestie. Praesent pretium mauris non tortor luctus blandit.\n" +
                        "\n" +
                        "Aliquam interdum volutpat vehicula. Morbi varius fringilla augue et egestas. In placerat, quam id ultrices auctor, sapien sapien gravida lorem, et porttitor nulla tellus a nisi. Quisque nec nisl massa. Maecenas sit amet mattis massa. Duis tincidunt pellentesque tortor vitae volutpat. Integer vel nisi et magna aliquam semper eu sed lectus. Proin bibendum augue ipsum, vel convallis mauris laoreet quis. Duis consequat odio vitae sodales lobortis. Praesent congue a tellus et vehicula. Sed sodales mauris ac ligula porta tempus. Nullam dapibus dictum lectus quis ornare. Sed eget lacinia libero."

            )
        )

    }

    private fun processImage(bitmap_: Bitmap): Bitmap {
        var bitmap =
            Bitmap.createScaledBitmap(bitmap_, 600, 600, true)
        return bitmap
    }


    private fun saveImage(bitmap: Bitmap, inmuebleId: Int) {
        val image = Foto(inmuebleId, bitmap)
        database.fotoDAO().insertAll(image)
        Log.e("SAVED", "SAVED")
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }
}
package com.kotlin_t.trobify.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.kotlin_t.trobify.logica.*
import com.kotlin_t.trobify.presentacion.Constantes
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


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
        saveImage(posterBitmap!!, inmuebleId)
    }

    private fun createInmueble(i: Int) {
        var posterBitmap: Bitmap? = null
        posterBitmap = getBitmapFromURL("https://source.unsplash.com/300x300/?building")
        posterBitmap = processImage(posterBitmap!!)
        var latitud = 39.489658 + (i.toDouble() / 20.0)
        var longitud = -0.422140 + (i.toDouble() / 20.0)
        var direcciones = listOf(
            "Piso en venta en Altea Pueblo",
            "Estudio en venta en calle Currica",
            "Estudio en venta en calle Currica s/n",
            "Estudio en venta en Zona de Mascarat",
            "Piso en venta en calle Camp Preciós",
            "Estudio en venta en calle Sorell",
            "Piso en venta en Llavador, 1",
            "Piso en venta en calle Metge Adolfo Quiles, 5",
            "Casa o chalet en venta en calle Forat",
            "Casa o chalet en venta en calle Forat, 23",
            "Piso en venta en calle Camp Preciós, 15",
            "Piso en venta en calle Ascar, 24"
        )
        val tipoOperacion = listOf(
            Constantes.ALQUILER,
            Constantes.VENTA,
            Constantes.ALQUILER_HABITACION,
            Constantes.INTERCAMBIO_VIVIENDA,
        )

        val tipoInmueble = listOf(
            Constantes.ATICO,
            Constantes.CASA_CHALET,
            Constantes.HABITACION,
            Constantes.INTERCAMBIO_VIVIENDA,
        )

        val estados = listOf(
            Constantes.NUEVA_CONSTRUCCION,
            Constantes.BUEN_ESTADO,
            Constantes.REFORMAR,
        )

        val descripciones = listOf(
            "Planta baja de 61m apto para comercio o almacén. Si se efectúa el cambio de uso pudiera convertir en precioso apartamento tipo loft, con 2 dormitorios. Da a la calle 1 puerta y 1 ventana. El fondo sin ventanas. Ubicación céntrica. Muy conveniente para personas de movilidad reducida. Buena inversión!",
            "Recientemente rehabilitado, poco espacio pero muy muy aprovechado y perfectamente decorado. Realmente es muy acogedor y RENTABLE inversión para ALQUILAR o ser utilizado por el mismo PROPIETARIO. Justo frente al PUERTO donde pueden encontrar desde excursiones SUBMARINAS PARA BUCEADORES, como ALQUILER DE MOTOS ACUATICAS Y BARCOS. Asi como una gran oferta de Bares, Pizzerias, Cafeterias y Restaurantes.",
            "Estudio en Altea zona Campomanes, 32 m. de superficie, 50 m. de la playa, un baño, propiedad para entrar a vivir, cocina equipada. Extras: aire acondicionado, armarios empotrados, ascensor, muebles, autobuses.",
            "Oportunidad estudio con reforma reciente, situado en el club naútico de Altea, con cocina americana, salón con cama de matrimonio, baño con plato de ducha, muy luminoso, a 50 metros de la playa, ideal para descansar, ya que en la zona hay muchas las actividades pesqueras y naúticas, numerosos restaurantes y el pueblo está muy cerca.",
            "El apartamento dispone de dos dormitorios, un baño, cocina abierta al salón y al comedor, el apartamento se ha reformado en el 2016, y cuenta con un diseño sencillo y acogedor, además de estar totalmente amueblado. Su precio es una oportunidad única dada la ubicación y estado del apartamento. El apartamento se encuentra actualmente alquilado, y se vende con el inquilino. Ideal para inversionistas.",
            "PRECIOSO LOFT EN ALTEA, ZONA MASCARAT Esta situado a 10 minutos de Benidorm y 5 de Calpe, es una zona espectacular de calas naturales, buen sitio para ir a relajarse y disfrutar de la vacaciones; las zonas comunes tiene 2 pistas de papel y tres piscinas en diferentes alturas, zona de fiestas para adultos en la comunidad, urbanización privada vigilada por cámaras y tags en la zonas comunes para poder acceder. El loft costa de una habitación doble, con armario empotrado.",
            "*SIN COMISIÓN DE AGENCIA* Piso situado en la localidad de Altea en la provincia de Alicante. Cuenta con una superficie de 89 metros útiles. La vivienda consta de 3 dormitorios, 1 baño, salón-comedor, cocina y terraza. Además, cuenta con una terraza y calefacción. Nuestro equipo le asesorará gratis sobre las posibilidades de financiación. No lo piense más y llámenos. Conozca todas nuestras viviendas pinchando en el logo.",
            "Vivienda que se distribuye en salón-comedor, cocina, 2 habitaciones, 1 baño y terraza. Cuenta con garaje y trastero anejo. Bien comunicado mediante carretera CV-755.",
            "Vivienda que se distribuye en salón-comedor, cocina, 2 habitaciones, 1 baño y terraza. Cuenta con garaje y trastero anejo. Situada en zona residencial en un entorno tranquilo con vistas a la montaña.",
            "Piso de 70 m2. Tiene 3 habitaciones, 2 cuartos de baño, 1 salón con balcón, 1 cocina comedor. Todas las habitaciones incluida la cocina, con ventanas al exterior, por lo que tiene mucha luz natural Con armarios empotrados en habitaciones y cocina comedor. 2º Piso sin ascensor. Comunidad de 6 viviendas. Zona tranquila a 3 min del casco antiguo de Altea.",
            "Loft totalmente reformado y amueblado. La comunidad dispone de pista de tenis. Playa y puerto deportivo a 200 metros.",
        )

        val direccion = direcciones.random()
        database.inmuebleDAO().insertAll(
            Inmueble(
                "12345678E",
                direccion,
                i % 2 == 0,
                posterBitmap,
                null,
                i + Random.nextInt(1, 10),
                (i + Random.nextInt(1, 10)) * Random.nextInt(10000, 100000),
                tipoInmueble.random(),
                tipoOperacion.random(),
                (i + Random.nextInt(1, 10)) * Random.nextInt(10, 100),
                i % 2 == 0,
                Random.nextInt(1, 10),
                Random.nextInt(1, 10),
                "Valencia",
                if (i % 2 == 0)"Valencia" else "Madrid",
                "Benimaclet",
                "España",
                latitud,
                longitud,
                estados.random(),
                i % 2 == 0,
                10,
                direccion,
                "Subtitulo",
                descripciones.random()
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
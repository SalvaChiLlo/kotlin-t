package com.kotlin_t.trobify.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import com.kotlin_t.trobify.persistencia.Favorito
import com.kotlin_t.trobify.persistencia.Foto
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.persistencia.Usuario
import com.kotlin_t.trobify.logica.Constantes
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.random.Random


class PopulateDB(
    val database: AppDatabase,
    val context: Context,
    val sharedViewModel: com.kotlin_t.trobify.logica.ContextClass
) {
    fun populate() {
        //         Crear Usuarios
        database.usuarioDAO().insertAll(
            Usuario(
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
                        null
                    )
                )
        }

        // Crear Inmuebles
        for (i in 0..50) {
            createInmueble(i)
            val ultimoInmueble = database.inmuebleDAO().getAllPublicAndNoPublic().last()
            database.fotoDAO()
                .insertAll(Foto(ultimoInmueble.inmuebleId!!, ultimoInmueble.miniatura!!, true))
        }

        val listaInmuebles = database.inmuebleDAO().getAllPublicAndNoPublic()
        // Crear Favoritos
        for (i in 0..6) {
            database.favoritoDAO().insertAll(Favorito(listaInmuebles[i].inmuebleId!!, "-1"))
        }
    }

    private fun processImageUris(url: String, inmuebleId: Int) {
        var posterBitmap: Bitmap? = null
        posterBitmap = getBitmapFromURL(url)
        posterBitmap = processImage(posterBitmap!!)
        saveImage(posterBitmap!!, inmuebleId)
    }

    fun createInmueble(i: Int) {
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
            "Piso en venta en calle Metge Adolfo Quiles, 5",
            "Casa o chalet en venta en calle Forat, 23",
            "Piso en venta en calle Camp Preciós, 15",
            "Piso en venta en calle Ascar, 24",
            "Casa o chalet independiente en venta en calle de Juan Martorell, 10",
            "Piso en venta en MARQUES DE DOS AGUAS",
            "Piso en venta en Ciutat Universitària",
            "Piso en venta en paseo Ciudadela",
            "Piso en venta en calle de Joaquín Costa",
            "Chalet adosado en venta en calle Mar de Marmara",
            "Piso en venta en avenida Vicente Blasco Ibáñez Novelista",
            "Piso en venta en avenida de les Corts Valèncianes",
            "Dúplex en venta en plaza Mayor Port Saplaya, Alboraya",
            "Chalet pareado en venta en beniferri",
            "Chalet adosado en venta en Els Orriols",
            "Piso en venta en calle de Daroca s/n",
            "Casa o chalet independiente en venta en Entrepinos",
            "Piso en venta en Benicalap",
            "Piso en venta en Mestalla, València",
            "Chalet adosado en venta en Peñagrande",
            "Chalet pareado en venta en Urb. El Encinar de los Reyes, Encinar de los Reyes",
            "Piso en venta en Almagro",
            "Piso en venta en calle de la Hiedra",
            "Piso en venta en vereda de Palacio",
            "Chalet pareado en venta en camino de la Huerta",
            "Casa o chalet independiente en venta en camino de Mesoncillos",
            "Casa o chalet independiente en venta en calle del Camino Alto",
            "Ático en venta en calle de Luis Martinez Feduchi",
            "Casa adosada en venta en Marina de Casares",
            "Piso en venta en Enric Hernandez Centre - Zona Alta",
            "Apartamento en venta en Calle la Habana Moncófar Playa",
            "Piso en venta en Calle de Cantalejo Fuentelarreina",
            "Piso en venta en Callejón de la Alcoholera, 24a Las Villas - Valparaiso",
            "Piso en venta en Calle de Alejandro Dumas, 20 Imperial",
            "Casa adosada en venta en Calle Fuente de la Fama, 71 Laguna de Duero",
            "Casa adosada en venta en Laguna de Duero",
            "Piso en venta en Recesvinto, 4 Venta de Baños",
            "Piso en venta en avenida Gran Vía Juan Carlos I",
            "Casa o chalet en venta en Cascajos - Piqueras",
            "Piso en venta en avenida de la Sierra",
            "Piso en venta en calle Torrecilla en Cameros",
            "Piso en venta en San Adrián - La Cava",
            "Piso en venta en calle General Vara de Rey",
            "Piso en venta en Portillejo - Valdegastea",
            "Casa o chalet en venta en travesía Palencia",
            "Piso en venta en Venta de Baños",
            "Casa o chalet en venta en calle León Felipe",
            "Chalet adosado en venta en calle Prado de la Lana s/n",
            "Casa o chalet en venta en calle el Cerrato",
            "Piso en venta en calle Leonardo Rucabado",
            "Piso en venta en calle Hermanos Menéndez Pidal, 14",
            "Ático en venta en calle Miguel de Cervantes",
            "Piso en venta en paseo Ocharan Mazas",
            "Ático en venta en calle Ataúlfo Argenta",
            "Casa o chalet independiente en venta en Barrio Mioño, 2075",
            "Ático en venta en calle del Pocillo, 10",
            "Piso en venta en MENENDEZ PELAYO",
            "Dúplex en venta en Colón, 11",
            "Piso en venta en calle Asturias",
            "Casa o chalet independiente en venta en Olivares",
            "Piso en venta en calle González Besada",
            "Ático en venta en Ataulfo Argenta",
            "Chalet pareado en venta en Barrio Sámano",
            "Ático en venta en calle EL GRECO",
            "Ático en venta en calle el greco",
            "Piso en venta en calle de Enrique Cubero, 38",
            "Piso en venta en València",
            "Piso en venta en calle de Joaquín Costa - València",
            "Piso en venta en calle del Convent de Santa Clara - València",
            "Piso en venta en Doctor Moliner, 2 - València",
            "Ático en venta en calle Luis Bolinches Compañ, 18 - València",
            "Piso en venta en duque calabria - València",
            "Ático en venta en Sant Francesc - València",
            "Chalet pareado en venta en Urb. Urb. el Mirador de La Reva, Riba-Roja de Túria - València",
            "Piso en venta en calle DEL ARZOBISPO COMPANY - València",
            "Ático en venta en Zona Avenida al Vedat - València",
            "Piso en venta en Gran Vía del Marqués del Turia - València",
            "Piso en venta en calle del Mestre Gozalbo - València",
            "Piso en venta en paseo Marítim de la Patacona - València",
            "Ático en venta en calle menorca - València",
            "Piso en venta en avenida Novelista Blasco Ibañez, 9 - València",
            "Piso en venta en camino Vell d'Alboraia - València",
            "Piso en venta en calle de Pelayo, 54 - València",
        )
        val tipoOperacion = listOf(
            Constantes.VENTA,
            Constantes.ALQUILER,
            Constantes.INTERCAMBIO_VIVIENDA
        )

        val tipoInmueble = listOf(
            Constantes.ATICO,
            Constantes.CASA_CHALET,
            Constantes.HABITACION,
            Constantes.PISO,
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

        fun getLatitude(): Double {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocationName(
                direccion,
                1
            )
            return if (result.isEmpty()) 0.0 else result.get(0).latitude
        }

        fun getLongitude(): Double {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocationName(
                direccion,
                1
            )
            return if (result.isEmpty()) 0.0 else result.get(0).longitude
        }

        fun getMunicipio(): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocationName(
                direccion,
                1
            )
            return if (result.isEmpty()) "" else result.get(0)?.locality + ""
        }

        fun getProvincia(): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val dir = geocoder.getFromLocationName(
                direccion,
                1
            )
            return if (!dir.isEmpty()) dir.get(0)?.adminArea + "" else ""
        }

        database.inmuebleDAO().insertAll(
            Inmueble(
                null,
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
                getProvincia(),
                getMunicipio(),
                "Benimaclet",
                "España",
                getLatitude(),
                getLongitude(),
                estados.random(),
                i % 2 == 0,
                10,
                direccion,
                "Subtitulo",
                descripciones.random(),
                46000 + (i * 10),
                true
            )
        )

    }

    private fun processImage(bitmap_: Bitmap): Bitmap {
        var bitmap =
            Bitmap.createScaledBitmap(bitmap_, 350, 350, true)
        return bitmap
    }


    private fun saveImage(bitmap: Bitmap, inmuebleId: Int) {
        val image: Foto
        if(database.fotoDAO().getAllFromInmuebleID(inmuebleId).isEmpty()) {
            image = Foto(inmuebleId, bitmap, false)
        } else {
            image = Foto(inmuebleId, bitmap, false)
        }
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
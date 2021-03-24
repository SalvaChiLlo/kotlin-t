import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentHomeBinding
import com.kotlin_t.trobify.databinding.FragmentListaFavoritosBinding
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModelFactory
import com.kotlin_t.trobify.presentacion.home.HomeViewModel
import com.kotlin_t.trobify.presentacion.home.HomeViewModelFactory

class ListaFavoritosFragment : Fragment() {
    lateinit var binding: FragmentListaFavoritosBinding
    lateinit var listaFavoritosViewModel: ListaFavoritosViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lista_favoritos, container, false)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = ListaFavoritosViewModelFactory(datasource, application)
        listaFavoritosViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListaFavoritosViewModel::class.java)
        binding.viewModel = listaFavoritosViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.favoritosRecyclerView

        recyclerView.adapter = FavoritoAdapter(
            requireContext(), listaFavoritosViewModel.getInmueblesFavoritos(), listaFavoritosViewModel
        )
    }
}
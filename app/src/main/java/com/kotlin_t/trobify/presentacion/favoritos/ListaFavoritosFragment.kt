import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentListaFavoritosBinding
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter
import com.kotlin_t.trobify.logica.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.logica.favoritos.ListaFavoritosViewModelFactory

class ListaFavoritosFragment : Fragment() {
    lateinit var binding: FragmentListaFavoritosBinding
    lateinit var listaFavoritosViewModel: ListaFavoritosViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lista_favoritos, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = ListaFavoritosViewModelFactory(datasource, application, sharedViewModel)
        listaFavoritosViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListaFavoritosViewModel::class.java)
        binding.viewModel = listaFavoritosViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.favoritosRecyclerView

        recyclerView.adapter = FavoritoAdapter(
            requireContext(), listaFavoritosViewModel.getInmueblesFavoritos(), listaFavoritosViewModel, listaFavoritosViewModel.database, sharedViewModel
        )
    }
}
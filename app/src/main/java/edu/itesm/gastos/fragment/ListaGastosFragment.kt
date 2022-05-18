package edu.itesm.gastos.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import edu.itesm.gastos.R
import edu.itesm.gastos.activities.GastoCapturaDialog
import edu.itesm.gastos.databinding.FragmentListaGastosBinding
import edu.itesm.gastos.entities.Gasto
import edu.itesm.gastos.entities.GastoFb
import edu.itesm.gastos.utils.FirebaseUtils.firebaseAuth
import edu.itesm.gastos.utils.FirebaseUtils.firebaseUser
import edu.itesm.perros.adapter.GastosAdapter


class ListaGastosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var  gastos: List<Gasto>
    private lateinit var binding: FragmentListaGastosBinding
    private lateinit var adapter: GastosAdapter
    private val databaseReference = Firebase.database.getReference("gastos")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListaGastosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecycler()
        fabAgregaDatos()
        logOut()
    }
    private fun initRecycler(){
        gastos = mutableListOf<Gasto>()
        adapter = GastosAdapter(gastos)
        binding.gastos.layoutManager = LinearLayoutManager(activity)
        binding.gastos.adapter = adapter
        binding.gastos.setListener(object :
            SwipeLeftRightCallback.Listener{
            override fun onSwipedLeft(position: Int) {
                removeGasto(position)
            }
            override fun onSwipedRight(position: Int) {
                binding.gastos.adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun removeGasto(position: Int){
        val gasto = adapter.getGasto(position)
        val userReference = databaseReference.child(firebaseUser!!.uid.toString())
        userReference
            .child(gasto.id.toString()).removeValue().addOnSuccessListener {
                Toast.makeText(activity,
                    "Borrado de la BD", Toast.LENGTH_LONG).show()
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(activity,
                    "Falla PPPOOOOOOMMMM BD", Toast.LENGTH_LONG).show()
            }

    }
    private fun initViewModel(){
        val userReference = databaseReference.child(firebaseUser!!.uid.toString())
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var lista = mutableListOf<Gasto>()
                for (gastoObject in snapshot.children){
                    val objeto = gastoObject.getValue(GastoFb::class.java)
                    lista.add(Gasto(objeto!!.id.toString(),
                        objeto!!.description!!, objeto.monto!!))
                }
                adapter.setGastos(lista)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private val agregaDatosLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado->
            if(resultado.resultCode == AppCompatActivity.RESULT_OK){
                val gasto : Gasto= resultado.data?.getSerializableExtra("gasto") as Gasto
                Toast.makeText(activity, gasto.description, Toast.LENGTH_LONG ).show()
            }

        }
    private fun fabAgregaDatos(){
        binding.fab.setOnClickListener {

            activity?.let { it1 ->
                GastoCapturaDialog(onSubmitClickListener = { gasto->
                    val userReference = databaseReference.child(firebaseUser!!.uid.toString())
                    val id = userReference.push().key!!
                    val gastoFb = GastoFb(id,gasto.description, gasto.monto)
                    userReference.child(id).setValue(gastoFb)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "Agregado", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                        }
                }).show(it1.supportFragmentManager, "")
            }
        }

    }

    private fun logOut(){
        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_listaGastosFragment_to_loginFragment)
        }
    }

}
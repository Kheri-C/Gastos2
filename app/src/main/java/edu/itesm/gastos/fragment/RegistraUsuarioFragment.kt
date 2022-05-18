package edu.itesm.gastos.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import edu.itesm.gastos.R
import edu.itesm.gastos.databinding.FragmentLoginBinding
import edu.itesm.gastos.databinding.FragmentRegistraUsuarioBinding
import edu.itesm.gastos.utils.FirebaseUtils
import edu.itesm.gastos.utils.FirebaseUtils.firebaseAuth


class RegistraUsuarioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentRegistraUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreateAccount.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val passwordSecond = binding.etConfirmPassword.text.toString().trim()
            if (password == passwordSecond) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { respuesta ->
                    if(respuesta.isSuccessful){
                        findNavController().navigate(R.id.action_registraUsuarioFragment_to_loginFragment)
                    }else{
                        Toast.makeText(activity, "Error de registro", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(activity, "passwords are not the same", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistraUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }
}
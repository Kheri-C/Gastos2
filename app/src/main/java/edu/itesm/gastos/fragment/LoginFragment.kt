package edu.itesm.gastos.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import edu.itesm.gastos.R
import edu.itesm.gastos.databinding.FragmentListaGastosBinding
import edu.itesm.gastos.databinding.FragmentLoginBinding
import edu.itesm.gastos.utils.FirebaseUtils.firebaseAuth


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            val email = binding.etSignInEmail.text.toString().trim()
            val password = binding.etSignInPassword.text.toString().trim()

            if (!(email.isNullOrEmpty() && password.isNullOrEmpty())) {

            }else{
                Toast.makeText(activity, "email or password incorrect", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCreateAccount2.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registraUsuarioFragment)
        }
    }


    override fun onStart() {
        super.onStart()

    }



}
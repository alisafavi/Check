package aliSafavi.check

import aliSafavi.check.databinding.FragmentRetrofitBinding
import aliSafavi.check.service.RetrofitService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.create

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RetrofitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class RetrofitFragment : Fragment() {

    private val TAG = "Retrofit"
    private lateinit var binding: FragmentRetrofitBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRetrofitBinding.inflate(layoutInflater)

        testApis()

        return binding.root
    }

    private fun testApis() {
        binding.apiIndex.setOnClickListener {
            lifecycleScope.launch {
                RetrofitService.call.index().let {
                    if (!it.isSuccessful)
                        return@launch
                    Log.i(TAG, it.body()!!)
                    Toast.makeText(activity, it.body(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

package aliSafavi.check

import aliSafavi.check.databinding.FragmentRetrofitBinding
import aliSafavi.check.service.RetrofitService
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


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
            try {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.200:4204/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                val call = retrofit.create<RetrofitService>()

                call.index().enqueue(object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        Log.i(TAG,response.body()!!)
                        Toast.makeText(activity, response.body()!!, Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        Log.i(TAG, t.message!!)
                        Toast.makeText(activity, t.message!!, Toast.LENGTH_SHORT).show()
                    }
                })
            }catch (e : Exception){
                Log.e(TAG, e.message!!)
            }
        }
    }
}

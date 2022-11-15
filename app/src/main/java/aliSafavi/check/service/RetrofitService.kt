package aliSafavi.check.service

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitService {

    @GET("index")
    fun index(): Response<String>

    companion object api {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:4204/api/index/")
            .build()

        val call = RetrofitService.api.retrofit.create<RetrofitService>()


    }

}

package aliSafavi.check.service

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitService {

    @GET("index")
    fun index(): Call<String>

}

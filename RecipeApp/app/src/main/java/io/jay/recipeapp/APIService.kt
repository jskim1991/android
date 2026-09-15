package io.jay.recipeapp

import retrofit2.http.GET

interface APIService {
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse
}

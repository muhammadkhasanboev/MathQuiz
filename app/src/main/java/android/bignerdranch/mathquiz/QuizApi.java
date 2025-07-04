package android.bignerdranch.mathquiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizApi {

    @GET("api.php")
    Call<QuizResponse> getQuestions(
            @Query("amount") int amount,
            @Query("category") Integer category,        // Use Integer (nullable)
            @Query("difficulty") String difficulty,     // Pass null if "Any"
            @Query("type") String type                  // Pass null if "Any"
    );
}

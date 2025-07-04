package android.bignerdranch.mathquiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizApi {

    /**
     * Fetches quiz questions from the OpenTrivia API.
     *
     * @param amount     Number of questions (1–50)
     * @param category   Category ID (nullable: if null, API uses any category)
     * @param difficulty Difficulty level (nullable: "easy", "medium", "hard", or null for any)
     * @param type       Question type (nullable: "multiple", "boolean", or null for any)
     * @return A Retrofit Call returning QuizResponse
     */
    @GET("api.php")
    Call<QuizResponse> getQuestions(
            @Query("amount") int amount,
            @Query("category") Integer category,
            @Query("difficulty") String difficulty,
            @Query("type") String type
    );
}

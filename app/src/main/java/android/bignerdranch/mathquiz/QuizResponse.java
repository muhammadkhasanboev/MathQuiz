package android.bignerdranch.mathquiz;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizResponse {

    @SerializedName("response_code")
    private int responseCode;

    @SerializedName("results")
    private List<QuestionItem> results;

    public int getResponseCode() {
        return responseCode;
    }

    public List<QuestionItem> getResults() {
        return results;
    }

    // Optional: Useful for debugging
    public boolean isSuccessful() {
        return responseCode == 0;
    }

    @Override
    public String toString() {
        return "QuizResponse{" +
                "responseCode=" + responseCode +
                ", results=" + results +
                '}';
    }
}

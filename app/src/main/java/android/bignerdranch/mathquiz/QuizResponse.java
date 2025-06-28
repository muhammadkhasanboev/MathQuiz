package android.bignerdranch.mathquiz;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizResponse {

    @SerializedName("results")
    private List<QuestionItem> results;

    public List<QuestionItem> getResults() {
        return results;
    }
}

package android.bignerdranch.mathquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView usernameTextView;
    private Button button1, button2, button3, button4;
    private List<QuestionItem> questions;
    private int currentIndex = 0;
    private int score = 0;

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final QuizApi quizApi = retrofit.create(QuizApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Wire UI
        usernameTextView = findViewById(R.id.username_text_view);
        questionTextView = findViewById(R.id.question_text_view);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        // Get data from Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int amount = intent.getIntExtra("amount", 10);
        int category = intent.getIntExtra("category", 0);
        String difficulty = intent.getStringExtra("difficulty");
        String type = intent.getStringExtra("type");

        if (username != null) {
            usernameTextView.setText("Welcome, " + username + "!");
        } else {
            usernameTextView.setText("Welcome!");
        }

        // Log the customization input for debugging
        Log.d("QUIZ_DEBUG", "amount=" + amount + ", category=" + category + ", difficulty=" + difficulty + ", type=" + type);

        // Make API call with customized data
        Call<QuizResponse> call = quizApi.getQuestions(
                amount,
                category == 0 ? null : category, // API ignores null category
                difficulty == null || difficulty.isEmpty() ? null : difficulty,
                type == null || type.isEmpty() ? null : type
        );

        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuestionItem> result = response.body().getResults();
                    Log.d("QUIZ_DEBUG", "Questions received: " + result.size());
                    runQuiz(result);
                } else {
                    Toast.makeText(MainActivity.this, "API error: empty or bad response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("QUIZ_DEBUG", "API call failed", t);
            }
        });
    }

    private void runQuiz(List<QuestionItem> questions) {
        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
            return;
        }
        this.questions = questions;
        currentIndex = 0;
        score = 0;
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            questionTextView.setText("Quiz finished! Your score: " + score + "/" + questions.size());
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            return;
        }

        QuestionItem currentQuestion = questions.get(currentIndex);
        String question = android.text.Html.fromHtml(currentQuestion.getQuestion()).toString();
        questionTextView.setText(question);

        List<String> allAnswers = new ArrayList<>(currentQuestion.getIncorrectAnswers());
        allAnswers.add(currentQuestion.getCorrectAnswer());
        Collections.shuffle(allAnswers);

        button1.setText(allAnswers.get(0));
        button2.setText(allAnswers.get(1));
        button3.setText(allAnswers.get(2));
        button4.setText(allAnswers.get(3));

        setAnswerListener(button1, allAnswers.get(0), currentQuestion.getCorrectAnswer());
        setAnswerListener(button2, allAnswers.get(1), currentQuestion.getCorrectAnswer());
        setAnswerListener(button3, allAnswers.get(2), currentQuestion.getCorrectAnswer());
        setAnswerListener(button4, allAnswers.get(3), currentQuestion.getCorrectAnswer());
    }

    private void setAnswerListener(Button button, String selectedAnswer, String correctAnswer) {
        button.setOnClickListener(v -> {
            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                score++;
            } else {
                Toast.makeText(MainActivity.this, "Wrong! Correct: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }
            currentIndex++;
            showQuestion();
        });
    }
}

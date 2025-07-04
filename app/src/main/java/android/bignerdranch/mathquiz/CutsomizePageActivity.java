package android.bignerdranch.mathquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class CutsomizePageActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerDifficulty, spinnerType;
    private EditText editTextNumQuestions;
    private Button buttonStartQuiz;

    // Maps category names to OpenTrivia category IDs
    private final HashMap<String, Integer> categoryMap = new HashMap<String, Integer>() {{
        put("Any Category", 0);
        put("General Knowledge", 9);
        put("Science: Computers", 18);
        put("Science: Mathematics", 19);
        put("History", 23);
        put("Sports", 21);
        // Add more if needed
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutsomize_page);

        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerType = findViewById(R.id.spinner_type);
        editTextNumQuestions = findViewById(R.id.edittext_num_questions);
        buttonStartQuiz = findViewById(R.id.button_start_quiz);

        setupSpinners();

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQuiz();
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(
                this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
    }

    private void launchQuiz() {
        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        String selectedDifficulty = spinnerDifficulty.getSelectedItem().toString().toLowerCase();
        String selectedType = spinnerType.getSelectedItem().toString().toLowerCase();

        String numQuestionsStr = editTextNumQuestions.getText().toString().trim();
        if (numQuestionsStr.isEmpty()) {
            Toast.makeText(this, "Please enter number of questions", Toast.LENGTH_SHORT).show();
            return;
        }

        int numQuestions = Integer.parseInt(numQuestionsStr);
        if (numQuestions < 1 || numQuestions > 50) {
            Toast.makeText(this, "Enter between 1 to 50 questions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Map to actual API values
        int categoryId = categoryMap.getOrDefault(selectedCategory, 0);
        String apiDifficulty = selectedDifficulty.equals("any") ? "" : selectedDifficulty;
        String apiType = "";

        if (selectedType.equals("multiple choice")) {
            apiType = "multiple";
        } else if (selectedType.equals("true / false")) {
            apiType = "boolean";
        }

        // Launch MainActivity with values
        Intent intent = new Intent(CutsomizePageActivity.this, MainActivity.class);
        intent.putExtra("amount", numQuestions);
        intent.putExtra("category", categoryId);
        intent.putExtra("difficulty", apiDifficulty);
        intent.putExtra("type", apiType);
        startActivity(intent);
        finish();
    }
}

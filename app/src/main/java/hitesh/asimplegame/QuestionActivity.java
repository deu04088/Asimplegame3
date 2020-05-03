package hitesh.asimplegame;

/**
 * Created by H on 7/12/2015.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;        //Import Collections for shuffle problem
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;



public class QuestionActivity extends Activity {


    private static final String TAG = QuestionActivity.class.getSimpleName();

    private List<Question> questionList;
    private int score = 0;
    private int questionID = 0;

    private Question currentQ;
    private TextView txtQuestion, times, scored;
    private Button button1, button2, button3;
    private Button buttonpass, buttonstop, buttonhint;  // Declare Item Button

    public CounterClass timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuizDBOpenHelper db = new QuizDBOpenHelper(this);  // my question bank class
        questionList = db.getAllQuestions();  // this will fetch all quetonall questions
        currentQ = questionList.get(questionID); // the current question
        long seed = System.nanoTime();                             //To come up with problems in random order.
        Collections.shuffle(questionList, new Random(seed));


        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        // the textview in which the question will be displayed

//
        // the three buttons,
        // the idea is to set the text of three buttons with the options from question bank
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        buttonpass = (Button) findViewById(R.id.buttonpass); //Call Item Button's ID from activity_XML
        buttonstop = (Button) findViewById(R.id.buttonstop);
        buttonhint = (Button) findViewById(R.id.buttonhint);

        // the textview in which score will be displayed
        scored = (TextView) findViewById(R.id.score);

        // the timer
        times = (TextView) findViewById(R.id.timers);

//
        // method which will set the things up for our game
        setQuestionView();
        times.setText("00:02:00");

//        시간초재기
        // A timer of 60 seconds to play for, with an interval of 1 second (1000 milliseconds)
        timer = new CounterClass(60000, 1000);
        timer.start();

//        버튼동작
        // button click listeners
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing the button text to other method
                // to check whether the anser is correct or not
                // same for all three buttons
                getAnswer(button1.getText().toString());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button2.getText().toString());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button3.getText().toString());
            }
        });

        buttonpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                //Pass Item Button
                score++;                                        //Plus score
                scored.setText("Score : " + score);
                currentQ = questionList.get(questionID);        //The selection changes when you press the button.
                setQuestionView();
            }
        });

    }
//    문제를 맞출 때 동작
    public void getAnswer(String AnswerString) {
        if (currentQ.getANSWER().equals(AnswerString)) {
//          문제를 맞혔을 때 점수증가
            // if conditions matches increase the int (score) by 1
            // and set the text of the score view
            score++;
            scored.setText("Score : " + score);
        } else {
//            틀렸을 때 동작
            // if unlucky start activity and finish the game
            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);

            // passing the int value
            Bundle b = new Bundle();
            b.putInt("score", score); // Your score
            intent.putExtras(b); // Put your score to your next
            startActivity(intent);
            finish();
            timer.cancel();                        //Turn off the timer that keeps spinning when the game is over
        }

//        문제 20개로 고정
        if (questionID < 20) {
            // if questions are not over then do this
            currentQ = questionList.get(questionID);
            setQuestionView();
        }
//        ???
        else {
            // if over do this
            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            Bundle b = new Bundle();
            b.putInt("score", score); // Your score
            intent.putExtras(b); // Put your score to your next
            startActivity(intent);
            finish();
            timer.cancel();                     //Turn off the timer that keeps spinning when the game is over
        }

    }

//  타이머??
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer
    {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            times.setText("Time is up");
            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);  //Go to end screen when time is up

            Bundle b = new Bundle();
            b.putInt("score", score); // Your score
            intent.putExtras(b); // Put your score to your next
            startActivity(intent);
            finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;

            String hms = String.format( "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(millis)));

            Log.d(TAG, "current time: " + hms);
            times.setText(hms);

        }

    }

//
    private void setQuestionView() {
        // the method which will put all things together
        txtQuestion.setText(currentQ.getQUESTION());
        button1.setText(currentQ.getOPTA());
        button2.setText(currentQ.getOPTB());
        button3.setText(currentQ.getOPTC());

        questionID++;
    }



}

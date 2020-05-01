package hitesh.asimplegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


public class ResultActivity extends Activity {

//	마지막 화면 점수 계산
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		TextView textResult = (TextView) findViewById(R.id.textResult);
		Bundle b = getIntent().getExtras();
		int score = b.getInt("score");
        textResult.setText("Your score is " + " " + score + ". Thanks for playing my game.");
	}

//	게임 재시작
	public void playagain(View o) {
			Intent intent = new Intent(this, QuestionActivity.class);
			startActivity(intent);
	}
}
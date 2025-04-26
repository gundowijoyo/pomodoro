package com.my.pomodoro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerText, sessionType;
    private Button startButton, pauseButton, resetButton;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 1500000; // 25 minutes
    private int sessionCount = 0;

    private enum Session {
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }

    private Session currentSession = Session.FOCUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timer_text);
        sessionType = findViewById(R.id.session_type);
        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        resetButton = findViewById(R.id.reset_button);

        updateTimerText();

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        if (!isRunning) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    handleSessionEnd();
                }
            }.start();

            isRunning = true;
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    private void resetTimer() {
        countDownTimer.cancel();
        isRunning = false;
        setSession(currentSession); // Reset current session
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }

    private void handleSessionEnd() {
        sessionCount++;

        if (currentSession == Session.FOCUS) {
            if (sessionCount % 4 == 0) {
                setSession(Session.LONG_BREAK);
            } else {
                setSession(Session.SHORT_BREAK);
            }
        } else {
            setSession(Session.FOCUS);
        }

        startTimer();
    }

    private void setSession(Session session) {
        currentSession = session;
        switch (session) {
            case FOCUS:
                timeLeftInMillis = 25 * 60 * 1000;
                sessionType.setText("Focus Time");
                break;
            case SHORT_BREAK:
                timeLeftInMillis = 5 * 60 * 1000;
                sessionType.setText("Short Break");
                break;
            case LONG_BREAK:
                timeLeftInMillis = 15 * 60 * 1000;
                sessionType.setText("Long Break");
                break;
        }

        updateTimerText();
    }
}

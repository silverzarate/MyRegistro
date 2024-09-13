package com.utic.myregistro;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainJuego extends AppCompatActivity {



    private ImageView ball;
    private ImageView arco;
    private View goalArea;
    private TextView messageTextView;
    //movimiento de imagen
    private ImageView movingImage;
    private ValueAnimator animator;
    private float imageWidth;
    private float screenWidth;
    private float ballWidth;
    private float ballHeight;

    private boolean movingRight = true;
    private boolean movingLeft = true;



    private TextView resultText;
    private Button leftButton;
    private Button rightButton;
    private Button shootButton;
    private Button resetButton;

    private float initialX; // Posición inicial del balón en X
    private float initialY; // Posición inicial del balón en Y
    private Rect ballRect = new Rect(); // Rectángulo del balón
    private Rect goalRect = new Rect();// Rectángulo del arco

    private int[] songResIds = {R.raw.pista2};
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Inicializa los componentes de la interfaz
        ball = findViewById(R.id.ball);
        arco = findViewById(R.id.arco);
        goalArea = findViewById(R.id.goalArea);
        movingImage = findViewById(R.id.movingImage);
        messageTextView = findViewById(R.id.messageTextView);
        resultText = findViewById(R.id.resultText);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        shootButton = findViewById(R.id.shootButton);
        resetButton = findViewById(R.id.resetButton);

        movingImage.post(new Runnable() {
                             @Override
                             public void run() {
                                 imageWidth = movingImage.getWidth();
                                 screenWidth = getResources().getDisplayMetrics().widthPixels;
                                 ballWidth = ball.getWidth();
                                 ballHeight = ball.getHeight();

                                 // Configura y comienza la animación horizontal continua
                                 startHorizontalAnimation();
                             }
        });

                // Guarda la posición inicial del balón en X y Y
                initialX = ball.getX();
                initialY = ball.getY();

                // Configura el botón para mover el balón hacia la izquierda
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveBall(-200); // Mueve el balón 100 píxeles hacia la izquierda
                    }
                });

                // Configura el botón para mover el balón hacia la derecha
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveBall(200); // Mueve el balón 100 píxeles hacia la derecha
                    }
                });

                // Configura el botón para lanzar el balón
                shootButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchBallVertically();

                    }
                });


                // Configura el botón para restablecer la posición del balón
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetBallPosition();

                    }
                });
            }

            private void startHorizontalAnimation() {
                // Configura la animación para mover la imagen de izquierda a derecha
                animator = ValueAnimator.ofFloat(0, screenWidth - imageWidth);
                animator.setDuration(450); // Duración de la animación en milisegundos (2 segundos)
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE); // Repite la animación infinitamente

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        movingImage.setX(animatedValue);

                        checkCollision();

                        // Verifica si la imagen ha tocado los bordes de la pantalla y cambia la dirección
                        if (movingRight) {
                            if (animatedValue >= screenWidth - imageWidth) {
                                movingRight =false; // Cambia la dirección
                                animator.reverse(); // Reversa la animación
                            }
                        }

                        }

                });

                animator.start();
            }


            private void moveBall(float deltaX) {
                // Mueve el balón en el eje X
                float newX = ball.getX() + deltaX;
                ball.animate()
                        .translationX(newX - ball.getX())
                        .setDuration(1000)
                        .start();
            }

            private void launchPenalty() {
                // Establece la animación del balón hacia arriba y al frente
                int distanceX = 300; // Distancia horizontal en píxeles
                int distanceY = -300; // Distancia vertical en píxeles (negativo para arriba)

                // Animación para mover el balón hacia arriba
                ObjectAnimator animY = ObjectAnimator.ofFloat(ball, "translationY", ball.getTranslationY(), distanceY);
                animY.setDuration(500); // Duración de la animación en milisegundos

                // Animación para mover el balón hacia el frente
                ObjectAnimator animX = ObjectAnimator.ofFloat(ball, "translationX", ball.getTranslationX(), distanceX);
                animX.setDuration(500); // Duración de la animación en milisegundos

                // Ejecuta ambas animaciones simultáneamente
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animX, animY);
                animatorSet.start();

                // Muestra un mensaje de resultado
                resultText.setText("¡Lanzado!");


            }

            private void resetBallPosition() {
                // Restablece la posición del balón a la posición inicial
                float launchDistance = 180; // Distancia vertical en píxeles (negativo para arriba)

                ObjectAnimator animY = ObjectAnimator.ofFloat(ball, "translationY", ball.getTranslationY(), launchDistance);
                animY.setDuration(1000); // Duración de la animación en milisegundos

                animY.start();


                // Muestra un mensaje de resultado
                resultText.setText("¡De Nuevo!");
            }


            private void launchBallVertically() {

                // Establece la animación del balón hacia arriba
                float launchDistance = -560; // Distancia vertical en píxeles (negativo para arriba)

                ObjectAnimator animY = ObjectAnimator.ofFloat(ball, "translationY", ball.getTranslationY(), launchDistance);
                animY.setDuration(1100); // Duración de la animación en milisegundos

                animY.start();
                ball.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkGoal();

                    }
                }, 1100); // Ajusta es


                // Muestra un mensaje de resultado
                resultText.setText("?");
            }


    private void checkCollision() {
        // Obtén las coordenadas de los rectángulos de la pelota y de la imagen
        int[] ballLocation = new int[2];
        int[] imageLocation = new int[2];
        ball.getLocationOnScreen(ballLocation);
        movingImage.getLocationOnScreen(imageLocation);

        // Calcula los rectángulos
        float ballLeft = ballLocation[0];
        float ballRight = ballLeft + ballWidth;
        float ballTop = ballLocation[1];
        float ballBottom = ballTop + ballHeight;

        float imageLeft = imageLocation[0];
        float imageRight = imageLeft + imageWidth;
        float imageTop = imageLocation[1];
        float imageBottom = imageTop + movingImage.getHeight();

        // Verifica si los rectángulos se intersectan
        if (ballRight > imageLeft && ballLeft < imageRight &&
                ballBottom > imageTop && ballTop < imageBottom) {
            resultText.setText("¡Tapo El Portero!");
            resetBallPosition();
        }
    }



            private void checkGoal() {
                // Obtiene las dimensiones de los rectángulos del balón y del arco
                ball.getDrawingRect(ballRect);
                goalArea.getDrawingRect(goalRect);

                // Ajusta las coordenadas del rectángulo del balón
                int[] ballLocation = new int[2];
                ball.getLocationOnScreen(ballLocation);
                ballRect.offsetTo(ballLocation[0], ballLocation[1]);

                // Ajusta las coordenadas del rectángulo del arco
                int[] goalLocation = new int[2];
                goalArea.getLocationOnScreen(goalLocation);
                goalRect.offsetTo(goalLocation[0], goalLocation[1]);

                // Verifica si los rectángulos se intersectan
                if (Rect.intersects(ballRect, goalRect)) {
                    resultText.setText("¡Gool!");
                    showMessage("¡GOOOOL!");
                    mediaPlayer=MediaPlayer.create(this,R.raw.pista2);
                    mediaPlayer.start();
                } else {
                    resultText.setText("¡Tapo el Portero!");
                }
            }

    private void showMessage(String message) {
        messageTextView.setText(message);
        messageTextView.setVisibility(View.VISIBLE);

        // Hide the message after 2 seconds
        messageTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageTextView.setVisibility(View.GONE);
            }
        }, 3000); // 2000 milliseconds = 2 seconds

    }

}





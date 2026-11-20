package com.example.datadiri;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Membuat tampilan penuh hingga ke status bar (edge-to-edge)
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_main);

        FrameLayout rootContainer = findViewById(R.id.rootContainer);
        FrameLayout skyCanvasContainer = findViewById(R.id.skyCanvasContainer);
        LinearLayout cardContainer = findViewById(R.id.cardContainer);

        // 1. Membuat Background Gradasi (Oranye di atas -> Merah Muda/Magenta di bawah)
        GradientDrawable skyGradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{
                        Color.parseColor("#FF930F"), // Oranye terang bagian atas
                        Color.parseColor("#FF683B"), // Oranye kemerahan bagian tengah
                        Color.parseColor("#FF1457")  // Pink magenta bagian bawah
                }
        );
        rootContainer.setBackground(skyGradient);

        // 2. Menambahkan Custom View untuk menggambar 5 awan
        CloudBackgroundView cloudView = new CloudBackgroundView(this);
        skyCanvasContainer.addView(cloudView);

        // 3. Membuat Background Kartu Hijau dengan sudut membulat (Rounded Corners)
        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setShape(GradientDrawable.RECTANGLE);
        cardBackground.setColor(Color.parseColor("#47C172")); // Warna hijau kartu
        cardBackground.setCornerRadius(dpToPx(16));
        cardContainer.setBackground(cardBackground);
    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    // Class internal untuk menggambar bentuk awan secara presisi
    private static class CloudBackgroundView extends View {
        private final Paint cloudPaint;

        public CloudBackgroundView(Context context) {
            super(context);
            cloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            cloudPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float w = getWidth();
            float h = getHeight();

            // Awan 1: Kiri Atas (Putih hangat semi-transparan)
            cloudPaint.setColor(Color.parseColor("#FCEAD9"));
            drawStylizedCloud(canvas, w * 0.25f, h * 0.19f, w * 0.26f);

            // Awan 2: Kanan Atas (Lebih besar, putih terang)
            cloudPaint.setColor(Color.parseColor("#FDF2E9"));
            drawStylizedCloud(canvas, w * 0.69f, h * 0.23f, w * 0.35f);

            // Awan 3: Kiri Tengah (Di atas kartu hijau)
            cloudPaint.setColor(Color.parseColor("#FCE4D6"));
            drawStylizedCloud(canvas, w * 0.26f, h * 0.39f, w * 0.24f);

            // Awan 4: Kiri Bawah (Nuansa pink lembut menyatu dengan background)
            cloudPaint.setColor(Color.parseColor("#FCA8B8"));
            drawStylizedCloud(canvas, w * 0.35f, h * 0.78f, w * 0.21f);

            // Awan 5: Kanan Bawah (Lebih kecil, nuansa pink lembut)
            cloudPaint.setColor(Color.parseColor("#FCA5B8"));
            drawStylizedCloud(canvas, w * 0.75f, h * 0.84f, w * 0.16f);
        }

        /**
         * Menggambar siluet awan dengan bagian bawah rata dan lekukan khas di sisi kiri
         */
        private void drawStylizedCloud(Canvas canvas, float cx, float cy, float cloudWidth) {
            Path path = new Path();
            float unit = cloudWidth / 100f;

            // Bagian dasar awan yang rata
            float baseHeight = 14f * unit;
            float baseLeft = cx - (44f * unit);
            float baseRight = cx + (38f * unit);
            float baseBottom = cy + (14f * unit);
            float baseTop = baseBottom - baseHeight;

            RectF baseRect = new RectF(baseLeft, baseTop, baseRight, baseBottom);
            path.addRoundRect(baseRect, baseHeight / 2f, baseHeight / 2f, Path.Direction.CW);

            // Bulatan-bulatan pembentuk gumpalan awan dari kiri ke kanan
            path.addCircle(cx - 32f * unit, cy + 1f * unit, 13f * unit, Path.Direction.CW);
            path.addCircle(cx - 18f * unit, cy - 8f * unit, 16f * unit, Path.Direction.CW);
            path.addCircle(cx + 1f * unit, cy - 11f * unit, 19f * unit, Path.Direction.CW);
            path.addCircle(cx + 21f * unit, cy - 4f * unit, 15f * unit, Path.Direction.CW);
            path.addCircle(cx + 36f * unit, cy + 3f * unit, 12f * unit, Path.Direction.CW);

            canvas.drawPath(path, cloudPaint);
        }
    }
}
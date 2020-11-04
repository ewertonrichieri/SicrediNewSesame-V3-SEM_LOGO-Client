package com.anyvision.facekeyexample.activities.logged;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.GetVariables;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ChamadoGraficoBarraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado_grafico_barra);
        try {
            BarChart barChart = findViewById(R.id.pie_chart_barra);

            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.setMaxVisibleValueCount(50);
            barChart.setDrawGridBackground(true);

            String lblSelecionado = GetVariables.getInstance().GetTxtLabelSliceClickChart();

            Description description = new Description();
            description.setText(lblSelecionado);
            barChart.setDescription(description);

            ArrayList<BarEntry> entries = new ArrayList<>();
            Random rd = new Random();
            entries.add(new BarEntry(0, (float) rd.nextInt(50 + 1 - 10) + 10));
            entries.add(new BarEntry(1, (float) rd.nextInt(50 + 1 - 10) + 10));
            entries.add(new BarEntry(2, (float) rd.nextInt(50 + 1 - 11) + 11));
            entries.add(new BarEntry(3, (float) rd.nextInt(50 + 1 - 10) + 10));
            entries.add(new BarEntry(4, (float) rd.nextInt(50 + 1 - 10) + 10));
            entries.add(new BarEntry(5, (float) rd.nextInt(50 + 1 - 9) + 9));

            BarDataSet barDataSet = new BarDataSet(entries, "depenses");
            barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
            barDataSet.setValueTextColor(Color.WHITE);
            barChart.setGridBackgroundColor(Color.parseColor("#252526"));

            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.9f);

            barChart.animateXY(2000, 3000);
            barChart.setData(data);

            String[] months = new String[6];
            String[] meses = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
            int qtdMonthShowChart = -5;

            for (int i = 0; i <= 5; i++) {

                Calendar now = Calendar.getInstance();
                now.add(Calendar.MONTH, qtdMonthShowChart);
                int valor = now.get(Calendar.MONTH);

                months[i] = meses[valor];
                qtdMonthShowChart++;
            }

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

            xAxis.setTextColor(Color.WHITE);

            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setTextColor(Color.WHITE);

            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setTextColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, ChamadoGraficoBarraActivity.class);
        from.startActivity(intent);
    }

    public void onBackPressed() {
        ChamadoGraficoActivity.startActivity(ChamadoGraficoBarraActivity.this);
    }
}



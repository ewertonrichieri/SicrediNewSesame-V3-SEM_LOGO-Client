package com.anyvision.facekeyexample.activities.logged;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.utils.Enum;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChamadoGraficoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado_grafico);

        //pega os valores do grafico
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int sharedValorTotalPorcentagem = sharedPreferences.getInt(Enum.SharedPrivate.CHAMADO_GESTAO_VALOR_TOTAL.toString(), MODE_PRIVATE);
        int size = sharedPreferences.getInt(Enum.SharedPrivate.CHAMADO_GESTAO_CONTROLE_SALA_SIZE.toString(), MODE_PRIVATE);
        final ArrayList<String> listaGestaoControle = new ArrayList<String>(size);

        for (int i = 0; i < size; i++) {
            listaGestaoControle.add(sharedPreferences.getString(Enum.SharedPrivate.GRAFICO_CHAMADO.toString() + "_" + i, null));
        }

        final PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        Description desc = new Description();
        desc.setText("Total de chamados");
        desc.setTextColor(Color.WHITE);
        desc.setTextSize(50f);

        pieChart.setDescription(desc);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleRadius(25f);

        pieChart.setHoleColor(Color.BLACK);

        List<PieEntry> value = new ArrayList<>();

        if ((listaGestaoControle.size() == 0) || (listaGestaoControle == null)) {
            listaGestaoControle.add(Enum.Chamado.CFTV.toString() + ";" + (5));
            listaGestaoControle.add(Enum.Chamado.ALARME.toString() + ";" + (5));
            listaGestaoControle.add(Enum.Chamado.SISTEMA_INCÊNDIO.toString().replace("_", " ") + ";" + (5));
            listaGestaoControle.add(Enum.Chamado.HVAC.toString() + ";" + (5));
            listaGestaoControle.add(Enum.Chamado.ARCONDICIONADO.toString() + ";" + (5));
            sharedValorTotalPorcentagem = 5;
        }

        for (String li : listaGestaoControle) {

            String[] valorGestao = li.split(";");
            String nome = valorGestao[0].toUpperCase();
            double valor = Double.parseDouble(valorGestao[1]);

            double valorPorcentagem = ((double) valor / sharedValorTotalPorcentagem) * 100;
            float converterValorFinal = (float) valorPorcentagem;

            if (converterValorFinal != 0.0)
                value.add(new PieEntry(converterValorFinal, nome));
        }

        PieDataSet pieDataSet = new PieDataSet(value, "");
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextSize(10f);

        //teste
        pieChart.setDrawSlicesUnderHole(true);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.CIRCLE);

        pieChart.setNoDataText("Não ha informações no momento");

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS); //roxo verde azul
        pieChart.animateX(1400, Easing.EasingOption.EaseInOutCirc);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //float teste2 = e.getY();
                PieEntry pe = (PieEntry) e;
                String label = pe.getLabel();
                GetVariables.getInstance().SetTxtLabelSliceClickChart(label);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                ChamadoGraficoBarraActivity.startActivity(ChamadoGraficoActivity.this);
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                // Callbacks, em seguida, um gesto de arremesso é feito no gráfico.
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                /* * Retornos de chamada quando o gráfico é dimensionado / ampliado por meio de gesto de pinça de zoom.*/
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                // * Retornos de chamada quando o gráfico é movido / traduzido por meio do gesto de arrastar.
            }
        });


    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, ChamadoGraficoActivity.class);
        from.startActivity(intent);
    }

    public void onBackPressed() {
        GestaoActivity.startActivity(ChamadoGraficoActivity.this);
    }
}
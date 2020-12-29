package com.airi.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import io.realm.Realm


class GraphFragment : Fragment() {

//    var graphRealm: Realm? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //表示用サンプルデータの作成//
        val topDatas = listOf<Float>(1f, 2f, 3f, 4f, 5f)//X軸データ
        val y = topDatas.map { it * it }//Y軸データ（X軸の2乗）

        //①Entryにデータ格納
        var entryList = mutableListOf<BarEntry>()
        for (i in topDatas.indices) {
            entryList.add(
                BarEntry(topDatas[i], y[i])
            )
        }

        //BarDataSetのリスト
        val barDataSets = mutableListOf<IBarDataSet>()
        //②DataSetにデータ格納
        val barDataSet = BarDataSet(entryList, "square")
        //③DataSetのフォーマット指定
        barDataSet.color = Color.rgb(0, 255, 204)
        //リストに格納
        barDataSets.add(barDataSet)

        //④BarDataにBarDataSet格納
        val barData = BarData(barDataSets)
        //⑤BarChartにBarData格納
        val barChart = (R.id.barChartExample) as BarChart
        barChart.data = barData
        //⑥Chartのフォーマット指定
        //X軸の設定
        barChart.xAxis.apply {
            val isEnabled = true
            val textColor = Color.BLACK
        }
        //⑦barchart更新
        barChart.invalidate()

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Realm.init(context)
//        graphRealm = Realm.getDefaultInstance()

    }

    override fun onDestroy() {
        super.onDestroy()
//        graphRealm!!.close()
    }


}
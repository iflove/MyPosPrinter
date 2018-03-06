package com.baoda.mypos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.baoda.pos.PosPrinter
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity() {

    //订单菜品集合
    private var goodsBean: MutableList<GoodsBean>? = null


    private var posPrinter: PosPrinter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        doAsync {
            //初始化订单数据
            initData()

            posPrinter = PosPrinter()
            posPrinter!!.connect("192.168.2.150")
            posPrinter!!.initPosPrinter()
                    .selectStandardMode()
                    .setTextBold(true)
                    .setTextAlignment(PosPrinter.TEXT_ALIGNMENT_CENTER)
                    .printText("*** 天龙店铺 ***")
                    .printLine()
                    .setTextBold(false)
                    .setTextAlignment(PosPrinter.TEXT_ALIGNMENT_TEXT_START)
                    .printTextLine("订单编号：1005199")
                    .printTextLine("交易机台：test")
                    .printTextLine("交易时间：2016/2/19 12:34:53")
                    .printTextLine("支付方式：微信支付")
                    .printLine(2)
                    .printText("商品")
                    .setTextAbsoluteLocation(20)
                    .printText("单价")
                    .printWordSpace(3)
                    .printText("数量")
                    .printWordSpace(3)
                    .printText("小计")
                    .printTextLine("----------------------------------------------")
                    .innerPrint({
                        for (foods in goodsBean!!) {
                            printTextLine(foods.name)
                            setTextAbsoluteLocation(20)
                            printText(foods.price)
                            printWordSpace(3)
                            printText(foods.number)
                            printWordSpace(3)
                            printText(foods.sum)
                        }
                        this
                    })
                    .printTextLine("----------------------------------------------")
                    .printLine()
                    .printText("总计(人民币)：")
                    .printText("80.00")
                    .printLine()
                    .feedAndCut(50)

        }


    }

    private fun initData() {
        goodsBean = ArrayList()
        (0..1).map { GoodsBean("测试商品" + it, "10.00", "2", "20.00") }
                .forEach { goodsBean!!.add(it) }
    }

    data class GoodsBean(val name: String, val price: String, val number: String, val sum: String)
}

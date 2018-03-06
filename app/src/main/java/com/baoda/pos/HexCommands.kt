package com.baoda.pos

/**
 * 采用16进制定义指令
 */
enum class HexCommands(vararg parameters: Int) {
    /**
     * 初始化打印机
     */
    INIT_PRINTER(0x1B, 0x40),
    /**
     * 选择标准模式
     */
    STANDARD_MODE(0x1B, 0x53),
    /**
     * 进纸并且半切纸
     */
    FEED_AND_CUT(0x1D, 0x56, 65),
    /**
     * 设置绝对打印位置
     */
    PRINT_ABSOLUTE_LOCATION(0x1B, 0x24),
    /**
     * 选择/取消加粗模式
     */
    TEXT_BOLD(0x1B, 0x45),
    /**
     * 选择字符对齐模式
     */
    TEXT_ALIGNMENT(0x1B, 0x61);

    val hexValues = parameters
}
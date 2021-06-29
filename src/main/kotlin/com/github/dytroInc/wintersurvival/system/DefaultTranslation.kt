package com.github.dytroInc.wintersurvival.system

import com.github.dytroInc.wintersurvival.utils.BasicUtils.removeColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object DefaultTranslation {
    fun ItemStack.getTranslation() = when(type) {
        Material.STICK -> "막대기"
        Material.SPRUCE_LOG -> "나무"
        Material.COBBLESTONE -> "조약돌"
        Material.WHITE_WOOL -> "양털"
        Material.LEATHER -> "가죽"
        else -> ""
    }

    fun ItemStack.getName() = (PlainTextComponentSerializer.plainText().serialize(itemMeta.displayName()?: Component.text(getTranslation())).removeColor())!!
}
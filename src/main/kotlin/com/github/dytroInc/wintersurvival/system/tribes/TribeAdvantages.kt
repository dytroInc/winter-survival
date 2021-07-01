package com.github.dytroInc.wintersurvival.system.tribes

import org.bukkit.Material

enum class TribeAdvantages(
    val translatedName: String,
    val description: String,
    val icon: Material
) {
    COLD_REDUCTION("추위 감소", "기존에 느끼던 추위가 10% 감소됩니다.", Material.CAMPFIRE),
    RESISTANCE("피해 감소", "기존에 느끼던 피해가 20% 감소됩니다.", Material.DIAMOND_CHESTPLATE),
    STRENGTH("대미지 증가", "기존의 대미지의 10%를 더한 값이 적에게 적용됩니다.", Material.NETHERITE_SWORD),
    SPEED("속도 증가", "속도가 더욱 더 빨라집니다.", Material.FEATHER)
}

enum class TribeDisadvantages(
    val translatedName: String,
    val description: String,
    val icon: Material
) {
    SLOWNESS("속도 감소", "속도가 더욱 더 느려집니다.", Material.IRON_BLOCK),
    HUNGER("탄탈로스의 저주", "허기를 얻으며 포만감은 절대로 가득차지 않습니다.", Material.IRON_BLOCK),
    COLDER_MEMBER("약한 몸", "기존에 느끼던 추위가 20% 증가됩니다.", Material.PACKED_ICE)
}
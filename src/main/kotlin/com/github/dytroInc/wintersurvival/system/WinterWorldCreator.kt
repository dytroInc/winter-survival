package com.github.dytroInc.wintersurvival.system

import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType

class WinterWorldCreator : WorldCreator("winter") {
    override fun generator() = WinterWorldGen()

    override fun environment() = World.Environment.NORMAL
    override fun type() = WorldType.FLAT
    override fun generateStructures() = false

}
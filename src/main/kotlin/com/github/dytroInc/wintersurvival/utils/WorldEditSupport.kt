package com.github.dytroInc.wintersurvival.utils

import com.sk89q.worldedit.bukkit.BukkitAdapter
import org.bukkit.Material

object WorldEditSupport {
    fun Material.asType() = BukkitAdapter.asBlockType(this)
    fun Material.asState() = BukkitAdapter.adapt(createBlockData())
}
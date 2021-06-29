package com.github.dytroInc.wintersurvival.system

import com.github.monun.tap.ref.UpstreamReference
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.*

class EnvelopedMail(val itemStack: ItemStack, var time: Int, receiver: UUID, val sender: Location, val original: ItemStack) {
    private val receiverRef = UpstreamReference(receiver)
    val receiver
        get() = receiverRef.get()
}
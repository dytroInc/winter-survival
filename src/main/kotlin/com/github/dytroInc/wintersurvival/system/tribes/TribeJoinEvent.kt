package com.github.dytroInc.wintersurvival.system.tribes

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TribeJoinEvent(val player: Player, val tribe: Tribe) : Event(), Cancellable {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
    private var cancelled = false
    override fun getHandlers() = handlerList
    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) = run { cancelled = cancel }

}
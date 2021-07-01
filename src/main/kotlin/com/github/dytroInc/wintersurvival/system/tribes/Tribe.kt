package com.github.dytroInc.wintersurvival.system.tribes

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*
import kotlin.collections.HashMap

class Tribe(
    var name: String,
    var owner: UUID,
    private val advantages: TribeAdvantages,
    private val disadvantages: TribeDisadvantages
) {

    var team: Team
    init {
        totalTribes.add(this)
        totalTribeMembers[owner] = this
        val teamName = (++cnt).toString()
        val name = name
        Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam(teamName).apply {
            prefix(Component.text("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$name${ChatColor.DARK_GRAY}]"))
        }.let {
            val p = Bukkit.getPlayer(owner)!!
            Bukkit.getServer().pluginManager.callEvent(TribeJoinEvent(p, this))
            it.addEntry(p.name)
            team = it
        }
    }

    companion object {
        var cnt = 0
        val totalTribeMembers = HashMap<UUID, Tribe>()
        val totalTribes = ArrayList<Tribe>()

        infix fun Player.join(tribe: Tribe) {
            val event = TribeJoinEvent(this, tribe)
            Bukkit.getServer().pluginManager.callEvent(event)
            if(event.isCancelled) return
            totalTribeMembers[uniqueId] = tribe
            tribe.team.addEntry(name)
        }
        fun Player.tribe() = totalTribeMembers[uniqueId]
        fun Player.hasAdvantage(a: TribeAdvantages) = tribe()?.hasAdvantage(a)?: false
        fun Player.hasDisadvantage(a: TribeDisadvantages) = tribe()?.hasDisadvantage(a)?: false
    }
    val members
        get() = totalTribeMembers.filter { it.value == this }.keys

    fun hasAdvantage(a: TribeAdvantages) = advantages == a
    fun hasDisadvantage(a: TribeDisadvantages) = disadvantages == a

    override fun equals(other: Any?) = run {
        if(other is Tribe) return@run owner == other.owner
        false
    }


}
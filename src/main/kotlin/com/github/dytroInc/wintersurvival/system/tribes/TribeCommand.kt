package com.github.dytroInc.wintersurvival.system.tribes

import com.github.dytroInc.wintersurvival.plugin.WinterSurvival
import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.join
import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.totalTribes
import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.tribe
import com.github.monun.invfx.InvFX
import com.github.monun.invfx.openWindow
import com.github.monun.kommand.KommandContext
import com.github.monun.kommand.KommandDispatcherBuilder
import com.github.monun.kommand.argument.KommandArgument
import com.github.monun.kommand.argument.string
import com.github.monun.kommand.argument.suggest
import com.github.monun.kommand.sendFeedback
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.HashMap

object TribeCommand {

    val tribePrefix = ChatColor.translateAlternateColorCodes(
        '&', "&8[&6부족 정보&8]&f"
    )

    private val selectedAdvantages = HashMap<UUID, TribeAdvantages>()

    private fun advantage(name: String) = InvFX.scene(1, "${name}족의 장점을 선택해주세요") {
        panel(0, 0, 9, 1) {
            listView(0, 0, 9, 1, true, TribeAdvantages.values().asList()) {
                transform {
                    ItemStack(it.icon).apply {
                        itemMeta = itemMeta!!.apply {
                            displayName(Component.text("${ChatColor.YELLOW}${it.translatedName}"))
                            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        }
                        lore(
                            listOf(
                                Component.text("${ChatColor.WHITE}${it.description}").color(TextColor.color(0xFFFFFF))
                            )
                        )

                    }
                }
                onClickItem { _, _, _, item, e ->
                    selectedAdvantages[e.whoClicked.uniqueId] = item
                    (e.whoClicked as Player).openWindow(disadvantage(name))
                }
            }
        }
    }
    private fun disadvantage(name: String) = InvFX.scene(1, "${name}족의 단점을 선택해주세요") {
        panel(0, 0, 9, 1) {
            listView(0, 0, 9, 1, true, TribeDisadvantages.values().asList()) {
                transform {
                    ItemStack(it.icon).apply {
                        itemMeta = itemMeta!!.apply {
                            displayName(Component.text("${ChatColor.YELLOW}${it.translatedName}"))
                            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        }
                        lore(
                            listOf(
                                Component.text("${ChatColor.WHITE}${it.description}").color(TextColor.color(0xFFFFFF))
                            )
                        )

                    }
                }
                onClickItem { _, _, _, item, e ->

                    val p = e.whoClicked as Player
                    if(WinterSurvival.cooldown.contains(p.uniqueId)) return@onClickItem
                    WinterSurvival.cooldown.add(p.uniqueId)

                    object : BukkitRunnable() {
                        override fun run() {
                            WinterSurvival.cooldown.remove(p.uniqueId)
                        }
                    }.runTaskLater(WinterSurvival.instance, 2)
                    val advantage = selectedAdvantages[p.uniqueId]
                    if(advantage != null) {

                        Tribe(name + "족", p.uniqueId, advantage, item).also {
                            p.sendMessage(Component.text("${it.name}을 창립했습니다."))
                            p.closeInventory()
                        }
                    }

                }
            }
        }
    }

    fun buildCommands(builder: KommandDispatcherBuilder) {
        builder.register("tribe") {
            then("창립") {
                require { it is Player && it.tribe() == null }
                then("이름" to string()) {
                    executes {
                        val p = it.sender as Player
                        val n = it.parseArgument<String>("이름")
                        if(n.length > 5) return@executes p.sendFeedback {
                            Component.text("부족의 이름은 최대 5자까지 가능합니다.")
                        }
                        if(totalTribes.any { t -> t.name == n }) return@executes p.sendFeedback {
                            Component.text("그런 이름의 부족이 이미 존재합니다.")
                        }
                        p.openWindow(advantage(n))
                    }
                }
            }
            then("입장") {
                require { it is Player && it.tribe() == null }
                then("부족" to TribeArgument) {
                    executes {
                        val p = it.sender as Player
                        val tribe = it.parseOrNullArgument<Tribe>("부족")
                        if(tribe != null) {
                            (p join tribe).also {
                                tribe.members.forEach { member ->
                                    Bukkit.getPlayer(member)?.let { m ->
                                        m.sendMessage("$tribePrefix ${ChatColor.GOLD}${p.name}${ChatColor.WHITE}님이 ${tribe.name}에 입장하였습니다.")
                                    }
                                }
                            }
                        } else {
                            p.sendFeedback {
                                Component.text("해당 부족은 존재하지 않습니다.")
                            }
                        }


                    }
                }
            }
        }
    }
}

object TribeArgument : KommandArgument<Tribe> {
    override fun parse(context: KommandContext, param: String): Tribe? {
        return totalTribes.firstOrNull {
            it.name == param
        }
    }

    override fun suggest(context: KommandContext, target: String): Collection<String> {
        return totalTribes.suggest(target) {
            it.name
        }
    }

}
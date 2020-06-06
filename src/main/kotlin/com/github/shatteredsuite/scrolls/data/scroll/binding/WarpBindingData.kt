package com.github.shatteredsuite.scrolls.data.scroll.binding

import com.github.shatteredsuite.core.include.nbt.NBTCompound
import com.github.shatteredsuite.scrolls.data.warp.Warp
import com.github.shatteredsuite.scrolls.items.ScrollInstance
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent
import java.util.*

class WarpBindingData(private val warp: Warp) : BindingData("warp", BindingDisplay("Warp Scroll", false, LinkedList(), false, 0)) {
    override fun applyBindingNBT(compound: NBTCompound) {}
    override fun serialize(): Map<String?, Any?> {
        val map = mutableMapOf<String?, Any?>()
        map["warp-id"] = warp.id
        return map
    }

    override fun parsePlaceholders(name: String): String {
        return name.replace("%x%", warp.location.blockX.toString())
                .replace("%y%", warp.location.blockY.toString())
                .replace("%z%", warp.location.blockZ.toString())
                .replace("%yaw%", warp.location.yaw.toString())
                .replace("%pitch%", warp.location.pitch.toString())
                .replace("%warp%", warp.name)
    }

    override fun onInteract(instance: ScrollInstance, player: Player): ScrollInstance {
        if (instance.bindingData !is WarpBindingData) {
            return instance
        }
        val loc = instance.bindingData.warp.location
        val event = PlayerTeleportEvent(player, player.location, loc, PlayerTeleportEvent.TeleportCause.PLUGIN)
        Bukkit.getPluginManager().callEvent(event)
        return if (!event.isCancelled) {
            player.teleport(loc)
            ScrollInstance(instance.scrollType, instance.charges - 1, instance.isInfinite, instance.bindingData)
        }
        else instance
    }
}
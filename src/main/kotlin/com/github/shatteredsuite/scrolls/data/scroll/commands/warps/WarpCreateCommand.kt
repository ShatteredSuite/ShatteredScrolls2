package com.github.shatteredsuite.scrolls.data.scroll.commands.warps

import com.github.shatteredsuite.core.commands.ArgParser
import com.github.shatteredsuite.core.commands.LeafCommand
import com.github.shatteredsuite.core.commands.TabCompleters
import com.github.shatteredsuite.core.commands.predicates.ArgumentMinimumPredicate
import com.github.shatteredsuite.core.commands.predicates.CommandContext
import com.github.shatteredsuite.core.commands.responses.CancelResponse
import com.github.shatteredsuite.core.util.StringUtil
import com.github.shatteredsuite.scrolls.ShatteredScrolls
import com.github.shatteredsuite.scrolls.data.warp.Warp
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCreateCommand(val instance: ShatteredScrolls, parent: WarpCommand) : LeafCommand(instance, parent, "create", "shatteredscrolls.command.warp.create", "commands.warp.create") {
    init {
        contextPredicates["args"] = ArgumentMinimumPredicate(CancelResponse(this.helpPath), 2)
        addAlias("c")
    }

    override fun execute(ctx: CommandContext) {
        val name = ctx.args[0]
        val displayName = ctx.args[1]
        val location = if(ctx.sender is Player && ctx.args.size == 2) {
            (ctx.sender as Player).location
        } else if (ctx.sender is Player && ctx.args.size == 5) {
            ArgParser.validShortLocation(ctx.args, 2, ctx.sender as Player)
        }
        else {
            ArgParser.validLocation(ctx.args, 2)
        }
        val warp = Warp(name, displayName, location, false)
        instance.warps().register(warp)
        ctx.contextMessages.putAll(warp.placeholders)
        ctx.messenger.sendMessage(ctx.sender, "create-warp", ctx.contextMessages, true)
    }

    override fun onTabComplete(ctx: CommandContext): MutableList<String> {
        if(ctx.sender !is Player) {
            return mutableListOf()
        }
        if(ctx.args.size <= 2) {
            return mutableListOf()
        }
        val newArgs = StringUtil.fixArgs(ctx.args)
        return TabCompleters.completeLocationPlayer(newArgs.sliceArray(2..newArgs.lastIndex), 0, ctx.sender as Player)
    }
}
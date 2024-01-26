package cif.rochester.cursedskyblock.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WikiCommand extends Command {
    public WikiCommand() {
        super("cswiki", "Sends a link to the cursed skyblock wiki", "/cswiki", List.of("cs-wiki","cswiki,cursedskyblockwiki"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        sender.sendMessage(Component.text("Click me!").style(Style.style(TextDecoration.BOLD).color(NamedTextColor.GOLD)).clickEvent(ClickEvent.openUrl("https://github.com/CIF-Rochester/CursedSkyblock/wiki")));
        return true;
    }
}

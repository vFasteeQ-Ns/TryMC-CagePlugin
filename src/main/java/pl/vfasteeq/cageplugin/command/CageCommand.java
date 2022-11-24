package pl.vfasteeq.cageplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.vfasteeq.cageplugin.MCPlugin;
import pl.vfasteeq.cageplugin.MCPluginAPI;
import pl.vfasteeq.cageplugin.util.ChatUtil;
import pl.vfasteeq.cageplugin.util.TitleUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author vFasteeQ
 * @since 24.11.2022
 */

public class CageCommand implements CommandExecutor {
    private final MCPlugin mcPlugin;
    private final List<ItemStack> armorList = new ArrayList<>();
    private final List<ItemStack> otherList = new ArrayList<>();
    public int time = 10;
    public int taskId;
    Set<Player> playerHashSet = new HashSet<>();
    public CageCommand(MCPlugin mcPlugin) {
        this.mcPlugin = mcPlugin;
        armorList.add(new ItemStack(Material.IRON_HELMET, 1));
        armorList.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
        armorList.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        armorList.add(new ItemStack(Material.IRON_BOOTS, 1));
        otherList.add(new ItemStack(Material.COOKED_CHICKEN, 32));
        otherList.add(new ItemStack(Material.STONE_SWORD, 1));
        otherList.add(new ItemStack(Material.GOLDEN_APPLE, 4));
        this.mcPlugin.getCommand("cage").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player && commandSender.hasPermission("cageplugin.start"))) {
            commandSender.sendMessage(ChatUtil.fixColor("&4Blad &8>> &fNie możesz uzyć tej komendy."));
        }
        if (args.length < 2) {
            commandSender.sendMessage(ChatUtil.fixColor("&4Blad &8>> &f/cage <atakujacy> <broniący>"));
            return false;
        }
        Player player1 = Bukkit.getPlayer(args[0]);
        Player player2 = Bukkit.getPlayer(args[1]);
        if (player1 == null || player2 == null) {
            commandSender.sendMessage(ChatUtil.fixColor("&4Blad &8>> &fJeden z graczy jest offline."));
            return false;
        }
        startCage(player1, player2);
        setupPlayer(player1, true);
        setupPlayer(player2, false);
        return true;
    }

    private void setupPlayer(Player player, boolean attacker) {
        player.getActivePotionEffects().clear();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        ItemStack[] armorItemStack = (ItemStack[]) armorList.toArray((Object[]) new ItemStack[0]);
        player.getInventory().setArmorContents(armorItemStack);
        ItemStack[] otherItemStack = (ItemStack[]) otherList.toArray((Object[]) new ItemStack[0]);
        player.getInventory().setContents(otherItemStack);
        World world = Bukkit.getWorld("world");
        if (attacker) {
            player.teleport(new Location(world, 0.0D, 100.0D, 0.0D));
        } else {
            player.teleport(new Location(world, 0.0D, 100.0D, 0.0D));
        }
    }

    private void startCage(Player attacker, Player defender) {
        playerHashSet.clear();
        playerHashSet.add(attacker);
        playerHashSet.add(defender);
        time = 5;
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = MCPluginAPI.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(MCPluginAPI.getPlugin(), () -> {
            if (time == 1337) {
                return;
            }
            if (time != 0) {
                for (Player player : playerHashSet) {
                    TitleUtil.sendTitleMessage(player, "&c" + time);
                }
                --time;
                return;
            }
            for (Player player : playerHashSet) {
                TitleUtil.sendTitleMessage(player, "&aStart!");
            }
            time = 1337;
        }, 0L, 20L);
    }
}

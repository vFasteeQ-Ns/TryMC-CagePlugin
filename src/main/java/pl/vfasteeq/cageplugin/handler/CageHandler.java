package pl.vfasteeq.cageplugin.handler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import pl.fernikq.core.CoreAPI;
import pl.fernikq.core.user.User;
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

public class CageHandler implements CommandExecutor, Listener {
    private final MCPlugin mcPlugin;
    private final List<ItemStack> armorList = new ArrayList<>();
    private final List<ItemStack> otherList = new ArrayList<>();
    public int time = 10;
    public int taskId;
    private boolean started;
    private boolean running;
    Set<Player> playerHashSet = new HashSet<>();
    World world = Bukkit.getWorld("world");
    Player attacker;
    Player defender;
    Player killer;


    public CageHandler(MCPlugin mcPlugin) {
        this.mcPlugin = mcPlugin;
        armorList.add(new ItemStack(Material.IRON_BOOTS, 1));
        armorList.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        armorList.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
        armorList.add(new ItemStack(Material.IRON_HELMET, 1));
        otherList.add(new ItemStack(Material.COOKED_CHICKEN, 32));
        otherList.add(new ItemStack(Material.STONE_SWORD, 1));
        otherList.add(new ItemStack(Material.GOLDEN_APPLE, 4));
        this.mcPlugin.getCommand("cage").setExecutor(this);
        this.mcPlugin.getServer().getPluginManager().registerEvents(this, mcPlugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player && (commandSender.hasPermission("cageplugin.start") || commandSender.isOp()))) {
            commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fNie możesz użyć tej komendy."));
            return false;
        }

        if (args.length < 2) {
            commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &f/cage <atakujący> <broniący>"));
            return false;
        }
        attacker = Bukkit.getPlayer(args[0]);
        defender = Bukkit.getPlayer(args[1]);
        if (attacker == null || defender == null) {
            commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fJeden z graczy jest offline."));
            return false;
        }
        if(args[0].equals(args[1])) {
            commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fGracz nie może walczyć ze samym sobą."));
            return false;
        }
        running = false;
        startCage(attacker, defender);
        setupPlayer(attacker, true);
        setupPlayer(defender, false);
        Bukkit.broadcastMessage(ChatUtil.fixColor("&4CAGE &8>> &fGracz&8: &e" + attacker.getName() + " &f walczy z graczem&8: &e" + defender.getName()));
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
        if (attacker) {
            player.teleport(new Location(world, -8.0D, 100.0D, 0.0D));
        } else {
            player.teleport(new Location(world, 8.0D, 100.0D, 0.0D));
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() != null) {
            if(event.getEntity().getKiller() == attacker || event.getEntity().getKiller() == defender) {
                Bukkit.broadcastMessage(ChatUtil.fixColor("&4CAGE &8>> &fKlatke wygrał gracz&8: &e" + event.getEntity().getKiller().getName()));
                killer = event.getEntity().getKiller();
                Bukkit.getScheduler().runTaskLater(mcPlugin, () -> {
                    if(killer.isOnline()) {
                        killer.teleport(new Location(world, 0.5D, 92.0D, 0.5D));
                        killer.getInventory().clear();
                        killer.getInventory().setArmorContents(null);
                        attacker = null;
                        defender = null;
                        killer = null;
                        running = false;
                    }
                }, 100L);
            }
        }
    }

    private void startCage(Player attacker, Player defender) {
        playerHashSet.clear();
        playerHashSet.add(attacker);
        playerHashSet.add(defender);
        time = 5;
        running = true;
        started = true;
        taskId = MCPluginAPI.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(MCPluginAPI.getPlugin(), () -> {
            if (time != 0 && running) {
                for (Player player : playerHashSet) {
                    TitleUtil.sendTitleMessage(player, "&f" + time);
                }
                --time;
                return;
            }
            if(started) {
                for (Player player : playerHashSet) {
                    TitleUtil.sendTitleMessage(player, "&aStart!");
                }
                Bukkit.getScheduler().cancelTask(taskId);
                started = false;
            }
        }, 0L, 30L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(event.getPlayer() == attacker) {
            event.getPlayer().teleport(new Location(world, 0.5D, 92.0D, 0.5D));
            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().setArmorContents(null);
            defender.teleport(new Location(world, 0.5D, 92.0D, 0.5D));
            defender.getInventory().clear();
            defender.getInventory().setArmorContents(null);
            User user = CoreAPI.getPlugin().getUserManager().getUser(defender);
            CoreAPI.getPlugin().getFightManager().removeFight(user);
            Bukkit.broadcastMessage(ChatUtil.fixColor("&4CAGE &8>> &fKlatke walkowerem wygrał gracz&8: &e" + defender.getName()));
            attacker = null;
            defender = null;
            running = false;
            if(Bukkit.getScheduler().isCurrentlyRunning(taskId)) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }
        if(event.getPlayer() == defender) {
            event.getPlayer().teleport(new Location(world, 0.5D, 92.0D, 0.5D));
            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().setArmorContents(null);
            attacker.teleport(new Location(world, 0.5D, 92.0D, 0.5D));
            attacker.getInventory().clear();
            attacker.getInventory().setArmorContents(null);
            User user = CoreAPI.getPlugin().getUserManager().getUser(attacker);
            CoreAPI.getPlugin().getFightManager().removeFight(user);
            Bukkit.broadcastMessage(ChatUtil.fixColor("&4CAGE &8>> &fKlatke walkowerem wygrał gracz&8: &e" + attacker.getName()));
            attacker = null;
            defender = null;
            running = false;
            if(Bukkit.getScheduler().isCurrentlyRunning(taskId)) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if(event.getPlayer() == attacker || event.getPlayer() == defender && running || !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fNie możesz uzywać komend podczas klatki."));
        }
    }
}

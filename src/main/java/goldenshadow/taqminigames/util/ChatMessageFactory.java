package goldenshadow.taqminigames.util;

import goldenshadow.taqminigames.enums.DefaultFontInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChatMessageFactory {

    private final static int CENTER_PX = 154;

    public static String adminErrorMessage(String message) {
        return ChatColor.RED + "[Minigames] Error: " + message;
    }

    public static String adminUsageMessage(String message) {
        return ChatColor.RED + "[Minigames] Usage: " + message;
    }

    public static String adminInfoMessage(String message) {
        return ChatColor.GOLD + "[Minigames] " + ChatColor.YELLOW + message;
    }

    public static String singleLineInfo(String message) {
        return ChatColor.GOLD + "[" + ChatColor.YELLOW + "!" + ChatColor.GOLD + "] " + ChatColor.YELLOW + message;
    }

    /**
     * Used to send players who gained points a message
     * @param message The reason they gained the points
     * @param amount The amount they gained. Should be larger than zero
     * @param descriptor The name of the score being added (e.g. emeralds)
     * @return The correctly formatted string
     */
    public static String pointsGainedInfo(String message, int amount, String descriptor) {
        return ChatColor.GREEN + "[" + (amount < 0 ? "" : "+") + amount + " " + descriptor + "] " + ChatColor.DARK_GREEN + message;
    }


    public static void sendInfoMessageBlock(Player player, String... content) {
        sendCenteredMessage(player,ChatColor.DARK_AQUA + String.valueOf(ChatColor.STRIKETHROUGH) + "==================================================");
        player.sendMessage(" ");
        Arrays.stream(content).forEach(m -> sendCenteredMessage(player, m));
        player.sendMessage(" ");
        sendCenteredMessage(player,ChatColor.DARK_AQUA + String.valueOf(ChatColor.STRIKETHROUGH) + "==================================================");
    }

    public static void sendInfoBlockToAll(String... content) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendInfoMessageBlock(p, content);
        }
    }


    /**
     * Used to send a centered chat message to a player
     * @param player The player who should receive the message
     * @param message The message that should be sent
     * @author SirSpoodles on spigotmc.org
     */
    private static void sendCenteredMessage(Player player, String message){
        if(message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb + message);
    }

    public static void sendActionbarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

}

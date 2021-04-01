package net.emuman.spigotutils.worlds;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Static class containing several useful utilities for creating and removing worlds.
 */
public class WorldUtils {

    /**
     * Copies whatever is in the specified folder into the Spigot root directory and creates a new world.
     * Do not rely on this working. There are many strange mechanics with Spigot world creation, and I
     * am not yet quite sure how they all work.
     *
     * @param folder    the folder to copy.
     * @param worldName the name of the world to create.
     * @return          the world that was created.
     */
    public static World fromExisting(String folder, String worldName) {
        File worldsFolder = Bukkit.getServer().getWorldContainer();
        String[] wfList = worldsFolder.list();
        if (wfList == null || Arrays.asList(wfList).contains(worldName)) return null;
        if (copyDirectory(new File(folder), new File(worldsFolder, worldName))) {
            return Bukkit.getServer().createWorld(new WorldCreator(worldName));
        } else {
            return null;
        }
    }

    /**
     * Unloads the specified world and deletes the associated folder.
     *
     * @param worldName the name of the world to be unloaded and removed.
     * @return          true if the world removal was successful, false otherwise.
     */
    public static boolean remove(String worldName) {
        Bukkit.getServer().unloadWorld(worldName, false);
        File worldsFolder = Bukkit.getServer().getWorldContainer();
        String[] wfList = worldsFolder.list();
        if (wfList == null || !Arrays.asList(wfList).contains(worldName)) return false;
        return deleteDirectory(worldsFolder.toPath().resolve(worldName).toFile());
    }

    /**
     * Saves the specified world to a given folder. Reverse operation as WorldUtils#fromExisting.
     *
     * @param folder    the folder to copy the world information into.
     * @param worldName the name of the world that should be saved.
     * @return          true if the world saving was successful, false otherwise.
     */
    public static boolean saveTo(String folder, String worldName) {
        File worldsFolder = Bukkit.getServer().getWorldContainer();
        String[] wfList = worldsFolder.list();
        if (wfList == null || !Arrays.asList(wfList).contains(worldName)) return false;
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) return false;
        else world.save();
        return copyDirectory(new File(worldsFolder, worldName), new File(folder));
    }

    /**
     * Recursive deletion utility for directories.
     *
     * @param directory the directory to delete.
     * @return          true if the folder deletion was successful, false otherwise.
     */
    private static boolean deleteDirectory(File directory) {
        String[] entries = directory.list();
        if (entries == null) return false;
        File currentFile;
        for (String s : entries) {
            currentFile = new File(directory.getPath(), s);
            if (currentFile.isDirectory()) {
                deleteDirectory(currentFile);
            } else {
                currentFile.delete();
            }
        }
        return directory.delete();
    }

    /**
     * Recursive copy utility for directories.
     *
     * @param path   the directory with the contents that should be copied.
     * @param target the directory to copy the contents into.
     * @return       true if the folder copy was successful, false otherwise.
     */
    private static boolean copyDirectory(File path, File target) {
        boolean success = true;
        target.mkdir();
        String[] entries = path.list();
        if (entries == null) return false;
        File currentFile;
        FileInputStream inputStream;
        FileChannel inChannel;
        FileOutputStream outputStream;
        FileChannel outChannel;
        for (String s : entries) {
            if (s.equals("uid.dat")) continue; // This identifier will prevent us from creating duplicate worlds.
            currentFile = new File(path.getPath(), s);
            if (currentFile.isDirectory()) {
                File newDirectory = new File(target, s);
                newDirectory.mkdir();
                copyDirectory(currentFile, newDirectory);
            } else {
                try {
                    // Apparently this more long-winded method is faster.
                    inputStream = new FileInputStream(currentFile);
                    inChannel = inputStream.getChannel();
                    outputStream = new FileOutputStream(new File(target, s));
                    outChannel = outputStream.getChannel();
                    inChannel.transferTo(0, currentFile.length(), outChannel);
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    if (success) {
                        // We probably only want to print this message once to avoid spamming
                        e.printStackTrace();
                        success = false;
                    }
                }
            }
        }
        return success;
    }

}

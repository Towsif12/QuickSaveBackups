package me.rockyhawk.qsBackup.fileclasses;

import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rockyhawk.qsBackup.QuickSave;
import org.bukkit.ChatColor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WorldZipper {
    QuickSave plugin;
    public WorldZipper(QuickSave pl) { this.plugin = pl; }

    private boolean currentlyBackingUp = false;

    public void zip(File worldDirectory, String destZipFile, TextChannel channel, String worldName, String filename, String strDate){
        new Thread (() -> {
            try {
                new File(destZipFile).getParentFile().mkdirs();
                try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(destZipFile)))) {
                    zipDirectory(worldDirectory, worldDirectory.getName(), zos);
                }

                plugin.getServer().getConsoleSender().sendMessage(plugin.colourize(plugin.tag + ChatColor.GREEN + "Finished backing up " + ChatColor.WHITE + worldDirectory.getName()));
                plugin.oldBackup.checkWorldForOldBackups(new File(plugin.saveFolder.getAbsolutePath() + File.separator + worldDirectory.getName()));

                if(channel !=null) {
                    plugin.getLogger().info("Sending backup to discord channel: "+worldName);
                    File bfile = new File(destZipFile);
                    channel.sendMessage(
                                    new StringBuilder("**")
                                            .append(plugin.config.getString("discord.server_name"))
                                            .append("** - **")
                                            .append(worldName)
                                            .append("** - `")
                                            .append(worldName + File.separator + strDate + ".zip")
                                            .append("`")
                            ).addFile(bfile, filename)
                            .queue();
                }
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getServer().getConsoleSender().sendMessage(plugin.colourize(plugin.tag + ChatColor.RED + "Failed to back up " + ChatColor.WHITE + worldDirectory.getName()));
            }
        }).start();
    }

    private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }

            try {
                zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                    int BUFFER_SIZE = 4096;
                    byte[] bytesIn = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = bis.read(bytesIn)) != -1) {
                        zos.write(bytesIn, 0, read);
                    }
                }

                zos.closeEntry();
            } catch (IOException ignore) {
                // skipping file, most likely 'session.lock' file which can be ignored
            }
        }
    }
}

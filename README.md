# QuickSaveBackups - Discord Upload
This plugin is a fork of [QuickSave](https://github.com/rockyhawk64/QuickSaveBackups) ([Spigot]()). 

QuickSave is a very straightforward backup plugin for your Minecraft worlds. This fork is a small modification to the plugin that adds a way to upload backups to a Discord channel with the help of [DiscordSRV](https://www.spigotmc.org/resources/discordsrv.18494/).

> [!IMPORTANT]
> Discord channels typically have a 25MB file upload limit, but on boosted servers, this can extend to 100MB. However, these limits aren't particularly useful for large survival worlds. Hence, this fork was specifically created to upload backups of lobby/minigame arena worlds.

This fork appends the following section to the default config file:
```yml
discord:
  enabled: true
  server_name: server1
  channel_id: '0000000000000000000'
```
Backups created either manually using the `/qs backup <world>` command or automatically triggered will be uploaded to the channel.

If you find yourself using fork and managing a lot backups within a channel, there's a helpful tool called [Haystackfs](https://github.com/dhrumilp15/haystackfs). This tool is able locate specific files within the channel based on their names.








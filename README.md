# TG-MOTD

A powerful and feature-rich MOTD (Message of the Day) plugin for Velocity proxy servers with advanced customization options.

## ✨ Features

- 🎨 **Advanced Text Formatting**
  - MiniMessage support with gradients and colors
  - Legacy color codes with hex color support (&#RRGGBB)
  - Full formatting options (bold, italic, underline, etc.)

- 🔧 **Highly Customizable**
  - Multi-line MOTD with placeholders
  - Custom hover information
  - Custom server icons (favicons)
  - Fake player count manipulation
  - Protocol-specific MOTDs

- 🛠️ **Maintenance Mode**
  - Enable/disable maintenance with a command
  - Custom maintenance MOTD and kick message
  - Whitelist system for admins
  - Player count override during maintenance

- 👥 **Whitelist System**
  - `players.yml` for whitelisted players
  - Whitelisted players get admin permissions
  - Easy management with commands
  - Bypass maintenance mode

- ⚡ **Performance Optimized**
  - Configurable update intervals
  - Cached favicon loading
  - Lightweight and efficient

## 📋 Requirements

- Velocity 3.3.0 or higher
- Java 21 or higher

## 🚀 Installation

1. Download the latest `TG-MOTD-1.0.0.jar` from releases
2. Place the JAR file in your Velocity `plugins` folder
3. Restart your Velocity proxy
4. Configure `config.yml` and `players.yml` in `plugins/tgmotd/`
5. Run `/tgmotd reload` to apply changes

## ⚙️ Configuration

### config.yml

```yaml
formatter: MINIMESSAGE  # MINIMESSAGE or LEGACY

motd:
  lines:
    - "<gradient:#ff3030:#ff7a7a:#ff0040><bold>SG NETWORK</bold></gradient> <gray>| <white>[1.21+]</white>"
    - "<gradient:#ffe600:#ffb300:#ff6a00>⚡ Lifesteal SMP Season 1 • Join Today!</gradient>"
  
  hover:
    - "<gold><bold>SG NETWORK</bold></gold>"
    - "<gray>Competitive Lifesteal Server</gray>"
    - ""
    - "<yellow>Community:</yellow> <red>discord.gg/2bVcPtfeJz"
  
  icon: "server-icon.png"

players:
  max: 200
  show-real-count: true
  fake:
    add: 0        # Add fixed number of fake players
    percent: 0    # Add percentage of fake players

update:
  interval: 3000  # Update interval in milliseconds

maintenance:
  enabled: false
  kick-on-join: true
  kick-message: "<red>The server is currently under maintenance.</red>"
  
  motd:
    - "<red><bold>SERVER MAINTENANCE</bold></red>"
    - "<gray>Please try again later</gray>"
  
  hover:
    - "<yellow>Status:</yellow> Maintenance"
    - "<gray>Follow updates on Discord</gray>"
  
  player-count:
    override: false
    online: 0
    max: 0
```

### players.yml

```yaml
whitelisted-players:
  - Prabin109
  - SanjitGamingYT
```

## 📝 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/tgmotd reload` | Reload configuration | `tgmotd.admin` or whitelisted |
| `/whitelist add <player>` | Add player to whitelist | `tgmotd.admin` or whitelisted |
| `/whitelist remove <player>` | Remove player from whitelist | `tgmotd.admin` or whitelisted |
| `/whitelist list` | List whitelisted players | `tgmotd.admin` or whitelisted |
| `/maintenance <on\|off\|toggle>` | Toggle maintenance mode | `tgmotd.admin` or whitelisted |

## 🔑 Permissions

- `tgmotd.admin` - Access to all commands
- Whitelisted players automatically get admin access

## 🎨 Text Formatting

### MiniMessage Format
```
<gradient:#ff0000:#00ff00>Gradient Text</gradient>
<bold>Bold</bold>
<italic>Italic</italic>
<color:#FF5555>Custom Color</color>
```

### Legacy Format with Hex Colors
```
&#FF0000&lRed Bold Text
&a&lGreen Bold Text
&7Gray Text
```

### Placeholders
- `{online}` - Current online players
- `{max}` - Maximum players

## 📦 Building from Source

```bash
git clone https://github.com/yourusername/TG-MOTD.git
cd TG-MOTD
mvn clean package
```

The compiled JAR will be in the `target` folder.

## 🐛 Support

If you encounter any issues or have suggestions:
- Open an issue on GitHub
- Join our Discord server
- Contact the developer

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Created by **TG (Techinpoint Gamerz)**

---

**Note:** This plugin is for Velocity proxy servers only. It does not work on Bukkit/Spigot/Paper servers.


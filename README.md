The **config** file of this mod is in your **world folder**! 

E.g. .minecraft/saves/NewWorld/serverconfigs/oreunification-server.toml

# Ore Unification

**Minecraft Version: 1.16.4**

**Mod Version: 1.0.2**

A highly-configurable Minecraft mod which can unify ores, ingots, nuggets, dusts from different mods.

# About

Author: ScottCTD (ScottCTD@outlook.com)

# Default Config

```toml
[MatchingSettings]

	[MatchingSettings.ItemsRegistryNames]
		#True to enable whitelist, and false to enable blacklist.
		#If the whitelist was enabled, the blacklist won't do anything.
		Mode = false
		#Whitelist of items to be replaced, not commonly used. E.g. "minecraft:iron_ingot"
		WhiteList = []
		#Blacklist of items to be replaced. E.g. "minecraft:iron_ingot"
		BlackList = []

	[MatchingSettings.Priority]
		#Mods Priority Settings (e.g. mekanism)
		#From highest to lowest.
		#Use mod id here (For "mekanism:copper_ingot", the mod id is "mekanism"
		#The first one should always be "minecraft"
		Mods = ["minecraft", "mekanism", "thermal", "immersiveengineering"]

	[MatchingSettings.Tags]
		#Whitelist for tags to be replaced.
		#[Notice]If you want to match all the occurrences, you have to add "*"!
		#(e.g. "forge:ingots/*")
		#You can use * but you cannot use other regex.
		WhiteList = ["forge:ingots/*", "forge:nuggets/*", "forge:dusts/*", "forge:gems/*", "forge:ores/*"]
		#Blacklist for tags to be replaced.
		#The priority of blacklist is higher than that of whitelist. So if you have both "forge:ingots/*" in the whitelist and blacklist, "forge:ingots/*" will not be replaced.
		BlackList = []

[EventSettings]
	#The period of ticks to replace the entire player inventory if "EnablePlayerTickListener" is true. 20 tick (= 1 seconds) by default
	#Range: > 1
	PlayerTickEventGap = 20
	#True to enable replacing items in players' inventory every configurable period.
	EnablePlayerTickListener = true
	#True to enable replacing items when items drop.
	EnableItemDropListener = true
```


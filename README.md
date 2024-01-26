# CursedSkyblock
 A very wacky skyblock plugin developed for the CIF minecraft server.
 This plugin makes obtaining most vanilla blocks in a void world much more managable using custom mechanics.

# Using the plugin
If you are looking to use the plugin on your server, there is minimal setup required.

First of all, Cursed Skyblock adds in custom world gen, or rather custom lack of world gen. To enable void world generation in a world you must tell bukkit to use our world generator.
In bukkit.yml, add the following lines to the end of the text file:

```yaml
worlds:
  <world>:
    generator: cursedskyblock
  <world>_nether:
    generator: cursedskyblock
```

Replace "<world>" with the **level-name** value in **server.properties**. If you have done this correctly, generating a new world will make both the overworld and the nether empty voids. If you want to make the end a void too add a line for <world>_the_end, this is not recommended though.

Start your server, and you should be good to play!

# Setting up the Development Environment
If you are not using Intellij IDEA with the Minecraft Development plugin installed I highly recommend you do so now.

This plugin uses ViciousLib for Reflection and configuration purposes. For the time being there are some extra steps you need to take to use the library. Follow step 2 (here)[https://github.com/Vicious-Development/ViciousLib/wiki]. Use environment variables! 

Once you have set up your env variables, simply import the gradle project. You should now be ready to code.

# Building the jar
Do not use gradle build, this will not include ViciousLib in the output. Use the task shadowJar, it will generate a jar in build/libs with 'all' at the end.


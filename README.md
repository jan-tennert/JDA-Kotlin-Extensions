# JDA-Ktx

JDA-Ktx is a package for Discord.JDA which uses Kotlin-Only features like Type-Safe Builders and it has a built in SlashCommandHandler.

# Installation

Currently you can only install this dependency through jitpack: https://jitpack.io/#jan-tennert/JDA-Ktx/

# Example:

### Slash Commands

```kotlin
val jda = JDABuilder.createDefault("token").build()
val commandHandler = CommandHandler(jda)

jda.awaitReady()
//If it a guild only command you can do
test.guildID = 98318748314

//You can create a command through a class:

commandHandler.registerCommands(testCommand())

class testCommand : Command("hi", "This is a Test Command") {
      override fun run(
            channel: TextChannel?,
            member: Member?,
            user: User,
            privateChannel: PrivateChannel?,
            hook: CommandHook,
            options: MutableList<SlashCommandEvent.OptionData>,
            event: SlashCommandEvent
        ) {
            event.reply("Hallo!").queue()
        }

}


//Or thrugh a type safe builder:
commandHandler.registerCommands(createSlashCommand {
     name = "hi"
     description = "Say hi to the bot"
     guildID = 631131922424135716

     action {
        it.reply("Hi, ${it.user.name}").queue()
     }
})
```

### Create roles & guild channels in custom event manager

```kotlin

val manager = KEventManager()
val jda = JDABuilder.createDefault("token")
      .setEventManager(manager)
      .build()

manager.on<GuildMessageReceivedEvent>() {
     it.channel.sendMessage("Received Message!").queue()
        
     //Create channel:
        
     it.guild.createTextChannel("test") {
            
        slowMode = 20
        topic = "Very cool text channel!"
            
        addMemberPermission(523405005402) {
             + Permission.MESSAGE_READ //allow permission
             - Permission.MESSAGE_WRITE //deny permission
        }
            
    }
        
     //Create role:
        
     it.guild.createRole { 
            
         name = "role"
         color = Color.CYAN
         mentionable = false
            
         permissions {  //Add permissions to the role
             + Permission.KICK_MEMBERS
             + Permission.BAN_MEMBERS
         }
            
     }
}

# JDA-Ktx

JDA-Ktx is a package for Discord.JDA which uses Kotlin-Only features like Type-Safe Builders and it has a built in SlashCommandHandler.

# Example:

### Slash Commands

```kotlin
val jda = JDABuilder.createDefault("token").build()
val commandHandler = CommandHandler(jda)

jda.awaitReady()
val test = testCommand()
//If it a guild only command you can do
test.guildID = 98318748314
commandHandler.registerCommands(test)

class testCommand : Command("test", "This is a Test Command") {
      override fun run(
            channel: TextChannel?,
            member: Member?,
            user: User,
            privateChannel: PrivateChannel?,
            hook: CommandHook,
            options: MutableList<SlashCommandEvent.OptionData>,
            event: SlashCommandEvent
        ) {
            val embed = messageEmbed {
                title("Title")
                field() {
                    name = "Field"
                    value = "Value"
                    inline = true
                }
                timestamp(Date())
                footer("Footer")
            }
            event.reply(embed).queue()
        }

}
```

### Create roles & guild channels in custom event manager

```java

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
